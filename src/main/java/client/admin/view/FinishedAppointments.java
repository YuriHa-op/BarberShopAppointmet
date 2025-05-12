package client.admin.view;

import client.admin.model.FinishedAppointmentsModel;
import common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class FinishedAppointments extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private TableRowSorter<DefaultTableModel> sorter;
    private FinishedAppointmentsModel model;

    public FinishedAppointments(FinishedAppointmentsModel model) throws Exception {
        this.model = model;
        setTitle("Finished Appointments");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() throws Exception {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();


        JLabel titleLabel = new JLabel("FINISHED APPOINTMENTS", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabl
        tableModel = model.getFinishedAppointmentsTableModel();
        appointmentsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        //  theme
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

    public JTable getAppointmentsTable() {
        return appointmentsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
