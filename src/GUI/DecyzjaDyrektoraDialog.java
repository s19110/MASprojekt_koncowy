package GUI;

import Controller.ProblemController;
import Model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasa zarządzająca oknem dialogowym umożliwiającym dyrektorowi przekazanie podjętej decyzji
 */
public class DecyzjaDyrektoraDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton sukcesRadioButton;
    private JRadioButton bladRadioButton;
    private ButtonGroup group;
    private Problem wybranyProblem;
    private static final String[] STATUSY = new String[]{"sukces","blad"};

    public DecyzjaDyrektoraDialog(Window owner, Problem wybranyProblem) {
        super(owner,"Decyzja");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.wybranyProblem = wybranyProblem;
        updateGUI();
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String typPowiadomienia = group.getSelection() != null ? group.getSelection().getActionCommand() : null;
        if(typPowiadomienia != null) {
            wybranyProblem.setDecyzjaDyrektora(typPowiadomienia.equals(STATUSY[0]) ? Problem.CzySukces.TAK : Problem.CzySukces.NIE);
            wybranyProblem.getRozpatrujacyDyrektor().usunPrzekazanyProblem(wybranyProblem);
            ProblemController.updateProblem(wybranyProblem);
            JOptionPane.showMessageDialog(this.rootPane,"Powiadomienie zostało przesłane, problem zostaje usunięty z listy do rozpatrzenia","Sukces",JOptionPane.INFORMATION_MESSAGE);
            dispose();
            getOwner().dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        group = new ButtonGroup();
        sukcesRadioButton = new JRadioButton();
        bladRadioButton = new JRadioButton();
        sukcesRadioButton.setActionCommand(STATUSY[0]);
        bladRadioButton.setActionCommand(STATUSY[1]);
        group.add(sukcesRadioButton);
        group.add(bladRadioButton);

    }

    /**
     * Metoda odpowiednio konfigurująca elementy okna
     */
    private void updateGUI(){

        if(wybranyProblem.getBlad() == null){
            sukcesRadioButton.setSelected(true);
            bladRadioButton.setText("Nie wykryto błędu - rozkaz anulowania");
        }
        else if(wybranyProblem.getBlad().equals(Problem.TypBledu.ILOSC_UCZNIOW)){
            bladRadioButton.setSelected(true);
            bladRadioButton.setText("Błąd - Typ błędu: limit uczniów w wybranej klasie");
            sukcesRadioButton.setEnabled(false);
        }
        else if (wybranyProblem.getBlad().equals(Problem.TypBledu.ROZNICA_DAT)){
            bladRadioButton.setSelected(true);
            bladRadioButton.setText("Błąd - Typ błędu: wybrana klasa rozpoczęła edukację w innym czasie");
            sukcesRadioButton.setEnabled(false);
        }

    }
}
