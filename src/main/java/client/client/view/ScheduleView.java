package client.client.view;

import common.model.Appointment;
import common.model.AppointmentList;
import common.ui.Theme;
import common.ui.CalendarPanel;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Map;
import java.util.function.BiConsumer;

public class ScheduleView extends JFrame {
    private ScheduleCalendarPanel calendarPanel;
    private JPanel timePanel;
    private String selectedDate;
    private BiConsumer<String, String> bookingCallback; // Change to BiConsumer
    private BiConsumer<String, String> cancelCallback;

    public ScheduleView() {
        setTitle("Schedule View");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {
        common.ui.BackgroundPanel mainPanel = new common.ui.BackgroundPanel("/background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Calendar
        calendarPanel = new ScheduleCalendarPanel();

        // Time
        timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setOpaque(false);

        JScrollPane timeScrollPane = new JScrollPane(timePanel);
        timeScrollPane.setOpaque(false);
        timeScrollPane.getViewport().setOpaque(false);
        timeScrollPane.setBorder(Theme.getInstance().createBorder());
        timeScrollPane.setPreferredSize(new Dimension(280, 400));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

        JLabel timeSlotsTitle = new JLabel("AVAILABLE TIME SLOTS", JLabel.CENTER);
        timeSlotsTitle.setFont(Theme.getInstance().createFont(18));
        timeSlotsTitle.setForeground(Color.WHITE);
        timeSlotsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        rightPanel.add(timeSlotsTitle, BorderLayout.NORTH);
        rightPanel.add(timeScrollPane, BorderLayout.CENTER);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);

        // Legend
        JPanel legendPanel = createLegendPanel();

        // Add all
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(legendPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        legendPanel.setOpaque(false);

        Theme theme = Theme.getInstance();
        legendPanel.add(theme.createLegendItem("Available", new Color(100, 200, 100)));
        legendPanel.add(theme.createLegendItem("Booked", new Color(230, 230, 100)));
        legendPanel.add(theme.createLegendItem("No Slots", new Color(120, 120, 120)));

        return legendPanel;
    }

    public void displayTimeSlots(String date, Map<String, Boolean> timeSlots, BiConsumer<String, String> bookAction, BiConsumer<String, String> cancelAction, String currentUser) {
        timePanel.removeAll();
        selectedDate = date;

        // Add date header
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(Theme.getInstance().createFont(16));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timePanel.add(dateLabel);
        timePanel.add(Box.createVerticalStrut(15));

        if (timeSlots == null || timeSlots.isEmpty()) {
            JLabel noSlotsLabel = new JLabel("No available time slots");
            noSlotsLabel.setFont(Theme.getInstance().createFont(14));
            noSlotsLabel.setForeground(Color.WHITE);
            noSlotsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            timePanel.add(noSlotsLabel);
        } else {
            //  booking or cancel button
            for (Map.Entry<String, Boolean> entry : timeSlots.entrySet()) {
                String timeSlot = entry.getKey();
                boolean isAvailable = entry.getValue();

                JPanel slotPanel = new JPanel(new BorderLayout(10, 0));
                slotPanel.setOpaque(false);
                slotPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                slotPanel.setMaximumSize(new Dimension(250, 40));

                JLabel timeLabel = new JLabel(timeSlot);
                timeLabel.setFont(Theme.getInstance().createFont(14));
                timeLabel.setForeground(Color.WHITE);
                slotPanel.add(timeLabel, BorderLayout.WEST);

                if (isAvailable) {
                    JButton bookButton = Theme.getInstance().createButton("Book");
                    bookButton.addActionListener(e -> bookAction.accept(date, timeSlot));
                    slotPanel.add(bookButton, BorderLayout.EAST);
                } else {
                    // Check if the current user booked this slot
                    String bookedBy = getBookedByUser(date, timeSlot);
                    if (bookedBy.equals(currentUser)) {
                        JButton cancelButton = Theme.getInstance().createButton("Cancel");
                        cancelButton.addActionListener(e -> cancelAction.accept(date, timeSlot));
                        slotPanel.add(cancelButton, BorderLayout.EAST);
                    } else {
                        JLabel bookedLabel = new JLabel("Booked");
                        bookedLabel.setFont(Theme.getInstance().createFont(14));
                        bookedLabel.setForeground(Color.RED);
                        slotPanel.add(bookedLabel, BorderLayout.EAST);
                    }
                }

                timePanel.add(slotPanel);
                timePanel.add(Box.createVerticalStrut(5));
            }
        }

        timePanel.revalidate();
        timePanel.repaint();
    }

    private String getBookedByUser(String date, String startTime) {
        try {
            List<Appointment> appointments = AppointmentList.loadFromFile("appointments.json");
            for (Appointment appointment : appointments) {
                if (appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime)) {
                    return appointment.getUsername();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Specialized
    private class ScheduleCalendarPanel extends CalendarPanel {
        private Map<String, Map<String, Boolean>> availabilityData;

        public ScheduleCalendarPanel() {
            super();
            // Remove the default navigation listeners
            for (ActionListener al : getPrevButton().getActionListeners()) {
                getPrevButton().removeActionListener(al);
            }
            for (ActionListener al : getNextButton().getActionListeners()) {
                getNextButton().removeActionListener(al);
            }
        }

        @Override
        protected void colorDayButton(JButton button, String dateString, Map<String, ? extends Map<String, ?>> dateData) {
            this.availabilityData = (Map<String, Map<String, Boolean>>) dateData;

            if (availabilityData != null && availabilityData.containsKey(dateString)) {
                Map<String, Boolean> daySlots = availabilityData.get(dateString);
                if (daySlots.isEmpty()) {
                    button.setBackground(new Color(120, 120, 120)); // Gray - no slots
                } else if (daySlots.containsValue(true)) {
                    button.setBackground(new Color(100, 200, 100)); // Green - available slots
                } else {
                    button.setBackground(new Color(230, 230, 100)); // Yellow - all booked
                }
            } else {
                button.setBackground(new Color(120, 120, 120)); // Gray - no
            }
        }
    }

    public void setBookingCallback(BiConsumer<String, String> bookingCallback) {
        this.bookingCallback = bookingCallback;
    }

    public void setCancelCallback(BiConsumer<String, String> cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    /**
     * Sets a listener for day selection that accepts an int day parameter
     */
    public void setDaySelectedListener(java.util.function.Consumer<Integer> listener) {
        calendarPanel.setDateSelectedListener(dateString -> {
            // Extract day from date string (yyyy-MM-dd format)
            try {
                int day = Integer.parseInt(dateString.split("-")[2]);
                listener.accept(day);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Gets the month/year label from the calendar panel
     */
    public JLabel getMonthLabel() {
        return calendarPanel.getMonthYearLabel();
    }

    // Getters
    public Calendar getCurrentCalendar() {
        return calendarPanel.getCurrentCalendar();
    }

    public JButton getPrevButton() {
        return calendarPanel.getPrevButton();
    }

    public JButton getNextButton() {
        return calendarPanel.getNextButton();
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void updateCalendar(Map<String, Map<String, Boolean>> availabilityData) {
        calendarPanel.updateCalendar(availabilityData);
    }
}