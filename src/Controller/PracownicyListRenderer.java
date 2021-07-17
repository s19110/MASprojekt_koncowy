package Controller;

import Model.Osoba;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa umożliwiająca wyświetlanie imion i nazwisk pracowników sekretariatu w liście z klasy ZglosProblemPanel
 */
public class PracownicyListRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof Osoba) {
            value = ((Osoba)value).getSimpleName();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}
