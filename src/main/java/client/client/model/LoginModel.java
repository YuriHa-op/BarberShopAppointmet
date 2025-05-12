package client.client.model;

import common.service.UserService;

public class LoginModel {
    private final UserService userService;

    public LoginModel() {
        this.userService = UserService.getInstance();
    }

    public boolean authenticateUser(String username, String password) throws Exception {
        return userService.loginUser(username, password);
    }
}
