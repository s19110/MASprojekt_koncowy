package Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa wspomagająca zarządzanie oknami w GUI
 */
public class WindowUtils {


    /**
     * Metoda automatycznie tworząca okno
     * @param name - tytuł okna
     * @param rootPanel - główny panel okna
     * @param closeAction - co ma się wydarzyć przy zamknięciu okna
     */
    public static void createWindow(String name, JPanel rootPanel, int closeAction){
        JFrame frame = new JFrame(name);
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(closeAction);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Metoda zamykająca okno, w którym znajduje się podany panel
     * @param panel - panel, którego okno należy zamknąć
     */
    public static void disposeWindow(JPanel panel){
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
        parentFrame.dispose();
    }

    /**
     * Metoda pomocnicza do usystematyzowania procesu zapełniania pól tekstowych
     * @param tresc - zawartość danego pola
     * @return - JTextArea z podaną zawartością
     */
    public static JTextArea initTextArea(String tresc, JTextArea areaToChange){
        if(areaToChange == null) {
            JTextArea area = new JTextArea();
            area.setText(tresc);
            area.setPreferredSize(new Dimension(200, 20));
            area.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            return area;
        }else
            areaToChange.setText(tresc);
        return areaToChange;
    }
}
