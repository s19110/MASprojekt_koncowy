package GUI;

import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiedzialna za stworzenie okna do wyboru osób o odpowiednich rolach
 */
public class MainMenu {
    public JPanel rootPanel;
    private JButton wybierzUczniaButton;
    private JButton wybierzPracownikaButton;
    private JButton wybierzDyrektoraButton;

    public MainMenu() {
        wybierzUczniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otworzPanelUczniow();
            }
        });
        wybierzPracownikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otworzPanelPracownikow();
            }
        });
        wybierzDyrektoraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otworzPanelDyrektorow();
            }
        });
    }

    /**
     * Metoda generująca okno z listą uczniów
     */
    private void otworzPanelUczniow(){
        WindowUtils.createWindow("Wybór ucznia",new UczenPanel().uczenPanel,JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Metoda generująca okno z listą pracowników sekretariatu
     */
    private void otworzPanelPracownikow(){
        WindowUtils.createWindow("Wybór pracownika sekretariatu",new PracownikPanel().pracownikPanel,JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Metoda generująca okno z listą dyrektorów
     */
    private void otworzPanelDyrektorow(){
        WindowUtils.createWindow("Wybór dyrektora",new DyrektorzyPanel().dyrektorzyPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
