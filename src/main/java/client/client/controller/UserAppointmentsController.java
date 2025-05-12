package client.client.controller;

import client.client.model.UserAppointmentsModel;
import client.client.view.UserAppointmentsView;
import common.model.Appointment;
import common.utility.Logger;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UserAppointmentsController {
    private UserAppointmentsView view;
    private UserAppointmentsModel model;

    public UserAppointmentsController(UserAppointmentsView view, UserAppointmentsModel model) {
        this.view = view;
        this.model = model;
        initController();
    }

    private void initController() {
        view.setRefreshCallback(this::loadUserAppointments);
        view.setCancelCallback(this::cancelAppointment);
        view.setBookingCallback(this::loadUserAppointments); //  callback
        loadUserAppointments();
    }

    public void loadUserAppointments() {
        try {
            DefaultTableModel tableModel = view.getTableModel();
            tableModel.setRowCount(0);

            List<Appointment> appointments = model.getAppointments();

            for (Appointment appointment : appointments) {
                tableModel.addRow(new Object[]{
                        appointment.getId(),
                        appointment.getDate(),
                        model.getUsername(),
                        appointment.getStartTime(),
                        appointment.getEndTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error loading appointments: " + e.getMessage());
        }
    }

    private void cancelAppointment() {
        int selectedRow = view.getAppointmentsTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Please select an appointment to cancel.");
            return;
        }

        String appointmentId = (String) view.getTableModel().getValueAt(selectedRow, 0);

        try {
            boolean success = model.cancelAppointment(appointmentId);
            if (success) {
                view.showMessage("Appointment cancelled successfully.");
                loadUserAppointments();
                // Log the cancellation
                Logger.log(model.getUsername(), "cancel", "Appointment cancelled with ID: " + appointmentId);
            } else {
                view.showMessage("Failed to cancel appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error: " + e.getMessage());
        }
    }
}