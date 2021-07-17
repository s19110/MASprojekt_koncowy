package GUI;

import Controller.OsobyFetcher;
import Controller.PracownicyListRenderer;
import Controller.ProblemController;
import Model.Osoba;
import Model.Problem;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Klasa do zarządzania oknem wyświetlającym szczegóły problemu dla pracownika sekretariatu
 */
public class ProblemPracownikSzczegolyPanel {
    public JPanel rootPanel;
    private JPanel optionsLabelPanel;
    private JButton zmienStatusButton;
    private JButton powiadomUczniaButton;
    private JButton przekazDyrektorowiButton;
    private JButton zamknijButton;
    private JTextArea dataZgloszeniaArea;
    private JLabel dataZgloszeniaLabel;
    private JLabel zglaszajacyLabel;
    private JLabel statusLabel;
    private JLabel trescLabel;
    private JLabel wybranaKlasaLabel;
    private JLabel zgloszonyBladLabel;
    private JTextArea zglaszajacyArea;
    private JTextArea statusArea;
    private JTextArea wybranaKlasaArea;
    private JTextArea zgloszonyBladArea;
    private JTextField trescField;
    private JLabel decyzjaDyrektoraLabel;
    private Problem wybranyProblem;
    private boolean przekazanoProblem;
    private JPanel statusPanel;
    private JRadioButton wTrakcieRadioButton;
    private JRadioButton rozwiazanyRadioButton;
    private JRadioButton anulowanyRadioButton;
    private ButtonGroup group;
    private static String[] actionCommands = new String[]{"w_trakcie","rozwiazany","anulowany"};

