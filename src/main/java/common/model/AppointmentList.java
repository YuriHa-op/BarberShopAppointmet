package common.model;

import common.utility.ClientCommunicator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentList {
    private List<Appointment> appointments;

    public AppointmentList() {
        this.appointments = new ArrayList<>();
    }

    public AppointmentList(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    // Helper method to convert Appointment to JSONObject
    private static JSONObject appointmentToJson(Appointment appointment) {
        JSONObject apptJson = new JSONObject();
        apptJson.put("id", appointment.getId());
        apptJson.put("username", appointment.getUsername());
        apptJson.put("date", appointment.getDate());
        apptJson.put("startTime", appointment.getStartTime());
        apptJson.put("endTime", appointment.getEndTime());
        return apptJson;
    }

    // Helper method to convert JSONObject to Appointment
    private static Appointment jsonToAppointment(JSONObject json) {
        return new Appointment(
                json.optString("id"),
                json.optString("username"),
                json.optString("date"),
                json.optString("startTime"),
                json.optString("endTime")
        );
    }

    // Helper method to convert appointments list to JSONObject
    private static JSONObject appointmentsToJsonObject(List<Appointment> appointments) {
        JSONObject root = new JSONObject();
        JSONArray appointmentsArray = new JSONArray();

        if (appointments != null) {
            for (Appointment appointment : appointments) {
                appointmentsArray.put(appointmentToJson(appointment));
            }
        }

        root.put("appointments", appointmentsArray);
        return root;
    }

    public static List<Appointment> loadFromFile(String filePath) {
        List<Appointment> appointments = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder jsonStr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonStr.append(line);
                    }

                    JSONObject json = new JSONObject(jsonStr.toString());
                    JSONArray appointmentsArray = json.getJSONArray("appointments");

                    for (int i = 0; i < appointmentsArray.length(); i++) {
                        appointments.add(jsonToAppointment(appointmentsArray.getJSONObject(i)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static void saveToFile(String filePath, List<Appointment> appointments) {
        try (FileWriter writer = new FileWriter(filePath)) {
            JSONObject root = appointmentsToJsonObject(appointments);
            writer.write(root.toString(2)); // 2 is the indentation level for JSON formatting
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendRequest(String requestJSON) {
        return ClientCommunicator.getInstance().sendRequest(requestJSON);
    }

    @Override
    public String toString() {
        return appointmentsToJsonObject(appointments).toString();
    }
}