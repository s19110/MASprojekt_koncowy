package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca rolę pracownika sekretariatu w systemie
 */
@Entity(name = "PracownikSekretariatu")
public class PracownikSekretariatu {
    private long id;
    private LocalTime godzinaRozpoczecia;
    private static LocalTime godzinaZakonczenia = LocalTime.of(17,0);
    private int stawkaGodzinowa;

    private Osoba osoba;
    private List<Problem> obslugiwaneProblemy;

    public PracownikSekretariatu() {}

    PracownikSekretariatu(LocalTime godzinaRozpoczecia, int stawkaGodzinowa, Osoba osoba){
        this.godzinaRozpoczecia = godzinaRozpoczecia;
        this.stawkaGodzinowa = stawkaGodzinowa;
        this.osoba = osoba;
        obslugiwaneProblemy = new ArrayList<>();
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

    @Basic(optional =  false)
    public LocalTime getGodzinaRozpoczecia() {
        return godzinaRozpoczecia;
    }
    public void setGodzinaRozpoczecia(LocalTime godzinaRozpoczecia) {
        this.godzinaRozpoczecia = godzinaRozpoczecia;
    }

    @Basic(optional = false)
    public int getStawkaGodzinowa() {
        return stawkaGodzinowa;
    }
    public void setStawkaGodzinowa(int stawkaGodzinowa) {
        this.stawkaGodzinowa = stawkaGodzinowa;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    public Osoba getOsoba() {
        return osoba;
    }

    @OneToMany(mappedBy = "obslugujacyPracownik")
    public List<Problem> getObslugiwaneProblemy() {
        return obslugiwaneProblemy;
    }
    public void setObslugiwaneProblemy(List<Problem> obslugiwaneProblemy) {
        this.obslugiwaneProblemy = obslugiwaneProblemy;
    }

    void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String toString(){
        return"Pracownik sekretariatu: godzinaRozpoczęcia="+this.getGodzinaRozpoczecia()+";stawkaGodzinowa="
                +this.getStawkaGodzinowa();
    }

    /**
     * Metoda informująca, czy problem jest obsługiwany przez pracownika, czy nie
     * @param problem - sprawdzany problem
     */
    public void obsluzProblem(Problem problem){
        if(getObslugiwaneProblemy().contains(problem))
            System.out.println("Problem jest już obsługiwany");
        else
            System.out.println("Podany problem nie należy do problemów rozwiązywanych przez tego pracownika");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PracownikSekretariatu)) return false;
        PracownikSekretariatu that = (PracownikSekretariatu) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
