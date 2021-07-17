package Model;


import Controller.OcenyController;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.OptionalDouble;

/**
 * Klasa reprezentująca ocenę semestralną w systemie
 */
@Entity(name = "OcenaSemestralna")
public class OcenaSemestralna extends Ocena {

    private LocalDate dataWystawienia;
    private String uzasadnienie;

    public OcenaSemestralna() {}


    private OcenaSemestralna(int wartosc, Przedmiot przedmiot, Uczen uczen, String  uzasadnienie){
        super(wartosc, przedmiot, uczen);
        this.dataWystawienia = LocalDate.now();
        this.uzasadnienie = uzasadnienie;
    }

    /**
     /**
     * Metoda sprawdzająca, czy podane parametry są odpowiednie do stworzenia obiektu oceny
     * @param wartosc - wartość oceny
     * @param przedmiot - przedmiot z którego ocena została wystawiona
     * @param uczen - uczeń który uzyskał ocenę
     * @param uzasadnienie - uzasadnienie oceny semestralnej
     * @return - utworzony obiekt oceny semestralnej
     * @throws IllegalArgumentException - wyjątek jest rzucany jeżeli parametry nie spełniają ograniczeń oceny
     */
    public static OcenaSemestralna utworz(int wartosc, Przedmiot przedmiot, Uczen uczen, String  uzasadnienie) throws IllegalArgumentException {
        checkTworzenie(wartosc,przedmiot,uczen);
        return new OcenaSemestralna(wartosc, przedmiot, uczen,uzasadnienie);
    }

    @Basic(optional = false)
    public LocalDate getDataWystawienia() {
        return dataWystawienia;
    }
    public void setDataWystawienia(LocalDate dataWystawienia) {
        this.dataWystawienia = dataWystawienia;
    }
    @Basic
    public String getUzasadnienie() {
        return uzasadnienie;
    }

    public void setUzasadnienie(String uzasadnienie) {
        this.uzasadnienie = uzasadnienie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    public double getPunkty(){
        return uzasadnienie != null ? getWartosc() : 0;
    }


    /**
     * Metoda licząca średnią ocen semestralny z danego przedmiotu
     * @param nazwaPrzemiotu - nazwa przedmiotu z którego zostały wystawione oceny
     * @return - średnia ocen
     */
    public static double obliczSrednia(String nazwaPrzemiotu)
    {
        OptionalDouble srednia = OcenyController.getOcenySemestralne().stream().filter(ocenaSemestralna ->
                ocenaSemestralna.getPrzedmiot() != null && ocenaSemestralna.getPrzedmiot().getNazwa().equals(nazwaPrzemiotu)).mapToDouble(Ocena::getWartosc).average();
        return srednia.orElse(0.0d);
    }

    /**
     * Metoda licząca średnią ocen semestralny wystawionych w danym roku
     * @param rokWystawienia - rok w którym oceny zostały wystawione
     * @return - średnia ocen
     */
    public static double obliczSrednia(int rokWystawienia){
        OptionalDouble srednia = OcenyController.getOcenySemestralne().stream().filter(ocenaSemestralna ->
                ocenaSemestralna.getDataWystawienia().getYear() == rokWystawienia).mapToDouble(Ocena::getWartosc).average();
        return srednia.orElse(0.0d);

    }

    /**
     * Metoda licząca średnią ocen semestralny które dany uczeń uzyskał w danym roku
     * @param uczen - uczeń którego oceny należy uzyskać
     * @param rokWystawienia - rok w którym oceny zostały wystawione
     * @return - średnia ocen
     */
    public static double obliczSrednia(Uczen uczen, int rokWystawienia){
        OptionalDouble srednia = OcenyController.getOcenySemestralne().stream().filter(ocenaSemestralna ->
                ocenaSemestralna.getUczen() != null && ocenaSemestralna.getUczen().equals(uczen) && ocenaSemestralna.getDataWystawienia().getYear() == rokWystawienia).mapToDouble(Ocena::getWartosc).average();
        return srednia.orElse(0.0d);
    }

}
