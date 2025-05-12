package client.admin.controller;

import client.admin.view.CalendarView;
import client.admin.model.CalendarModel;
import javax.swing.*;
import java.util.Map;

public class CalendarController {
    private CalendarView view;
    private CalendarModel model;

    public CalendarController(CalendarView view, CalendarModel model) {
        this.view = view;
        this.model = model;
        initializeListeners();
        updateView();
    }

    private void initializeListeners() {
        view.getPrevButton().addActionListener(e -> {
            model.moveMonth(-1);
            updateView();
        });

        view.getNextButton().addActionListener(e -> {
            model.moveMonth(1);
            updateView();
        });

        // Add date selection callb
        view.setDateSelectedCallback(dateString -> {
            Map<String, String> appointments = model.getAppointmentsForDate(dateString);
            view.displayAppointments(dateString, appointments);
        });

        //callback
        view.setAppointmentActionCallback(action -> {
            if ("CANCEL".equals(action.getAction())) {
                try {
                    String appointmentId = model.getAppointmentId(action.getDate(), action.getTimeSlot());
                    if (appointmentId != null) {
                        model.cancelAppointment(appointmentId);
                        updateView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(view,
                            "Error canceling appointment: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateView() {
        try {
            model.updateAppointments();
            view.setCurrentCalendar(model.getCurrentCalendar());
            view.updateCalendarGrid();
            view.updateAppointmentData(model.getAppointmentData());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Error updating calendar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showCalendar() {
        updateView();
        view.setVisible(true);
    }
}