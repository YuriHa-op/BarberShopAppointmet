package client.admin.model;

import common.model.Appointment;
import common.service.AppointmentService;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FinishedAppointmentsModel {
    private AppointmentService appointmentService;

    public FinishedAppointmentsModel() {
        this.appointmentService = AppointmentService.getInstance();
    }

    public DefaultTableModel getFinishedAppointmentsTableModel() throws Exception {
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"ID", "Date", "Username", "Start Time", "End Time"}, 0);

        List<Appointment> appointments = appointmentService.getFinishedAppointments();

        for (Appointment appointment : appointments) {
            tableModel.addRow(new Object[]{
                    appointment.getId(),
                    appointment.getDate(),
                    appointment.getUsername(),
                    appointment.getStartTime(),
                    appointment.getEndTime()
            });
        }
        return tableModel;
    }
}
