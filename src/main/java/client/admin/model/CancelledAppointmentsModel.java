package client.admin.model;

import common.model.Appointment;
import common.service.AppointmentService;

import java.util.ArrayList;
import java.util.List;

public class CancelledAppointmentsModel {
    private List<Appointment> cancelledAppointments;
    private AppointmentService appointmentService;

    public CancelledAppointmentsModel() {
        this.cancelledAppointments = new ArrayList<>();
        this.appointmentService = AppointmentService.getInstance();
    }

    public List<Appointment> getCancelledAppointments() throws Exception {
        this.cancelledAppointments = appointmentService.getCancelledAppointments();
        return cancelledAppointments;
    }

    public String[][] getCancelledAppointmentsData() throws Exception {
        List<Appointment> appointments = getCancelledAppointments();
        String[][] data = new String[appointments.size()][5];

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            data[i][0] = appointment.getId();
            data[i][1] = appointment.getDate();
            data[i][2] = appointment.getUsername();
            data[i][3] = appointment.getStartTime();
            data[i][4] = appointment.getEndTime();
        }

        return data;
    }
}