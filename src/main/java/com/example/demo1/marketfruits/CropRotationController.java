package com.example.demo1.marketfruits;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CropRotationController implements Initializable {

    // Navigation Buttons
    @FXML private Button btnHome, btnAdvisory, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, btnLocalManagement, btnStorage;
    @FXML private Button btnAiHelper, btnWeather;

    // Inputs
    @FXML private ComboBox<String> districtComboBox;
    @FXML private ComboBox<String> landTypeComboBox;
    @FXML private ComboBox<String> soilTypeComboBox;
    @FXML private ComboBox<String> currentSeasonComboBox;
    @FXML private ComboBox<String> prevCropComboBox;
    @FXML private RadioButton irrigationYes, irrigationRain;
    @FXML private Button generateBtn, resetBtn;

    // Results
    @FXML private VBox resultsContainer, emptyState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Crop Rotation Controller Initialized");

        setupNavigationHandlers();
        populateDropdowns();

        generateBtn.setOnAction(e -> calculateRotation());
        resetBtn.setOnAction(e -> resetForm());
    }

    // ===========================
    // 1. Navigation Logic
    // ===========================
    private void setupNavigationHandlers() {

        if(btnHome != null)
            btnHome.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/dashboard.fxml"));

        if(btnGuide != null)
            btnGuide.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/CropAdvisory.fxml"));

        if(btnFertilizer != null)
            btnFertilizer.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/FertilizerCalculator.fxml"));

        if(btnIrrigation != null)
            btnIrrigation.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/IrrigationCalculator.fxml"));

        if(btnCropRotation != null)
            btnCropRotation.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/CropRotation.fxml"));

        if(btnLocalManagement != null)
            btnLocalManagement.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/LocalManagement.fxml"));

        if(btnStorage != null)
            btnStorage.setOnAction(e -> loadPage(e, "/com/example/demo1/fxml/WarehouseView.fxml"));
    }

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            URL url = getClass().getResource(fxmlPath);
            if(url == null) return;

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Apply CSS
            String[] cssFiles = {"dashboard.css", "CropAdvisory.css", "CropRotation.css"};
            for(String css : cssFiles) {
                URL cssUrl = getClass().getResource("/com/example/demo1/css/" + css);
                if(cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===========================
    // 2. Data Population
    // ===========================
    private void populateDropdowns() {
        districtComboBox.getItems().addAll("ঢাকা", "কুমিল্লা", "বগুড়া", "রাজশাহী", "রংপুর", "দিনাজপুর", "যশোর", "বরিশাল", "ময়মনসিংহ");
        landTypeComboBox.getItems().addAll("উঁচু জমি", "মাঝারি উঁচু জমি", "মাঝারি নিচু জমি", "নিচু জমি");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল দোআঁশ", "এঁটেল", "পলি মাটি");
        currentSeasonComboBox.getItems().addAll("রবি (শীত: নভে-ফেব্রু)", "খরিফ-১ (গ্রীষ্ম: মার্চ-জুন)", "খরিফ-২ (বর্ষা: জুলাই-অক্টো)");

        prevCropComboBox.getItems().addAll(
                "আমন ধান", "বোরো ধান", "গম", "ভুট্টা", "আলু", "সরিষা", "মসুর ডাল", "পাট", "সবজি"
        );
    }

    // ===========================
    // 3. Calculation Logic
    // ===========================
    private void calculateRotation() {

        if(landTypeComboBox.getValue() == null || soilTypeComboBox.getValue() == null ||
                currentSeasonComboBox.getValue() == null || prevCropComboBox.getValue() == null) {

            showAlert("দয়া করে * চিহ্নিত ঘরগুলো পূরণ করুন।");
            return;
        }

        String land = landTypeComboBox.getValue();
        String soil = soilTypeComboBox.getValue();
        String season = currentSeasonComboBox.getValue();
        String prevCrop = prevCropComboBox.getValue();
        boolean hasIrrigation = irrigationYes.isSelected();

        resultsContainer.getChildren().clear();
        emptyState.setVisible(false);
        emptyState.setManaged(false);

        generatePatterns(land, soil, season, prevCrop, hasIrrigation);
    }

    private void generatePatterns(String land, String soil, String currentSeason, String prevCrop, boolean irrigation) {

        // Scenario 1
        if(prevCrop.contains("আমন") || currentSeason.contains("রবি")) {

            if((soil.contains("দোআঁশ") || soil.contains("বেলে")) && irrigation) {
                addRotationCard("বানিজ্যিক লাভজনক মডেল", "সর্বাধিক মুনাফা",
                        new CycleStep("সরিষা/আলু", "রবি (বর্তমান)", "💰"),
                        new CycleStep("বোরো ধান/ভুট্টা", "খরিফ-১", "🌾"),
                        new CycleStep("আমন ধান", "খরিফ-২", "🌧️"),
                        "আলু বা সরিষা স্বল্পমেয়াদী লাভজনক ফসল। এরপর বোরো বা ভুট্টা চাষ করলে ফলন ভালো হয়।"
                );
            }

            addRotationCard("মাটির স্বাস্থ্য সুরক্ষা মডেল", "মাটির উর্বরতা বৃদ্ধি",
                    new CycleStep("মসুর/মুগ ডাল", "রবি (বর্তমান)", "🌿"),
                    new CycleStep("পাট/আউশ", "খরিফ-১", "☘️"),
                    new CycleStep("আমন ধান", "খরিফ-২", "🌧️"),
                    "ডাল জাতীয় ফসল মাটির নাইট্রোজেন বাড়ায়। এরপর পাট চাষ করলে মাটির গঠন ভালো থাকে।"
            );

            if(land.contains("উঁচু") || land.contains("মাঝারি উঁচু")) {
                addRotationCard("স্বল্প সেচ মডেল", "পানি সাশ্রয়ী",
                        new CycleStep("গম", "রবি (বর্তমান)", "🌾"),
                        new CycleStep("মুগ ডাল/সবজি", "খরিফ-১", "🥗"),
                        new CycleStep("আমন ধান", "খরিফ-২", "🌧️"),
                        "বোরো ধানের চেয়ে গমে সেচ কম লাগে। উঁচু জমির জন্য এটি আদর্শ।"
                );
            }
        }

        else if(prevCrop.contains("বোরো") || prevCrop.contains("গম") || currentSeason.contains("খরিফ-১")) {

            addRotationCard("সবুজ সার মডেল", "জৈব পদার্থ বৃদ্ধি",
                    new CycleStep("ধঞ্চে (সবুজ সার)", "খরিফ-১ (বর্তমান)", "🌿"),
                    new CycleStep("আমন ধান", "খরিফ-২", "🌧️"),
                    new CycleStep("সরিষা/আলু", "ರবি", "💰"),
                    "ধঞ্চে চাষ করে মাটিতে মিশিয়ে দিলে ইউরিয়া সারের খরচ অর্ধেক কমে যায়।"
            );

            addRotationCard("অর্থকরী ফসল মডেল", "পাট চাষ",
                    new CycleStep("পাট", "খরিফ-১ (বর্তমান)", "🌿"),
                    new CycleStep("আমন ধান", "খরিফ-২", "🌧️"),
                    new CycleStep("গম/সবজি", "রবি", "🥗"),
                    "পাটের পাতা পচে মাটির উর্বরতা বাড়ায় এবং এটি লাভজনক অর্থকরী ফসল।"
            );
        }

        else {
            addRotationCard("আদর্শ সবজি চক্র", "পারিবারিক পুষ্টি",
                    new CycleStep("বেগুন/টমেটো", "রবি", "🍆"),
                    new CycleStep("লালশাক/ডাঁটা", "খরিফ-১", "🥬"),
                    new CycleStep("লতাজাতীয় সবজি", "খরিফ-২", "🥒"),
                    "একই জমিতে বারবার একই সবজি না করে এভাবে চক্রাকারে চাষ করুন।"
            );
        }
    }

    // ===========================
    // 4. UI Card Builder
    // ===========================
    private void addRotationCard(String title, String badgeText, CycleStep step1, CycleStep step2, CycleStep step3, String benefit) {

        VBox card = new VBox(10);
        card.getStyleClass().add("rotation-card");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("card-header");

        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("option-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label(badgeText);
        badge.getStyleClass().add("option-badge");

        header.getChildren().addAll(titleLbl, spacer, badge);

        HBox cycleBox = new HBox(5);
        cycleBox.setAlignment(Pos.CENTER);
        cycleBox.getStyleClass().add("cycle-container");

        cycleBox.getChildren().add(createStepView(step1));
        cycleBox.getChildren().add(createArrow());
        cycleBox.getChildren().add(createStepView(step2));
        cycleBox.getChildren().add(createArrow());
        cycleBox.getChildren().add(createStepView(step3));

        HBox benefitBox = new HBox(5);
        benefitBox.getStyleClass().add("benefit-box");
        Label bulb = new Label("💡");
        Label text = new Label(benefit);
        text.getStyleClass().add("benefit-text");
        text.setWrapText(true);
        benefitBox.getChildren().addAll(bulb, text);

        card.getChildren().addAll(header, cycleBox, benefitBox);
        resultsContainer.getChildren().add(card);
    }

    private VBox createStepView(CycleStep step) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("cycle-step");
        box.setPrefWidth(100);

        Label icon = new Label(step.icon);
        icon.getStyleClass().add("step-icon");

        Label name = new Label(step.name);
        name.getStyleClass().add("step-crop");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);

        Label season = new Label(step.season);
        season.getStyleClass().add("step-season");

        box.getChildren().addAll(icon, name, season);
        return box;
    }

    private Label createArrow() {
        Label arrow = new Label("➜");
        arrow.getStyleClass().add("arrow-icon");
        return arrow;
    }

    private void resetForm() {
        prevCropComboBox.getSelectionModel().clearSelection();
        landTypeComboBox.getSelectionModel().clearSelection();
        emptyState.setVisible(true);
        emptyState.setManaged(true);
        resultsContainer.getChildren().clear();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("সতর্কতা");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private static class CycleStep {
        String name, season, icon;
        public CycleStep(String name, String season, String icon) {
            this.name = name;
            this.season = season;
            this.icon = icon;
        }
    }
}
