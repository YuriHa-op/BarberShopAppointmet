package client.admin.controller;

import client.admin.model.CalendarModel;
import client.admin.model.ViewAppointmentsModel;
import client.admin.view.ViewAppointmentsView;
import client.admin.view.CreatedScheduleView;
import client.admin.view.CalendarView;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ViewAppointmentsController {
    private ViewAppointmentsView view;
    private ViewAppointmentsModel model;
    private static CreatedScheduleView createdScheduleView;
    private static CalendarView calendarView;

    public ViewAppointmentsController(ViewAppointmentsView view, ViewAppointmentsModel viewAppointmentsModel) {
        this.view = view;
        this.model = new ViewAppointmentsModel(new String[]{"ID", "Date", "Username", "Start Time", "End Time"});
        initController();
    }

    private void initController() {
        view.getRefreshButton().addActionListener(e -> model.loadAppointments(view.getTableModel()));
        view.getCancelButton().addActionListener(e -> {
            try {
                cancelAppointment();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        view.getCreateAppointmentButton().addActionListener(e -> createAppointment());
        view.getFinishAppointmentButton().addActionListener(e -> finishAppointment());
        view.getCalendarButton().addActionListener(e -> openCalendar());
        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { searchAppointments(); }
            @Override
            public void removeUpdate(DocumentEvent e) { searchAppointments(); }
            @Override
            public void changedUpdate(DocumentEvent e) { searchAppointments(); }});
        model.loadAppointments(view.getTableModel());
        view.getLogoutButton().addActionListener(e -> logout());
    }

    private void cancelAppointment() throws Exception {
        int selectedRow = view.getAppointmentsTable().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Please select an appointment to cancel.");
            return;
        }

        String appointmentId = (String) view.getTableModel().getValueAt(selectedRow, 0);
        if (model.cancelAppointment(appointmentId)) {
            view.showMessage("Appointment canceled successfully.");
            model.loadAppointments(view.getTableModel());
        } else {
            view.showMessage("Failed to cancel appointment.");
        }
    }

    private void finishAppointment() {
        int selectedRow = view.getAppointmentsTable().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Please select an appointment to mark as finished.");
            return;
        }

        String appointmentId = (String) view.getTableModel().getValueAt(selectedRow, 0);
        if (model.finishAppointment(appointmentId)) {
            view.showMessage("Appointment marked as finished.");
            model.loadAppointments(view.getTableModel());
        } else {
            view.showMessage("Failed to mark appointment as finished.");
        }
    }

    private void searchAppointments() {
        String searchText = view.getSearchField().getText();
        model.searchAppointments(view.getSorter(), searchText);
    }

    private void createAppointment() {
        if (createdScheduleView == null || !createdScheduleView.isVisible()) {
            createdScheduleView = new CreatedScheduleView();
            new CreatedScheduleController(createdScheduleView);
            createdScheduleView.setVisible(true);
            view.minimizeAndRestoreOnClose(createdScheduleView);
        } else {
            createdScheduleView.toFront();
        }
    }

    private void openCalendar() {
        if (calendarView == null || !calendarView.isVisible()) {
            calendarView = new CalendarView();
            CalendarModel calendarModel = new CalendarModel();
            new CalendarController(calendarView, calendarModel).showCalendar();
            view.minimizeAndRestoreOnClose(calendarView);
        } else {
            calendarView.toFront();
        }
    }
    private void logout() {
        view.dispose();
    }
}

