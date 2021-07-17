package GUI;

import Controller.ProblemController;
import Controller.ProblemyPracownikTableModel;
import Model.PracownikSekretariatu;
import Model.Problem;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa do zarządzania oknem z listą problemów obsługiwanych przez wybranego pracownika sekretariatu
 */
public class PracownikProblemyPanel {
    public JPanel rootPanel;
    private JTable problemyPracownikTable;
    private JButton wybierzButton;
    private JButton anulujButton;
    private ProblemyPracownikTableModel problemyPracownikTableModel;
    private PracownikSekretariatu wybranyPracownik;

    public PracownikProblemyPanel(PracownikSekretariatu wybranyPracownik){
        this.wybranyPracownik = wybranyPracownik;
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
        wybierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = problemyPracownikTable.getSelectedRow();
               if(row >= 0){
                   otworzOknoSzczegolowProblemu(problemyPracownikTableModel.getProblem(row));
               }
            }
        });
    }

    /**
     * Metoda do generowania tabeli problemów obsługiwanych przez pracownika sekretariatu i wypełniania jej treścią
     */
    private void createUIComponents() {
        problemyPracownikTableModel = new ProblemyPracownikTableModel(ProblemController.getProblemy(wybranyPracownik));
        problemyPracownikTable = new JTable(problemyPracownikTableModel);
        problemyPracownikTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    /**
     * Metoda do generowania okna ze szczegółami wybranego problemu
     * @param problem - Problem, którego szczegóły mają zostać wyświetlone
     */
    private void otworzOknoSzczegolowProblemu(Problem problem){
        WindowUtils.createWindow("Problem",new ProblemPracownikSzczegolyPanel(problem).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
