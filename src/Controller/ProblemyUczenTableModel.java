package Controller;

import Model.Problem;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Klasa umożliwiająca wyświetlenie listy problemów zgłoszonych przez ucznia
 * w oknie z klasy UczenProblemyPanel
 */
public class ProblemyUczenTableModel extends AbstractTableModel {
    private List<Problem> problemy;

    public ProblemyUczenTableModel(List<Problem> problemy){
        this.problemy = problemy;
    }

    @Override
    public int getRowCount() {
        return problemy.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column){
        String nazwa = "???";
        switch (column){
            case 0: nazwa = "Data zgłoszenia"; break;
            case 1: nazwa = "Pracownik obsługujący"; break;
            case 2: nazwa = "Status"; break;
            case 3: nazwa = "Powiadomienie"; break;
        }
        return nazwa;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Problem problem = problemy.get(rowIndex);
        Object wartosc = null;
        switch (columnIndex){
            case 0: wartosc = problem.getDataZgloszenia(); break;
            case 1: wartosc = problem.getObslugujacyPracownik().getOsoba().getSimpleName(); break;
            case 2: wartosc = problem.getStatus(); break;
            case 3: wartosc = problem.isSprawdzonoPowiadomienie() ? "" : "!!!"; break;
        }
        return wartosc;
    }
    /**
     * Metoda zwracająca wybrany problem z listy
     * @param id - id problemu na liście
     * @return - wybrany problem
     */
    public Problem getProblem(int id){
        return problemy.get(id);
    }
}
