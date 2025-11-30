package com.example.demo1.marketfruits;

import com.example.demo1.User; // আপনার User ক্লাস আমদানি করুন
import com.example.demo1.utils.NavigationHelper; // আপনার NavigationHelper আমদানি করুন
import com.example.demo1.utils.SessionManager; // SessionManager আমদানি করুন

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

public class ProfileController {

    @FXML private Label lblUsernameInitial;
    @FXML private TextField txtName, txtMobile, txtEmail, txtUsername;

    /**
     * কন্ট্রোলার লোড হওয়ার সময় এই মেথডটি কল হয়।
     */
    public void initialize() {
        System.out.println("✅ Profile Page Initialized");
        loadUserProfile();
    }

    /**
     * সেশন ম্যানেজার থেকে লগইন করা ইউজারের তথ্য লোড করে ডিসপ্লে করা হয়।
     */
    private void loadUserProfile() {
        User user = SessionManager.getLoggedInUser();

        if (user != null) {
            // ডাটা ফিল্ডে সেট করা
            txtName.setText(user.getName());
            txtMobile.setText(user.getMobile());
            txtEmail.setText(user.getEmail());
            txtUsername.setText(user.getUsername());

            // প্রোফাইল ইনিশিয়াল সেট করা
            if (user.getName() != null && !user.getName().isEmpty()) {
                lblUsernameInitial.setText(user.getName().substring(0, 1).toUpperCase());
            } else {
                lblUsernameInitial.setText("?");
            }
        } else {
            // যদি কোনো ইউজার লগইন না থাকে, তবে অ্যালার্ট দিয়ে লগইন পেজে পাঠিয়ে দিন
            showAlert(Alert.AlertType.WARNING, "সেশন শেষ", "কোনো সক্রিয় লগইন সেশন নেই।");
            handleLogout(); // লগইন পেজে রিডাইরেক্ট করুন
        }
    }

    /**
     * ছবি আপলোড করার লজিক (বর্তমানে শুধু একটি অ্যালার্ট দেখায়)।
     */
    @FXML
    private void handleUploadPicture() {
        showAlert(Alert.AlertType.INFORMATION, "ফিচার আসছে", "প্রোফাইল ছবি আপলোড ফিচারটি বর্তমানে তৈরি হচ্ছে।");
        // ভবিষ্যতে এখানে FileChooser লজিক যুক্ত করা হবে
    }

    /**
     * লগআউট লজিক: সেশন পরিষ্কার করে লগইন পেজে ফিরে যায়।
     */
    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        try {
            // 🛑 ফিক্স: লগইন পেজের পাথ আপডেট করা হলো 🛑
            // ধরে নেওয়া হলো লগইন পেজ: hello-view.fxml
            NavigationHelper.navigateTo(txtName.getScene(), "/com/example/demo1/fxml/hello-view.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load Login View after logout: " + e.getMessage());
            showErrorAlert("লোডিং ত্রুটি", "লগইন পেজ লোড করা যায়নি।");
        }
    }

    // --- Helper Methods ---

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}