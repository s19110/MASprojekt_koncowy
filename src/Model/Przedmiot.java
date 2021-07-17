package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Klasa reprezentująca przedmiot szkolny w systemie
 */
@Entity(name = "Przedmiot")
public class Przedmiot {

    public enum TypPrzedmiotu {OPCJONALNY, OBOWIAZKOWY}

    private long id;
    private String nazwa;
    private String podrecznik;
    private LocalTime godzinaRozpoczecia;
    private DayOfWeek dzienTygodnia;
    private String opis;
    private List<String> wymaganiaProgramowe;
    private TypPrzedmiotu typ;

    private static final LocalTime MIN_GODZINA_ROZPOCZECIA = LocalTime.of(8,0);
    private static final Set<DayOfWeek> MOZLIWE_DNI = new HashSet<>(Arrays.asList(DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY));

    private PrzedmiotHumanistyczny przedmiotHumanistyczny;
    private PrzedmiotPrzyrodniczy przedmiotPrzyrodniczy;
    private PrzedmiotScisly przedmiotScisly;

    private List<Ocena> oceny;

    public Przedmiot() {}

    /**
     * Konstruktor do tworzenia przedmiotów opcjonalnych
     * @param nazwa - nazwa przedmiotu
     * @param podrecznik - podręcznik używany do uczenia przedmiotu
     * @param godzinaRozpoczecia - godzina rozpoczęcia zajęć opcjonalnych
     * @param dzienTygodnia - dni tygodnia w które zajęcia opcjonalne się odbywają
     * @param opis - opis przedmiotu
     */
    private Przedmiot(String nazwa, String podrecznik, LocalTime godzinaRozpoczecia, DayOfWeek dzienTygodnia, String opis){
        this.nazwa = nazwa;
        this.podrecznik = podrecznik;
        this.godzinaRozpoczecia = godzinaRozpoczecia;
        this.dzienTygodnia = dzienTygodnia;
        this.opis = opis;
        this.typ =TypPrzedmiotu.OPCJONALNY;
        oceny = new ArrayList<>();
    }

    /**
     * Konstruktor do tworzenia przedmiotów obowiązkowych
     * @param nazwa - nazwa przedmiotu
     * @param podrecznik - podręcznik używany do uczenia przedmiotu
     * @param wymaganiaProgramowe - lista wymagań programowe wobec przedmiotu
     */
    private Przedmiot(String nazwa, String podrecznik, String... wymaganiaProgramowe){
        this.nazwa = nazwa;
        this.podrecznik = podrecznik;
        this.wymaganiaProgramowe = new ArrayList<>(Arrays.asList(wymaganiaProgramowe));
        this.typ = TypPrzedmiotu.OBOWIAZKOWY;
    }

    /**
     * Metoda umożliwiająca tworzenie przedmiotu opcjonalnego po sprawdzeniu ograniczeń
     * @param nazwa - nazwa przedmiotu
     * @param podrecznik  - podręcznik używany do uczenia przedmiotu
     * @param godzinaRozpoczecia - godzina rozpoczęcia zajęć opcjonalnych
     * @param dzienTygodnia - dni tygodnia w które zajęcia opcjonalne się odbywają
     * @param opis - opis przedmiotu
     * @return - obiekt reprezentujący przedmiot opcjonalny
     * @throws IllegalArgumentException - wyjątek jest rzucany gdy ograniczenia nie są przestrzegane
     */
    public static Przedmiot utworzPrzedmiotOpcjonalny(String nazwa, String podrecznik, LocalTime godzinaRozpoczecia, DayOfWeek dzienTygodnia, String opis) throws IllegalArgumentException{
        sprawdzAtrybutyPrzedmiotuOpcjonalnego(godzinaRozpoczecia,dzienTygodnia);
        return new Przedmiot(nazwa,podrecznik,godzinaRozpoczecia,dzienTygodnia,opis);
    }

    /**
     * Metoda umożliwiająca tworzenie przedmiotu obowiązkowego. Pomimo braku ograniczeń, ta metoda istnieje w celu
     * utrzymania konwencji tworzenia przedmiotów za pomocą metod statycznych
     * @param nazwa - nazwa przedmiotu
     * @param podrecznik - podręcznik używany do uczenia przedmiotu
     * @param wymaganiaProgramowe - lista wymagań programowe wobec przedmiotu
     * @return - obiekt reprezentujący przedmiot obowiązkowy
     */
    public static Przedmiot utworzPrzedmiotObowiazkowy(String nazwa, String podrecznik, String... wymaganiaProgramowe){
        return new Przedmiot(nazwa,podrecznik,wymaganiaProgramowe);
    }

