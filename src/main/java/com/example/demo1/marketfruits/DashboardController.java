package com.example.demo1.marketfruits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    // Sidebar buttons
    @FXML
    private Button btnHome;

    @FXML
    private Button btnAiHelper;

    @FXML
    private Button btnVideoEducation;

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
    private Button btnStorage;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("কৃষি সাখী ড্যাশবোর্ড চালু হয়েছে!");

        // Set up button click handlers
        setupNavigationHandlers();
    }

    private void setupNavigationHandlers() {
        // Home button - reload dashboard
        if (btnHome != null) {
            btnHome.setOnAction(event -> loadPage("/com/example/demo1/fxml/dashboard.fxml"));
        }

        // Advisory button - load crop advisory page
        if (btnAdvisory != null) {
            btnAdvisory.setOnAction(event -> loadPage("/com/example/demo1/fxml/CropAdvisory.fxml"));
        }

        // AI Helper
        if (btnStorage != null) {
            btnStorage.setOnAction(event -> loadPage("/com/example/demo1/fxml/WarehouseView.fxml"));

        }
        if (btnLocalManagement != null) {
            btnLocalManagement.setOnAction(event -> loadPage("/fxml/LocalManagement.fxml"));

        }

        // Video Education
        if (btnVideoEducation != null) {
            btnVideoEducation.setOnAction(event -> {
                System.out.println("ভিডিও শিক্ষা পেজ লোড হচ্ছে...");
                showComingSoon("ভিডিও শিক্ষা");
            });
        }

        // Farm Weather
        if (btnFarmWeather != null) {
            btnFarmWeather.setOnAction(event -> {
                System.out.println("খামার আবহাওয়া পেজ লোড হচ্ছে...");
                showComingSoon("খামার আবহাওয়া");
            });
        }

        // Agri Analysis
        if (btnAgriAnalysis != null) {
            btnAgriAnalysis.setOnAction(event -> {
                System.out.println("কৃষি বিশ্লেষণ পেজ লোড হচ্ছে...");
                showComingSoon("কৃষি বিশ্লেষণ");
            });
        }

        // Add more handlers for other menu items as needed
        // You can follow the same pattern for all menu items
    }

    /**
     * Load a new page and replace the entire scene
     */
    private void loadPage(String fxmlPath) {
        try {
            System.out.println("পেজ লোড হচ্ছে: " + fxmlPath);

            // Get current stage
            Stage stage = (Stage) btnHome.getScene().getWindow();

            // Load new FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create new scene with the same size
            Scene scene = new Scene(root);

            // Apply CSS
            String css = getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            }

            // If it's CropAdvisory page, add its CSS too
            if (fxmlPath.contains("CropAdvisory")) {
                String cropCss = getClass().getResource("/com/example/demo1/css/CropAdvisory.css").toExternalForm();
                if (cropCss != null) {
                    scene.getStylesheets().add(cropCss);
                }
            }

            // Set the new scene
            stage.setScene(scene);

            System.out.println("পেজ সফলভাবে লোড হয়েছে!");

        } catch (IOException e) {
            System.err.println("পেজ লোড করতে সমস্যা হয়েছে: " + fxmlPath);
            e.printStackTrace();
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