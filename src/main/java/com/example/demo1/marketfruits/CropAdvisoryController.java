package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Import the Helper
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class CropAdvisoryController implements Initializable {

    // --- Sidebar Buttons (Only the ones used for navigation) ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement;
    @FXML private Button btnAiHelper; // Kept as it had specific logic

    // --- Top Navigation / Filter Buttons ---
    @FXML private Button btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Crop Advisory Page Initialized!");
        setupNavigation();
    }

    // ---------------------------------------------------------
    // NAVIGATION LOGIC
    // ---------------------------------------------------------
    private void setupNavigation() {
        // 1. Setup Sidebar (Home, Advisory, Storage, Local)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement);

        // 2. Setup Top Menu (Guide, Fertilizer, Irrigation, Rotation)
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        // 3. Extra Buttons (Specific to this controller)
        if (btnAiHelper != null) {
            btnAiHelper.setOnAction(e -> System.out.println("AI Helper Feature Coming Soon..."));
        }
    }
}