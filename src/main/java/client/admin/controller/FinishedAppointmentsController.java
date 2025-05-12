package client.admin.controller;

import client.admin.view.FinishedAppointments;
import client.admin.model.FinishedAppointmentsModel;

import javax.swing.table.DefaultTableModel;

public class FinishedAppointmentsController {
    private FinishedAppointments view;
    private FinishedAppointmentsModel model;

    public FinishedAppointmentsController(FinishedAppointments view, FinishedAppointmentsModel model) {
        this.view = view;
        this.model = model;
        initController();
    }

    private void initController() {
        view.getRefreshButton().addActionListener(e -> loadFinishedAppointments());
        loadFinishedAppointments();
    }

    private void loadFinishedAppointments() {
        try {
            DefaultTableModel tableModel = model.getFinishedAppointmentsTableModel();
            view.getAppointmentsTable().setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error loading finished appointments: " + e.getMessage());
        }
    }
}
