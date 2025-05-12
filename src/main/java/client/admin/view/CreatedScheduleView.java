package client.admin.view;

import common.ui.Theme;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;


public class CreatedScheduleView extends JFrame {
    private JDatePickerImpl dateChooser;
    private JComboBox<String> startTimeCombo;
    private JComboBox<String> endTimeCombo;
    private JButton createButton;
    private JButton cancelButton;
    public static final String[] TIME_SLOTS = { "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM" };


    public CreatedScheduleView() {
        setTitle("Create Schedule Slot");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Create New Schedule Slot", JLabel.CENTER);
        titleLabel.setFont(theme.createFont(20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        dateChooser = createDatePicker(theme);
        addFormRow(formPanel, gbc, "Date:", dateChooser, 1);

        startTimeCombo = createTimeComboBox(theme);
        addFormRow(formPanel, gbc, "Start Time:", startTimeCombo, 2);

        endTimeCombo = createTimeComboBox(theme);
        addFormRow(formPanel, gbc, "End Time:", endTimeCombo, 3);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        createButton = theme.createButton("Create Slot");
        cancelButton = theme.createButton("Cancel");
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JDatePickerImpl createDatePicker(Theme theme) {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getComponent(0).setFont(theme.createFont(12));
        datePicker.setBorder(theme.createBorder());
        return datePicker;
    }

    private JComboBox<String> createTimeComboBox(Theme theme) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(theme.createFont(12));
        comboBox.setBackground(new Color(100, 100, 100));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(theme.createBorder());
        return comboBox;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JComponent component, int row) {
        Theme theme = Theme.getInstance();
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(theme.createFont(14));
        jLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(jLabel, gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    public String[] getTimeSlots() {
        return TIME_SLOTS;
    }


    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    public JDatePickerImpl getDateChooser() {
        return dateChooser;
    }

    public JComboBox<String> getStartTimeCombo() {
        return startTimeCombo;
    }

    public JComboBox<String> getEndTimeCombo() {
        return endTimeCombo;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
