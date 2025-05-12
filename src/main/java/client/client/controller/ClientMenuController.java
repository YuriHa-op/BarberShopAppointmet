package client.client.controller;

import client.client.model.ClientMenuModel;
import client.client.model.LoginModel;
import client.client.view.ClientMenu;
import client.client.view.LoginView;
import common.utility.Logger;

import javax.swing.*;
import java.rmi.RemoteException;

public class ClientMenuController {
    private ClientMenu view;
    private ClientMenuModel model;

    public ClientMenuController(ClientMenu view, ClientMenuModel model) {
        this.view = view;
        this.model = model;
        initController();
    }

    private void initController() {
        view.getShowCalendarButton().addActionListener(e -> showCalendar());
        view.getBookButton().addActionListener(e -> showBookedAppointments());
        view.getLogoutButton().addActionListener(e -> logout());
    }

    private void showCalendar() {
        model.showCalendar(view);
    }

    private void showBookedAppointments() {
        model.showBookedAppointments(view);
    }

    private void logout() {
        Logger.log(view.getUsername(), "logout", "User logged out");
        view.dispose();
        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel();  // Create the model
        new LoginController(loginView, loginModel);  // Pass the model to the controller
        loginView.setVisible(true);
    }
}