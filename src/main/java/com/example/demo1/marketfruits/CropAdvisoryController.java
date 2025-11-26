package com.example.demo1.marketfruits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CropAdvisoryController implements Initializable {

    // Sidebar buttons
    @FXML
    private Button btnHome;

    @FXML
    private Button btnAiHelper;

    @FXML
    private Button btnStorage;

    @FXML
    private Button btnFarmWeather;

    @FXML
    private Button btnAgriAnalysis;

    @FXML
    private Button btnLocalManagement;

    @FXML
    private Button btnAgriNews;

    @FXML
    private Button btnCropPlanning;

    @FXML
    private Button btnAdvisory;

    @FXML
    private Button btnProfitLoss;

    @FXML
    private Button btnWeather;

    @FXML
    private Button btnMarket;

    @FXML
    private Button btnFarmerMarket;

    @FXML
    private Button btnSoilHealth;

    @FXML
    private Button btnPestDetection;

    @FXML
    private Button btnComments;

    @FXML
    private Button btnWamService;

    @FXML
    private Button btnNewsTraffic;

    @FXML
    private Button btnEmergencyHelp;

    @FXML
    private Button btnCommunity;

    @FXML
    private Button btnStudy;

    @FXML
    private Button btnProfile;

    // Filter buttons
    @FXML
    private Button btnGuide;

    @FXML
    private Button btnFertilizer;

    @FXML
    private Button btnIrrigation;

    @FXML
    private Button btnCropRotation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("ফসল পরিকল্পনা ও পরামর্শ পেজ চালু হয়েছে!");

        // Set up button click handlers
        setupNavigationHandlers();
    }

    private void setupNavigationHandlers() {
        // 1. Sidebar Navigation
        if (btnHome != null) {
            btnHome.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/dashboard.fxml"));
        }
        if (btnAdvisory != null) {
            btnAdvisory.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/CropAdvisory.fxml"));
        }
        if (btnAiHelper != null) {
            btnAiHelper.setOnAction(event -> showComingSoon("AI সহায়ক"));
        }

        // 2. Filter Button Navigation (Top Bar)

        // Guide (Current Page - just refreshes)
        if (btnGuide != null) {
            btnGuide.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/CropAdvisory.fxml"));
        }

        // Fertilizer Calculator
        if (btnFertilizer != null) {
            btnFertilizer.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/FertilizerCalculator.fxml"));
        }

        // Irrigation Calculator
        if (btnIrrigation != null) {
            btnIrrigation.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/IrrigationCalculator.fxml"));
        }
        if (btnStorage != null) {
            btnStorage.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/WarehouseView.fxml"));
        }
        if (btnLocalManagement != null) {
            btnLocalManagement.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/LocalManagement.fxml"));
        }


        // Crop Rotation (Assuming you have a file for this, otherwise shows message)
        if (btnCropRotation != null) {
            btnCropRotation.setOnAction(event -> showComingSoon("ফসল চক্র (Crop Rotation)"));
            // If you have the page, use this instead:
             btnCropRotation.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/CropRotation.fxml"));
        }
    }

    /**
     * Updated loadPage method to use the Event Source for the Stage
     * This prevents NullPointerException if btnHome is not available
     */
    private void loadPage(javafx.event.ActionEvent event, String fxmlPath) {
        try {
            System.out.println("পেজ লোড হচ্ছে: " + fxmlPath);

            // 1. Get the current Stage from the button that was clicked
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // 2. Load the new FXML
            URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();

            // 3. Create Scene
            Scene scene = new Scene(root);

            // 4. Add CSS
            // Add Global Dashboard CSS
            String dashboardCss = getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm();
            if (dashboardCss != null) scene.getStylesheets().add(dashboardCss);

            // Add specific CSS if needed
            if (fxmlPath.contains("CropAdvisory")) {
                String cropCss = getClass().getResource("/com/example/demo1/css/CropAdvisory.css").toExternalForm();
                if (cropCss != null) scene.getStylesheets().add(cropCss);
            }

            // 5. Set Scene
            stage.setScene(scene);
            stage.show();

            System.out.println("পেজ সফলভাবে লোড হয়েছে!");

        } catch (Exception e) {
            System.err.println("পেজ লোড করতে সমস্যা হয়েছে: " + fxmlPath);
            e.printStackTrace();
            // Optional: Show an alert to the user here
        }
    }

    /**
     * Show a "Coming Soon" message for pages that are not yet implemented
     */
    private void showComingSoon(String pageName) {
        System.out.println(pageName + " শীঘ্রই আসছে...");
        // You can add a dialog or notification here later
    }
}