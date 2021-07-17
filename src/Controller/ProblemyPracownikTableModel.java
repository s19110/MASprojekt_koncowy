package Controller;

import Model.Problem;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Klasa umożliwiająca wyświetlenie listy problemów obsługiwanych przez pracownika sekretariatu
 * w oknie z klasy PracownikProblemyPanel
 */
public class ProblemyPracownikTableModel extends AbstractTableModel {
    private List<Problem> problemy;

    public ProblemyPracownikTableModel(List<Problem> problemy){
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
            case 1: nazwa = "Zgłaszający"; break;
            case 2: nazwa = "Status"; break;
        }
        return nazwa;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Problem problem = problemy.get(rowIndex);
        Object wartosc = null;
        switch (columnIndex){
            case 0: wartosc = problem.getDataZgloszenia(); break;
            case 1: wartosc = problem.getZglaszajacyUczen().getOsoba().getSimpleName(); break;
            case 2: wartosc = problem.getStatus(); break;
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
