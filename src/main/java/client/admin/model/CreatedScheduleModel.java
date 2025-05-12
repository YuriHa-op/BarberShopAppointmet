package client.admin.model;

import common.model.Appointment;
import common.service.AppointmentService;

import java.util.*;
import java.util.stream.Collectors;

public class CreatedScheduleModel {
    private AppointmentService appointmentService;
    private List<Appointment> existingAppointments;

    public CreatedScheduleModel() {
        this.appointmentService = AppointmentService.getInstance();
        this.existingAppointments = new ArrayList<>();
        loadAppointments();
    }

    private void loadAppointments() {
        try {
            existingAppointments = appointmentService.getAllAppointments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAvailableStartTimes(String date, String[] timeSlots) {
        Set<String> scheduledTimes = existingAppointments.stream()
                .filter(a -> a.getDate().equals(date))
                .map(Appointment::getStartTime)
                .collect(Collectors.toSet());

        List<String> availableTimes = new ArrayList<>();
        for (String time : timeSlots) {
            if (!scheduledTimes.contains(time)) {
                availableTimes.add(time);
            }
        }
        return availableTimes;
    }

    public boolean createScheduleSlot(Date selectedDate, String startTime, String endTime) throws Exception {
        return appointmentService.createScheduleSlot(selectedDate, startTime, endTime);
    }
}