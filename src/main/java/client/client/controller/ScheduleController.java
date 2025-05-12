package client.client.controller;

import client.client.model.ScheduleModel;
import client.client.view.ScheduleView;
import common.model.Appointment;
import common.model.AppointmentList;
import common.model.Request;
import common.model.Response;
import common.utility.JSONUtil;
import common.utility.Logger;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleController {
    private ScheduleView view;
    private ScheduleModel model;
    private String username;
    private UserAppointmentsController userAppointmentsController;

    public ScheduleController(ScheduleView view, String username, UserAppointmentsController userAppointmentsController) {
        this.view = view;
        this.username = username;
        this.model = new ScheduleModel(username);
        this.userAppointmentsController = userAppointmentsController;

        view.updateCalendar(model.getAvailableTimeSlots());

        view.getPrevButton().addActionListener(e -> changeMonth(-1));
        view.getNextButton().addActionListener(e -> changeMonth(1));
        view.setDaySelectedListener(this::showTimeSlots);
        view.setBookingCallback(this::bookAppointment); // Set booking callback
        view.setCancelCallback(this::cancelAppointment); // Set canceling callback
    }

    private void changeMonth(int offset) {
        Calendar calendar = view.getCurrentCalendar();
        calendar.add(Calendar.MONTH, offset);
        view.getMonthLabel().setText(getMonthYearString(calendar));
        view.updateCalendar(model.getAvailableTimeSlots());
    }

    private String getMonthYearString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        return sdf.format(calendar.getTime());
    }

    private void showTimeSlots(int day) {
        Calendar selected = (Calendar) view.getCurrentCalendar().clone();
        selected.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(selected.getTime());

        Map<String, Boolean> dateTimes = model.getAvailableTimeSlots().getOrDefault(dateString, new HashMap<>());

        view.displayTimeSlots(dateString, dateTimes, this::bookAppointment, this::cancelAppointment, username);
    }

    private void bookAppointment(String date, String startTime) {
        try {
            String endTime = model.findEndTime(startTime);
            if (endTime == null) {
                JOptionPane.showMessageDialog(view, "Could not determine end time for this slot", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Appointment appointment = new Appointment(null, username, date, startTime, endTime);
            String appointmentJSON = JSONUtil.marshal(appointment, Appointment.class);

            Request request = new Request("book", appointmentJSON);
            String requestJSON = JSONUtil.marshal(request, Request.class);
            String responseJSON = AppointmentList.sendRequest(requestJSON);
            Response response = JSONUtil.unmarshal(responseJSON, Response.class);

            if (response.getStatus().equalsIgnoreCase("success")) {
                JOptionPane.showMessageDialog(view, "Appointment booked successfully for " + date + " at " + startTime, "Success", JOptionPane.INFORMATION_MESSAGE);

                if (!model.getAvailableTimeSlots().containsKey(date)) {
                    model.getAvailableTimeSlots().put(date, new HashMap<>());
                }
                model.getAvailableTimeSlots().get(date).put(startTime, false);

                view.updateCalendar(model.getAvailableTimeSlots());
                showTimeSlots(Integer.parseInt(date.split("-")[2]));

                // Notify UserAppointmentsController to refresh the view
                if (userAppointmentsController != null) {
                    userAppointmentsController.loadUserAppointments();
                }

                // Log the booking
                Logger.log(username, "book", "Appointment booked for " + date + " at " + startTime);
            } else {
                JOptionPane.showMessageDialog(view, "Failed to book appointment: " + response.getMessage(), "Booking Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error booking appointment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelAppointment(String date, String startTime) {
        try {
            String appointmentId = model.findAppointmentId(date, startTime, username);
            if (appointmentId == null) {
                JOptionPane.showMessageDialog(view, "Could not find appointment to cancel", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Request request = new Request("cancel", appointmentId);
            String requestJSON = JSONUtil.marshal(request, Request.class);
            String responseJSON = AppointmentList.sendRequest(requestJSON);
            Response response = JSONUtil.unmarshal(responseJSON, Response.class);

            if (response.getStatus().equalsIgnoreCase("success")) {
                JOptionPane.showMessageDialog(view, "Appointment cancelled successfully for " + date + " at " + startTime, "Success", JOptionPane.INFORMATION_MESSAGE);
                Logger.log(model.getUsername(), "cancel", "Appointment cancelled for " + date + " at " + startTime);
                if (model.getAvailableTimeSlots().containsKey(date)) {
                    model.getAvailableTimeSlots().get(date).put(startTime, true);
                }

                view.updateCalendar(model.getAvailableTimeSlots());
                showTimeSlots(Integer.parseInt(date.split("-")[2]));

                // Notify UserAppointmentsController to refresh the view
                if (userAppointmentsController != null) {
                    userAppointmentsController.loadUserAppointments();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to cancel appointment: " + response.getMessage(), "Cancellation Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error cancelling appointment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}