package common.ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class CalendarPanel extends JPanel {
    private JPanel daysPanel;
    private JLabel monthYearLabel;
    private JButton prevButton;
    private JButton nextButton;
    private Calendar currentCalendar = Calendar.getInstance();
    private Consumer<String> dateSelectedListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(10, 10));

        // Navigation panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);

        Theme theme = Theme.getInstance();
        prevButton = theme.createButton("◀ Previous");
        monthYearLabel = new JLabel(getMonthYearString(), JLabel.CENTER);
        monthYearLabel.setFont(theme.createFont(20));
        monthYearLabel.setForeground(Color.WHITE);
        nextButton = theme.createButton("Next ▶");

        navPanel.add(prevButton);
        navPanel.add(monthYearLabel);
        navPanel.add(nextButton);

        // Days grid panel
        daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        daysPanel.setOpaque(false);
        daysPanel.setBorder(theme.createBorder());

        // Add day headers
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(theme.createFont(14));
            dayLabel.setForeground(Color.WHITE);
            daysPanel.add(dayLabel);
        }

        // Add navigation events
        prevButton.addActionListener(e -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendar(null);
        });

        nextButton.addActionListener(e -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendar(null);
        });

        add(navPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);

        updateCalendar(null);
    }

    public void setDateSelectedListener(Consumer<String> listener) {
        this.dateSelectedListener = listener;
    }

    public void updateCalendar(Map<String, ? extends Map<String, ?>> dateData) {
        // Clear days panel but keep headers
        Component[] components = daysPanel.getComponents();
        daysPanel.removeAll();

        // Add day headers back
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(Theme.getInstance().createFont(14));
            dayLabel.setForeground(Color.WHITE);
            daysPanel.add(dayLabel);
        }

        // Calculate month display details
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Empty cells before month start
        for (int i = 0; i < monthStartDay; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            daysPanel.add(emptyPanel);
        }

        // Create day buttons
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String dateString = dateFormat.format(calendar.getTime());

            JButton dayButton = Theme.getInstance().createCalendarButton(String.valueOf(day));
            colorDayButton(dayButton, dateString, dateData);

            final String finalDateString = dateString;
            dayButton.addActionListener(e -> {
                if (dateSelectedListener != null) {
                    dateSelectedListener.accept(finalDateString);
                }
            });

            daysPanel.add(dayButton);
        }

        // Update month/year label
        monthYearLabel.setText(getMonthYearString());

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    // This method should
    protected void colorDayButton(JButton button, String dateString, Map<String, ? extends Map<String, ?>> dateData) {
        button.setBackground(new Color(100, 100, 100));
    }

    private String getMonthYearString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        return sdf.format(currentCalendar.getTime());
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public void setCurrentCalendar(Calendar calendar) {
        this.currentCalendar = calendar;
    }

    public JButton getPrevButton() {
        return prevButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }
    public JLabel getMonthYearLabel() {
        return monthYearLabel;
    }
}