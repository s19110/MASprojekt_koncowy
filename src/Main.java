import GUI.MainMenu;
import GUI.ProblemUczenSzczegolyPanel;
import GUI.ZglosProblemPanel;
import Utils.WindowUtils;

import javax.swing.*;

/**
 * Klasa służąca do uruchomienia aplikacji
 */
public class Main {
    public static void main(String[] args) {
        WindowUtils.createWindow("Wybór roli w systemie",new MainMenu().rootPanel,JFrame.EXIT_ON_CLOSE);
    }
}
