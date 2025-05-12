package client.admin.view;

import client.admin.model.CancelledAppointmentsModel;
import common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class CancelledAppointmentsView extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JButton refreshButton;
    private JButton closeButton;
    private JButton deleteButton;

    public CancelledAppointmentsView() {
        setTitle("Cancelled Appointments");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        Theme theme = Theme.getInstance();

        // Main panel with background
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // North panel - Title and Search
        JPanel northPanel = new JPanel(new BorderLayout(5, 0));
        northPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Cancelled Appointments", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(20));
        titleLabel.setForeground(Color.WHITE);
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchPanel.add(searchLabel, BorderLayout.WEST);

        searchField = new JTextField(20);
        searchPanel.add(searchField, BorderLayout.CENTER);

        refreshButton = theme.createButton("Refresh");
        searchPanel.add(refreshButton, BorderLayout.EAST);

        northPanel.add(searchPanel, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Center - Appointments table
        String[] columnNames = {"ID", "Date", "Username", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // South - Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        closeButton = theme.createButton("Close");
        deleteButton = theme.createButton("Delete Selected");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);
    }


    public void refreshTable(String[][] data) {
        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }

    public JTable getAppointmentsTable() {
        return appointmentsTable;
    }
    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getCloseButton() {
        return closeButton;
    }
    public JButton getDeleteButton() {
        return deleteButton;
    }


    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}