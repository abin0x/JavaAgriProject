package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper;
import com.example.demo1.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader; // 🛑 নতুন আমদানি 🛑
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane; // 🛑 নতুন আমদানি 🛑
import javafx.scene.Parent; // 🛑 নতুন আমদানি 🛑

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

public class DashboardController implements Initializable {

    // --- Core Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, btnProfile;

    // --- All Other Feature Buttons ---
    @FXML private Button btnAiHelper, btnVideoEducation, btnFarmWeather, btnAgriAnalysis;
    @FXML private Button btnAgriNews, btnCropPlanning, btnProfitLoss, btnWeather, btnMarket, btnFarmerMarket, btnSoilHealth, btnPestDetection, btnComments, btnWamService, btnNewsTraffic, btnEmergencyHelp, btnCommunity, btnStudy;

    // 🛑 ফিক্স: FXML থেকে লোড হওয়া কন্টেন্ট এরিয়া 🛑
    @FXML private StackPane contentArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Dashboard Initialized");

        // 1. Setup Main Sidebar Navigation
        // আপনি চাইলে এই সেটআপের ভেতরেই loadContent কল করতে পারেন।
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // 🛑 ফিক্স ১: initialize-এ প্রথম কন্টেন্ট (হোম) লোড করুন 🛑
        // এই হোম FXML টিই আপনার পুরনো ড্যাশবোর্ড কন্টেন্ট ধারণ করবে।
        loadContent("/com/example/demo1/fxml/home-content.fxml");

        // 2. Setup Profile Navigation
        // যেহেতু আপনি FXML-এ onAction="#handleProfileClick" সেট করেছেন, তাই এই লজিকটি (setOnAction) অপ্রয়োজনীয়, তবে এটি থাকলেও সাধারণত সমস্যা হয় না।
        if (btnProfile != null) {
            btnProfile.setOnAction(e -> handleProfileClick(e));
        }

        // 3. Setup Placeholder Features (বাকি বাটনগুলোও loadContent ব্যবহার করতে পারে)
        setupPlaceholder(btnAiHelper, "AI সহায়ক");
        setupPlaceholder(btnVideoEducation, "ভিডিও শিক্ষা");
        // ... অন্যান্য বাটন ...
    }

    // 🛑 নতুন মেথড: StackPane এ FXML লোড করার জন্য 🛑
    private void loadContent(String fxmlPath) {
        if (contentArea == null) {
            System.err.println("❌ ERROR: contentArea StackPane is NULL. FXML might not be correctly linked.");
            return;
        }
        try {
            // FXML লোড করুন
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));

            // contentArea এর ভেতরের পুরাতন কন্টেন্ট সরিয়ে নতুন FXML যুক্ত করুন
            contentArea.getChildren().setAll(content);

            System.out.println("Page loaded successfully into contentArea: " + fxmlPath);

        } catch (IOException e) {
            System.err.println("Failed to load FXML content: " + fxmlPath + e.getMessage());
            showErrorAlert("লোডিং ত্রুটি", "পেজ লোড করা যায়নি: " + fxmlPath);
        } catch (Exception e) {
            System.err.println("General error loading content: " + e.getMessage());
        }
    }


    // 🛑 ফিক্স ২: Profile ক্লিক হ্যান্ডলার পরিবর্তন করুন 🛑
    @FXML
    private void handleProfileClick(ActionEvent event) {
        System.out.println("DEBUG: Attempting to load Profile into contentArea. Session status: " + (SessionManager.getLoggedInUser() != null ? "Active" : "NULL"));

        // ❌ NavigationHelper.navigateTo কল করবেন না, কারণ এটি পুরো Scene পরিবর্তন করে!
        // ❌ এর পরিবর্তে loadContent মেথড কল করুন।

        loadContent("/com/example/demo1/fxml/profile-view.fxml");

        // হোম বাটনে ক্লিক হলে, আপনি চাইলে এটিও যোগ করতে পারেন:
        // if (event.getSource() == btnHome) {
        //    loadContent("/com/example/demo1/fxml/home-content.fxml");
        // }
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