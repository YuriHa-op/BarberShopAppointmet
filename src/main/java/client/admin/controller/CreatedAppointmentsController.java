package client.admin.controller;

import client.admin.view.CreatedAppointmentsView;
import common.model.Appointment;
import common.service.AppointmentService;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CreatedAppointmentsController { /// ////useless feature
    private CreatedAppointmentsView view;
    private AppointmentService appointmentService;

    public CreatedAppointmentsController(CreatedAppointmentsView view) {
        this.view = view;
        this.appointmentService = AppointmentService.getInstance();
        initController();
    }

    private void initController() {
        view.getRefreshButton().addActionListener(e -> loadCreatedAppointments());
        loadCreatedAppointments();
    }

    private void loadCreatedAppointments() {
        try {
            List<Appointment> barberAppointments = appointmentService.getAvailableTimeSlots();

            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0); // Clear existing data

            if (barberAppointments.isEmpty()) {
                model.addRow(new Object[]{"-", "No created appointments", "-", "-"});
            } else {
                for (Appointment appointment : barberAppointments) {
                    model.addRow(new Object[]{
                            appointment.getId(),
                            appointment.getDate(),
                            appointment.getStartTime(),
                            appointment.getEndTime()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           // view.showMessage("Error: " + e.getMessage());
        }
    }
}
