package Model;

import Controller.OcenyController;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Klasa reprezentująca ocenę cząstkową w systemie
 */
@Entity(name = "OcenaCzastkowa")
public class OcenaCzastkowa extends Ocena {
    private int waga;
    private static final int MIN_WAGA = 0;

    public OcenaCzastkowa() {
    }

    private OcenaCzastkowa(int wartosc, Przedmiot przedmiot, Uczen uczen, int waga) {
        super(wartosc, przedmiot, uczen);
        this.waga = waga;
    }


    /**
     * Metoda sprawdzająca, czy podane parametry są odpowiednie do stworzenia obiektu oceny
     * @param wartosc - wartość oceny
     * @param przedmiot - przedmiot z którego ocena została wystawiona
     * @param uczen - uczeń który uzyskał ocenę
     * @param waga - waga oceny cząstkowej
     * @return - utworzony obiekt oceny cząstkowej
     * @throws IllegalArgumentException - wyjątek jest rzucany jeżeli parametry nie spełniają ograniczeń oceny
     */
    public static OcenaCzastkowa utworz(int wartosc, Przedmiot przedmiot, Uczen uczen, int waga) throws IllegalArgumentException {
        checkTworzenie(wartosc,przedmiot,uczen);
        checkWaga(waga);

        return new OcenaCzastkowa(wartosc, przedmiot, uczen, waga);
    }


    @Basic(optional = false)
    public int getWaga() {
        return waga;
    }

    public void setWaga(int waga) throws IllegalArgumentException {
        checkWaga(waga);
        this.waga = waga;
    }


    private static void checkWaga(int waga) throws IllegalArgumentException {
        if (waga < MIN_WAGA)
            throw new IllegalArgumentException("Waga oceny nie moze byc mniejsza niz" + MIN_WAGA);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    @Transient
    public double getPunkty() {
        float wszystkieWagi = OcenyController.getOcenyCzastkowe().stream()
                             .filter(ocenaCzastkowa -> ocenaCzastkowa.getPrzedmiot().equals(this.getPrzedmiot())).mapToInt(OcenaCzastkowa::getWaga).sum();

        return (this.getWaga()/wszystkieWagi)*this.getWartosc();
    }

}

