package server.controller;

import common.model.*;
import common.utility.JSONUtil;
import common.utility.Logger;
import server.callback.ServerCB;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BusinessLogic extends UnicastRemoteObject {
    public BusinessLogic() throws RemoteException {
        super();
    }

    private static final String DATA_DIRECTORY = "data/";
    private static final String USERS_FILE = DATA_DIRECTORY + "users.json";
    private static final String APPOINTMENTS_FILE = DATA_DIRECTORY + "appointments.json";
    private static final String CANCELLED_APPOINTMENTS_FILE = DATA_DIRECTORY + "cancelled_appointments.json";
    private static final String FINISHED_APPOINTMENTS_FILE = DATA_DIRECTORY + "finished_appointments.json";

    public Response process(Request request) {
        String operation = request.getOperation();
        String data = request.getData();

        try {
            switch (operation) {
                case "login":
                    return handleLogin(data);
                case "register":
                    return handleRegister(data);
                case "view":
                    return handleViewAppointments();
                case "view_finished":
                    return handleViewFinishedAppointments();
                case "view_cancelled":
                    return handleViewCancelledAppointments();
                case "book":
                    return handleBookAppointment(data);
                case "cancel":
                    return handleCancelAppointment(data);
                case "create_schedule":
                    return handleCreateSchedule(data);
                case "mark_finished":
                    return handleMarkFinishedAppointment(data);
                default:
                    return new Response("error", "Unknown operation: " + operation, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("error", "Error processing request: " + e.getMessage(), "");
        }
    }

    public Response handleLogin(String data) throws Exception {
        User user = JSONUtil.unmarshal(data, User.class);
        List<User> users = UserList.loadFromFile(USERS_FILE);

        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                Logger.log(user.getUsername(), "login", "User logged in successfully");
                return new Response("success", "Login successful", "");
            }
        }

        return new Response("error", "Invalid username or password", "");
    }

    public Response handleRegister(String data) throws Exception {
        User newUser = JSONUtil.unmarshal(data, User.class);
        List<User> users = UserList.loadFromFile(USERS_FILE);

        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                return new Response("error", "Username already exists", "");
            }
        }

        users.add(newUser);
        UserList.saveToFile(USERS_FILE, users);
        Logger.log(newUser.getUsername(), "register", "User registered successfully");

        return new Response("success", "Registration successful", "");
    }

    public Response handleViewAppointments() throws Exception {
        List<Appointment> appointments = AppointmentList.loadFromFile(APPOINTMENTS_FILE);
        AppointmentList appointmentList = new AppointmentList(appointments);
        String appointmentsJson = appointmentList.toString();

        return new Response("success", "Appointments retrieved", appointmentsJson);
    }

    public Response handleViewFinishedAppointments() throws Exception {
        List<Appointment> appointments = AppointmentList.loadFromFile(FINISHED_APPOINTMENTS_FILE);
        AppointmentList appointmentList = new AppointmentList(appointments);
        String appointmentsJson = appointmentList.toString();
        return new Response("success", "Finished appointments retrieved", appointmentsJson);
    }

    public Response handleBookAppointment(String data) throws Exception {
        Appointment appointment = JSONUtil.unmarshal(data, Appointment.class);
        List<Appointment> appointments = AppointmentList.loadFromFile(APPOINTMENTS_FILE);

        boolean slotFound = false;
        for (int i = 0; i < appointments.size(); i++) {
            Appointment existing = appointments.get(i);
            if (existing.getUsername().equals("barber") &&
                    existing.getDate().equals(appointment.getDate()) &&
                    existing.getStartTime().equals(appointment.getStartTime())) {
                slotFound = true;
                // Preserve the original appointment ID
                appointment.setId(existing.getId());
                appointments.set(i, appointment);
                break;
            }
        }

        if (!slotFound) {
            return new Response("error", "Appointment slot not available", "");
        }

        AppointmentList.saveToFile(APPOINTMENTS_FILE, appointments);
        Logger.log(appointment.getUsername(), "book",
                "Appointment booked for " + appointment.getDate() + " at " + appointment.getStartTime());

        return new Response("success", "Appointment booked successfully", "");
    }

    public Response handleCancelAppointment(String data) throws Exception {
        String appointmentId = data.trim();
        List<Appointment> appointments = AppointmentList.loadFromFile(APPOINTMENTS_FILE);
        List<Appointment> cancelledAppointments = AppointmentList.loadFromFile(CANCELLED_APPOINTMENTS_FILE);

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getId().equals(appointmentId)) {
                // Move to cancelled list instead of just removing
                cancelledAppointments.add(appointment);
                appointments.remove(i);

                // Save both files
                AppointmentList.saveToFile(APPOINTMENTS_FILE, appointments);
                AppointmentList.saveToFile(CANCELLED_APPOINTMENTS_FILE, cancelledAppointments);

                Logger.log(appointment.getUsername(), "cancel", "Appointment cancelled");
                return new Response("success", "Appointment cancelled successfully", "");
            }
        }

        return new Response("error", "Appointment not found", "");
    }

    public Response handleViewCancelledAppointments() throws Exception {
        List<Appointment> appointments = AppointmentList.loadFromFile(CANCELLED_APPOINTMENTS_FILE);
        AppointmentList appointmentList = new AppointmentList(appointments);
        String appointmentsJson = appointmentList.toString();

        return new Response("success", "Cancelled appointments retrieved", appointmentsJson);
    }


    public Response handleCreateSchedule(String data) throws Exception {
        Appointment slot = JSONUtil.unmarshal(data, Appointment.class);
        List<Appointment> appointments = AppointmentList.loadFromFile(APPOINTMENTS_FILE);

        slot.setId(UUID.randomUUID().toString().substring(0, 8));
        appointments.add(slot);
        AppointmentList.saveToFile(APPOINTMENTS_FILE, appointments);

        return new Response("success", "Schedule slot created successfully", "");
    }

    public Response handleMarkFinishedAppointment(String appointmentId) throws Exception {
        System.out.println("Processing mark as finished for appointmentId: " + appointmentId);
        List<Appointment> activeAppointments = AppointmentList.loadFromFile(APPOINTMENTS_FILE);
        List<Appointment> finishedAppointments = AppointmentList.loadFromFile(FINISHED_APPOINTMENTS_FILE);

        for (int i = 0; i < activeAppointments.size(); i++) {
            Appointment appointment = activeAppointments.get(i);
            if (appointment.getId().equals(appointmentId)) {
                System.out.println("Appointment found. Moving to finished list.");
                finishedAppointments.add(appointment);
                activeAppointments.remove(i);

                AppointmentList.saveToFile(APPOINTMENTS_FILE, activeAppointments);
                AppointmentList.saveToFile(FINISHED_APPOINTMENTS_FILE, finishedAppointments);

                Logger.log(appointment.getUsername(), "mark_finished", "Appointment marked as finished");
                return new Response("success", "Appointment marked as finished", "");
            }
        }
        System.out.println("Appointment not found: " + appointmentId);
        return new Response("error", "Appointment not found", "");
    }

    public Response handleDeleteCancelledAppointment(String appointmentId) throws Exception {
        List<Appointment> cancelledAppointments = AppointmentList.loadFromFile(CANCELLED_APPOINTMENTS_FILE);
        boolean removed = false;

        for (int i = 0; i < cancelledAppointments.size(); i++) {
            if (cancelledAppointments.get(i).getId().equals(appointmentId)) {
                cancelledAppointments.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            AppointmentList.saveToFile(CANCELLED_APPOINTMENTS_FILE, cancelledAppointments);
            Logger.log("admin", "delete_cancelled", "Permanently deleted appointment: " + appointmentId);
            return new Response("success", "Appointment permanently deleted", "");
        } else {
            return new Response("error", "Cancelled appointment not found", "");
        }
    }
}
