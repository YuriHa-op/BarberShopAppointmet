package client.admin.view;

import common.ui.Theme;
import common.ui.CalendarPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Map;
import java.util.function.Consumer;

public class CalendarView extends JFrame {
    private AppointmentCalendarPanel calendarPanel;
    private JPanel appointmentDetailsPanel;
    private Consumer<String> dateSelectedCallback;
    private Consumer<AppointmentAction> appointmentActionCallback;

    public CalendarView() {
        setTitle("Appointment Calendar View");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
    }

    private void initializeUI() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        calendarPanel = new AppointmentCalendarPanel();
        calendarPanel.setDateSelectedListener(date -> {
            if (dateSelectedCallback != null) {
                dateSelectedCallback.accept(date);
            }
        });

        appointmentDetailsPanel = createAppointmentDetailsPanel();
        JScrollPane detailsScrollPane = createDetailsScrollPane();

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(detailsScrollPane, BorderLayout.EAST);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createLegendPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createAppointmentDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(Theme.getInstance().createBorder());

        JLabel detailsTitle = createTitleLabel("APPOINTMENTS FOR SELECTED DATE");
        panel.add(detailsTitle);
        panel.add(Box.createVerticalStrut(10));

        JLabel placeholder = createPlaceholderLabel();
        panel.add(placeholder);

        return panel;
    }

    private JScrollPane createDetailsScrollPane() {
        JScrollPane scrollPane = new JScrollPane(appointmentDetailsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(Theme.getInstance().createFont(16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createPlaceholderLabel() {
        JLabel label = new JLabel("Select a date to view appointments", JLabel.CENTER);
        label.setFont(Theme.getInstance().createFont(14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        Theme theme = Theme.getInstance();

        panel.add(theme.createLegendItem("Available", new Color(100, 200, 100)));
        panel.add(theme.createLegendItem("Partially Booked", new Color(230, 230, 100)));
        panel.add(theme.createLegendItem("Fully Booked", new Color(230, 100, 100)));

        return panel;
    }

    public void displayAppointments(String dateString, Map<String, String> appointments) {
        appointmentDetailsPanel.removeAll();
        appointmentDetailsPanel.add(createTitleLabel("APPOINTMENTS FOR " + dateString));
        appointmentDetailsPanel.add(Box.createVerticalStrut(10));

        if (appointments != null && !appointments.isEmpty()) {
            appointments.forEach((timeSlot, username) ->
                    appointmentDetailsPanel.add(createAppointmentPanel(dateString, timeSlot, username)));
        } else {
            appointmentDetailsPanel.add(createNoAppointmentsLabel());
        }

        appointmentDetailsPanel.revalidate();
        appointmentDetailsPanel.repaint();
    }

    private JPanel createAppointmentPanel(String date, String timeSlot, String username) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setMaximumSize(new Dimension(250, 40));

        JLabel timeLabel = new JLabel(timeSlot);
        timeLabel.setFont(Theme.getInstance().createFont(14));
        timeLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel(username.equals("barber") ? "Available" : username);
        userLabel.setFont(Theme.getInstance().createFont(14));
        userLabel.setForeground(username.equals("barber") ? new Color(100, 200, 100) : Color.WHITE);

        JButton cancelButton = createCancelButton(date, timeSlot);

        panel.add(timeLabel, BorderLayout.WEST);
        panel.add(userLabel, BorderLayout.CENTER);
        panel.add(cancelButton, BorderLayout.EAST);

        return panel;
    }

    private JButton createCancelButton(String date, String timeSlot) {
        JButton button = new JButton("Cancel");
        button.setFont(Theme.getInstance().createFont(14));
        button.setBackground(new Color(230, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.addActionListener(e -> {
            if (appointmentActionCallback != null) {
                appointmentActionCallback.accept(new AppointmentAction(date, timeSlot, "CANCEL"));
            }
        });
        return button;
    }

    private JLabel createNoAppointmentsLabel() {
        JLabel label = new JLabel("No appointments for this date", JLabel.CENTER);
        label.setFont(Theme.getInstance().createFont(14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public void setDateSelectedCallback(Consumer<String> callback) {
        this.dateSelectedCallback = callback;
    }

    public void setAppointmentActionCallback(Consumer<AppointmentAction> callback) {
        this.appointmentActionCallback = callback;
    }

    public Calendar getCurrentCalendar() {
        return calendarPanel.getCurrentCalendar();
    }

    public void setCurrentCalendar(Calendar calendar) {
        calendarPanel.setCurrentCalendar(calendar);
    }

    public JButton getPrevButton() {
        return calendarPanel.getPrevButton();
    }

    public JButton getNextButton() {
        return calendarPanel.getNextButton();
    }

    public void updateCalendarGrid() {
        calendarPanel.updateCalendar(null);
    }

    public void updateAppointmentData(Map<String, Map<String, String>> appointmentData) {
        calendarPanel.updateCalendar(appointmentData);
    }

    public static class AppointmentAction {
        private final String date;
        private final String timeSlot;
        private final String action;

        public AppointmentAction(String date, String timeSlot, String action) {
            this.date = date;
            this.timeSlot = timeSlot;
            this.action = action;
        }

        public String getDate() { return date; }
        public String getTimeSlot() { return timeSlot; }
        public String getAction() { return action; }
    }

    private class AppointmentCalendarPanel extends CalendarPanel {
        private Map<String, Map<String, String>> appointmentData;

        public AppointmentCalendarPanel() {
            super();
            removeDefaultListeners();
        }

        private void removeDefaultListeners() {
            for (ActionListener al : getPrevButton().getActionListeners()) {
                getPrevButton().removeActionListener(al);
            }
            for (ActionListener al : getNextButton().getActionListeners()) {
                getNextButton().removeActionListener(al);
            }
        }

        @Override
        protected void colorDayButton(JButton button, String dateString, Map<String, ? extends Map<String, ?>> dateData) {
            this.appointmentData = (Map<String, Map<String, String>>) dateData;

            if (appointmentData != null && appointmentData.containsKey(dateString)) {
                Map<String, String> dayAppointments = appointmentData.get(dateString);
                int bookedCount = 0;
                int totalCount = dayAppointments.size();

                for (String username : dayAppointments.values()) {
                    if (!username.equals("barber")) {
                        bookedCount++;
                    }
                }
                if (bookedCount == 0) {
                    button.setBackground(new Color(100, 200, 100)); // Green - all available
                } else if (bookedCount < totalCount) {
                    button.setBackground(new Color(230, 230, 100)); // Yellow - partially booked
                } else {
                    button.setBackground(new Color(230, 100, 100)); // Red - fully booked
                }
            } else {
                button.setBackground(new Color(100, 100, 100)); // Gray - no data
            }
        }
    }

}