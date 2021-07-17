package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Klasa reprezentująca abstrakcyjną ocenę w systemie
 */
@Entity(name="Ocena")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Ocena {
    private long id;
    private int wartosc;

    private static final int MIN_WARTOSC = 1;
    private static final int MAX_WARTOSC = 5;

    private Uczen uczen;
    private Przedmiot przedmiot;


    public Ocena() {}

    protected Ocena(int wartosc, Przedmiot przedmiot, Uczen uczen){
        this.wartosc = wartosc;
        this.uczen = uczen;
        this.przedmiot = przedmiot;
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
    public int getWartosc(){
        return wartosc;
    }

    public void setWartosc(int wartosc) throws IllegalArgumentException{
        checkWartosc(wartosc);
        this.wartosc = wartosc;
    }

    /**
     * Metoda umożliwiająca sprawdzenie, czy ograniczenie wartości oceny jest spełnione
     * @param wartosc - proponowana wartość oceny
     * @throws IllegalArgumentException - wyjątek jest rzucany jeżeli ograniczenie nie jest spełnione
     */
    static void checkWartosc(int wartosc) throws IllegalArgumentException{
        if(wartosc < MIN_WARTOSC || wartosc > MAX_WARTOSC)
            throw new IllegalArgumentException("Wartość oceny musi zawierać się w przedziale od "
                    + MIN_WARTOSC +" do " + MAX_WARTOSC);
    }

    /**
     * Metoda sprawdzająca, czy ocena jest tworzona prawidłowo
     * @param wartosc - wartość oceny
     * @param przedmiot - przedmiot, z którego ocena została wystawiona
     * @param uczen - uczeń, któremu ocenę wystawiono
     * @throws IllegalArgumentException - wyjątek jest rzucany, gdy nie są spełnione ograniczenia oraz gdy przedmiot
     *                                    albo uczeń są null
     */
    static void checkTworzenie(int wartosc, Przedmiot przedmiot, Uczen uczen) throws IllegalArgumentException{
        checkWartosc(wartosc);
        if (przedmiot == null)
            throw new IllegalArgumentException("Przedmiot oceny nie powinien byc null");
        if (uczen == null)
            throw new IllegalArgumentException("Uczen nie powinien byc null");
    }

    /**
     * Metoda zwracająca wartość punktową zgodnie z wymaganiami z dokumentacji
     * @return liczba punktów
     */
    @Transient
    public abstract double getPunkty();

    @ManyToOne
    public Uczen getUczen() {
        return uczen;
    }

    public void setUczen(Uczen uczen) {
        this.uczen = uczen;
    }

    @ManyToOne
    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }
    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }
}
