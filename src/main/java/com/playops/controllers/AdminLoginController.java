package com.playops.controllers;

import com.playops.service.StoreService;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AdminLoginController {

    private static final String ADMIN_PASSWORD = "admin123"; // simple demo

    public static Node createContent(StoreService storeService, TabPane tabPane) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label label = new Label("Enter Admin Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label feedback = new Label();

        loginButton.setOnAction(e -> {
            if (passwordField.getText().equals(ADMIN_PASSWORD)) {
                feedback.setText("Login successful!");
                // Enable dashboard tab
                tabPane.getTabs().get(1).setDisable(false);
                tabPane.getSelectionModel().select(1);
            } else {
                feedback.setText("Incorrect password.");
            }
        });

        layout.getChildren().addAll(label, passwordField, loginButton, feedback);
        return layout;
    }
}
