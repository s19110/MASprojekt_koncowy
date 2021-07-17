package Model;

import Controller.ProblemController;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca problem zgłoszony przez ucznia do pracownika sekretariatu
 */
@Entity(name = "Problem")
public class Problem {

    public enum StatusProblemu {
        NOWY("Nowy"),W_TRAKCIE("W trakcie rozwiązywania"),ROZWIAZANY("Rozwiązany"),ANULOWANY("Anulowany");
        private final String nazwa;

        private StatusProblemu(String s){
            nazwa = s;
        }

        public String toString() {
            return this.nazwa;
        }
    }
    public enum TypBledu {ROZNICA_DAT,ILOSC_UCZNIOW}
    public enum CzySukces {TAK, NIE}

    private long id;
    private LocalDate dataZgloszenia;
    private String tresc;
    private StatusProblemu status;
    private Klasa wybranaKlasa;
    private TypBledu blad;
    private String powiadomienie;
    private boolean sprawdzonoPowiadomienie;
    //Zmienna pomocnicza do reprezentacji decyzji dyrektora
    private CzySukces decyzjaDyrektora;


    private Uczen zglaszajacyUczen;
    private PracownikSekretariatu obslugujacyPracownik;
    private Nauczyciel rozpatrujacyDyrektor;


    protected Problem() {}

    Problem(String tresc, Klasa wybranaKlasa,Uczen zglaszajacyUczen, PracownikSekretariatu obslugujacyPracownik) {
        this.tresc = tresc;
        this.wybranaKlasa = wybranaKlasa;
        this.setZglaszajacyUczen(zglaszajacyUczen);
        this.dataZgloszenia = LocalDate.now();
        this.status = StatusProblemu.NOWY;
        this.setObslugujacyPracownik(obslugujacyPracownik);
        this.blad = null;
        this.rozpatrujacyDyrektor = null;
        this.powiadomienie = null;
        this.sprawdzonoPowiadomienie = true;
    }

    /**
     * Metoda umożliwiająca stworzenie listy nierozwiązanych problemów
     * @return - lista problemów
     */
    public static List<Problem> stworzListeNierozwiazanychProblemow(){
        return ProblemController.getNierozwiazaneProblemy();
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


    @ManyToOne
    public Uczen getZglaszajacyUczen() {
        return zglaszajacyUczen;
    }

    public void setZglaszajacyUczen(Uczen zglaszajacyUczen) {
        this.zglaszajacyUczen = zglaszajacyUczen;
    }

    @Basic(optional = false)
    public LocalDate getDataZgloszenia() {
        return dataZgloszenia;
    }
    public void setDataZgloszenia(LocalDate dataZgloszenia) {
        this.dataZgloszenia = dataZgloszenia;
    }

    @Basic(optional = false)
    public String getTresc() {
        return tresc;
    }
    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    @Basic(optional = false)
    public StatusProblemu getStatus() {
        return status;
    }
    public void setStatus(StatusProblemu status) {
        this.status = status;
    }

    @Enumerated
    public TypBledu getBlad() {
        return blad;
    }

    public void setBlad(TypBledu blad) {
        this.blad = blad;
    }

    @Basic
    public String getPowiadomienie() {
        return powiadomienie;
    }

    public void setPowiadomienie(String powiadomienie) {
        this.powiadomienie = powiadomienie;
    }
    @Basic
    public boolean isSprawdzonoPowiadomienie() {
        return sprawdzonoPowiadomienie;
    }

    public void setSprawdzonoPowiadomienie(boolean sprawdzonoPowiadomienie) {
        this.sprawdzonoPowiadomienie = sprawdzonoPowiadomienie;
    }

    @Enumerated
    public CzySukces getDecyzjaDyrektora() {
        return decyzjaDyrektora;
    }
    public void setDecyzjaDyrektora(CzySukces decyzjaDyrektora) {
        this.decyzjaDyrektora = decyzjaDyrektora;
    }

    @ManyToOne
    public PracownikSekretariatu getObslugujacyPracownik() {
        return obslugujacyPracownik;
    }

    public void setObslugujacyPracownik(PracownikSekretariatu obslugujacyPracownik) {
        this.obslugujacyPracownik = obslugujacyPracownik;
    }

    @ManyToOne
    public Klasa getWybranaKlasa() {
        return wybranaKlasa;
    }
    public void setWybranaKlasa(Klasa wybranaKlasa) {
        this.wybranaKlasa = wybranaKlasa;
    }

    public String toString(){
        return "Problem: tresc=" + this.getTresc()+ "\r\nZgłaszajacy="+this.getZglaszajacyUczen().getOsoba()
                +"\r\nObsługujący="+this.getObslugujacyPracownik().getOsoba();
    }

    @ManyToOne
    public Nauczyciel getRozpatrujacyDyrektor() {
        return rozpatrujacyDyrektor;
    }

    public void setRozpatrujacyDyrektor(Nauczyciel rozpatrujacyDyrektor) {
        this.rozpatrujacyDyrektor = rozpatrujacyDyrektor;
    }


    /**
     * Metoda do kopiowania pól między obiektami -  kopiowane są tylko pola, bez asocjacji
     * metoda istotna przy zapisywaniu zmian obiektu w bazie danych
     * @param problem - problem, z którego pola są kopiowane
     */
    public void copyFields(Problem problem){
        if(problem != null){
            this.setDataZgloszenia(problem.getDataZgloszenia());
            this.setBlad(problem.getBlad());
            this.setStatus(problem.getStatus());
            this.setSprawdzonoPowiadomienie(problem.isSprawdzonoPowiadomienie());
            this.setPowiadomienie(problem.getPowiadomienie());
            this.setDecyzjaDyrektora(problem.getDecyzjaDyrektora());
            this.setObslugujacyPracownik(problem.getObslugujacyPracownik());
            this.setZglaszajacyUczen(problem.getZglaszajacyUczen());
            this.setRozpatrujacyDyrektor(problem.getRozpatrujacyDyrektor());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;
        Problem problem = (Problem) o;
        return getId() == problem.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
