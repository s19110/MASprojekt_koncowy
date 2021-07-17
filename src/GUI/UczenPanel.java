package GUI;

import Controller.OsobyFetcher;
import Controller.UczenTableModel;
import Model.Uczen;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa do zarządzania oknem z listą uczniów
 */
public class UczenPanel {
    JPanel uczenPanel;
    private JTable uczenTable;
    private JButton zglosButton;
    private JButton przegladajProblemyButton;
    private UczenTableModel uczenTableModel;

    public UczenPanel() {
        zglosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(uczenTableModel.getUczen(uczenTable.getSelectedRow()));
                otworzZglosProblemPanel((Uczen) uczenTableModel.getUczen(uczenTable.getSelectedRow()).getAktualnaRola());
            }
        });
        przegladajProblemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = uczenTable.getSelectedRow();
                if(row >= 0) {
                    otworzZgloszoneProblemyPanel((Uczen) uczenTableModel.getUczen(row).getAktualnaRola());
                }
            }
        });
    }

    private void createUIComponents() {
        uczenTableModel = new UczenTableModel(OsobyFetcher.getOsoby(OsobyFetcher.Role.UCZEN));
        uczenTable = new JTable(uczenTableModel);
        uczenTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Metoda do generowania okna do zgłaszania problemów przez ucznia
     * @param wybranyUczen - uczeń, który chce zgłosić problem
     */
    private void otworzZglosProblemPanel(Uczen wybranyUczen){
        WindowUtils.createWindow("Zgłoś problem",new ZglosProblemPanel(wybranyUczen).zglosProblemPanel,JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Metoda do generowania okna z listą zgłoszonych przez wybranego ucznia problemów
     * @param wybranyUczen - uczen, dla którego ma być otwarta lista zgłoszonych problemów
     */
    private void otworzZgloszoneProblemyPanel(Uczen wybranyUczen){
        WindowUtils.createWindow("Zgłoszone problemy",new UczenProblemyPanel(wybranyUczen).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
