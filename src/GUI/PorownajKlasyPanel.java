package GUI;

import Controller.KlasaController;
import Controller.ProblemController;
import Model.Klasa;
import Model.Problem;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa zarządzająca oknem do porównywania aktualnej i wybranej w problemie klasy szkolnej
 */
public class PorownajKlasyPanel {
    public JPanel rootPanel;
    private JPanel optionsLabelPanel;
    private JLabel wybranaLabel;
    private JTextArea wybranaArea;
    private JLabel dataWybranaLabel;
    private JTextArea wybranaDataArea;
    private JLabel wybranaLiczbaUczniowLabel;
    private JTextArea wybranaUczniowieArea;
    private JLabel aktualnaLabel;
    private JTextArea aktualnaArea;
    private JLabel aktualnaDataLabel;
    private JTextArea aktualnaDataArea;
    private JLabel aktualnaUczniowieLabel;
    private JTextArea aktualnaUczniowieArea;
    private JButton przeniesUczniaButton;
    private JButton zamknijButton;
    private JLabel wykrytyProblemLabel;
    private Problem wybranyProblem;
    private Klasa wybranaKlasa;
    private Klasa aktualnaKlasa;

    /**
     * Konstruktor
     * @param wybranyProblem - problem wyrażający chęć zmiany klasy
     */
    public PorownajKlasyPanel(Problem wybranyProblem){
        this.wybranyProblem = wybranyProblem;

        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
        przeniesUczniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                przeniesUcznia();
            }
        });
    }


    private void createUIComponents() {
        updateGUI();
    }

    /**
     * Metoda umożliwiająca przeniesienie ucznia do innej klasy przy pomocy GUI
     */
    private void przeniesUcznia(){
        JTextArea noweIdArea = new JTextArea();
        noweIdArea.setPreferredSize(new Dimension(40, 20));
        int option = JOptionPane.showConfirmDialog(rootPanel,noweIdArea,"Podaj nowe ID ucznia",JOptionPane.DEFAULT_OPTION);
        String noweID = noweIdArea.getText();
        if(option == JOptionPane.OK_OPTION && noweID != null && noweID.length() > 0){
            if(KlasaController.czyIDJestZajete(wybranaKlasa,noweID)){
                JOptionPane.showMessageDialog(rootPanel,"Podane ID już znajduje się w wybranej klasie","Błąd",JOptionPane.ERROR_MESSAGE);
                return;
            }
            ProblemController.przeniesUcznia(wybranyProblem,noweID);
            JOptionPane.showMessageDialog(rootPanel,"Uczeń został przeniesiony do klasy " + wybranaKlasa.getSimpleName(),"Sukces",JOptionPane.INFORMATION_MESSAGE);
            WindowUtils.disposeWindow(rootPanel);
        }
    }

    /**
     * Metoda aktualizująca elementy GUI
     */
    private void updateGUI(){

        this.wybranaKlasa = wybranyProblem.getWybranaKlasa();
        this.aktualnaKlasa = wybranyProblem.getZglaszajacyUczen().getKlasa();

        wybranaArea = WindowUtils.initTextArea(wybranaKlasa.getSimpleName(),wybranaArea);
        aktualnaArea = WindowUtils.initTextArea(aktualnaKlasa.getSimpleName(),aktualnaArea);
        wybranaDataArea = WindowUtils.initTextArea(wybranaKlasa.getDataRozpoczeciaEdukacji().toString(),wybranaDataArea);
        aktualnaDataArea = WindowUtils.initTextArea(aktualnaKlasa.getDataRozpoczeciaEdukacji().toString(),aktualnaDataArea);
        wybranaUczniowieArea = WindowUtils.initTextArea(Integer.toString(wybranaKlasa.getLiczbaUczniow()),wybranaUczniowieArea);
        aktualnaUczniowieArea = WindowUtils.initTextArea(Integer.toString(aktualnaKlasa.getLiczbaUczniow()),aktualnaUczniowieArea);

        wykrytyProblemLabel = new JLabel();
        Font f = wykrytyProblemLabel.getFont();
        wykrytyProblemLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

        przeniesUczniaButton = new JButton();
        if(!wybranaKlasa.sprawdzLimitUczniow()){
            System.out.println("A");
            wykrytyProblemLabel.setText("Wykryto problem: przekroczenie maksymalnej liczby uczniów w klasie!");
            wybranyProblem.setBlad(Problem.TypBledu.ILOSC_UCZNIOW);
            przeniesUczniaButton.setEnabled(false);
        }
        else if(wybranaKlasa.getDataRozpoczeciaEdukacji().getYear() != aktualnaKlasa.getDataRozpoczeciaEdukacji().getYear()){
            System.out.println("B");
            wykrytyProblemLabel.setText("Wykryto problem: klasy nie rozpoczęły edukacji w tym samym czasie!");
            wybranyProblem.setBlad(Problem.TypBledu.ROZNICA_DAT);
            przeniesUczniaButton.setEnabled(false);
        }
        else{
            System.out.println("C");
            wykrytyProblemLabel.setText("Uczeń może zostać przeniesiony");
            przeniesUczniaButton.setEnabled(true);
        }
        wykrytyProblemLabel.setVisible(true);
    }
}
