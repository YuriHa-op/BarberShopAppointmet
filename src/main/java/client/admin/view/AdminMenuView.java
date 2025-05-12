package client.admin.view;

import common.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminMenuView extends JFrame {
    private JButton viewAppointmentsButton;
    private JButton createdAppointmentsButton;
    private JButton finishedAppointmentsButton;
    private JButton logoutButton;
    private JButton cancelledAppointmentsButton;

    public AdminMenuView() {
        setTitle("Admin Menu View");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel panel = new common.ui.BackgroundPanel("/background.png");
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Biglang Gwapo Barber Shop", SwingConstants.CENTER);
        titleLabel.setFont(theme.createFont(20));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        viewAppointmentsButton = theme.createButton("View Appointments");
        panel.add(viewAppointmentsButton, gbc);

        gbc.gridy = 2;
        createdAppointmentsButton = theme.createButton("View Created Appointments");
        panel.add(createdAppointmentsButton, gbc);

        gbc.gridy = 3;
        finishedAppointmentsButton = theme.createButton("View Finished Appointments");
        panel.add(finishedAppointmentsButton, gbc);

        gbc.gridy = 4;
        cancelledAppointmentsButton = theme.createButton("View Cancelled Appointments");
        panel.add(cancelledAppointmentsButton, gbc);

        gbc.gridy = 5;
        logoutButton = theme.createButton("Logout");
        panel.add(logoutButton, gbc);
        add(panel);
    }
    public JButton getCancelledAppointmentsButton() { return cancelledAppointmentsButton; }
    public JButton getViewAppointmentsButton() { return viewAppointmentsButton; }
    public JButton getCreatedAppointmentsButton() { return createdAppointmentsButton; }
    public JButton getFinishedAppointmentsButton() { return finishedAppointmentsButton; }
    public JButton getLogoutButton() { return logoutButton; }


    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void minimizeAndRestoreOnClose(JFrame childFrame) {
        setState(JFrame.ICONIFIED);

        childFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setState(JFrame.NORMAL);
            }
        });
    }
}