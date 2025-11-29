package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Helper Import
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class FertilizerCalculatorController implements Initializable {

    // --- Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnLocalManagement, btnStorage,btnMachinery;
    @FXML private Button btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    // --- Inputs ---
    @FXML private ComboBox<String> cropComboBox, varietyComboBox, seasonComboBox, unitComboBox, soilTypeComboBox, previousCropComboBox;
    @FXML private TextField landAreaField, organicManureAmount;
    @FXML private RadioButton nLow, nMedium, nHigh, pLow, pMedium, pHigh, kLow, kMedium, kHigh;
    @FXML private CheckBox organicManureCheck;
    @FXML private Button calculateBtn, resetBtn;

    // --- Results ---
    @FXML private VBox emptyResultState, resultContentContainer;
    @FXML private VBox fertilizerResultsContainer, costBreakdownContainer, timelineContainer;
    @FXML private Label totalCostLabel, summaryCrop, summaryLand, summarySoil;

    // --- Data ---
    private final Map<String, CropData> cropDatabase = new HashMap<>();
    private final Map<String, Double> prices = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Fertilizer Calculator Initialized");

        // 1. Setup Navigation (1 Line for Sidebar, 1 Line for Top Nav)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        // 2. Setup Logic
        initData();
        setupInputs();
        setupActions();
    }

    // ===========================
    // 1. DATA INITIALIZATION
    // ===========================
    private void initData() {
        // Crop Data: Name -> {Varieties}, {Seasons}, {N, P, K requirements}
        addCrop("সরিষা (Mustard)", new String[]{"রবি"}, new double[]{90, 30, 40}, "bari-14", "local");
        addCrop("ধান (Boro)", new String[]{"রবি"}, new double[]{140, 50, 60}, "bri-28", "hybrid");
        addCrop("গম (Wheat)", new String[]{"রবি"}, new double[]{110, 40, 50}, "bari-30", "prodip");
        addCrop("আলু (Potato)", new String[]{"রবি"}, new double[]{150, 60, 120}, "diamond", "cardinal");

        // Prices per KG
        prices.put("Urea", 27.0); prices.put("TSP", 24.0); prices.put("MOP", 18.0);
        prices.put("Gypsum", 12.0); prices.put("Zinc", 180.0);
    }

    private void addCrop(String name, String[] seasons, double[] npk, String... vars) {
        cropDatabase.put(name, new CropData(vars, seasons, npk));
    }

    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDatabase.keySet());
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা"); unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল", "পলি মাটি");
        previousCropComboBox.getItems().addAll("কিছু নয়", "ধান", "গম", "ডাল জাতীয়", "সবজি");

        cropComboBox.setOnAction(e -> {
            CropData d = cropDatabase.get(cropComboBox.getValue());
            if (d != null) {
                varietyComboBox.getItems().setAll(d.varieties); varietyComboBox.getSelectionModel().selectFirst();
                seasonComboBox.getItems().setAll(d.seasons); seasonComboBox.getSelectionModel().selectFirst();
            }
        });
    }

    private void setupActions() {
        calculateBtn.setOnAction(e -> calculate());
        resetBtn.setOnAction(e -> reset());
        organicManureCheck.setOnAction(e -> organicManureAmount.setDisable(!organicManureCheck.isSelected()));
        // Set Defaults
        if(nMedium != null) nMedium.setSelected(true);
        if(pMedium != null) pMedium.setSelected(true);
        if(kMedium != null) kMedium.setSelected(true);
    }

    // ===========================
    // 2. CALCULATION LOGIC
    // ===========================
    private void calculate() {
        if (!validate()) return;

        try {
            double area = Double.parseDouble(landAreaField.getText().trim());
            double ha = convertToHa(area, unitComboBox.getValue());
            CropData data = cropDatabase.get(cropComboBox.getValue());
            double[] npk = data.npk.clone();

            // Adjustments
            if (varietyComboBox.getValue().contains("hybrid")) { npk[0]*=1.2; npk[1]*=1.2; npk[2]*=1.2; }
            adjustForSoil(npk);
            adjustForManure(npk);

            // Calculate Final Weights (KG)
            double urea = Math.max(0, npk[0] * 2.17 * ha);
            double tsp  = Math.max(0, npk[1] * 2.17 * ha);
            double mop  = Math.max(0, npk[2] * 1.67 * ha);
            double gyp  = 60 * ha;
            double zinc = 8 * ha;

            displayResults(area, urea, tsp, mop, gyp, zinc);

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for land area.").show();
        }
    }

    private void adjustForSoil(double[] npk) {
        if (nHigh.isSelected()) npk[0] *= 0.6; else if (nLow.isSelected()) npk[0] *= 1.25;
        if (pHigh.isSelected()) npk[1] *= 0.6; else if (pLow.isSelected()) npk[1] *= 1.25;
        if (kHigh.isSelected()) npk[2] *= 0.6; else if (kLow.isSelected()) npk[2] *= 1.25;
    }

    private void adjustForManure(double[] npk) {
        if (organicManureCheck.isSelected() && !organicManureAmount.getText().isEmpty()) {
            try {
                double ton = Double.parseDouble(organicManureAmount.getText());
                npk[0] -= ton * 4; npk[1] -= ton * 1.5; npk[2] -= ton * 4;
            } catch (Exception ignored) {}
        }
    }

    // ===========================
    // 3. UI RENDERING
    // ===========================
    private void displayResults(double area, double u, double t, double m, double g, double z) {
        summaryCrop.setText(cropComboBox.getValue());
        summaryLand.setText(area + " " + unitComboBox.getValue());
        summarySoil.setText(soilTypeComboBox.getValue());

        fertilizerResultsContainer.getChildren().clear();
        costBreakdownContainer.getChildren().clear();
        timelineContainer.getChildren().clear();

        double total = 0;
        total += addCard("ইউরিয়া (Urea)", u, prices.get("Urea"), "নাইট্রোজেন (N)");
        total += addCard("টিএসপি (TSP)", t, prices.get("TSP"), "ফসফরাস (P)");
        total += addCard("এমওপি (MoP)", m, prices.get("MOP"), "পটাশ (K)");
        total += addCard("জিপসাম", g, prices.get("Gypsum"), "সালফার (S)");
        total += addCard("জিংক", z, prices.get("Zinc"), "জিংক (Zn)");

        totalCostLabel.setText("৳ " + df.format(total));

        addTimeline("1", "জমি তৈরির শেষ চাষে: সম্পূর্ণ টিএসপি, এমওপি, জিপসাম ও জিংক প্রয়োগ করুন।");
        addTimeline("2", "চারা রোপণের ৭-১০ দিন পর: ইউরিয়া সারের ১ম কিস্তি দিন।");
        addTimeline("3", "৩০ দিন পর: ইউরিয়া সারের ২য় কিস্তি দিন।");

        emptyResultState.setVisible(false); emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true); resultContentContainer.setManaged(true);
    }

    private double addCard(String name, double kg, double price, String sub) {
        if (kg <= 0.1) return 0;

        // Main Result Card
        HBox card = new HBox(15);
        card.getStyleClass().add("result-card"); // Use CSS
        card.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(3, new Label(name), new Label(sub));
        ((Label)left.getChildren().get(0)).setStyle("-fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        Label weight = new Label(df.format(kg) + " কেজি");
        weight.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        HBox.setHgrow(left, Priority.ALWAYS);
        card.getChildren().addAll(left, new Region(), weight); // Spacer logic handled by HBox grow
        ((Region) card.getChildren().get(1)).setMinWidth(20);
        HBox.setHgrow(card.getChildren().get(1), Priority.ALWAYS);

        fertilizerResultsContainer.getChildren().add(card);

        // Cost Item
        double cost = kg * price;
        HBox row = new HBox(10, new Label("• " + name), new Region(), new Label("৳ " + df.format(cost)));
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
        costBreakdownContainer.getChildren().add(row);

        return cost;
    }

    private void addTimeline(String num, String txt) {
        Label badge = new Label(num);
        badge.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        Label text = new Label(txt); text.setWrapText(true);
        timelineContainer.getChildren().add(new HBox(10, badge, text));
    }

    private boolean validate() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all required fields.").show();
            return false;
        }
        return true;
    }

    private void reset() {
        landAreaField.clear(); cropComboBox.setValue(null);
        resultContentContainer.setVisible(false); emptyResultState.setVisible(true);
    }

    // --- Helpers ---
    private double convertToHa(double val, String unit) {
        if ("হেক্টর".equals(unit)) return val;
        if ("শতাংশ".equals(unit)) return val * 0.004046;
        if ("বিঘা".equals(unit)) return val * 0.1338;
        return val * 0.4046; // Acre
    }

    private static class CropData {
        String[] varieties, seasons;
        double[] npk;
        CropData(String[] v, String[] s, double[] n) { varieties = v; seasons = s; npk = n; }
    }
}