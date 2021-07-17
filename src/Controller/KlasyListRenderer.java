package Controller;

import Model.Klasa;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa umożliwiająca wyświetlanie numeru i litery klasy szkolnej w liście z klasy ZglosProblemPanel
 */
public class KlasyListRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof Klasa) {
            value = ((Klasa)value).getSimpleName();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}
