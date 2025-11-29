package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController { // 🛑 নিশ্চিত করুন যে এই ক্লাস সংজ্ঞাটি ফাইলে একবারই আছে 🛑

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // --- Core Login Logic ---
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "অনুরোধ", "ইউজারনেম এবং পাসওয়ার্ড উভয়ই দিন।");
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService(); // Assumes JsonDbService is in the same package
            User loggedInUser = dbService.loginUser(username, password);

            if (loggedInUser != null) {
                // ✅ Load the Dashboard Scene

                // 🛑 FXML Fix: Absolute path to dashboard.fxml 🛑
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("/com/example/demo1/fxml/dashboard.fxml")
                );
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                // 🛑 CSS Fix: Absolute path to dashboard.css 🛑
                scene.getStylesheets().add(
                        getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm()
                );

                stage.setTitle("কৃষি সাখী ড্যাশবোর্ড");
                stage.setScene(scene);
                stage.show();

                System.out.println("User logged in: " + loggedInUser.getUsername());

            } else {
                showAlert(Alert.AlertType.ERROR, "ব্যর্থ", "ভুল ইউজারনেম/মোবাইল বা পাসওয়ার্ড।");
            }
        } catch (IOException e) {
            // Catches errors like 'Dashboard লোড করতে সমস্যা হয়েছে।'
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "ড্যাশবোর্ড লোড করতে সমস্যা হয়েছে।");
            System.err.println("Error loading Dashboard FXML or resources:");
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "ডাটাবেস বা সিস্টেমে সমস্যা: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Handles the click event to open the Registration view.
     */
    @FXML
    public void onOpenRegisterClick(ActionEvent event) {
        try {
            // ✅ Load the Register FXML

            // 🛑 FXML Fix: Absolute path to register-view.fxml 🛑
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/com/example/demo1/fxml/register-view.fxml")
            );
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 800, 600);

            // 🛑 CSS Fix: Absolute path to style.css (for register view) 🛑
            scene.getStylesheets().add(
                    getClass().getResource("/com/example/demo1/css/style.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("নতুন অ্যাকাউন্ট তৈরি করুন");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ত্রুটি", "রেজিস্টার পেজ লোড করা যায়নি।");
            e.printStackTrace();
        }
    }

    /**
     * Helper method to display JavaFX Alerts.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}