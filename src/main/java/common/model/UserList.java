package common.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> users;

    public UserList() { }

    public UserList(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static List<User> loadFromFile(String filename) {
        List<User> users = new ArrayList<>();
        File file = new File(filename);

        try {
            if (!file.exists()) {
                System.out.println("Users file not found: " + file.getAbsolutePath());
                return users;
            }
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            try {
                JSONObject json = new JSONObject(content);
                JSONArray usersArray = json.getJSONArray("users");

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userJson = usersArray.getJSONObject(i);
                    User user = new User();
                    user.setUsername(userJson.getString("username"));
                    user.setPassword(userJson.getString("password"));
                    users.add(user);
                }

                System.out.println("Successfully loaded " + users.size() + " users");
            } catch (Exception e) {
                System.err.println("JSON parsing error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("File reading error: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public static void saveToFile(String filePath, List<User> users) {
        try (FileWriter writer = new FileWriter(filePath)) {
            JSONObject root = new JSONObject();
            JSONArray usersArray = new JSONArray();

            for (User user : users) {
                JSONObject userJson = new JSONObject();
                userJson.put("username", user.getUsername());
                userJson.put("password", user.getPassword());
                usersArray.put(userJson);
            }

            root.put("users", usersArray);
            writer.write(root.toString(2)); // Pretty print
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        JSONArray usersArray = new JSONArray();

        if (users != null) {
            for (User user : users) {
                usersArray.put(new JSONObject(user.toString()));
            }
        }

        json.put("users", usersArray);
        return json.toString();
    }
}