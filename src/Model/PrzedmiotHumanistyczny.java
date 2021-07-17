package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca aspekt przedmiotu humanistycznego w systemie
 */
@Entity(name = "PrzedmiotHumanistyczny")
public class PrzedmiotHumanistyczny {

    private long id;
    private List<String> literaturaUzupelniajaca;

    private Przedmiot przedmiot;


    public PrzedmiotHumanistyczny() {}

    public PrzedmiotHumanistyczny(String... literaturaUzupelniajaca){
        this.literaturaUzupelniajaca = new ArrayList<>(Arrays.asList(literaturaUzupelniajaca));
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
    public List<String> getLiteraturaUzupelniajaca() {
        return literaturaUzupelniajaca;
    }
    public void setLiteraturaUzupelniajaca(List<String> literaturaUzupelniajaca) {
        this.literaturaUzupelniajaca = literaturaUzupelniajaca;
    }


    @OneToOne(fetch = FetchType.EAGER)
    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }
    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    public String toString(){
        return "Przedmiot humanistyczny: literatura uzupelniajaca=" + this.getLiteraturaUzupelniajaca();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrzedmiotHumanistyczny)) return false;
        PrzedmiotHumanistyczny that = (PrzedmiotHumanistyczny) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
