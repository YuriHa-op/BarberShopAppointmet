package client.client.view;

import common.utility.Callback;
import common.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Callback loginCallback;
    private Callback registerCallback;

    public LoginView() {
        setTitle("Login View");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        common.ui.BackgroundPanel panel = new common.ui.BackgroundPanel("/background.png");
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Theme theme = Theme.getInstance();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(theme.createFont(16));
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(theme.createFont(16));
        usernameField.setBackground(new Color(200, 200, 200));
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(theme.createFont(16));
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(theme.createFont(16));
        passwordField.setBackground(new Color(200, 200, 200));
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton = theme.createButton("Login");
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        registerButton = theme.createButton("Register");
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            if (loginCallback != null) {
                loginCallback.execute();
            }
        });

        registerButton.addActionListener(e -> {
            if (registerCallback != null) {
                registerCallback.execute();
            }
        });

        add(panel);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setLoginCallback(Callback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public void setRegisterCallback(Callback registerCallback) {
        this.registerCallback = registerCallback;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}