    public ProblemPracownikSzczegolyPanel(Problem wybranyProblem){
        this.wybranyProblem = wybranyProblem;
        this.przekazanoProblem = false;
        this.statusPanel = new JPanel();
        wTrakcieRadioButton = new JRadioButton(Problem.StatusProblemu.W_TRAKCIE.toString());
        wTrakcieRadioButton.setActionCommand(actionCommands[0]);
        rozwiazanyRadioButton = new JRadioButton(Problem.StatusProblemu.ROZWIAZANY.toString());
        rozwiazanyRadioButton.setActionCommand(actionCommands[1]);
        anulowanyRadioButton = new JRadioButton(Problem.StatusProblemu.ANULOWANY.toString());
        anulowanyRadioButton.setActionCommand(actionCommands[2]);
        group = new ButtonGroup();
        group.add(wTrakcieRadioButton);
        group.add(rozwiazanyRadioButton);
        group.add(anulowanyRadioButton);


        statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.PAGE_AXIS));
        statusPanel.add(wTrakcieRadioButton);
        statusPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        statusPanel.add(rozwiazanyRadioButton);
        statusPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        statusPanel.add(anulowanyRadioButton);

        //Listenery
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.disposeWindow(rootPanel);
            }
        });
        przekazDyrektorowiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox dyrektorzyBox = new JComboBox();
                List<Osoba> dyrektorzy = OsobyFetcher.getOsoby(OsobyFetcher.Role.DYREKTORZY);
                for(Osoba o : dyrektorzy){
                    dyrektorzyBox.addItem(o);
                }
                dyrektorzyBox.setRenderer(new PracownicyListRenderer());
                if(dyrektorzy.size() > 0) {
                    int option = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(rootPanel), dyrektorzyBox, "Wybierz dyrektora", JOptionPane.DEFAULT_OPTION);
                    if(option == JOptionPane.OK_OPTION) {
                        ProblemController.przekazProblemDoDyrektora((Osoba) dyrektorzyBox.getSelectedItem(), wybranyProblem);
                        przekazanoProblem = true;
                        updateGUI();
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(rootPanel), "Problem przekazany pomyślnie", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(rootPanel),"W bazie nie ma żadnego dyrektora", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        zmienStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(rootPanel), statusPanel, "Wybierz status", JOptionPane.DEFAULT_OPTION);
                if(option == JOptionPane.OK_OPTION) {
                    String wybranaOpcja = group.getSelection().getActionCommand();

                    System.out.println(wybranaOpcja);
                    if(wybranaOpcja == null)
                        return;

                    Problem.CzySukces decyzjaDyrektora = wybranyProblem.getDecyzjaDyrektora();
                    //Zmiana na "w trakcie rozwiązywania"
                    if (wybranaOpcja.equals(actionCommands[0])) {
                        if (wybranyProblem.getStatus() != Problem.StatusProblemu.NOWY) {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                    "Wybrany problem nie jest nowy!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        wybranyProblem.setStatus(Problem.StatusProblemu.W_TRAKCIE);
                        ProblemController.updateProblem(wybranyProblem);
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                "Status problemu został zmieniony", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        updateGUI();
                    }
                    //Zmiana na "Rozwiązany"
                    else if (wybranaOpcja.equals(actionCommands[1])) {
                        if (wybranyProblem.getStatus() != Problem.StatusProblemu.W_TRAKCIE) {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                    "Tylko rozpoczęty problem można rozwiązać!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(decyzjaDyrektora != null && decyzjaDyrektora.equals(Problem.CzySukces.NIE)){
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                    "Dyrektor nakazał anulować problem!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        wybranyProblem.setStatus(Problem.StatusProblemu.ROZWIAZANY);
                        wybranyProblem.setDecyzjaDyrektora(null);
                        ProblemController.updateProblem(wybranyProblem);
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                "Status problemu został zmieniony", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        updateGUI();
                    }
                    //Zmiana na "Anulowany"
                    else if (wybranaOpcja.equals(actionCommands[2])){
                        if (wybranyProblem.getStatus() != Problem.StatusProblemu.W_TRAKCIE
                            && wybranyProblem.getStatus() != Problem.StatusProblemu.NOWY) {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                    "Można anulować tylko problemy o statusie \""+Problem.StatusProblemu.NOWY
                                    +"\" lub \""+ Problem.StatusProblemu.W_TRAKCIE+"\"!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(decyzjaDyrektora != null && decyzjaDyrektora.equals(Problem.CzySukces.TAK)){
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                    "Dyrektor nakazał zatwierdzić rozwiązanie problemu!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        wybranyProblem.setStatus(Problem.StatusProblemu.ANULOWANY);
                        wybranyProblem.setDecyzjaDyrektora(null);
                        ProblemController.updateProblem(wybranyProblem);
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(statusPanel),
                                "Status problemu został zmieniony", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        updateGUI();
                    }
                }

            }
        });

        powiadomUczniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrzeslijPowiadomienieDialog dialog = new PrzeslijPowiadomienieDialog(SwingUtilities.getWindowAncestor(rootPanel),wybranyProblem);
                dialog.setLocationRelativeTo(rootPanel);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }

    /**
     * Metoda zapełniająca treścią poszczególne pola w oknie oraz zarządzająca przyciskami
     */
    private void createUIComponents() {
        trescField = new JTextField();
        trescField.setPreferredSize(new Dimension(300, 300));
        trescField.setText(wybranyProblem.getTresc());

        przekazDyrektorowiButton = new JButton("Przekaż dyrektorowi");
        updateGUI();
    }

    /**
     * Metoda do aktualizacji pól w oknie i aktywności przycisków
     */
    private void updateGUI(){
        dataZgloszeniaArea = WindowUtils.initTextArea(wybranyProblem.getDataZgloszenia().toString(), dataZgloszeniaArea);
        zglaszajacyArea = WindowUtils.initTextArea(wybranyProblem.getZglaszajacyUczen().getOsoba().getSimpleName(),zglaszajacyArea);
        statusArea = WindowUtils.initTextArea(wybranyProblem.getStatus().toString(),statusArea);
        wybranaKlasaArea = WindowUtils.initTextArea(wybranyProblem.getWybranaKlasa() != null ? wybranyProblem.getWybranaKlasa().toString() : "Brak",wybranaKlasaArea);
        zgloszonyBladArea = WindowUtils.initTextArea( wybranyProblem.getBlad() != null ?  wybranyProblem.getBlad().toString() : "Brak",zgloszonyBladArea);
        przekazDyrektorowiButton.setEnabled(czyMoznaPrzekazacDyrektorowi());
        decyzjaDyrektoraLabel = new JLabel();
        decyzjaDyrektoraLabel.setFont(new Font("Serif",Font.BOLD,16));
        decyzjaDyrektoraLabel.setForeground(Color.RED);
        if(wybranyProblem.getDecyzjaDyrektora() != null){
            if(wybranyProblem.getDecyzjaDyrektora().equals(Problem.CzySukces.TAK))
                decyzjaDyrektoraLabel.setText("Dyrektor uznał problem za rozwiązany - należy zmienić status problemu");
            else
                decyzjaDyrektoraLabel.setText("Dyrektor uznał, że należy anulować problem - należy zmienić status problemu");
        }
        powiadomUczniaButton = new JButton();
        powiadomUczniaButton.setEnabled(wybranyProblem.getPowiadomienie() == null);
    }


    private boolean czyMoznaPrzekazacDyrektorowi(){
        return wybranyProblem.getWybranaKlasa() != null && !przekazanoProblem
                && wybranyProblem.getRozpatrujacyDyrektor() == null && wybranyProblem.getStatus() == Problem.StatusProblemu.W_TRAKCIE;
    }



}
