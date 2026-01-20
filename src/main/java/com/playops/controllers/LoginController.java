package com.playops.controllers;

import com.playops.service.AdminService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private final AdminService adminService = new AdminService();

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        AdminService.AuthResult result = adminService.authenticate(username, password);

        if (result.success()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/dashboard/DashboardView.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/ui/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
        } else {
            errorLabel.setText(result.message());
        }
    }
}