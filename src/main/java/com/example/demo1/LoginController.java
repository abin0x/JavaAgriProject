package com.example.demo1;

import javafx.event.ActionEvent; // IMPORTANT IMPORT
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;        // IMPORTANT IMPORT
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;       // IMPORTANT IMPORT
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            // Existing error code
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService();
            User loggedInUser = dbService.loginUser(username, password);

            if (loggedInUser != null) {
                showAlert(Alert.AlertType.INFORMATION, "সফল", "লগইন সফল হয়েছে! স্বাগতম, " + loggedInUser.getName());
                // Proceed to the main application view
                System.out.println("User logged in: " + username);
            } else {
                showAlert(Alert.AlertType.ERROR, "ব্যর্থ", "ভুল ইউজারনেম বা পাসওয়ার্ড");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "ডাটাবেস সংযোগে সমস্যা: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Removes the default header text for a cleaner look
        alert.setContentText(message);
        alert.showAndWait(); // Shows the alert and waits for the user to close it
    }


    // --- THIS IS THE FIXED METHOD ---
    @FXML
    public void onOpenRegisterClick(ActionEvent event) {
        try {
            // 1. Load the Register FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent root = fxmlLoader.load();

            // 2. Create the Scene
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // 3. Get the current Window (Stage) using the click event
            // This line replaces your "usernameField" logic
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Switch the scene
            stage.setTitle("Create Account");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}