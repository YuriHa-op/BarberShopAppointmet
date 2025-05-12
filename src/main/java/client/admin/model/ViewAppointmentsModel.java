package client.admin.model;

import common.model.Appointment;
import common.service.AppointmentService;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.stream.Collectors;

public class ViewAppointmentsModel {
    private AppointmentService appointmentService;

    public ViewAppointmentsModel(String[] strings) {
        this.appointmentService = AppointmentService.getInstance();
    }

    public void loadAppointments(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments().stream()
                    .filter(a -> !a.getUsername().equalsIgnoreCase("barber"))
                    .collect(Collectors.toList());

            for (Appointment appointment : appointments) {
                tableModel.addRow(new Object[]{
                        appointment.getId(),
                        appointment.getDate(),
                        appointment.getUsername(),
                        appointment.getStartTime(),
                        appointment.getEndTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean cancelAppointment(String appointmentId) throws Exception {
        return appointmentService.cancelAppointment(appointmentId);
    }

    public boolean finishAppointment(String appointmentId) {
        return appointmentService.markAppointmentAsFinished(appointmentId);
    }

    public void searchAppointments(TableRowSorter<DefaultTableModel> sorter, String searchText) {
        appointmentService.searchAppointments(sorter, searchText);
    }
}