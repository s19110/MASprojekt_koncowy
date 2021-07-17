package Controller;

import Model.Problem;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Klasa umożliwiająca wyświetlenie listy problemów przekazanych dyrektorowi
 * w oknie z klasy DyrektorProblemyPanel
 */
public class ProblemyDyrektorTableModel extends AbstractTableModel {
    private List<Problem> problemy;

    public ProblemyDyrektorTableModel(List<Problem> problemy){
        this.problemy = problemy;
    }

    @Override
    public int getRowCount() {
        return problemy.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column){
        String nazwa = "???";
        switch (column){
            case 0: nazwa = "Data zgłoszenia"; break;
            case 1: nazwa = "Pracownik obsługujący"; break;
            case 2: nazwa = "Zgłaszający uczeń"; break;
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
            case 2: wartosc = problem.getZglaszajacyUczen().getOsoba().getSimpleName(); break;
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
