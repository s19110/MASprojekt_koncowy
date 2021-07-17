package Model;

import Controller.PrzedmiotyFetcher;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca aspekt przedmiotu przyrodniczego w systemie
 */
@Entity(name = "PrzedmiotPrzyrodniczy")
public class PrzedmiotPrzyrodniczy {
    private long id;
    private List<String> sprzetLabolatoryjny;

    private Przedmiot przedmiot;


    public PrzedmiotPrzyrodniczy() {}

    public PrzedmiotPrzyrodniczy(String... sprzetLabolatoryjny){
        this.sprzetLabolatoryjny = new ArrayList<>(Arrays.asList(sprzetLabolatoryjny));
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

    @ElementCollection
    public List<String> getSprzetLabolatoryjny() {
        return sprzetLabolatoryjny;
    }
    public void setSprzetLabolatoryjny(List<String> sprzetLabolatoryjny) {
        this.sprzetLabolatoryjny = sprzetLabolatoryjny;
    }


    @OneToOne(fetch = FetchType.EAGER)
    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }
    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    public String toString(){
        return "Przedmiot przyrodniczy: spręt laboratoryjny=" + this.getSprzetLabolatoryjny();
    }


    /**
     * Metoda umożliwiająca dodanie sprzętu, który jest używany przy nauczaniu przedmiotu
     * @param sprzet - dodawany sprzęt
     */
    public void wstawSprzet(String sprzet){
        getSprzetLabolatoryjny().add(sprzet);
        System.out.println("Sprzęt został dodany");
    }

    /**
     * Metoda umożliwiająca inwentaryzację sprzętu labolatoryjnego
     */
    public void dokonajInwentaryzacji(){
        List<PrzedmiotPrzyrodniczy> przedmioty = PrzedmiotyFetcher.getPrzedmiotyPrzyrodnicze();
        for(PrzedmiotPrzyrodniczy p : przedmioty){
            System.out.println(p.getPrzedmiot().getNazwa());
            System.out.println("Sprzęt: " + p.getSprzetLabolatoryjny());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrzedmiotPrzyrodniczy)) return false;
        PrzedmiotPrzyrodniczy that = (PrzedmiotPrzyrodniczy) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
