package common.service;

import common.model.Request;
import common.model.Response;
import common.model.User;
import common.utility.JSONUtil;
import common.utility.RMICommunicator;

public class UserService {
    private static UserService instance;
    private final RMICommunicator communicator;

    private UserService() {
        this.communicator = RMICommunicator.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean registerUser(String username, String password) throws Exception {
        User user = new User(username, password);
        String userJSON = JSONUtil.marshal(user, User.class);
        Request request = new Request("register", userJSON);
        Response response = communicator.processRequest(request);

        return response.getStatus().equalsIgnoreCase("success");
    }

    public boolean loginUser(String username, String password) throws Exception {
        User user = new User(username, password);
        String userJSON = JSONUtil.marshal(user, User.class);
        Request request = new Request("login", userJSON);
        Response response = communicator.processRequest(request);

        return response.getStatus().equalsIgnoreCase("success");
    }
}