    /**
     * Metoda sprawdzająca, czy ograniczenia przedmiotu opcjonalnego są przestrzegane
     * @param godzinaRozpoczecia - godzina rozpoczęcia zajęć
     * @param dzienTygodnia - dzień tygodnia w który zajęcia się odbywają
     * @throws IllegalArgumentException - wyjątek jest rzucany gdy ograniczenia nie są przestrzegane
     */
    private static void sprawdzAtrybutyPrzedmiotuOpcjonalnego(LocalTime godzinaRozpoczecia, DayOfWeek dzienTygodnia) throws IllegalArgumentException{
        if(godzinaRozpoczecia.compareTo(MIN_GODZINA_ROZPOCZECIA) < 0)
            throw  new IllegalArgumentException("Zajecia z tego przedmiotu nie moga sie zaczac przed " + MIN_GODZINA_ROZPOCZECIA);
        if(!MOZLIWE_DNI.contains(dzienTygodnia))
            throw new IllegalArgumentException("Dzień tygodnia musi być jednym z następujących: " +MOZLIWE_DNI);
    }

    /**
     * Metoda pozwalająca na utworzenie przedmiotu ścisłego
     * @param przedmiotScisly - obiekt reprezentujący aspekt przedmiotu ścisłego
     * @throws IllegalArgumentException - wyjatek jest rzucany jeżeli ten przedmiot jest już ścisły
     *                                    lub jeśli podany aspekt jest już przypisany do przedmiotu
     */
    public void dodajTyp(PrzedmiotScisly przedmiotScisly) throws IllegalArgumentException{
        if(this.getPrzedmiotScisly() != null)
            throw new IllegalArgumentException("Ten przedmiot już jest przedmiotem ścisłym");
        if(przedmiotScisly != null) {
            if(przedmiotScisly.getPrzedmiot() != null)
                throw new IllegalArgumentException("Ten typ przedmiotu został już przypisany");

            this.setPrzedmiotScisly(przedmiotScisly);
            przedmiotScisly.setPrzedmiot(this);
        }
    }

    /**
     * Metoda pozwalająca na utworzenie przedmiotu przyrodniczego
     * @param przedmiotPrzyrodniczy - obiekt reprezentujący aspekt przedmiotu przyrodniczego
     * @throws IllegalArgumentException - wyjatek jest rzucany jeżeli ten przedmiot jest już przyrodniczy
     *                                    lub jeśli podany aspekt jest już przypisany do przedmiotu
     */
    public void dodajTyp(PrzedmiotPrzyrodniczy przedmiotPrzyrodniczy){
        if(this.getPrzedmiotPrzyrodniczy() != null)
            throw new IllegalArgumentException("Ten przedmiot już jest przedmiotem przyrodniczym");
        if(przedmiotPrzyrodniczy != null) {
            if(przedmiotPrzyrodniczy.getPrzedmiot() != null)
                throw new IllegalArgumentException("Ten typ przedmiotu został już przypisany");

            this.setPrzedmiotPrzyrodniczy(przedmiotPrzyrodniczy);
            przedmiotPrzyrodniczy.setPrzedmiot(this);
        }
    }

    /**
     * Metoda pozwalająca na utworzenie przedmiotu humanistycznego
     * @param przedmiotHumanistyczny - obiekt reprezentujący aspekt przedmiotu humanistycznego
     * @throws IllegalArgumentException - wyjatek jest rzucany jeżeli ten przedmiot jest już humanistyczny
     *                                    lub jeśli podany aspekt jest już przypisany do przedmiotu
     */
    public void dodajTyp(PrzedmiotHumanistyczny przedmiotHumanistyczny){
        if(this.getPrzedmiotHumanistyczny() != null)
            throw new IllegalArgumentException("Ten przedmiot już jest przedmiotem humanistycznym");
        if(przedmiotHumanistyczny != null) {
            if(przedmiotHumanistyczny.getPrzedmiot() != null)
                throw new IllegalArgumentException("Ten typ przedmiotu został już przypisany");

            this.setPrzedmiotHumanistyczny(przedmiotHumanistyczny);
            przedmiotHumanistyczny.setPrzedmiot(this);
        }
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }


