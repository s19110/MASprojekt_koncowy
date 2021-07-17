package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Klasa reprezentująca aspekt przedmiotu ścisłego w systemie
 */
@Entity(name = "PrzedmiotScisly")
public class PrzedmiotScisly {
    private long id;
    private String zbiorZadan;
    private String oprogramowanie;

    private Przedmiot przedmiot;

    public PrzedmiotScisly() {}

    public PrzedmiotScisly(String zbiorZadan, String oprogramowanie){
        this.zbiorZadan = zbiorZadan;
        this.oprogramowanie = oprogramowanie;
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
    public String getZbiorZadan() {
        return zbiorZadan;
    }
    public void setZbiorZadan(String zbiorZadan) {
        this.zbiorZadan = zbiorZadan;
    }

    @Basic
    public String getOprogramowanie() {
        return oprogramowanie;
    }
    public void setOprogramowanie(String oprogramowanie) {
        this.oprogramowanie = oprogramowanie;
    }


    @OneToOne(fetch = FetchType.EAGER)
    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }
    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    public String toString(){
        return "Przedmiot ścisły: zbiór zadań=" + this.getZbiorZadan() +";oprogramowanie="+this.getOprogramowanie();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrzedmiotScisly)) return false;
        PrzedmiotScisly that = (PrzedmiotScisly) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
