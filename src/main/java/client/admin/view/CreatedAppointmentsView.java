package client.admin.view;

import common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class CreatedAppointmentsView extends JFrame {/// //////will remove
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public static final String[] TIME_SLOTS = { "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM" };

    public CreatedAppointmentsView() {
        setTitle("Barber - Created Appointments");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();


        JLabel titleLabel = new JLabel("CREATED APPOINTMENTS", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(20));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // setup
        String[] columnNames = {"ID", "Date", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        // Apply
        theme.styleTable(appointmentsTable);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.getViewport().setBackground(new Color(70, 70, 70));
        scrollPane.setBorder(theme.createBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        refreshButton = theme.createButton("Refresh");
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    public String[] getTimeSlots() {
        return TIME_SLOTS;
    }


    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }
}