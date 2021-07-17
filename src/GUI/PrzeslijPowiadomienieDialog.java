package GUI;

import Controller.ProblemController;
import Model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasa zarządzająca oknem dialogowym do przesyłania powiadomienia uczniowi przez pracownika sekretariatu
 */
public class PrzeslijPowiadomienieDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton sukcesRadio;
    private JRadioButton bladRadio;
    private JRadioButton anulowanieRadio;
    private JTextArea powiadomienieTrescArea;
    private ButtonGroup typPowiadomieniaGroup;
    private Problem wybranyProblem;

    public PrzeslijPowiadomienieDialog(Window owner, Problem wybranyProblem) {
        super(owner,"Powiadomienie");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.wybranyProblem = wybranyProblem;
        typPowiadomieniaGroup = new ButtonGroup();
        sukcesRadio.setActionCommand("Sukces: ");
        anulowanieRadio.setActionCommand("Anulowano problem:");
        bladRadio.setActionCommand("Błąd:");
        typPowiadomieniaGroup.add(sukcesRadio);
        typPowiadomieniaGroup.add(bladRadio);
        typPowiadomieniaGroup.add(anulowanieRadio);

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

    /**
     * Po zatwierdzeniu powiadomienie jest przesyłane do ucznia
     */
    private void onOK() {
        StringBuilder builder = new StringBuilder();
        String typPowiadomienia = typPowiadomieniaGroup.getSelection() != null ? typPowiadomieniaGroup.getSelection().getActionCommand() : null;
        if(typPowiadomienia != null)
            builder.append(typPowiadomienia);
        if(powiadomienieTrescArea.getText() != null && powiadomienieTrescArea.getText().length() > 0)
            builder.append(powiadomienieTrescArea.getText());
        else
            builder.append("Brak informacji.");
        //System.out.println(builder);
        wybranyProblem.setPowiadomienie(builder.toString());
        wybranyProblem.setSprawdzonoPowiadomienie(false);
        ProblemController.updateProblem(wybranyProblem);
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(super.getOwner()),
                "Powiadomienie zostało wysłane", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
