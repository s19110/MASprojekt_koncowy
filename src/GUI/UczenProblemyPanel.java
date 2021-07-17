package GUI;

import Controller.ProblemController;
import Controller.ProblemyUczenTableModel;
import Model.Problem;
import Model.Uczen;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa do zarządzania oknem z listą problemów zgłoszonych przez ucznia
 */
public class UczenProblemyPanel {
    public JPanel rootPanel;
    private JTable uczenProblemyTable;
    private JButton wybierzButton;
    private JButton anulujButton;
    private ProblemyUczenTableModel problemyUczenTableModel;
    private Uczen wybranyUczen;

    public UczenProblemyPanel(Uczen wybranyUczen){
        this.wybranyUczen = wybranyUczen;
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
        wybierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = uczenProblemyTable.getSelectedRow();
                if(row >= 0){
                    otworzOknoSzczegolowProblemu(problemyUczenTableModel.getProblem(row));
                }
            }
        });
    }

    /**
     * Metoda do generowania tabeli z problemami i zapełniania ją treścią
     */
    private void createUIComponents() {
        problemyUczenTableModel = new ProblemyUczenTableModel(ProblemController.getProblemy(wybranyUczen));
        uczenProblemyTable = new JTable(problemyUczenTableModel);
        uczenProblemyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void otworzOknoSzczegolowProblemu(Problem problem){
        problem.setSprawdzonoPowiadomienie(true);
        ProblemController.updateProblem(problem);
        WindowUtils.createWindow("Problem",new ProblemUczenSzczegolyPanel(problem).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
