package GUI;

import Controller.*;
import Model.Klasa;
import Model.Osoba;
import Model.PracownikSekretariatu;
import Model.Uczen;
import Utils.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Klasa do zarządzania oknem do zgłaszania problemów przez ucznia
 */
public class ZglosProblemPanel {
    public JPanel zglosProblemPanel;
    private JPanel opcjePanel;
    private JComboBox pracownicyComboBox;
    private JPanel trescPanel;
    private JTextArea trescArea;
    private JLabel trescLabel;
    private JButton zglosButton;
    private JRadioButton czyZmieniacKlaseRadio;
    private JComboBox klasyComboBox;
    private JLabel opcjeLabel;
    private Uczen wybranyUczen;


    public ZglosProblemPanel(Uczen wybranyUczen) {
        this.wybranyUczen = wybranyUczen;
        czyZmieniacKlaseRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                klasyComboBox.setEnabled(czyZmieniacKlaseRadio.isSelected());
            }
        });
        zglosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Klasa wybranaKlasa = null;
                if(klasyComboBox.getSelectedItem() instanceof Klasa && czyZmieniacKlaseRadio.isSelected())
                    if(klasyComboBox.getSelectedItem().equals(wybranyUczen.getKlasa())){
                        JOptionPane.showMessageDialog(zglosProblemPanel,"Uczeń już należy do tej klasy!","Błąd",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                else {
                        wybranaKlasa = (Klasa) klasyComboBox.getSelectedItem();
                    }
                if(!(pracownicyComboBox.getSelectedItem() instanceof Osoba)){
                    JOptionPane.showMessageDialog(zglosProblemPanel,"Należy wybrać pracownika sekretariatu!","Błąd",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Osoba wybranyPracownik = (Osoba) pracownicyComboBox.getSelectedItem();
                    ProblemController.zglosProblem(wybranyUczen, trescArea.getText(), wybranaKlasa, (PracownikSekretariatu) wybranyPracownik.getAktualnaRola());
                    JOptionPane.showMessageDialog(zglosProblemPanel,"Problem zgłoszony pomyślnie","Wiadomość",JOptionPane.INFORMATION_MESSAGE);

                    WindowUtils.disposeWindow(zglosProblemPanel);
                }
            }
        });
    }

    /**
     * Metoda umożliwiająca wyłączenie listy klas do przeniesienia ucznia przy pomocy widgetu typu RadioBox
     * oraz generująca listę pracowników sekretariatu
     */
    private void createUIComponents() {
        pracownicyComboBox = new JComboBox();
        pracownicyComboBox.setToolTipText("Wybierz pracownika");
        pracownicyComboBox.setEnabled(true);
        List<Osoba> pracownicy = OsobyFetcher.getOsoby(OsobyFetcher.Role.PRACOWNIK);

        pracownicyComboBox.addItem("Wybierz pracownika");
        for(Osoba o : pracownicy){
            pracownicyComboBox.addItem(o);
        }
        pracownicyComboBox.setRenderer(new PracownicyListRenderer());

        klasyComboBox = new JComboBox();
        klasyComboBox.setToolTipText("Wybierz klasę");
        klasyComboBox.setEnabled(false);
        List<Klasa> klasy = KlasyFetcher.getKlasy();
        klasyComboBox.addItem("Wybierz klasę");
        for(Klasa k : klasy){
            klasyComboBox.addItem(k);
        }
        klasyComboBox.setRenderer(new KlasyListRenderer());
    }
}
