package Controller;

import Model.Osoba;
import Model.Uczen;

import javax.swing.table.AbstractTableModel;
import java.util.List;
/**
 * Klasa umożliwiająca wyświetlenie listy uczniów
 * w oknie z klasy UczenPanel
 */
public class UczenTableModel extends AbstractTableModel {
    private List<Osoba> uczniowie;

    public UczenTableModel(List<Osoba> uczniowie){
        this.uczniowie = uczniowie;
    }

    @Override
    public int getRowCount() {
        return uczniowie.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column){
        String nazwa = "???";
        switch (column){
            case 0: nazwa = "PESEL"; break;
            case 1: nazwa = "Imie"; break;
            case 2: nazwa = "Nazwisko"; break;
            case 3: nazwa = "Data urodzenia"; break;
        }
        return nazwa;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Osoba uczen = uczniowie.get(rowIndex);
        Object wartosc = null;
        switch (columnIndex){
            case 0: wartosc = uczen.getPesel(); break;
            case 1: wartosc = uczen.getImie(); break;
            case 2: wartosc = uczen.getNazwisko(); break;
            case 3: wartosc = ((Uczen) uczen.getAktualnaRola()).getDataUrodzenia(); break;
        }
        return wartosc;
    }
    /**
     * Metoda zwracająca wybranego ucznia z listy
     * @param id - id ucznia na liście
     * @return - wybrany uczeń
     */
    public Osoba getUczen(int id){
        return uczniowie.get(id);
    }
}
