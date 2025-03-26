package com.windows;

import com.models.User;
import com.services.custom_exceptions.ValidationException;
import com.services.interfaces.IAuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AuthWindow {
    private final JFrame auth;
    private User currentUser;
    private final IAuthenticationService authenticationService;
    private final WindowsManager windowsManager;
    private JPasswordField confirmPasswordField;
    private JTextField lastNameText;
    private JTextField firstNameText;

    public AuthWindow(boolean isLogin, WindowsManager windowsManager, IAuthenticationService service) {
        this.windowsManager = windowsManager;
        this.authenticationService = service;
        int marginButton = 80;
        String text = "Register";
        if (isLogin) {
            text = "Login";
            marginButton = 0;
        }
        auth = new JFrame(text);
        JPanel panel = new JPanel();
        auth.setResizable(false);
        panel.setLayout(null);

        JLabel label = new JLabel("Please " + text.toLowerCase());
        label.setBounds(150, 20, 400, 35);
        label.setFont(new Font("Verdana", Font.PLAIN, 30));

        JLabel emailLabel = new JLabel("Enter your email: ");
        emailLabel.setBounds(50, 80, 200, 21);
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

        JTextField emailText = new JTextField(30);
        emailText.setFont(new Font("Verdana", Font.PLAIN, 16));
        emailText.setBounds(210, 80, 250, 25);

        JLabel passwordLabel = new JLabel("Enter your password: ");
        passwordLabel.setBounds(50, 120, 200, 21);
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("Verdana", Font.PLAIN, 16));
        passwordField.setBounds(240, 120, 250, 25);


        if (!isLogin) {
            JLabel confirmPasswordLabel = new JLabel("Confirm your password: ");
            confirmPasswordLabel.setBounds(50, 150, 200, 21);
            confirmPasswordLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

            confirmPasswordField = new JPasswordField(30);
            confirmPasswordField.setBounds(250, 150, 250, 25);
            confirmPasswordField.setFont(new Font("Verdana", Font.PLAIN, 16));

            JLabel firstNameLabel = new JLabel("Enter your first name: ");
            firstNameLabel.setBounds(50, 180, 200, 21);
            firstNameLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

            firstNameText = new JTextField(30);
            firstNameText.setFont(new Font("Verdana", Font.PLAIN, 16));
            firstNameText.setBounds(240, 180, 250, 25);

            JLabel lastNameLabel = new JLabel("Enter your last name: ");
            lastNameLabel.setBounds(50, 210, 200, 21);
            lastNameLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

            lastNameText = new JTextField(30);
            lastNameText.setFont(new Font("Verdana", Font.PLAIN, 16));
            lastNameText.setBounds(240, 210, 250, 25);

            panel.add(firstNameLabel);
            panel.add(firstNameText);
            panel.add(lastNameLabel);
            panel.add(lastNameText);
            panel.add(confirmPasswordLabel);
            panel.add(confirmPasswordField);
        }

        JButton authButton = new JButton(text);
        authButton.setBounds(180, 170 + marginButton, 150, 50);
        authButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        authButton.addActionListener(l -> {
            String password = String.valueOf(passwordField.getPassword());
            String email = emailText.getText();
            if (isLogin) {
                try {
                    currentUser = authenticationService.login(password, email);
                    this.closeWindow();
                    windowsManager.setUser(currentUser);
                    windowsManager.openMainMenuWindow(currentUser);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(authButton, ex.getMessage());
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(authButton, "Validation error: " + ex.getMessage());
                }
                return;
            }
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
            if (!confirmPassword.equals(password) || password.length() == 0) {
                JOptionPane.showMessageDialog(authButton, "Password mismatch");
                return;
            }
            try {
                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                currentUser = new User(firstName, lastName, new ArrayList<>(), confirmPassword, email, 1000);
                currentUser = authenticationService.register(currentUser);
                this.closeWindow();
                windowsManager.openMainMenuWindow(currentUser);
                windowsManager.setUser(currentUser);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(authButton, ex.getMessage());
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(authButton, "Validation error: " + ex.getMessage());
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setBounds(400, 10, 100, 30);
        backButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        backButton.addActionListener(l -> {
            this.closeWindow();
            windowsManager.openIntroductionWindow();
        });

        panel.add(label);
        panel.add(emailLabel);
        panel.add(passwordLabel);
        panel.add(authButton);
        panel.add(backButton);

        panel.add(emailText);
        panel.add(passwordField);

        auth.add(panel);
        auth.setSize(530, 380);
        auth.setLocationRelativeTo(null);
        auth.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        auth.setVisible(true);
    }

    public void closeWindow() {
        auth.setVisible(false);
        auth.dispose();
    }
}
