package client.client.model;

import client.client.view.ClientMenu;
import client.client.view.ScheduleView;
import client.client.view.UserAppointmentsView;
import client.client.controller.ScheduleController;
import client.client.controller.UserAppointmentsController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientMenuModel {
    private static ScheduleView scheduleView;
    private static UserAppointmentsView userAppointmentsView;
    private static UserAppointmentsController userAppointmentsController;

    public void showCalendar(ClientMenu view) {
        if (scheduleView == null || !scheduleView.isVisible()) {
            scheduleView = new ScheduleView();
            new ScheduleController(scheduleView, view.getUsername(), userAppointmentsController); // Pass the controller
            scheduleView.setVisible(true);
            view.setState(JFrame.ICONIFIED); // Minimize the main window

            scheduleView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    view.setState(JFrame.NORMAL); // Restore the main window
                }
            });
        } else {
            scheduleView.toFront();
        }
    }

    public void showBookedAppointments(ClientMenu view) {
        if (userAppointmentsView == null || !userAppointmentsView.isVisible()) {
            userAppointmentsView = new UserAppointmentsView(view.getUsername());
            UserAppointmentsModel userAppointmentsModel = new UserAppointmentsModel(view.getUsername());
            userAppointmentsController = new UserAppointmentsController(userAppointmentsView, userAppointmentsModel); // Initialize the controller
            userAppointmentsView.setVisible(true);
            view.setState(JFrame.ICONIFIED);

            userAppointmentsView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    view.setState(JFrame.NORMAL);
                }
            });
        } else {
            userAppointmentsView.toFront();
        }
    }
}