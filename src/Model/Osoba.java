package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Klasa reprezentująca osobę w systemie
 */
@Entity(name = "Osoba")
public class Osoba {

    //Atrybuty
    private long id;
    private String pesel;
    private String imie;
    private String nazwisko;

    //Asocjacje
    private Uczen uczen;
    private Nauczyciel nauczyciel;
    private PracownikSekretariatu pracownikSekretariatu;

    public Osoba() {}

    /**
     * Konstuktor do tworzenia osoby będącej uczniem
     * @param pesel  - PESEL osoby
     * @param imie - imię osoby
     * @param nazwisko - nazwisko osoby
     * @param dataUrodzenia - data urodzenia - przechowywana w klasie Uczen
     * @param dataUkonczeniaSzkoly - data ukończenia szkoły przez ucznia - przechowywana w klasie Uczen
     * @param ukonczylPedagogiczne - fakt ukończenia studiów pedagogicznych - przechowywany w klasie Uczen
     */
    public Osoba(String pesel, String imie, String nazwisko, LocalDate dataUrodzenia, LocalDate dataUkonczeniaSzkoly,boolean ukonczylPedagogiczne){
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.uczen = new Uczen(dataUrodzenia,dataUkonczeniaSzkoly,ukonczylPedagogiczne,this);
    }

    /**
     * Konstruktor do tworzenia osoby będącej nauczycielem
     * @param pesel - PESEL osoby
     * @param imie - imię osoby
     * @param nazwisko - nazwisko osoby
     * @param dataZatrudnienia - data zatrudnienia nauczyciela - przechowywana w klasie Nauczyciel
     * @param pensja - pensja nauczyciela - przechowywana w klasie Nauczyciel
     * @param czyDyrektor - fakt bycia dyrektorem - przechowywany w klasie Nauczyciel
     */
    public Osoba(String pesel, String imie, String nazwisko, LocalDate dataZatrudnienia, int pensja ,boolean czyDyrektor){
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nauczyciel = new Nauczyciel(dataZatrudnienia,pensja,czyDyrektor,this);
    }

    /**
     * Konstruktor do tworzenia osoby będącej pracownikiem sekretariatu
     * @param pesel - PESEL osoby
     * @param imie - imię osoby
     * @param nazwisko - nazwisko osoby
     * @param godzinaRozpoczecia - godzina rozpoczęcia pracy - przechowywana w klasie PracownikSekretariatu
     * @param stawkaGodzinowa - stawka godzinowa pracowkina - przechowywana w klasie PracownikSekretariatu
     */
    public Osoba(String pesel, String imie, String nazwisko, LocalTime godzinaRozpoczecia, int stawkaGodzinowa){
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pracownikSekretariatu = new PracownikSekretariatu(godzinaRozpoczecia,stawkaGodzinowa,this);
    }

    /**
     * Metoda pozwalająca zmień rolę danej osoby na ucznia
     * @param dataUrodzenia - data urodzenia ucznia
     * @param dataUkonczeniaSzkoly - data ukończenia szkoły
     * @param ukonczylPedagogiczne - fakt ukończenia studiów pedagogicznych
     * @return - dawna rola - używane przy zapisywaniu zmian w bazie danych
     * @throws Exception - wyjątek jest rzucany gdy dana osoba jest juz uczniem
     */
    public Object zmienNaUcznia(LocalDate dataUrodzenia, LocalDate dataUkonczeniaSzkoly, boolean ukonczylPedagogiczne) throws Exception{
        if(this.getUczen() != null)
            throw new Exception("Ta osoba już jest uczniem");
        Object dawnaRola = null;
        if(this.getNauczyciel() != null){
            dawnaRola = this.getNauczyciel();
            this.usunNauczyciela();
        }
        else if(this.getPracownikSekretariatu() != null){
            dawnaRola = this.getPracownikSekretariatu();
            this.usunPracownikaSekretariatu();
        }
        this.setUczen(new Uczen(dataUrodzenia,dataUkonczeniaSzkoly,ukonczylPedagogiczne,this));
        return dawnaRola;
    }

    /**
     * Metoda pozwalająca zmień rolę danej osoby na nauczyciela
     * @param dataZatrudnienia - data zatrudnienia nauczyciela
     * @param pensja - pensja nauczyciela
     * @param czyDyrektor - fakt bycia dyrektorem
     * @return - dawna rola - używane przy zapisywaniu zmian w bazie danych
     * @throws Exception - wyjątek jest rzucany gdy dana osoba jest juz uczniem
     */
    public Object zmienNaNauczyciela(LocalDate dataZatrudnienia, int pensja, boolean czyDyrektor) throws Exception{
        if(this.getNauczyciel() != null)
            throw new Exception("Ta osoba już jest nauczycielem");
        Object dawnaRola = null;
        if(this.getUczen() != null){
            if(!this.getUczen().isUkonczylPedagogiczne())
                throw new Exception("Tylko uczeń ze skończonymi studiami pedagogicznymi może zostać nauczycielem");
            dawnaRola = this.getUczen();
            this.usunUcznia();
        }

        if(this.getPracownikSekretariatu() != null){
            dawnaRola = this.getPracownikSekretariatu();
            this.usunPracownikaSekretariatu();
        }
        this.setNauczyciel(new Nauczyciel(dataZatrudnienia,pensja,czyDyrektor,this));
        return dawnaRola;
    }

