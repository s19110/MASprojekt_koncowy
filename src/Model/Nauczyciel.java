package Model;

import com.sun.org.apache.bcel.internal.generic.ILOAD;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Klasa reprezentująca w systemie rolę nauczyciela
 */
@Entity(name = "Nauczyciel")
public class Nauczyciel {

    private long id;
    private LocalDate dataZatrudnienia;
    private int pensja;
    private boolean czyDyrektor;

    private Osoba osoba;
    private Set<Problem> przekazaneProblemy;
    private Set<Klasa> nauczaneKlasy;
    private Klasa klasaWychowawcza;

    public Nauczyciel() {}

    Nauczyciel(LocalDate dataZatrudnienia, int pensja, boolean czyDyrektor, Osoba osoba) {
        this.dataZatrudnienia = dataZatrudnienia;
        this.pensja = pensja;
        this.czyDyrektor = czyDyrektor;
        this.osoba = osoba;
        this.przekazaneProblemy = new HashSet<>();
        this.nauczaneKlasy = new HashSet<>();
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
    public LocalDate getDataZatrudnienia() {
        return dataZatrudnienia;
    }
    public void setDataZatrudnienia(LocalDate dataZatrudnienia) {
        this.dataZatrudnienia = dataZatrudnienia;
    }

    @Basic(optional = false)
    public int getPensja() {
        return pensja;
    }
    public void setPensja(int pensja) {
        this.pensja = pensja;
    }

    @Basic(optional = false)
    public boolean isCzyDyrektor() {
        return czyDyrektor;
    }
    public void setCzyDyrektor(boolean czyDyrektor) {
        this.czyDyrektor = czyDyrektor;
    }


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    public Osoba getOsoba() {
        return osoba;
    }
    void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String toString(){
        return "Nauczyciel: dataZatrudnienia="+this.getDataZatrudnienia()+";pensja="+this.getPensja()+";czyDyrektor="
                +this.isCzyDyrektor();
    }

    @OneToMany(mappedBy = "rozpatrujacyDyrektor", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    public Set<Problem> getPrzekazaneProblemy() {
        return przekazaneProblemy;
    }

    public void setPrzekazaneProblemy(Set<Problem> przekazaneProblemy) {
        this.przekazaneProblemy = przekazaneProblemy;
    }

    @ManyToMany(mappedBy = "nauczyciele",cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    public Set<Klasa> getNauczaneKlasy() {
        return nauczaneKlasy;
    }

    public void setNauczaneKlasy(Set<Klasa> nauczaneKlasy) {
        this.nauczaneKlasy = nauczaneKlasy;
    }

    @OneToOne(mappedBy = "wychowawca",cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    public Klasa getKlasaWychowawcza() {
        return klasaWychowawcza;
    }
    public void setKlasaWychowawcza(Klasa klasaWychowawcza) throws IllegalArgumentException {
        if(klasaWychowawcza != null && this.nauczaneKlasy.contains(klasaWychowawcza))
            throw new IllegalArgumentException("Nauczyciel może być wychowawcą tylko klasy, którą uczy");
        this.klasaWychowawcza = klasaWychowawcza;
    }

    /**
     * Metoda umożliwiająca przekazanie problemu dyrektorowi
     * @param problem - przekazany problem
     */
    public void addPrzekazanyProblem(Problem problem){
        if(!this.isCzyDyrektor())
            return;
        if(problem != null)
        if(problem.getRozpatrujacyDyrektor() == null){
            if(!this.getPrzekazaneProblemy().contains(problem)){
                problem.setRozpatrujacyDyrektor(this);
                this.getPrzekazaneProblemy().add(problem);
            }
        }
    }

    /**
     * Metoda umożliwiająca usunięcie problemu z listy przekazanych dyrektorowi
     * @param problem - usuwany problem
     */
    public void usunPrzekazanyProblem(Problem problem){
        System.out.println("PRZEKAZANY PROBLEM:" + problem);
        if(this.getPrzekazaneProblemy().contains(problem)){
            problem.setRozpatrujacyDyrektor(null);
            this.getPrzekazaneProblemy().remove(problem);
        }
    }

    /**
     * Metoda umożliwiająca zwiększenie pensji pracownika o podaną wartość
     * @param ile - o ile należy zwiększyć pensję
     * @throws Exception - wyjątek jest rzucany, gdy podwyżka <= 0
     */
    public void dajPodwyzke(int ile) throws Exception{
        if(ile <= 0)
            throw new Exception("Podwyżka musi być większa od 0");

        setPensja(getPensja()+ile);
        System.out.println("Pensja została zwiększona do: " +getPensja());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nauczyciel)) return false;
        Nauczyciel that = (Nauczyciel) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
