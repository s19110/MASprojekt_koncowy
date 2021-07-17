package GUI;

import Controller.DyrektorzyTableModel;
import Controller.OsobyFetcher;
import Model.Nauczyciel;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiedzialna za stworzenie okna z listą dyrektorów szkoły
 */
public class DyrektorzyPanel {
    public JPanel dyrektorzyPanel;
    private JTable dyrektorzyTable;
    private JButton wybierzButton;
    private DyrektorzyTableModel dyrektorzyTableModel;

    public DyrektorzyPanel() {
        wybierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = dyrektorzyTable.getSelectedRow();
                if(row >= 0) {
                    otworzOknoProblemowDyrektora(dyrektorzyTableModel.getDyrektor(row));
                }
            }
        });
    }

    /**
     * Metoda tworząca tabelę z dyrektorami i wypełniająca ją treścią
     */
    private void createUIComponents() {
        dyrektorzyTableModel = new DyrektorzyTableModel(OsobyFetcher.getOsoby(OsobyFetcher.Role.DYREKTORZY));
        dyrektorzyTable = new JTable(dyrektorzyTableModel);
        dyrektorzyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Metoda otwierająca okno z listą problemów przekazanych danemu dyrektorowi
     * @param wybranyDyrektor - dyrektor, któremu przekazane problemy chcemy wyświetlić
     */
    private void otworzOknoProblemowDyrektora(Nauczyciel wybranyDyrektor){
        SwingUtilities.getWindowAncestor(dyrektorzyPanel).dispose();
        WindowUtils.createWindow("Wybierz problem",new DyrektorProblemyPanel(wybranyDyrektor).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
