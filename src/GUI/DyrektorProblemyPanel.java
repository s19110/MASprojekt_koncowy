package GUI;

import Controller.ProblemController;
import Controller.ProblemyDyrektorTableModel;
import Model.Nauczyciel;
import Model.Problem;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa zarządzająca oknem z listą problemów przekazaną wybranemu dyrektorowi
 */
public class DyrektorProblemyPanel {
    public JPanel rootPanel;
    private JTable problemyTable;
    private JButton wybierzButton;
    private JButton anulujButton;
    private Nauczyciel wybranyDyrektor;
    private ProblemyDyrektorTableModel tableModel;

    public DyrektorProblemyPanel(Nauczyciel wybranyDyrektor){
        this.wybranyDyrektor = wybranyDyrektor;
        wybierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = problemyTable.getSelectedRow();
                if(row >= 0){
                    otworzOknoSzczegolowProblemu(tableModel.getProblem(row));
                }
            }
        });
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
    }

    private void createUIComponents() {
        tableModel = new ProblemyDyrektorTableModel(ProblemController.getProblemy(wybranyDyrektor));
        problemyTable = new JTable(tableModel);
        problemyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Metoda otwierająca okno ze szczegółami wybranego problemu - dla dyrektora
     * @param problem - problem wybrany z listy
     */
    private void otworzOknoSzczegolowProblemu(Problem problem){
        SwingUtilities.getWindowAncestor(rootPanel).dispose();
        WindowUtils.createWindow("Problem",new ProblemDyrektorSzczegolyPanel(problem).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
