package GUI;

import Model.Problem;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa zarządzająca oknem ze szczegółami problemu wybranego przez ucznia
 */
public class ProblemUczenSzczegolyPanel {
    public JPanel rootPanel;
    private JPanel optionsLabelPanel;
    private JLabel statusLabel;
    private JTextArea statusArea;
    private JLabel powiadomienieLabel;
    private JLabel dataZgloszeniaLabel;
    private JLabel pracownikLabel;
    private JLabel trescLabel;
    private JLabel wybranaKlasaLabel;
    private JTextArea dataZgloszeniaArea;
    private JTextArea pracownikArea;
    private JTextArea wybranaKlasaArea;
    private JTextField trescField;
    private JTextField powiadomienieField;
    private JButton zamknijButton;
    private Problem wybranyProblem;


    public ProblemUczenSzczegolyPanel(Problem wybranyProblem) {
        this.wybranyProblem = wybranyProblem;
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
    }

    private void createUIComponents() {
        updateGUI();
    }

    /**
     * Metoda aktualizująca elementy GUI
     */
    private void updateGUI(){
        trescField = new JTextField();
        trescField.setPreferredSize(new Dimension(300, 200));
        trescField.setText(wybranyProblem.getTresc());
        powiadomienieField = new JTextField();
        powiadomienieField.setPreferredSize(new Dimension(300,100));
        powiadomienieField.setText(wybranyProblem.getPowiadomienie() == null ? "Pracownik nie przesłał jeszcze powiadomienia" : wybranyProblem.getPowiadomienie());

        statusArea = WindowUtils.initTextArea(wybranyProblem.getStatus().toString(),statusArea);
        dataZgloszeniaArea = WindowUtils.initTextArea(wybranyProblem.getDataZgloszenia().toString(), dataZgloszeniaArea);
        wybranaKlasaArea = WindowUtils.initTextArea(wybranyProblem.getWybranaKlasa() == null ? "Brak" : wybranyProblem.getWybranaKlasa().toString(),wybranaKlasaArea);
        pracownikArea = WindowUtils.initTextArea(wybranyProblem.getObslugujacyPracownik() == null ? "Brak" : wybranyProblem.getObslugujacyPracownik().getOsoba().getSimpleName(),pracownikArea);
    }


}
