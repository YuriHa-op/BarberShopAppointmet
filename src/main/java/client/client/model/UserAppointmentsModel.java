package client.client.model;

import common.model.Appointment;
import common.service.AppointmentService;

import java.util.List;

public class UserAppointmentsModel {
    private AppointmentService appointmentService;
    private String username;

    public UserAppointmentsModel(String username) {
        this.username = username;
        this.appointmentService = AppointmentService.getInstance();
    }

    public List<Appointment> getAppointments() throws Exception {
        return appointmentService.getAppointments(username);
    }

    public boolean cancelAppointment(String appointmentId) throws Exception {
        return appointmentService.cancelAppointment(appointmentId);
    }

    public String getUsername() {
        return username;
    }
}