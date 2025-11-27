package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Import Helper
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // --- Core Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery;

    // --- Feature Buttons (Placeholders) ---
    @FXML private Button btnAiHelper, btnVideoEducation, btnFarmWeather, btnAgriAnalysis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Dashboard Initialized");

        // 1. Setup Main Sidebar Navigation (1 Line)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery);

        // 2. Setup Placeholder Features
        setupPlaceholder(btnAiHelper, "AI Helper");
        setupPlaceholder(btnVideoEducation, "Video Education");
        setupPlaceholder(btnFarmWeather, "Farm Weather");
        setupPlaceholder(btnAgriAnalysis, "Agri Analysis");
    }

    // --- Helper for Coming Soon Buttons ---
    private void setupPlaceholder(Button btn, String featureName) {
        if (btn != null) {
            btn.setOnAction(e -> showComingSoon(featureName));
        }
    }

    private void showComingSoon(String title) {
        System.out.println(title + " clicked.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(null);
        alert.setContentText(title + " feature is currently under development.");
        alert.show();
    }
}