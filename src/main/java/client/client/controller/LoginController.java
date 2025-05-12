package client.client.controller;

import client.admin.controller.AdminMenuController;
import client.admin.model.AdminMenuModel;
import client.admin.view.AdminMenuView;
import client.client.model.ClientMenuModel;
import client.client.model.LoginModel;
import client.client.model.RegistrationModel;
import client.client.view.ClientMenu;
import client.client.view.LoginView;
import client.client.view.RegistrationView;
import common.utility.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginController {
    private final LoginView view;
    private final LoginModel model;
    private static RegistrationView registrationView;

    public LoginController(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
        initController();
    }

    private void initController() {
        view.setLoginCallback(this::performLogin);
        view.setRegisterCallback(this::openRegistration);
    }

    private void performLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        try {
            boolean success = model.authenticateUser(username, password);
            if (success) {
                Logger.log(username, "login", "User logged in successfully");
                view.showMessage("Login Successful");
                view.dispose();

                if (username.equalsIgnoreCase("barber") || username.equalsIgnoreCase("admin")) {
                    AdminMenuView adminMenuView = new AdminMenuView();
                    AdminMenuModel adminMenuModel = new AdminMenuModel();
                    new AdminMenuController(adminMenuView, adminMenuModel);
                    adminMenuView.setVisible(true);
                } else {
                    ClientMenu bookingView = new ClientMenu(username);
                    ClientMenuModel clientMenuModel = new ClientMenuModel();
                    new ClientMenuController(bookingView, clientMenuModel);
                    bookingView.setVisible(true);
                }
            } else {
                Logger.log(username, "login", "Login failed: Invalid username or password");
                view.showMessage("Login Failed: Invalid username or password");
            }
        } catch (Exception ex) {
            Logger.log(username, "login", "Error: " + ex.getMessage());
            view.showMessage("Error: " + ex.getMessage());
        }
    }

    private void openRegistration() {
        if (registrationView == null || !registrationView.isVisible()) {
            registrationView = new RegistrationView();
            RegistrationModel registrationModel = new RegistrationModel();
            new RegistrationController(registrationView, registrationModel);
            registrationView.setVisible(true);
            view.setState(JFrame.ICONIFIED);

            registrationView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    view.setState(JFrame.NORMAL);
                }
            });
        } else {
            registrationView.toFront();
        }
    }
}