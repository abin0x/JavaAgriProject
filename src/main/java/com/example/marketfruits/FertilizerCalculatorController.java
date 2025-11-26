package com.example.marketfruits;

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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FertilizerCalculatorController implements Initializable {

    // ==========================================
    // 1. Sidebar Buttons
    // ==========================================
    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnStorage;
    @FXML private Button btnFarmWeather;
    @FXML private Button btnAgriAnalysis;
    @FXML private Button btnAgriNews;
    @FXML private Button btnCropPlanning;
    @FXML private Button btnProfitLoss;
    @FXML private Button btnWeather;
    @FXML private Button btnMarket;

    // ==========================================
    // 2. Top Filter Buttons (Navigation)
    // ==========================================
    @FXML private Button btnGuide;
    @FXML private Button btnFertilizer;
    @FXML private Button btnIrrigation;
    @FXML private Button btnCropRotation;

    // ==========================================
    // 3. Calculator Inputs & Outputs
    // ==========================================
    @FXML private ComboBox<String> cropComboBox, varietyComboBox, seasonComboBox;
    @FXML private TextField landAreaField;
    @FXML private ComboBox<String> unitComboBox, soilTypeComboBox, previousCropComboBox;

    @FXML private RadioButton nLow, nMedium, nHigh;
    @FXML private RadioButton pLow, pMedium, pHigh;
    @FXML private RadioButton kLow, kMedium, kHigh;
    @FXML private ToggleGroup nitrogenGroup, phosphorusGroup, potassiumGroup;

    @FXML private CheckBox organicManureCheck;
    @FXML private TextField organicManureAmount;

    @FXML private Button calculateBtn, resetBtn;

    @FXML private VBox emptyResultState, resultContentContainer;
    @FXML private VBox fertilizerResultsContainer, costBreakdownContainer, timelineContainer, tipsContainer;
    @FXML private Label totalCostLabel, summaryCrop, summaryLand, summarySoil;

    // Data Structures
    private Map<String, CropData> cropDatabase;
    private Map<String, Double> fertilizerPrices;
    private DecimalFormat df = new DecimalFormat("#.##");
    private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Fertilizer Controller Initialized.");

        // Setup Calculator
        initializeCropDatabase();
        initializeFertilizerPrices();
        populateDropdowns();
        setupEventHandlers();

        // Setup Navigation
        setupNavigationHandlers();

        // Default Selections
        if(nMedium != null) nMedium.setSelected(true);
        if(pMedium != null) pMedium.setSelected(true);
        if(kMedium != null) kMedium.setSelected(true);
    }

    // ==========================================
    // 4. Navigation Logic
    // ==========================================
    private void setupNavigationHandlers() {
        if (btnHome != null) btnHome.setOnAction(e -> loadPage(e, "/fxml/dashboard.fxml"));
        if (btnAdvisory != null) btnAdvisory.setOnAction(e -> loadPage(e, "/fxml/CropAdvisory.fxml"));
        if (btnGuide != null) btnGuide.setOnAction(e -> loadPage(e, "/fxml/CropAdvisory.fxml"));
        if (btnFertilizer != null) btnFertilizer.setOnAction(e -> loadPage(e, "/fxml/FertilizerCalculator.fxml"));
        if (btnIrrigation != null) btnIrrigation.setOnAction(e -> loadPage(e, "/fxml/IrrigationCalculator.fxml"));
        if (btnCropRotation != null) btnCropRotation.setOnAction(e -> loadPage(e, "/fxml/CropRotation.fxml"));
        if (btnLocalManagement != null) btnLocalManagement.setOnAction(e -> loadPage(e, "/fxml/LocalManagement.fxml"));
        if (btnStorage != null) btnStorage.setOnAction(e -> loadPage(e, "/fxml/WarehouseView.fxml"));
    }

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) return;

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // CSS Loading
            String dashboardCss = getClass().getResource("/css/dashboard.css").toExternalForm();
            if (dashboardCss != null) scene.getStylesheets().add(dashboardCss);

            if (fxmlPath.contains("CropAdvisory") || fxmlPath.contains("Calculator")) {
                String cropCss = getClass().getResource("/css/CropAdvisory.css").toExternalForm();
                if (cropCss != null) scene.getStylesheets().add(cropCss);
            }
            String fertCss = getClass().getResource("/css/FertilizerCalculator.css").toExternalForm();
            if (fertCss != null) scene.getStylesheets().add(fertCss);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 5. Calculation Logic
    // ==========================================

    private static class CropData {
        String[] varieties;
        String[] seasons;
        double[] npk;
        CropData(String[] varieties, String[] seasons, double[] npk) {
            this.varieties = varieties;
            this.seasons = seasons;
            this.npk = npk;
        }
    }

    private void initializeCropDatabase() {
        cropDatabase = new HashMap<>();
        cropDatabase.put("সরিষা (Mustard)", new CropData(new String[]{"বারি সরিষা-১৪", "স্থানীয়"}, new String[]{"রবি"}, new double[]{90, 30, 40}));
        cropDatabase.put("ধান (Rice - Boro)", new CropData(new String[]{"ব্রি ধান-২৮", "হাইব্রিড"}, new String[]{"রবি"}, new double[]{140, 50, 60}));
        cropDatabase.put("গম (Wheat)", new CropData(new String[]{"বারি গম-৩০", "প্রদীপ"}, new String[]{"রবি"}, new double[]{110, 40, 50}));
        cropDatabase.put("ভুট্টা (Maize)", new CropData(new String[]{"হাইব্রিড", "স্থানীয়"}, new String[]{"রবি"}, new double[]{180, 70, 80}));
        cropDatabase.put("আলু (Potato)", new CropData(new String[]{"ডায়মন্ট", "কার্ডিনাল"}, new String[]{"রবি"}, new double[]{150, 60, 120}));
    }

    private void initializeFertilizerPrices() {
        fertilizerPrices = new HashMap<>();
        fertilizerPrices.put("Urea", 27.0);
        fertilizerPrices.put("TSP", 24.0);
        fertilizerPrices.put("MOP", 18.0);
        fertilizerPrices.put("Gypsum", 12.0);
        fertilizerPrices.put("Zinc", 180.0);
    }

    private void populateDropdowns() {
        if(cropComboBox == null) return;
        cropComboBox.getItems().addAll(cropDatabase.keySet());
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");
        unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল", "পলি মাটি");
        previousCropComboBox.getItems().addAll("কিছু নয়", "ধান", "গম", "ডাল জাতীয়", "সবজি");
        previousCropComboBox.setValue("কিছু নয়");
        cropComboBox.setOnAction(e -> updateVarietiesAndSeasons());
    }

    private void updateVarietiesAndSeasons() {
        String selectedCrop = cropComboBox.getValue();
        if (selectedCrop != null && cropDatabase.containsKey(selectedCrop)) {
            CropData data = cropDatabase.get(selectedCrop);
            varietyComboBox.getItems().setAll(data.varieties);
            varietyComboBox.getSelectionModel().selectFirst();
            seasonComboBox.getItems().setAll(data.seasons);
            seasonComboBox.getSelectionModel().selectFirst();
        }
    }

    private void setupEventHandlers() {
        if(calculateBtn != null) calculateBtn.setOnAction(event -> calculateFertilizer());
        if(resetBtn != null) resetBtn.setOnAction(event -> resetForm());
        if(organicManureCheck != null) organicManureCheck.setOnAction(e -> organicManureAmount.setDisable(!organicManureCheck.isSelected()));
    }

    private void calculateFertilizer() {
        try {
            if (cropComboBox.getValue() == null || landAreaField.getText().trim().isEmpty() || soilTypeComboBox.getValue() == null) {
                showError("দয়া করে সব তথ্য সঠিকভাবে পূরণ করুন।");
                return;
            }

            double landArea = Double.parseDouble(landAreaField.getText().trim());
            double landHa = convertToHectares(landArea, unitComboBox.getValue());
            CropData data = cropDatabase.get(cropComboBox.getValue());
            double[] npk = data.npk.clone();

            // Variety Adjustment
            if(varietyComboBox.getValue() != null && varietyComboBox.getValue().contains("হাইব্রিড")) {
                npk[0] *= 1.2; npk[1] *= 1.2; npk[2] *= 1.2;
            }

            // Soil Fertility Adjustment
            if (nHigh.isSelected()) npk[0] *= 0.6; else if (nLow.isSelected()) npk[0] *= 1.25;
            if (pHigh.isSelected()) npk[1] *= 0.6; else if (pLow.isSelected()) npk[1] *= 1.25;
            if (kHigh.isSelected()) npk[2] *= 0.6; else if (kLow.isSelected()) npk[2] *= 1.25;

            // Organic Manure Adjustment
            if (organicManureCheck.isSelected() && !organicManureAmount.getText().isEmpty()) {
                double manureTon = Double.parseDouble(organicManureAmount.getText());
                npk[0] -= manureTon * 4; npk[1] -= manureTon * 1.5; npk[2] -= manureTon * 4;
            }
            for(int i=0; i<3; i++) if(npk[i]<0) npk[i]=0;

            // Calculate Amounts (KG)
            double ureaKg = (npk[0] * 2.17) * landHa;
            double tspKg = (npk[1] * 2.17) * landHa;
            double mopKg = (npk[2] * 1.67) * landHa;
            double gypsumKg = 60 * landHa;
            double zincKg = 8 * landHa;

            // Display Results
            displayResults(cropComboBox.getValue(), landArea, unitComboBox.getValue(), soilTypeComboBox.getValue(), ureaKg, tspKg, mopKg, gypsumKg, zincKg);

        } catch (NumberFormatException e) {
            showError("জমির পরিমাণ ইংরেজিতে সঠিক সংখ্যায় লিখুন (যেমন: 5.5)");
        } catch (Exception e) {
            e.printStackTrace();
            showError("সমস্যা হয়েছে: " + e.getMessage());
        }
    }

    private void displayResults(String crop, double land, String unit, String soil, double u, double t, double m, double g, double z) {
        summaryCrop.setText(crop);
        summaryLand.setText(land + " " + unit);
        summarySoil.setText(soil);

        fertilizerResultsContainer.getChildren().clear();
        costBreakdownContainer.getChildren().clear();
        timelineContainer.getChildren().clear();

        double totalCost = 0;

        // Items add logic
        totalCost += addResultItem("ইউরিয়া (Urea)", u, fertilizerPrices.get("Urea"), "নাইট্রোজেন (N)");
        totalCost += addResultItem("টিএসপি (TSP)", t, fertilizerPrices.get("TSP"), "ফসফরাস (P)");
        totalCost += addResultItem("এমওপি (MoP)", m, fertilizerPrices.get("MOP"), "পটাশ (K)");
        totalCost += addResultItem("জিপসাম", g, fertilizerPrices.get("Gypsum"), "সালফার (S)");
        totalCost += addResultItem("জিংক", z, fertilizerPrices.get("Zinc"), "জিংক (Zn)");

        totalCostLabel.setText("৳ " + moneyFormat.format(totalCost));

        // Timeline items
        addTimelineItem("1", "জমি তৈরির শেষ চাষে: সম্পূর্ণ টিএসপি, এমওপি, জিপসাম ও জিংক প্রয়োগ করুন।");
        addTimelineItem("2", "চারা রোপণের ৭-১০ দিন পর: ইউরিয়া সারের ১ম কিস্তি দিন।");
        addTimelineItem("3", "চারা রোপণের ২৫-৩০ দিন পর: ইউরিয়া সারের ২য় কিস্তি দিন।");

        emptyResultState.setVisible(false); emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true); resultContentContainer.setManaged(true);
    }

    // ==========================================
    // 6. FIX: Showing Weight in Main Card, Cost in Breakdown
    // ==========================================
    private double addResultItem(String name, double amount, double price, String nut) {
        if (amount <= 0.1) return 0; // Skip negligible amounts

        // 1. Create Main Card
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #f1f8e9; -fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #c5e1a5;");

        // Left Side: Name
        VBox left = new VBox(3);
        Label nLbl = new Label(name); nLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2e7d32;");
        Label nutLbl = new Label(nut); nutLbl.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
        left.getChildren().addAll(nLbl, nutLbl);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right Side: WEIGHT (Updated Fix)
        VBox right = new VBox(3); right.setAlignment(Pos.CENTER_RIGHT);

        // This line ensures WEIGHT is shown in the card, NOT price
        Label amtLbl = new Label(df.format(amount) + " কেজি");
        amtLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #1b5e20;");

        right.getChildren().add(amtLbl);

        card.getChildren().addAll(left, spacer, right);
        fertilizerResultsContainer.getChildren().add(card);

        // 2. Add to Cost Breakdown (Bottom Section)
        double cost = amount * price;
        HBox costRow = new HBox(10);
        Label cName = new Label("• " + name); cName.setStyle("-fx-text-fill: #555;");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label cVal = new Label("৳ " + df.format(cost)); // Shows Price here
        cVal.setStyle("-fx-font-weight: bold;");
        costRow.getChildren().addAll(cName, sp, cVal);
        costBreakdownContainer.getChildren().add(costRow);

        return cost;
    }

    private void addTimelineItem(String num, String text) {
        HBox row = new HBox(10);
        Label badge = new Label(num);
        badge.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10; -fx-font-weight: bold;");
        Label txt = new Label(text); txt.setWrapText(true);
        row.getChildren().addAll(badge, txt);
        timelineContainer.getChildren().add(row);
    }

    private double convertToHectares(double area, String unit) {
        switch (unit) {
            case "হেক্টর": return area;
            case "একর": return area * 0.404686;
            case "শতাংশ": return area * 0.004046;
            case "বিঘা": return area * 0.1338;
            default: return area;
        }
    }

    private void resetForm() {
        landAreaField.clear();
        cropComboBox.getSelectionModel().clearSelection();
        emptyResultState.setVisible(true);
        emptyResultState.setManaged(true);
        resultContentContainer.setVisible(false);
        resultContentContainer.setManaged(false);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ত্রুটি");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}