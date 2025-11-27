package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Import Helper
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CropRotationController implements Initializable {

    // --- Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, btnLocalManagement, btnStorage;

    // --- Inputs ---
    @FXML private ComboBox<String> districtComboBox, landTypeComboBox, soilTypeComboBox, currentSeasonComboBox, prevCropComboBox;
    @FXML private RadioButton irrigationYes;
    @FXML private Button generateBtn, resetBtn;

    // --- Results ---
    @FXML private VBox resultsContainer, emptyState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Crop Rotation Controller Initialized");

        // 1. Setup Navigation (1 Line)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        // 2. Setup Logic
        populateDropdowns();
        generateBtn.setOnAction(e -> calculateRotation());
        resetBtn.setOnAction(e -> resetForm());
    }

    // ===========================
    // 1. DATA & INPUTS
    // ===========================
    private void populateDropdowns() {
        districtComboBox.getItems().addAll("ঢাকা", "কুমিল্লা", "বগুড়া", "রাজশাহী", "রংপুর", "দিনাজপুর", "যশোর", "বরিশাল");
        landTypeComboBox.getItems().addAll("উঁচু জমি", "মাঝারি উঁচু জমি", "মাঝারি নিচু জমি", "নিচু জমি");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল দোআঁশ", "এঁটেল");
        currentSeasonComboBox.getItems().addAll("রবি (শীত)", "খরিফ-১ (গ্রীষ্ম)", "খরিফ-২ (বর্ষা)");
        prevCropComboBox.getItems().addAll("আমন ধান", "বোরো ধান", "গম", "ভুট্টা", "আলু", "সরিষা", "মসুর ডাল", "পাট", "সবজি");
    }

    // ===========================
    // 2. CALCULATION LOGIC
    // ===========================
    private void calculateRotation() {
        if (!validateInputs()) return;

        String land = landTypeComboBox.getValue();
        String soil = soilTypeComboBox.getValue();
        String season = currentSeasonComboBox.getValue();
        String prevCrop = prevCropComboBox.getValue();
        boolean irrigation = irrigationYes.isSelected();

        resultsContainer.getChildren().clear();
        emptyState.setVisible(false); emptyState.setManaged(false);

        // Logic for Suggesting Patterns
        if (prevCrop.contains("আমন") || season.contains("রবি")) {
            if ((soil.contains("দোআঁশ") || soil.contains("বেলে")) && irrigation) {
                addCard("বানিজ্যিক লাভজনক মডেল", "সর্বাধিক মুনাফা", "💰",
                        new Step("সরিষা/আলু", "রবি"), new Step("বোরো/ভুট্টা", "খরিফ-১"), new Step("আমন ধান", "খরিফ-২"),
                        "আলু বা সরিষা স্বল্পমেয়াদী লাভজনক ফসল। এরপর বোরো বা ভুট্টা চাষ করলে ফলন ভালো হয়।");
            }

            addCard("মাটির স্বাস্থ্য সুরক্ষা মডেল", "উর্বরতা বৃদ্ধি", "🌿",
                    new Step("মসুর/মুগ ডাল", "রবি"), new Step("পাট/আউশ", "খরিফ-১"), new Step("আমন ধান", "খরিফ-২"),
                    "ডাল জাতীয় ফসল মাটির নাইট্রোজেন বাড়ায়। পাট মাটির গঠন ভালো রাখে।");

            if (land.contains("উঁচু")) {
                addCard("স্বল্প সেচ মডেল", "পানি সাশ্রয়ী", "💧",
                        new Step("গম", "রবি"), new Step("মুগ ডাল", "খরিফ-১"), new Step("আমন ধান", "খরিফ-২"),
                        "বোরো ধানের চেয়ে গমে সেচ কম লাগে। উঁচু জমির জন্য এটি আদর্শ।");
            }
        }
        else if (prevCrop.contains("বোরো") || season.contains("খরিফ-১")) {
            addCard("সবুজ সার মডেল", "জৈব সার", "🍀",
                    new Step("ধঞ্চে", "খরিফ-১"), new Step("আমন ধান", "খরিফ-২"), new Step("সরিষা", "রবি"),
                    "ধঞ্চে চাষ করে মাটিতে মিশিয়ে দিলে ইউরিয়া সারের খরচ অর্ধেক কমে যায়।");

            addCard("অর্থকরী ফসল মডেল", "পাট চাষ", "💸",
                    new Step("পাট", "খরিফ-১"), new Step("আমন ধান", "খরিফ-২"), new Step("গম", "রবি"),
                    "পাটের পাতা পচে মাটির উর্বরতা বাড়ায় এবং এটি লাভজনক।");
        }
        else {
            addCard("আদর্শ সবজি চক্র", "পারিবারিক পুষ্টি", "🥗",
                    new Step("বেগুন/টমেটো", "রবি"), new Step("লালশাক", "খরিফ-১"), new Step("লতাজাতীয়", "খরিফ-২"),
                    "একই জমিতে বারবার একই সবজি না করে চক্রাকারে চাষ করুন।");
        }
    }

    private boolean validateInputs() {
        if (landTypeComboBox.getValue() == null || soilTypeComboBox.getValue() == null ||
                currentSeasonComboBox.getValue() == null || prevCropComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "দয়া করে সব তথ্য পূরণ করুন।").show();
            return false;
        }
        return true;
    }

    // ===========================
    // 3. UI GENERATION
    // ===========================
    private void addCard(String title, String badge, String badgeIcon, Step s1, Step s2, Step s3, String tip) {
        VBox card = new VBox(10);
        card.getStyleClass().add("rotation-card");

        // Header
        Label badgeLbl = new Label(badgeIcon + " " + badge);
        badgeLbl.getStyleClass().add("option-badge");
        HBox header = new HBox(10, new Label(title), new Region(), badgeLbl);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        ((Label)header.getChildren().get(0)).getStyleClass().add("option-title");

        // Cycle View
        HBox cycle = new HBox(5, createStep(s1), createArrow(), createStep(s2), createArrow(), createStep(s3));
        cycle.setAlignment(Pos.CENTER);
        cycle.getStyleClass().add("cycle-container");

        // Benefit Footer
        HBox footer = new HBox(10, new Label("💡"), new Label(tip));
        footer.getStyleClass().add("benefit-box");
        ((Label)footer.getChildren().get(1)).setWrapText(true);

        card.getChildren().addAll(header, cycle, footer);
        resultsContainer.getChildren().add(card);
    }

    private VBox createStep(Step s) {
        VBox box = new VBox(2, new Label(getCropIcon(s.name)), new Label(s.name), new Label(s.season));
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("cycle-step");
        box.setPrefWidth(90);
        ((Label)box.getChildren().get(1)).setWrapText(true); // Name wrapping
        ((Label)box.getChildren().get(1)).getStyleClass().add("step-crop");
        ((Label)box.getChildren().get(2)).getStyleClass().add("step-season");
        return box;
    }

    private Label createArrow() {
        Label arrow = new Label("➜");
        arrow.getStyleClass().add("arrow-icon");
        return arrow;
    }

    private String getCropIcon(String name) {
        if (name.contains("ধান")) return "🌾";
        if (name.contains("আলু") || name.contains("সবজি")) return "🥔";
        if (name.contains("ভুট্টা")) return "🌽";
        if (name.contains("পাট") || name.contains("ধঞ্চে")) return "🌿";
        if (name.contains("সরিষা")) return "🌼";
        if (name.contains("ডাল")) return "🥘";
        return "🌱";
    }

    private void resetForm() {
        prevCropComboBox.setValue(null);
        resultsContainer.getChildren().clear();
        emptyState.setVisible(true); emptyState.setManaged(true);
    }

    // --- Helper Class ---
    private static class Step {
        String name, season;
        Step(String n, String s) { name = n; season = s; }
    }
}