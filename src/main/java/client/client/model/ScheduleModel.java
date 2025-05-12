package client.client.model;

import common.model.Appointment;
import common.model.AppointmentList;
import common.model.Request;
import common.model.Response;
import common.utility.JSONUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class ScheduleModel {
        private Map<String, Map<String, Boolean>> availableTimeSlots = new HashMap<>();
        private static final String[] ALL_TIMES = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"};
        private String username;
        public ScheduleModel(String username) { // Add username parameter to constructor
            this.username = username;
            loadAvailableTimes();
        }

        public Map<String, Map<String, Boolean>> getAvailableTimeSlots() {
            return availableTimeSlots;
        }

        private void loadAvailableTimes() {
            try {
                Request request = new Request("view", "");
                String requestJSON = JSONUtil.marshal(request, Request.class);
                String responseJSON = AppointmentList.sendRequest(requestJSON);
                Response response = JSONUtil.unmarshal(responseJSON, Response.class);

                if (response.getStatus().equalsIgnoreCase("success")) {
                    JSONObject json = new JSONObject(response.getData());
                    JSONArray appointmentsArray = json.getJSONArray("appointments");

                    for (int i = 0; i < appointmentsArray.length(); i++) {
                        JSONObject appt = appointmentsArray.getJSONObject(i);
                        String date = appt.getString("date");
                        String startTime = appt.getString("startTime");
                        String username = appt.getString("username");

                        if (!availableTimeSlots.containsKey(date)) {
                            availableTimeSlots.put(date, new HashMap<>());
                        }

                        boolean isAvailable = username.equalsIgnoreCase("barber");
                        availableTimeSlots.get(date).put(startTime, isAvailable);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String findEndTime(String startTime) {
            for (int i = 0; i < ALL_TIMES.length - 1; i++) {
                if (ALL_TIMES[i].equals(startTime)) {
                    return ALL_TIMES[i + 1];
                }
            }
            return null;
        }

        public String findAppointmentId(String date, String startTime, String username) {
            try {
                List<Appointment> appointments = AppointmentList.loadFromFile("appointments.json");
                for (Appointment appointment : appointments) {
                    if (appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getUsername().equals(username)) {
                        return appointment.getId();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getUsername() {
            return username;
        }
}