package client.client.controller;

import client.client.model.RegistrationModel;
import client.client.view.RegistrationView;
import common.service.UserService;
import common.utility.Logger;

public class RegistrationController {
    private final RegistrationView view;
    private final RegistrationModel model;
    private final UserService userService;

    public RegistrationController(RegistrationView view, RegistrationModel model) {
        this.view = view;
        this.model = model;
        this.userService = UserService.getInstance();
        initController();
    }

    private void initController() {
        view.setRegisterCallback(this::registerUser);
    }

    private void registerUser() {
        String username = view.getUsernameField().getText();
        String password = new String(view.getPasswordField().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Username and password cannot be empty");
            return;
        }

        try {
            model.setUsername(username);
            model.setPassword(password);

            boolean success = userService.registerUser(username, password);
            if (success) {
                Logger.log(username, "register", "User registered successfully");
                view.showMessage("Registration successful! You can now login.");
                view.dispose();
            } else {
                view.showMessage("Registration failed. Username may already exist.");
            }
        } catch (Exception ex) {
            Logger.log(username, "register", "Error: " + ex.getMessage());
            view.showMessage("Error: " + ex.getMessage());
        }
    }
}