    /**
     * Metoda pozwalająca zmień rolę danej osoby na pracownika sekretariatu
     * @param godzinaRozpoczecia - godzina rozpoczęcia pracy
     * @param stawkaGodzinowa - stawka godzinowa pracownika
     * @return - dawna rola - używane przy zapisywaniu zmian w bazie danych
     * @throws Exception - wyjątek jest rzucany gdy dana osoba jest juz uczniem
     */
    public Object zmienNaPracownikaSekretariatu(LocalTime godzinaRozpoczecia, int stawkaGodzinowa) throws Exception{
        if(this.getPracownikSekretariatu() != null)
            throw new Exception("Ta osoba już jest pracownikiem sekretariatu");
        Object dawnaRola = null;
        if(this.getNauczyciel() != null){
            dawnaRola = this.getNauczyciel();
            this.usunNauczyciela();
        }
        if(this.getUczen() != null){
            dawnaRola = this.getUczen();
            this.usunUcznia();
        }
        this.setPracownikSekretariatu(new PracownikSekretariatu(godzinaRozpoczecia,stawkaGodzinowa,this));
        return dawnaRola;
    }

    /**
     * Metoda usuwająca rolę ucznia
     */
    void usunUcznia(){
        if(this.getUczen() != null){
            this.getUczen().setOsoba(null);
            this.setUczen(null);
        }
    }

    /**
     * Metoda usuwająca rolę nauczyciela
     */
    void usunNauczyciela(){
        if(this.getNauczyciel() != null){
            this.getNauczyciel().setOsoba(null);
            this.setNauczyciel(null);
        }
    }

    /**
     * Metoda usuwająca rolę pracownika sekretariatu
     */
    void usunPracownikaSekretariatu(){
        if(this.getPracownikSekretariatu() != null){
            this.getPracownikSekretariatu().setOsoba(null);
            this.setPracownikSekretariatu(null);
        }
    }



    @Transient
    public Klasa getKlasa(){
        if(this.getUczen() != null){
            return this.getUczen().getKlasa();
        }else return null;
    }

    //=======================ATRYBUTY I ASOCJACJE=========================
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
    @Column(unique = true)
    public String getPesel(){
        return pesel;
    }
    public void setPesel(String  pesel){
        this.pesel = pesel;
    }

    @Basic(optional = false)
    public String getImie() {
        return imie;
    }
    public void setImie(String imie) {
        this.imie = imie;
    }

    @Basic(optional = false)
    public String getNazwisko() {
        return nazwisko;
    }
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    @OneToOne(mappedBy = "osoba",orphanRemoval = true)
    protected Uczen getUczen() {
        return uczen;
    }

    protected void setUczen(Uczen uczen) {
        this.uczen = uczen;
    }


    @OneToOne(mappedBy = "osoba",fetch = FetchType.EAGER,orphanRemoval = true)
    protected Nauczyciel getNauczyciel() {
        return nauczyciel;
    }

    protected void setNauczyciel(Nauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }

    @OneToOne(mappedBy = "osoba", orphanRemoval = true)
    protected PracownikSekretariatu getPracownikSekretariatu() {
        return pracownikSekretariatu;
    }

    protected void setPracownikSekretariatu(PracownikSekretariatu pracownikSekretariatu) {
        this.pracownikSekretariatu = pracownikSekretariatu;
    }

    @Transient
    public Object getAktualnaRola(){
        if(this.getUczen() != null)
            return this.getUczen();
        else if (this.getNauczyciel() != null)
            return this.getNauczyciel();
        else if (this.getPracownikSekretariatu() != null)
            return this.getPracownikSekretariatu();
        else
            return null;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder("Osoba: imie=");
        builder.append(this.getImie());
        builder.append(";nazwisko=");
        builder.append(this.getNazwisko());
        builder.append(";PESEL=");
        builder.append(this.getPesel());
        builder.append(";rola=");

        if(this.getUczen() != null)
            builder.append(this.getUczen());
        if(this.getNauczyciel() != null)
            builder.append(this.getNauczyciel());
        if(this.getPracownikSekretariatu() != null)
            builder.append(this.getPracownikSekretariatu());
        return builder.toString();
    }

    /**
     * Metoda umożliwiająca reprezentację osoby jedynie poprzez imię i nazwisko - używane w GUI
     * @return - uproszczona reprezentacja tekstowa osoby
     */
    @Transient
    public String getSimpleName(){
        return this.getImie() + " " + this.getNazwisko();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return getId() == osoba.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
