package client.admin.controller;

import client.admin.model.AdminMenuModel;
import client.admin.model.CancelledAppointmentsModel;
import client.admin.model.FinishedAppointmentsModel;
import client.admin.model.ViewAppointmentsModel;
import client.admin.view.*;

import javax.swing.*;

public class AdminMenuController {
    private AdminMenuView view;
    private AdminMenuModel model;

    public AdminMenuController(AdminMenuView view, AdminMenuModel model) {
        this.view = view;
        this.model = model;
        initController();
    }

    private void initController() {
        view.getViewAppointmentsButton().addActionListener(e -> openViewAppointments());
        view.getCreatedAppointmentsButton().addActionListener(e -> openCreatedAppointments());
        view.getFinishedAppointmentsButton().addActionListener(e -> {
            try {
                openFinishedAppointments();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        view.getCancelledAppointmentsButton().addActionListener(e -> openCancelledAppointments());
        view.getLogoutButton().addActionListener(e -> view.dispose());
    }

    private void openViewAppointments() {
        if (model.getViewAppointmentsView() == null || !model.getViewAppointmentsView().isVisible()) {
            ViewAppointmentsView viewAppointmentsView = new ViewAppointmentsView();
            ViewAppointmentsModel viewAppointmentsModel = new ViewAppointmentsModel(
                    new String[]{"ID", "Date", "Username", "Start Time", "End Time"});
            new ViewAppointmentsController(viewAppointmentsView, viewAppointmentsModel);
            model.setViewAppointmentsView(viewAppointmentsView);
            viewAppointmentsView.setVisible(true);
            view.minimizeAndRestoreOnClose(viewAppointmentsView);
        } else {
            model.getViewAppointmentsView().toFront();
        }
    }

    private void openCreatedAppointments() {
        if (model.getCreatedAppointmentsView() == null || !model.getCreatedAppointmentsView().isVisible()) {
            CreatedAppointmentsView createdAppointmentsView = new CreatedAppointmentsView();
            new CreatedAppointmentsController(createdAppointmentsView);
            model.setCreatedAppointmentsView(createdAppointmentsView);
            createdAppointmentsView.setVisible(true);
            view.minimizeAndRestoreOnClose(createdAppointmentsView);
        } else {
            model.getCreatedAppointmentsView().toFront();
        }
    }

    private void openCancelledAppointments() {
        if (model.getCancelledAppointmentsView() == null || !model.getCancelledAppointmentsView().isVisible()) {
            CancelledAppointmentsModel cancelledAppointmentsModel = new CancelledAppointmentsModel();
            CancelledAppointmentsView cancelledAppointmentsView = new CancelledAppointmentsView();
            new CancelledAppointmentsController(cancelledAppointmentsView, cancelledAppointmentsModel);
            model.setCancelledAppointmentsView(cancelledAppointmentsView);
            cancelledAppointmentsView.setVisible(true);
            view.minimizeAndRestoreOnClose(cancelledAppointmentsView);
        } else {
            model.getCancelledAppointmentsView().toFront();
        }
    }

    private void openFinishedAppointments() throws Exception {
        if (model.getFinishedAppointmentsView() == null || !model.getFinishedAppointmentsView().isVisible()) {
            FinishedAppointmentsModel finishedAppointmentsModel = new FinishedAppointmentsModel();
            FinishedAppointments finishedAppointmentsView = new FinishedAppointments(finishedAppointmentsModel);
            new FinishedAppointmentsController(finishedAppointmentsView, finishedAppointmentsModel);
            model.setFinishedAppointmentsView(finishedAppointmentsView);
            finishedAppointmentsView.setVisible(true);
            view.minimizeAndRestoreOnClose(finishedAppointmentsView);
        } else {
            model.getFinishedAppointmentsView().toFront();
        }
    }
}