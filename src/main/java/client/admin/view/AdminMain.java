package client.admin.view;

import client.admin.controller.AdminMenuController;
import client.admin.model.AdminMenuModel;
import common.utility.NetworkUtil;

import javax.swing.SwingUtilities;

public class AdminMain {
    public static void main(String[] args) {
        // Initialize RMI connection instead of testing socket
        NetworkUtil.initRMIConnection();

        SwingUtilities.invokeLater(() -> {
            AdminMenuView menuView = new AdminMenuView();
            AdminMenuModel menuModel = new AdminMenuModel();
            new AdminMenuController(menuView, menuModel);
            menuView.setVisible(true);
        });
    }
}