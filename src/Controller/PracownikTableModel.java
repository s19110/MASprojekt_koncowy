package Controller;

import Model.Osoba;
import Model.PracownikSekretariatu;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Klasa umożliwiająca wyświetlania pracowników sekretariatu w tabeli z okna z klasy PracownikPanel
 */
public class PracownikTableModel extends AbstractTableModel {
    List<Osoba> pracownicy;

    public PracownikTableModel(List<Osoba> pracownicy){
        this.pracownicy = pracownicy;
    }

    @Override
    public int getRowCount() {
        return pracownicy.size();
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
            case 3: nazwa = "Godzina rozpoczęcia pracy"; break;
        }
        return nazwa;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Osoba pracownik = pracownicy.get(rowIndex);
        Object wartosc = null;
        switch (columnIndex){
            case 0: wartosc = pracownik.getPesel(); break;
            case 1: wartosc = pracownik.getImie(); break;
            case 2: wartosc = pracownik.getNazwisko(); break;
            case 3: wartosc = ((PracownikSekretariatu) pracownik.getAktualnaRola()).getGodzinaRozpoczecia(); break;
        }
        return wartosc;
    }

    /**
     * Metoda zwracająca wybranego pracownika sekretariatu
     * @param id - wartość id pracownika na liście
     * @return
     */
    public PracownikSekretariatu getPracownikSekretariatu(int id){
        return (PracownikSekretariatu) pracownicy.get(id).getAktualnaRola();
    }
}
