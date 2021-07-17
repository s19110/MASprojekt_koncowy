package GUI;

import Controller.OsobyFetcher;
import Controller.PracownikTableModel;
import Model.PracownikSekretariatu;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiedzialna za zarządzaniem oknem z listą pracowników sekretariatu
 */
public class PracownikPanel {
    public JPanel pracownikPanel;
    private JTable pracownicyTable;
    private JButton wybierzPracownikaButton;
    private PracownikTableModel pracownikTableModel;

    public PracownikPanel() {
        wybierzPracownikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = pracownicyTable.getSelectedRow();
                if(row >= 0) {
                    otworzObslugiwaneProblemyPanel(pracownikTableModel.getPracownikSekretariatu(row));
                }
            }
        });
    }
    /**
     * Metoda do generowania okna z listą problemów obsługiwanych przez wybranego pracownika sekretariatu
     * @param wybranyPracownik - pracownik, dla którego ma być otwarta lista zgłoszonych problemów
     */
    private void otworzObslugiwaneProblemyPanel(PracownikSekretariatu wybranyPracownik) {
        SwingUtilities.getWindowAncestor(pracownikPanel).dispose();
        WindowUtils.createWindow("Wybierz problem",new PracownikProblemyPanel(wybranyPracownik).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Metoda do generowania tabeli pracowników sekretariatu i wypełniania jej treścią
     */
    private void createUIComponents() {
        pracownikTableModel = new PracownikTableModel(OsobyFetcher.getOsoby(OsobyFetcher.Role.PRACOWNIK));
        pracownicyTable = new JTable(pracownikTableModel);
        pracownicyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
