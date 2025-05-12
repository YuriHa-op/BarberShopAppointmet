package client.admin.view;

import common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewAppointmentsView extends JFrame {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton cancelButton;
    private JButton createAppointmentButton;
    private JButton finishAppointmentButton;
    private JButton logoutButton;
    private JButton calendarButton;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public ViewAppointmentsView() {
        setTitle("Appointments View");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("APPOINTMENTS", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(theme.createFont(14));
        searchLabel.setForeground(Color.WHITE);
        searchField = new JTextField(20);
        searchField.setFont(theme.createFont(14));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        titlePanel.add(searchPanel, BorderLayout.SOUTH);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Date", "Username", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentsTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        sorter.setSortable(0, false);

        theme.styleTable(appointmentsTable);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.getViewport().setBackground(new Color(70, 70, 70));
        scrollPane.setBorder(theme.createBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setOpaque(false);

        refreshButton = theme.createButton("Refresh");
        cancelButton = theme.createButton("Cancel Appointment");
        createAppointmentButton = theme.createButton("Create Schedule");
        finishAppointmentButton = theme.createButton("Finish Appointment");
        logoutButton = theme.createButton("Logout");
        calendarButton = theme.createButton("View Calendar");

        buttonPanel.add(calendarButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(createAppointmentButton);
        buttonPanel.add(finishAppointmentButton);
        buttonPanel.add(logoutButton);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);

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

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getCreateAppointmentButton() {
        return createAppointmentButton;
    }

    public JButton getFinishAppointmentButton() {
        return finishAppointmentButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JButton getCalendarButton() {
        return calendarButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

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