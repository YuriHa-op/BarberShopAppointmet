package client.client.view;

import common.ui.Theme;

import javax.swing.*;
import java.awt.*;

public class ClientMenu extends JFrame {
    private JButton showCalendarButton;
    private JButton bookButton;
    private JButton logoutButton;
    private String username;

    public ClientMenu(String username) {
        this.username = username;
        setTitle("Client Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();


        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("WELCOME, " + username.toUpperCase(), JLabel.CENTER);
        titleLabel.setFont(theme.createFont(24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Butto
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setOpaque(false);

        showCalendarButton = theme.createButton("View Available Appointments");
        bookButton = theme.createButton("View My Appointments");
        logoutButton = theme.createButton("Logout");

        buttonPanel.add(showCalendarButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(logoutButton);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Get
    public JButton getShowCalendarButton() {
        return showCalendarButton;
    }

    public JButton getBookButton() {
        return bookButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public String getUsername() {
        return username;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}