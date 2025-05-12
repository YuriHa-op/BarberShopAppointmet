package common.service;

import common.model.*;
import common.utility.JSONUtil;
import common.utility.RMICommunicator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private static AppointmentService instance;
    private final RMICommunicator communicator;

    private AppointmentService() {
        this.communicator = RMICommunicator.getInstance();
    }

    public static AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    /**
     * Gets all appointments from the server
     */
    public List<Appointment> getAllAppointments() throws Exception {
        Response response = sendRequest("view", "");
        if (response.getStatus().equalsIgnoreCase("success")) {
            JSONObject json = new JSONObject(response.getData());
            JSONArray appointmentsArray = json.getJSONArray("appointments");

            List<Appointment> appointments = new ArrayList<>();
            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject apptJson = appointmentsArray.getJSONObject(i);
                Appointment appointment = new Appointment(
                        apptJson.getString("id"),
                        apptJson.getString("username"),
                        apptJson.getString("date"),
                        apptJson.getString("startTime"),
                        apptJson.getString("endTime")
                );
                appointments.add(appointment);
            }

            return appointments;
        } else {
            throw new Exception("Failed to get appointments: " + response.getMessage());
        }
    }
    /**
     * Gets cancelled appointments
     */
    public List<Appointment> getCancelledAppointments() throws Exception {
        Request request = new Request("view_cancelled", "");
        Response response = communicator.processRequest(request);

        if (!response.getStatus().equals("success")) {
            throw new Exception("Failed to retrieve cancelled appointments: " + response.getMessage());
        }

        List<Appointment> cancelledAppointments = new ArrayList<>();
        if (response.getData() != null && !response.getData().isEmpty()) {
            JSONObject jsonResponse = new JSONObject(response.getData());
            JSONArray appointmentsArray = jsonResponse.getJSONArray("appointments");

            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject apptJson = appointmentsArray.getJSONObject(i);
                Appointment appointment = new Appointment(
                        apptJson.optString("id"),
                        apptJson.optString("username"),
                        apptJson.optString("date"),
                        apptJson.optString("startTime"),
                        apptJson.optString("endTime")
                );
                cancelledAppointments.add(appointment);
            }
        }

        return cancelledAppointments;
    }

    /**
     * Gets appointments for a specific user
     */
    public List<Appointment> getAppointments(String username) throws Exception {
        List<Appointment> allAppointments = getAllAppointments();
        return allAppointments.stream()
                .filter(a -> a.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * Gets available time slots (appointments with username "barber")
     */
    public List<Appointment> getAvailableTimeSlots() throws Exception {
        List<Appointment> allAppointments = getAllAppointments();
        return allAppointments.stream()
                .filter(a -> a.getUsername().equals("barber"))
                .collect(Collectors.toList());
    }

    /**
     * Books an appointment for the specified user
     */
    public boolean bookAppointment(String username, String date, String startTime, String endTime) throws Exception {
        Appointment appointment = new Appointment(null, username, date, startTime, endTime);
        Response response = sendAppointmentRequest("book", appointment);
        return response.getStatus().equalsIgnoreCase("success");
    }

    /**
     * Cancels an appointment with the specified ID
     */
    public boolean cancelAppointment(String appointmentId) throws Exception {
        Response response = sendRequest("cancel", appointmentId);
        return response.getStatus().equalsIgnoreCase("success");
    }

    /**
     * Creates a new schedule slot
     */
    public boolean createScheduleSlot(Date date, String startTime, String endTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);

        Appointment appointment = new Appointment(null, "barber", dateString, startTime, endTime);
        Response response = sendAppointmentRequest("create_schedule", appointment);

        return response.getStatus().equalsIgnoreCase("success");
    }

    /**
     * Marks an appointment as finished
     */
    public boolean markAppointmentAsFinished(String appointmentId) {
        try {
            System.out.println("Sending request to mark appointment as finished: " + appointmentId);
            Response response = sendRequest("mark_finished", appointmentId);
            System.out.println("Response received: " + response);
            return response.getStatus().equalsIgnoreCase("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a request with an appointment object
     */
    private Response sendAppointmentRequest(String operation, Appointment appointment) throws Exception {
        String appointmentJSON = JSONUtil.marshal(appointment, Appointment.class);
        return sendRequest(operation, appointmentJSON);
    }

    /**
     * Sends a request to the server
     */
    public Response sendRequest(String operation, String data) throws Exception {
        Request request = new Request(operation, data);
        return communicator.processRequest(request);
    }

    /**
     * Searches appointments based on a filter
     */
    public void searchAppointments(TableRowSorter<DefaultTableModel> sorter, String searchText) {
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + searchText);
            sorter.setRowFilter(rf);
        }
    }

    /**
     * Gets finished appointments
     */
    public List<Appointment> getFinishedAppointments() throws Exception {
        Request request = new Request("view_finished", "");
        Response response = communicator.processRequest(request);

        if (!response.getStatus().equals("success")) {
            throw new Exception("Failed to retrieve finished appointments: " + response.getMessage());
        }

        List<Appointment> finishedAppointments = new ArrayList<>();
        if (response.getData() != null && !response.getData().isEmpty()) {
            JSONObject jsonResponse = new JSONObject(response.getData());
            JSONArray appointmentsArray = jsonResponse.getJSONArray("appointments");

            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject apptJson = appointmentsArray.getJSONObject(i);
                Appointment appointment = new Appointment(
                        apptJson.optString("id"),
                        apptJson.optString("username"),
                        apptJson.optString("date"),
                        apptJson.optString("startTime"),
                        apptJson.optString("endTime")
                );
                finishedAppointments.add(appointment);
            }
        }

        return finishedAppointments;
    }

    /**
     * Permanently deletes a cancelled appointment
     */
    public boolean deleteCancelledAppointment(String appointmentId) throws Exception {
        Request request = new Request("delete_cancelled", appointmentId);
        Response response = communicator.processRequest(request);
        return response.getStatus().equalsIgnoreCase("success");
    }
}