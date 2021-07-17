package Model;

import Controller.OcenyController;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca ucznia w systemie
 */
@Entity(name = "Uczen")
public class Uczen {

    private long id;
    private String uczenID;
    private LocalDate dataUrodzenia;
    private LocalDate dataUkonczeniaSzkoly;
    private boolean ukonczylPedagogiczne;

    private Osoba osoba;
    private Klasa klasa;
    private List<Problem> zgloszoneProblemy;
    private List<Ocena> oceny;

    public Uczen() {}

    Uczen(LocalDate dataUrodzenia, LocalDate dataUkonczeniaSzkoly, boolean ukonczylPedagogiczne, Osoba osoba){
        this.dataUrodzenia = dataUrodzenia;
        this.dataUkonczeniaSzkoly = dataUkonczeniaSzkoly;
        this.ukonczylPedagogiczne = ukonczylPedagogiczne;
        this.setOsoba(osoba);
        this.zgloszoneProblemy = new ArrayList<>();
        this.oceny = new ArrayList<>();
    }


    public Problem zglosProblem(String tresc, Klasa wybranaKlasa, PracownikSekretariatu obslugujacyPracownik) throws IllegalArgumentException{
        if(obslugujacyPracownik == null)
            throw new IllegalArgumentException("Obsługujący pracownik nie może być null");

        Problem tmpProblem = new Problem(tresc,wybranaKlasa,this,obslugujacyPracownik);
        this.getZgloszoneProblemy().add(tmpProblem);
        return tmpProblem;
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


    public String getUczenID(){
        return uczenID;
    }
    public void setUczenID(String uczenID){
        this.uczenID = uczenID;
    }

    @Basic(optional =  false)
    public LocalDate getDataUrodzenia() {
        return dataUrodzenia;
    }
    public void setDataUrodzenia(LocalDate dataUrodzenia) {
        this.dataUrodzenia = dataUrodzenia;
    }

    @Basic(optional = true)
    public LocalDate getDataUkonczeniaSzkoly() {
        return dataUkonczeniaSzkoly;
    }
    public void setDataUkonczeniaSzkoly(LocalDate dataUkonczeniaSzkoly) {
        this.dataUkonczeniaSzkoly = dataUkonczeniaSzkoly;
    }

    @Basic(optional = false)
    public boolean isUkonczylPedagogiczne() {
        return ukonczylPedagogiczne;
    }
    public void setUkonczylPedagogiczne(boolean ukonczylPedagogiczne) {
        this.ukonczylPedagogiczne = ukonczylPedagogiczne;
    }

    @Transient
    public int getWiek(){
        return getDataUrodzenia() == null ? 0 : LocalDate.now().getYear() - getDataUrodzenia().getYear();
    }

    @OneToOne(cascade = CascadeType.DETACH)
    public Osoba getOsoba() {
        return osoba;
    }
    void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    @OneToMany(mappedBy = "zglaszajacyUczen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Problem> getZgloszoneProblemy() {
        return zgloszoneProblemy;
    }
    public void setZgloszoneProblemy(List<Problem> zgloszoneProblemy) {
        this.zgloszoneProblemy = zgloszoneProblemy;
    }



    @ManyToOne
    public Klasa getKlasa() {
        return klasa;
    }
    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }

    @OneToMany(mappedBy = "uczen", orphanRemoval = true)
    public List<Ocena> getOceny() {
        return oceny;
    }
    public void setOceny(List<Ocena> oceny) {
        this.oceny = oceny;
    }

    /**
     * Metoda umożliwiająca zmianę klasy do której chodzi uczeń
     * @param wybranaKlasa - klasa, do której należy przenieść ucznia
     * @param newID - kwalifikator ucznia w nowej klasie
     * @throws Exception - wyjątek jest rzucany jeśli kwalifikator jest zajęty
     */
    public void zmienKlase(Klasa wybranaKlasa,String newID) throws Exception
    {
        if(wybranaKlasa.getUczniowie().keySet().contains(newID)){
            throw new Exception("To ID jest zajęte");
        }

        Klasa tmpKlasa = getKlasa();
        if(tmpKlasa != null){
            tmpKlasa.usunUcznia(getUczenID());
        }
        wybranaKlasa.dodajUcznia(this,newID);
    }

    /**
     * Metoda umożliwiająca wystawienie świadectwa uczniowi
     * @param rok - rok za który ma być wystawione świadectwo
     */
    public void wystawSwiadectwo(int rok){
        System.out.println("Świadectwo ucznia :" + this.getOsoba().getSimpleName() + " za rok " + rok);
        List<OcenaSemestralna> oceny = OcenyController.getOcenySemestralne(this,rok);
        for(Ocena o : oceny){
            System.out.println(o.getPrzedmiot().getNazwa() + ": " + o.getWartosc());
        }
    }

    public String toString(){
        return "Uczeń: dataUrodzenia="+this.getDataUrodzenia() + ";dataUkończeniaSzkoły="+this.getDataUkonczeniaSzkoly()
                + ";ukończyłStudiaPedagogiczne="+this.isUkonczylPedagogiczne();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Uczen)) return false;
        Uczen uczen = (Uczen) o;
        return getId() == uczen.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
