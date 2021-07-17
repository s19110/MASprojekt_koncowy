package Controller;

import Model.Nauczyciel;
import Model.Osoba;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Klasa do zarządzania zawartością tabeli z dyrektorami w oknie z klasy DyrektorzyPanel
 */
public class DyrektorzyTableModel extends AbstractTableModel {
    List<Osoba> dyrektorzy;

    public DyrektorzyTableModel(List<Osoba> pracownicy){
        this.dyrektorzy = pracownicy;
    }


    @Override
    public int getRowCount() {
        return dyrektorzy.size();
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
            case 3: nazwa = "Pensja"; break;
        }
        return nazwa;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Osoba pracownik = dyrektorzy.get(rowIndex);
        Object wartosc = null;
        switch (columnIndex){
            case 0: wartosc = pracownik.getPesel(); break;
            case 1: wartosc = pracownik.getImie(); break;
            case 2: wartosc = pracownik.getNazwisko(); break;
            case 3: wartosc = ((Nauczyciel) pracownik.getAktualnaRola()).getPensja(); break;
        }
        return wartosc;
    }

    /**
     * Zwraca dyrektora o podanym ID
     * @param id - id obiektu na liście
     * @return
     */
    public Nauczyciel getDyrektor(int id){
        return (Nauczyciel) dyrektorzy.get(id).getAktualnaRola();
    }
}
