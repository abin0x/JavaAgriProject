package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.input.MouseEvent;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField mobileField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    // --- Action: Register Button Clicked ---
    @FXML
    protected void handleRegister(ActionEvent event) {

        // 1. Get data from the form
        String name = fullNameField.getText();
        String email = emailField.getText();
        String mobile = mobileField.getText();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        // 2. Validate Empty Fields
        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "দয়া করে সব তথ্য পূরণ করুন!");
            return;
        }

        // 3. Validate Password Match
        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "পাসওয়ার্ড দুটি মিলছে না!");
            return;
        }

        // 4. Try to Register User
        try {
            JsonDbService dbService = new JsonDbService();
            String hashedPassword = JsonDbService.hashPassword(pass);
            String usernameForDb = mobile; // using mobile as the unique ID

            User newUser = new User(name, email, mobile, usernameForDb, hashedPassword);

            // Check if registration logic in DB is successful
            if (dbService.registerUser(newUser)) {

                // --- SUCCESS POPUP ---
                showAlert(Alert.AlertType.INFORMATION, "অভিনন্দন", "আপনার অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে!");

                // --- Switch to Login Page ---
                switchToLoginScreen(event);

            } else {
                showAlert(Alert.AlertType.ERROR, "ত্রুটি", "এই মোবাইল নম্বরটি ইতিমধ্যে নিবন্ধিত হয়েছে।");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "সিস্টেম ত্রুটি", "ডাটাবেস সেভ করার সময় সমস্যা হয়েছে: " + e.getMessage());
        }
    }

    // --- Helper: Navigation Logic ---
    private void switchToLoginScreen(javafx.event.Event event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 600);

            // Add CSS if available
            try {
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS not found, skipping.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Action: "Login Here" Link Clicked ---
    @FXML
    public void switchToLogin(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 600);

            // Add CSS to the new scene
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // Get the current window (Stage) from the Label that was clicked
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Helper: Show Popup Message ---
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}