    @Basic(optional = false)
    public String getNazwa() {
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Basic(optional = false)
    public String getPodrecznik() {
        return podrecznik;
    }
    public void setPodrecznik(String podrecznik) {
        this.podrecznik = podrecznik;
    }

    @Basic
    public LocalTime getGodzinaRozpoczecia() {
        return godzinaRozpoczecia;
    }
    public void setGodzinaRozpoczecia(LocalTime godzinaRozpoczecia) {
        if(this.typ == TypPrzedmiotu.OPCJONALNY)
        this.godzinaRozpoczecia = godzinaRozpoczecia;
    }

    @Basic
    public DayOfWeek getDzienTygodnia() {
        return dzienTygodnia;
    }
    public void setDzienTygodnia(DayOfWeek dzienTygodnia) {
        if (this.typ == TypPrzedmiotu.OPCJONALNY)
        this.dzienTygodnia = dzienTygodnia;
    }

    @Basic
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        if (this.typ == TypPrzedmiotu.OPCJONALNY)
        this.opis = opis;
    }

    @ElementCollection
    public List<String> getWymaganiaProgramowe() {
        return wymaganiaProgramowe;
    }
    public void setWymaganiaProgramowe(List<String> wymaganiaProgramowe) {
        if(this.typ == TypPrzedmiotu.OBOWIAZKOWY)
        this.wymaganiaProgramowe = wymaganiaProgramowe;
    }

    @Enumerated(value = EnumType.ORDINAL)
    public TypPrzedmiotu getTyp() {
        return typ;
    }
    public void setTyp(TypPrzedmiotu typ) {
        this.typ = typ;
    }


    @OneToOne(orphanRemoval = true, mappedBy = "przedmiot", fetch = FetchType.EAGER)
    public PrzedmiotHumanistyczny getPrzedmiotHumanistyczny() {
        return przedmiotHumanistyczny;
    }
    private void setPrzedmiotHumanistyczny(PrzedmiotHumanistyczny przedmiotHumanistyczny) {
        this.przedmiotHumanistyczny = przedmiotHumanistyczny;
    }


    @OneToOne(orphanRemoval = true, mappedBy = "przedmiot", fetch = FetchType.EAGER)
    public PrzedmiotPrzyrodniczy getPrzedmiotPrzyrodniczy() {
        return przedmiotPrzyrodniczy;
    }
    private void setPrzedmiotPrzyrodniczy(PrzedmiotPrzyrodniczy przedmiotPrzyrodniczy) {
        this.przedmiotPrzyrodniczy = przedmiotPrzyrodniczy;
    }

    @OneToOne(orphanRemoval = true, mappedBy = "przedmiot", fetch = FetchType.EAGER)
    public PrzedmiotScisly getPrzedmiotScisly() {
        return przedmiotScisly;
    }
    private void setPrzedmiotScisly(PrzedmiotScisly przedmiotScisly) {
        this.przedmiotScisly = przedmiotScisly;
    }

    @OneToMany(mappedBy = "przedmiot")
    public List<Ocena> getOceny() {
        return oceny;
    }
    public void setOceny(List<Ocena> oceny) {
        this.oceny = oceny;
    }


    public String toString(){
        StringBuilder builder = new StringBuilder("Przedmiot: nazwa=");
        builder.append(this.getNazwa());
        builder.append(";podrecznik=");
        builder.append(this.getPodrecznik());
        builder.append(";Obligatoryjność=");
        builder.append(this.typ);
        builder.append(";Tematyka=\n\r");
        if(this.getPrzedmiotHumanistyczny() != null) {
            builder.append(this.getPrzedmiotHumanistyczny());
            builder.append("\r\n");
        }
        if(this.getPrzedmiotPrzyrodniczy() != null) {
            builder.append(this.getPrzedmiotPrzyrodniczy());
            builder.append("\r\n");
        }
        if(this.getPrzedmiotScisly() != null) {
            builder.append(this.getPrzedmiotScisly());
            builder.append("\r\n");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Przedmiot)) return false;
        Przedmiot przedmiot = (Przedmiot) o;
        return getId() == przedmiot.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
