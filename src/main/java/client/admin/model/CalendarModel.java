package client.admin.model;

import common.model.Appointment;
import common.service.AppointmentService;
import javax.swing.*;
import java.util.*;

public class CalendarModel {
    private Calendar currentCalendar;
    private Map<String, Map<String, String>> appointmentData;
    private AppointmentService appointmentService;

    public CalendarModel() {
        this.currentCalendar = Calendar.getInstance();
        this.appointmentData = new HashMap<>();
        this.appointmentService = AppointmentService.getInstance();
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public void setCurrentCalendar(Calendar calendar) {
        this.currentCalendar = calendar;
    }

    public void moveMonth(int delta) {
        currentCalendar.add(Calendar.MONTH, delta);
    }

    public Map<String, Map<String, String>> getAppointmentData() {
        return appointmentData;
    }

    public void updateAppointments() throws Exception {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        Map<String, Map<String, String>> appointmentsByDate = new HashMap<>();

        for (Appointment appointment : appointments) {
            Map<String, String> dayAppointments = appointmentsByDate
                    .computeIfAbsent(appointment.getDate(), k -> new HashMap<>());
            dayAppointments.put(appointment.getStartTime(), appointment.getUsername());
        }

        this.appointmentData = appointmentsByDate;
    }

    public String getAppointmentId(String date, String timeSlot) throws Exception {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getStartTime().equals(timeSlot)) {
                return appointment.getId();
            }
        }
        return null;
    }

    public boolean isAvailableSlot(String appointmentId) throws Exception {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                return appointment.getUsername().equals("barber");
            }
        }
        return false;
    }

    public boolean cancelAppointment(String appointmentId) throws Exception {
        return appointmentService.cancelAppointment(appointmentId);
    }

    public Map<String, String> getAppointmentsForDate(String dateString) {
        return appointmentData.getOrDefault(dateString, new HashMap<>());
    }
}