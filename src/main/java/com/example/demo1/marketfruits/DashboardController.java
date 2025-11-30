package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper;
import com.example.demo1.utils.SessionManager;
import javafx.event.ActionEvent; // এই লাইনটি ঠিক করা হয়েছে
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

public class DashboardController implements Initializable {

    // --- Core Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, btnProfile;

    // --- All Other Feature Buttons ---
    @FXML private Button btnAiHelper, btnVideoEducation, btnFarmWeather, btnAgriAnalysis;
    @FXML private Button btnAgriNews, btnCropPlanning, btnProfitLoss, btnWeather, btnMarket, btnFarmerMarket, btnSoilHealth, btnPestDetection, btnComments, btnWamService, btnNewsTraffic, btnEmergencyHelp, btnCommunity, btnStudy;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Dashboard Initialized");

        // 1. Setup Main Sidebar Navigation
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // 2. Setup Profile Navigation
        if (btnProfile != null) {
            // FXML-এ onAction সেট না থাকলে, এই লজিকটি হ্যান্ডেল করবে
            btnProfile.setOnAction(e -> handleProfileClick(e));
        }

        // 3. Setup Placeholder Features
        setupPlaceholder(btnAiHelper, "AI সহায়ক");
        setupPlaceholder(btnVideoEducation, "ভিডিও শিক্ষা");
        setupPlaceholder(btnFarmWeather, "খামার আবহাওয়া");
        setupPlaceholder(btnAgriAnalysis, "কৃষি বিশ্লেষণ");
        setupPlaceholder(btnAgriNews, "কৃষি সংবাদ");
        setupPlaceholder(btnCropPlanning, "ফসল পরিকল্পনা");
        setupPlaceholder(btnProfitLoss, "লাভ-ক্ষতি হিসাব");
        setupPlaceholder(btnWeather, "আবহাওয়া");
        setupPlaceholder(btnMarket, "বাজার");
        setupPlaceholder(btnFarmerMarket, "কৃষক বাজার");
        setupPlaceholder(btnSoilHealth, "মাটি স্বাস্থ্য");
        setupPlaceholder(btnPestDetection, "কীটপতঙ্গ শনাক্ত");
        setupPlaceholder(btnComments, "মন্তব্য ভাড্া");
        setupPlaceholder(btnWamService, "ওয়াম সেবা");
        setupPlaceholder(btnNewsTraffic, "খবর ট্র্যাফিক");
        setupPlaceholder(btnEmergencyHelp, "জরুরি সহায়তা");
        setupPlaceholder(btnCommunity, "সম্প্রদায়");
        setupPlaceholder(btnStudy, "অধ্যয়ন");
    }

    // 🛑 প্রোফাইল ক্লিক হ্যান্ডলার (এখানে ActionEvent পাস করে Scene লোড করা হলো) 🛑
    @FXML
    private void handleProfileClick(ActionEvent event) {
        // আপনার FXML এ যদি onAction="handleProfileClick" সেট থাকে, তবে এই মেথডটি ব্যবহার করুন।
        // Session ট্র্যাকিং যোগ করা হলো:
        System.out.println("DEBUG: Attempting to navigate to Profile. Session status: " + (SessionManager.getLoggedInUser() != null ? "Active" : "NULL"));

        try {
            // ✅ ফিক্স: সঠিক Scene অবজেক্ট পাওয়ার জন্য Event Source ব্যবহার করা হলো ✅
            // এটি বর্তমান বাটনের Scene ব্যবহার করবে, যা null হবে না।
            NavigationHelper.navigateTo(((Button) event.getSource()).getScene(),
                    "/com/example/demo1/fxml/profile-view.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load Profile View: " + e.getMessage());
            showErrorAlert("লোডিং ত্রুটি", "প্রোফাইল পেজ লোড করা যায়নি।");
        } catch (ClassCastException e) {
            // যদি initialize() থেকে কল হয়
            System.err.println("Error casting event source to Button in handleProfileClick.");
            showErrorAlert("নেভিগেশন ত্রুটি", "বাটনের Scene পেতে সমস্যা হয়েছে।");
        }
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

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}