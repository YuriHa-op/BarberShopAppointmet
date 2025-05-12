// Update ClientMain.java
package client.client.view;

import client.client.controller.LoginController;
import client.client.model.LoginModel;
import common.utility.NetworkUtil;

import javax.swing.SwingUtilities;

public class ClientMain {
    public static void main(String[] args) {
        // Initialize RMI connection instead of testing socket
        NetworkUtil.initRMIConnection();

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            LoginModel loginModel = new LoginModel();
            new LoginController(loginView, loginModel);
            loginView.setVisible(true);
        });
    }
}