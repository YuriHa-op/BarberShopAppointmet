package common.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Theme {
    // Singleton pattern
    private static Theme instance;

    public static Theme getInstance() {
        if (instance == null) {
            instance = new Theme();
        }
        return instance;
    }

    //  font
    public Font createFont(int size) {
        return new Font("Monospaced", Font.BOLD, size);
    }

    //  border
    public Border createBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 40), 2),
                BorderFactory.createLineBorder(new Color(120, 120, 120), 1)
        );
    }

    // butto
    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(createFont(14));
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(createBorder());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(138, 138, 138));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }
        });
        return button;
    }

    // Calendar
    public JButton createCalendarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(createFont(14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(createBorder());
        button.setPreferredSize(new Dimension(60, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Color current = button.getBackground();
                button.setBackground(current.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Color current = button.getBackground();
                button.setBackground(current.darker());
            }
        });
        return button;
    }

    // legend items
    public JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel(text);
        label.setFont(createFont(14));
        label.setForeground(Color.WHITE);

        item.add(colorBox);
        item.add(label);
        return item;
    }
    // Theme class
    public void styleTable(JTable table) {
        table.setFont(createFont(12));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(70, 70, 70));
        table.setGridColor(new Color(40, 40, 40));
        table.getTableHeader().setFont(createFont(14));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(100, 100, 100));
        table.setSelectionForeground(Color.WHITE);
    }
}