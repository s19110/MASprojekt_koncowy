package Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Klasa reprezentująca klasę szkolną w systemie
 */
@Entity(name = "Klasa")
public class Klasa {
    public enum Litera {A, B, C, D, E, F}


    private long id;
    private Litera litera;
    private LocalDate dataRozpoczeciaEdukacji;
    private static int maxUczniow = 30;
    private static final int MAX_NUMER_KLASY = 4;

    private Map<String,Uczen> uczniowie;
    private Set<Nauczyciel> nauczyciele;


    private Nauczyciel wychowawca;

    public Klasa() {}


    public Klasa(Litera litera, LocalDate dataRozpoczeciaEdukacji){
        this.litera = litera;
        this.dataRozpoczeciaEdukacji = dataRozpoczeciaEdukacji;
        uczniowie = new HashMap<>();
        nauczyciele = new HashSet<>();
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
    public Litera getLitera(){
        return litera;
    }
    public void setLitera(Litera litera){
        this.litera = litera;
    }

    @Basic(optional = false)
    public LocalDate getDataRozpoczeciaEdukacji(){
        return dataRozpoczeciaEdukacji;
    }
    public void setDataRozpoczeciaEdukacji(LocalDate dataRozpoczeciaEdukacji){
        this.dataRozpoczeciaEdukacji = dataRozpoczeciaEdukacji;
    }


    public boolean sprawdzLimitUczniow(){
        int aktualnaLiczbaUczniow = uczniowie.size();

        return aktualnaLiczbaUczniow+1 <= maxUczniow;

    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "klasa",cascade = CascadeType.DETACH)
    @MapKey(name = "uczenID")
    public Map<String,Uczen> getUczniowie(){
        return uczniowie;
    }

    public void setUczniowie(Map<String,Uczen> uczniowie){
        this.uczniowie = uczniowie;
    }

    @ManyToMany
    public Set<Nauczyciel> getNauczyciele() {
        return nauczyciele;
    }
    public void setNauczyciele(Set<Nauczyciel> nauczyciele) {
        this.nauczyciele = nauczyciele;
    }

    @OneToOne
    public Nauczyciel getWychowawca() {
        return wychowawca;
    }
    public void setWychowawca(Nauczyciel wychowawca) throws IllegalArgumentException{
        if(wychowawca != null && !getNauczyciele().contains(wychowawca))
            throw new IllegalArgumentException("Tylko nauczyciel uczący daną klasę może być wychowawcą");
        this.wychowawca = wychowawca;
    }

    /**
     * Metoda umożliwiająca dodanie ucznia do klasy
     * @param uczen - dodawany uczeń
     * @param uczenId - wartość ID ucznia w klase (asocjacja kwalifikowana)
     * @throws IllegalArgumentException - wyjątek jest rzucany, gdy dany kwalifikator jest zajęty
     */
    public void dodajUcznia(Uczen uczen, String uczenId) throws IllegalArgumentException{
        if(uczenId == null)
            throw new IllegalArgumentException("Id ucznia nie moze byc null");
        if(uczen == null)
            throw new IllegalArgumentException("Dodawany uczen nie moze byc null");
        if(uczniowie.containsKey(uczenId))
            throw new IllegalArgumentException("Uczen o id=" + uczenId +" juz nalezy do tej klasy");
        if(uczen.getUczenID() == null) {
            uczniowie.put(uczenId, uczen);
            uczen.setUczenID(uczenId);
            uczen.setKlasa(this);
        }
        else
            throw new IllegalArgumentException("Uczeń już należy do innej klasy");
    }

    /**
     * Metoda do usuwania ucznia z klasy
     * @param uczenId - kwalifikator danego ucznia
     * @return - usuwany uczeń jest zwracany, jeśli należał do tej klasy (co pozwala zapisać jego stan w bazie danych)
     *           null jest zwracany jeśli nie znaleziono ucznia o danym ID
     */
    public Uczen usunUcznia(String uczenId){
        if(uczniowie.containsKey(uczenId)){
            Uczen tmpUczen = uczniowie.get(uczenId);
                tmpUczen.setKlasa(null);
                tmpUczen.setUczenID(null);
                this.getUczniowie().remove(uczenId);
            System.out.println("!!!!!!!!!!!!!!!!!"+uczniowie);
                return tmpUczen;
        }
        return null;
    }

    @Transient
    public int getLiczbaUczniow(){
        return uczniowie.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Klasa)) return false;
        Klasa klasa = (Klasa) o;
        return getId() == klasa.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * Metoda zwracająca uproszczoną reprezentację tekstową klasy - używane w GUI
     * @return - uproszczona reprezentacja tekstowa klasy
     */
    @Transient
    public String getSimpleName(){
        long numerKlasy = ChronoUnit.YEARS.between(getDataRozpoczeciaEdukacji(),LocalDate.now())+1 <= MAX_NUMER_KLASY ?
                         ChronoUnit.YEARS.between(getDataRozpoczeciaEdukacji(),LocalDate.now())+1 : MAX_NUMER_KLASY;
        return numerKlasy+""+getLitera()+"("+getDataRozpoczeciaEdukacji().getYear()+")";
    }

    public String toString(){
        return "Klasa o literze: " + getLitera() + ", rozpoczęła edukację: " + this.getDataRozpoczeciaEdukacji();
    }

}
