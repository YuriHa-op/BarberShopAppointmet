package client.client.view;

import common.ui.Theme;
import common.utility.Callback;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserAppointmentsView extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton cancelButton;
    private Callback refreshCallback;
    private Callback cancelCallback;
    private Callback bookingCallback; // callback

    public UserAppointmentsView(String username) {
        setTitle("UserAppointmentsView - " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();

        JLabel titleLabel = new JLabel("MY APPOINTMENTS", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Date", "Username", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(tableModel);
        theme.styleTable(appointmentsTable);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.getViewport().setBackground(new Color(70, 70, 70));
        scrollPane.setBorder(theme.createBorder());

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        refreshButton = theme.createButton("Refresh");
        cancelButton = theme.createButton("Cancel Appointment");

        refreshButton.addActionListener(e -> {
            if (refreshCallback != null) {
                refreshCallback.execute();
            }
        });

        cancelButton.addActionListener(e -> {
            if (cancelCallback != null) {
                cancelCallback.execute();
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public void setRefreshCallback(Callback refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void setCancelCallback(Callback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    public void setBookingCallback(Callback bookingCallback) {
        this.bookingCallback = bookingCallback;
    }

    public JTable getAppointmentsTable() {
        return appointmentsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}