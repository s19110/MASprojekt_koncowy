package GUI;

import Model.Problem;
import Model.Uczen;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa zarządzająca oknem ze szczegółami wybranego przez dyrektora problemu
 */
public class ProblemDyrektorSzczegolyPanel {
    public JPanel rootPanel;
    private JPanel optionsLabelPanel;
    private JLabel dataZgloszeniaLabel;
    private JTextArea dataZgloszeniaArea;
    private JLabel zglaszajacyLabel;
    private JLabel pracownikLabel;
    private JLabel trescLabel;
    private JLabel aktualnaKlasaLabel;
    private JTextArea zglaszajacyArea;
    private JTextArea aktualnaKlasaArea;
    private JTextArea pracownikArea;
    private JTextField trescField;
    private JTextArea wybranaKlasaArea;
    private JLabel wybranaKlasaLabel;
    private JButton porownajKlasyButton;
    private JButton przekazDecyzjeButton;
    private JButton zamknijButton;
    private Problem wybranyProblem;


    public ProblemDyrektorSzczegolyPanel(Problem wybranyProblem){
        this.wybranyProblem = wybranyProblem;
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
        porownajKlasyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otworzOknoDoPorownywaniaKlas();
            }
        });
        przekazDecyzjeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DecyzjaDyrektoraDialog dialog = new DecyzjaDyrektoraDialog(SwingUtilities.getWindowAncestor(rootPanel),wybranyProblem);
                dialog.setLocationRelativeTo(rootPanel);
                dialog.pack();
                dialog.setVisible(true);
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

        Uczen zglaszajacyUczen = wybranyProblem.getZglaszajacyUczen();
        dataZgloszeniaArea = WindowUtils.initTextArea(wybranyProblem.getDataZgloszenia().toString(), dataZgloszeniaArea);
        zglaszajacyArea = WindowUtils.initTextArea(zglaszajacyUczen == null ? "Brak" :  zglaszajacyUczen.getOsoba().getSimpleName(), zglaszajacyArea);
        pracownikArea = WindowUtils.initTextArea(wybranyProblem.getObslugujacyPracownik() == null ? "Brak" : wybranyProblem.getObslugujacyPracownik().getOsoba().getSimpleName(),pracownikArea);
        aktualnaKlasaArea = WindowUtils.initTextArea( zglaszajacyUczen != null && zglaszajacyUczen.getKlasa() != null ?
                zglaszajacyUczen.getKlasa().toString() : "Brak", aktualnaKlasaArea);
        wybranaKlasaArea = WindowUtils.initTextArea(wybranyProblem.getWybranaKlasa() == null ? "Brak" : wybranyProblem.getWybranaKlasa().toString(),wybranaKlasaArea);

        porownajKlasyButton = new JButton();
        porownajKlasyButton.setEnabled(wybranyProblem.getWybranaKlasa() != wybranyProblem.getZglaszajacyUczen().getKlasa());
    }

    /**
     * Metoda otwierająca okno do porównywania klas opisane w klasie PorownajKlasyPanel
     */
    private void otworzOknoDoPorownywaniaKlas(){
        WindowUtils.createWindow("Porównanie klas",new PorownajKlasyPanel(wybranyProblem).rootPanel,JFrame.DISPOSE_ON_CLOSE);
    }
}
