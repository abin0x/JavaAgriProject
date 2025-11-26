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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class IrrigationCalculatorController implements Initializable {

    // ==========================================
    // 1. Sidebar & Navigation Buttons
    // ==========================================
    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnAiHelper;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnStorage;

    // Top Filter Buttons
    @FXML private Button btnGuide;
    @FXML private Button btnFertilizer;
    @FXML private Button btnIrrigation;
    @FXML private Button btnCropRotation;

    // ==========================================
    // 2. Input controls
    // ==========================================
    @FXML private ComboBox<String> cropComboBox, varietyComboBox, growthStageComboBox;
    @FXML private TextField cropAgeField, landAreaField;
    @FXML private ComboBox<String> farmingMethodComboBox, unitComboBox, soilTypeComboBox;
    @FXML private ComboBox<String> landTypeComboBox, seasonComboBox;
    @FXML private ComboBox<String> waterSourceComboBox, pumpPowerComboBox;
    @FXML private ComboBox<String> pipeTypeComboBox;
    @FXML private TextField pipeLengthField;
    @FXML private ComboBox<String> pipeLengthUnitComboBox;

    // Weather controls
    @FXML private CheckBox strongSunCheck, windyCheck, cloudyCheck;
    @FXML private RadioButton tempCold, tempModerate, tempHot;
    @FXML private ToggleGroup temperatureGroup;
    @FXML private CheckBox recentRainCheck;
    @FXML private TextField rainfallAmount;
    @FXML private RadioButton rainForecastYes, rainForecastNo, rainForecastUnsure;
    @FXML private ToggleGroup rainForecastGroup;
    @FXML private TextField lastIrrigationField;

    // Irrigation method
    @FXML private RadioButton methodSprinkler, methodDrip, methodFlood, methodFurrow;
    @FXML private ToggleGroup irrigationMethodGroup;

    // Buttons
    @FXML private Button calculateBtn, resetBtn;

    // Result containers
    @FXML private VBox emptyResultState, resultContentContainer;

    // Updated: Removed totalWaterLabel and efficiencyLabel as per FXML changes or needs
    @FXML private Label waterPerIrrigationLabel, efficiencyLabel;

    // Updated: Removed totalTimeLabel
    @FXML private Label timePerIrrigationLabel;

    @FXML private Label fuelCostLabel, laborCostLabel, maintenanceCostLabel, totalCostLabel;
    @FXML private VBox scheduleContainer;

    // Data structures
    private Map<String, CropWaterRequirement> cropWaterData;
    private DecimalFormat df = new DecimalFormat("#.##");
    private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ সেচ ক্যালকুলেটর চালু হয়েছে!");

        initializeCropWaterData();
        populateDropdowns();
        setupEventHandlers();
        setupNavigationHandlers();
    }

    // ==========================================
    // 3. Navigation Logic
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
        if (btnAiHelper != null) btnAiHelper.setOnAction(e -> System.out.println("AI Helper..."));
    }

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                System.err.println("❌ FXML File Not Found: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            String css = getClass().getResource("/css/dashboard.css").toExternalForm();
            if (css != null) scene.getStylesheets().add(css);

            if (fxmlPath.contains("CropAdvisory")) {
                String cropCss = getClass().getResource("/css/CropAdvisory.css").toExternalForm();
                if (cropCss != null) scene.getStylesheets().add(cropCss);
            } else if (fxmlPath.contains("IrrigationCalculator")) {
                String irrCss = getClass().getResource("/css/IrrigationCalculator.css").toExternalForm();
                if (irrCss != null) scene.getStylesheets().add(irrCss);
            } else if (fxmlPath.contains("FertilizerCalculator")) {
                String fertCss = getClass().getResource("/css/FertilizerCalculator.css").toExternalForm();
                if (fertCss != null) scene.getStylesheets().add(fertCss);
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 4. Data & Logic
    // ==========================================
    private static class CropWaterRequirement {
        String[] varieties;
        String[] growthStages;
        double waterPerDayMM;
        int irrigationFrequency;
        int totalIrrigations;

        CropWaterRequirement(String[] varieties, String[] stages, double waterPerDay, int frequency, int total) {
            this.varieties = varieties;
            this.growthStages = stages;
            this.waterPerDayMM = waterPerDay;
            this.irrigationFrequency = frequency;
            this.totalIrrigations = total;
        }
    }

    private void initializeCropWaterData() {
        cropWaterData = new HashMap<>();
        cropWaterData.put("ধান (Boro)", new CropWaterRequirement(new String[]{"ব্রি ধান-২৮", "ব্রি ধান-২৯", "ব্রি ধান-৮৯", "বিনা ধান-১০"}, new String[]{"চারা রোপণ", "কুশি", "থোড়/ফুল", "পাকা"}, 5.0, 3, 25));
        cropWaterData.put("ধান (Aman)", new CropWaterRequirement(new String[]{"ব্রি ধান-৪৯", "ব্রি ধান-৭১", "বিনা ধান-৭"}, new String[]{"চারা রোপণ", "কুশি", "থোড়/ফুল", "পাকা"}, 4.5, 4, 20));
        cropWaterData.put("গম (Wheat)", new CropWaterRequirement(new String[]{"বারি গম-৩০", "বারি গম-৩৩", "প্রদীপ", "শতাব্দী"}, new String[]{"বপন", "কুশি", "শীষ বের", "দানা পূর্ণতা"}, 4.0, 7, 5));
        cropWaterData.put("আলু (Potato)", new CropWaterRequirement(new String[]{"কার্ডিনাল", "ডায়মন্ট", "গ্রানোলা", "বারি আলু-২৯"}, new String[]{"গজানো", "বাড়ন্ত", "কন্দ গঠন", "পরিপক্কতা"}, 5.5, 4, 12));
        cropWaterData.put("ভুট্টা (Maize)", new CropWaterRequirement(new String[]{"বারি হাইব্রিড ভুট্টা-৯", "এনকে-৪০", "পেসিফিক-৯৮৪"}, new String[]{"গজানো", "বৃদ্ধি", "ফুল/পরাগায়ন", "পরিপক্কতা"}, 5.0, 5, 10));
        cropWaterData.put("টমেটো (Tomato)", new CropWaterRequirement(new String[]{"বারি টমেটো-১৪", "বারি টমেটো-১৫", "রতন", "হাইব্রিড"}, new String[]{"চারা", "ফুল", "ফল ধরা", "পাকা"}, 4.5, 3, 20));
        cropWaterData.put("পেঁয়াজ (Onion)", new CropWaterRequirement(new String[]{"বারি পেঁয়াজ-১", "বারি পেঁয়াজ-৫", "তাহেরপুরী"}, new String[]{"বপন", "পাতা বৃদ্ধি", "বাল্ব গঠন", "পরিপক্কতা"}, 4.0, 5, 15));
        cropWaterData.put("সরিষা (Mustard)", new CropWaterRequirement(new String[]{"বারি সরিষা-১৪", "বারি সরিষা-১৫", "তোরি-৭"}, new String[]{"বপন", "বৃদ্ধি", "ফুল", "শুঁটি পূর্ণতা"}, 3.5, 10, 4));
        cropWaterData.put("বেগুন (Brinjal)", new CropWaterRequirement(new String[]{"বারি বেগুন-১", "উত্তরা", "কাজলা", "হাইব্রিড"}, new String[]{"চারা রোপণ", "ফুল", "ফল ধরা", "ফল সংগ্রহ"}, 4.5, 3, 25));
        cropWaterData.put("শসা (Cucumber)", new CropWaterRequirement(new String[]{"গ্রীন লং", "বারি শসা-১", "হাইব্রিড"}, new String[]{"বপন", "লতা বৃদ্ধি", "ফুল/ফল", "সংগ্রহ"}, 5.0, 2, 30));
    }

    private void populateDropdowns() {
        if(cropComboBox == null) return;
        cropComboBox.getItems().addAll("ধান (Boro)", "ধান (Aman)", "গম (Wheat)", "আলু (Potato)", "ভুট্টা (Maize)", "টমেটো (Tomato)", "পেঁয়াজ (Onion)", "সরিষা (Mustard)", "বেগুন (Brinjal)", "শসা (Cucumber)");
        farmingMethodComboBox.getItems().addAll("সমতল চাষ (Flat Planting)", "বেড প্লান্টিং (Bed Planting)", "রিজ অ্যান্ড ফারো (Ridge & Furrow)", "জিরো টিলেজ (Zero Tillage)");
        farmingMethodComboBox.setValue("সমতল চাষ (Flat Planting)");
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");
        unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দোআঁশ (Loamy)", "বেলে দোআঁশ (Sandy Loam)", "এঁটেল দোআঁশ (Clay Loam)", "এঁটেল (Clay)", "বেলে (Sandy)");
        landTypeComboBox.getItems().addAll("উঁচু জমি (High Land)", "মাঝারি উঁচু জমি (Medium High Land)", "মাঝারি নিচু জমি (Medium Low Land)", "নিচু জমি (Low Land)");
        landTypeComboBox.setValue("মাঝারি উঁচু জমি (Medium High Land)");
        seasonComboBox.getItems().addAll("খরিফ-১ (মার্চ-জুন)", "খরিফ-২ (জুলাই-অক্টোবর)", "রবি (নভেম্বর-ফেব্রুয়ারি)", "গ্রীষ্ম (এপ্রিল-জুন)", "বর্ষা (জুলাই-সেপ্টেম্বর)", "শীত (নভেম্বর-ফেব্রুয়ারি)");
        waterSourceComboBox.getItems().addAll("গভীর নলকূপ (Deep Tubewell)", "অগভীর নলকূপ (Shallow Tubewell)", "পুকুর", "নদী/খাল", "ভূ-উপরিস্থ পানি", "বৃষ্টির পানি");

        // Pump Power options
        pumpPowerComboBox.getItems().addAll("নেই", "২ এইচপি/ঘোড়া", "৩ এইচপি/ঘোড়া", "৪ এইচপি/ঘোড়া", "৫ এইচপি/ঘোড়া", "৭.৫ এইচপি/ঘোড়া", "১০ এইচপি/ঘোড়া", "১৫ এইচপি/ঘোড়া", "২০ এইচপি/ঘোড়া+");
        pumpPowerComboBox.setValue("নেই");

        pipeTypeComboBox.getItems().addAll("ফিতা পাইপ (Lay Flat Hose)", "পিভিসি পাইপ (PVC Pipe)", "জিআই পাইপ (GI Pipe)", "মাটির নালা (Earthen Channel)", "পাকা নালা (Concrete Channel)");
        pipeLengthUnitComboBox.getItems().addAll("ফুট", "মিটার");
        pipeLengthUnitComboBox.setValue("ফুট");

        cropComboBox.setOnAction(e -> updateVarietiesAndGrowthStages());
    }

    private void updateVarietiesAndGrowthStages() {
        String selectedCrop = cropComboBox.getValue();
        if (selectedCrop != null && cropWaterData.containsKey(selectedCrop)) {
            CropWaterRequirement data = cropWaterData.get(selectedCrop);
            varietyComboBox.getItems().clear();
            varietyComboBox.getItems().addAll(data.varieties);
            varietyComboBox.setValue(data.varieties[0]);
            growthStageComboBox.getItems().clear();
            growthStageComboBox.getItems().addAll(data.growthStages);
            growthStageComboBox.setValue(data.growthStages[0]);
        }
    }

    private void setupEventHandlers() {
        calculateBtn.setOnAction(event -> calculateIrrigation());
        resetBtn.setOnAction(event -> resetForm());
        recentRainCheck.setOnAction(e -> rainfallAmount.setDisable(!recentRainCheck.isSelected()));
    }

    private void calculateIrrigation() {
        if (cropComboBox.getValue() == null) { showError("দয়া করে একটি ফসল নির্বাচন করুন"); return; }
        if (landAreaField.getText().trim().isEmpty()) { showError("দয়া করে জমির পরিমাণ লিখুন"); return; }
        if (soilTypeComboBox.getValue() == null) { showError("দয়া করে মাটির ধরন নির্বাচন করুন"); return; }
        if (seasonComboBox.getValue() == null) { showError("দয়া করে মৌসুম নির্বাচন করুন"); return; }

        double landArea;
        try {
            landArea = Double.parseDouble(landAreaField.getText().trim());
            if (landArea <= 0) { showError("জমির পরিমাণ শূন্যের বেশি হতে হবে"); return; }
        } catch (NumberFormatException e) {
            showError("সঠিক সংখ্যা লিখুন"); return;
        }
        performCalculation(landArea);
    }

    private void performCalculation(double landArea) {
        String crop = cropComboBox.getValue();
        CropWaterRequirement cropData = cropWaterData.get(crop);
        double landHa = convertToHectares(landArea, unitComboBox.getValue());
        double baseWaterMM = cropData.waterPerDayMM;

        // --- Factors Adjustments ---
        if (!cropAgeField.getText().trim().isEmpty()) {
            try {
                int cropAge = Integer.parseInt(cropAgeField.getText().trim());
                if (cropAge > 20 && cropAge < 60) baseWaterMM *= 1.2;
                else if (cropAge > 80) baseWaterMM *= 0.7;
            } catch (NumberFormatException ignored) {}
        }

        String farmingMethod = farmingMethodComboBox.getValue();
        if (farmingMethod != null && farmingMethod.contains("বেড প্লান্টিং")) baseWaterMM *= 0.85;

        if (strongSunCheck.isSelected()) baseWaterMM *= 1.25;
        if (windyCheck.isSelected()) baseWaterMM *= 1.15;
        if (cloudyCheck.isSelected()) baseWaterMM *= 0.9;

        if (tempHot.isSelected()) baseWaterMM *= 1.3;
        else if (tempCold.isSelected()) baseWaterMM *= 0.8;

        String soilType = soilTypeComboBox.getValue();
        if (soilType != null) {
            if (soilType.contains("বেলে")) baseWaterMM *= 1.25;
            else if (soilType.contains("এঁটেল")) baseWaterMM *= 0.85;
        }

        String landType = landTypeComboBox.getValue();
        if (landType != null) {
            if (landType.contains("উঁচু")) baseWaterMM *= 1.1;
            else if (landType.contains("নিচু")) baseWaterMM *= 0.9;
        }

        double rainfallReduction = 0;
        if (recentRainCheck.isSelected() && !rainfallAmount.getText().trim().isEmpty()) {
            try {
                double rainfall = Double.parseDouble(rainfallAmount.getText().trim());
                rainfallReduction = rainfall * 0.7;
            } catch (NumberFormatException ignored) {}
        }

        boolean urgentIrrigationNeeded = false;
        if (!lastIrrigationField.getText().trim().isEmpty()) {
            try {
                int daysSinceLastIrrigation = Integer.parseInt(lastIrrigationField.getText().trim());
                if (daysSinceLastIrrigation >= cropData.irrigationFrequency) urgentIrrigationNeeded = true;
            } catch (NumberFormatException ignored) {}
        }

        int adjustedFrequency = cropData.irrigationFrequency;
        if (rainForecastYes.isSelected()) adjustedFrequency += 2;

        double waterDepthMM = baseWaterMM * adjustedFrequency - rainfallReduction;
        if (waterDepthMM < 0) waterDepthMM = 0;

        // Liters calculation
        double waterPerIrrigationLiters = waterDepthMM * landHa * 10000;
        double totalWaterLiters = waterPerIrrigationLiters * cropData.totalIrrigations;

        double efficiency = getIrrigationEfficiency();
        efficiency = adjustEfficiencyForPipe(efficiency);

        double actualWaterPerIrrigation = waterPerIrrigationLiters / (efficiency / 100.0);
        double actualTotalWater = totalWaterLiters / (efficiency / 100.0);

        // --- TIME CALCULATION LOGIC ---
        double pumpFlowRateLPH = getEstimatedFlowRate(pumpPowerComboBox.getValue());
        double hoursPerIrrigation = 0;
        double totalHoursSeason = 0;

        if (pumpFlowRateLPH > 0) {
            hoursPerIrrigation = actualWaterPerIrrigation / pumpFlowRateLPH;
            totalHoursSeason = actualTotalWater / pumpFlowRateLPH;
        }

        double[] costs = calculateCosts(actualTotalWater, landArea, cropData.totalIrrigations);

        displayResults(actualWaterPerIrrigation, actualTotalWater,
                hoursPerIrrigation, totalHoursSeason, // Pass time vars
                efficiency, costs, cropData.totalIrrigations, adjustedFrequency, urgentIrrigationNeeded);
    }

    // --- Helper to estimate Pump Flow Rate ---
    private double getEstimatedFlowRate(String pumpPower) {
        if (pumpPower == null || pumpPower.contains("নেই")) return 0.0;

        // Approximate discharge rates (Liters per Hour) for agricultural pumps
        if (pumpPower.contains("২")) return 15000;  // 2 HP
        if (pumpPower.contains("৩")) return 25000;  // 3 HP
        if (pumpPower.contains("৪")) return 35000;  // 4 HP
        if (pumpPower.contains("৫")) return 45000;  // 5 HP
        if (pumpPower.contains("৭.৫")) return 65000; // 7.5 HP
        if (pumpPower.contains("১০")) return 85000;  // 10 HP
        if (pumpPower.contains("১৫")) return 110000; // 15 HP
        if (pumpPower.contains("২০")) return 140000; // 20 HP+

        return 20000; // Default
    }

    private double convertToHectares(double area, String unit) {
        switch (unit) {
            case "হেক্টর": return area;
            case "একর": return area * 0.404686;
            case "শতাংশ": return area * 0.00404686;
            case "বিঘা": return area * 0.1338;
            default: return area * 0.404686;
        }
    }

    private double getIrrigationEfficiency() {
        if (methodDrip.isSelected()) return 90.0;
        if (methodSprinkler.isSelected()) return 75.0;
        if (methodFurrow.isSelected()) return 60.0;
        if (methodFlood.isSelected()) return 50.0;
        return 50.0;
    }

    private double[] calculateCosts(double totalWaterLiters, double landArea, int numIrrigations) {
        double totalWaterM3 = totalWaterLiters / 1000.0;
        double fuelCostPerM3 = 2.5;
        String pumpPower = pumpPowerComboBox.getValue();
        if (pumpPower != null && (pumpPower.contains("১০") || pumpPower.contains("১৫") || pumpPower.contains("২০"))) {
            fuelCostPerM3 = 3.0;
        }
        double fuelCost = totalWaterM3 * fuelCostPerM3;
        double laborPerIrrigation = 300;
        double laborCost = laborPerIrrigation * landArea * numIrrigations;
        double maintenanceCost = (fuelCost + laborCost) * 0.1;
        return new double[]{fuelCost, laborCost, maintenanceCost};
    }

    private double adjustEfficiencyForPipe(double baseEfficiency) {
        String pipeType = pipeTypeComboBox.getValue();
        if (pipeType != null) {
            if (pipeType.contains("ফিতা পাইপ") || pipeType.contains("পিভিসি")) baseEfficiency += 5;
            else if (pipeType.contains("মাটির নালা")) baseEfficiency -= 10;
        }
        if (!pipeLengthField.getText().trim().isEmpty()) {
            try {
                double length = Double.parseDouble(pipeLengthField.getText().trim());
                String unit = pipeLengthUnitComboBox.getValue();
                if (unit.equals("ফুট")) length = length * 0.3048;
                double efficiencyLoss = (length / 100.0) * 1.0;
                baseEfficiency -= efficiencyLoss;
            } catch (NumberFormatException ignored) {}
        }
        if (baseEfficiency > 95) baseEfficiency = 95;
        if (baseEfficiency < 30) baseEfficiency = 30;
        return baseEfficiency;
    }

    private void displayResults(double waterPerIrrigation, double totalWater,
                                double hoursPerIrrigation, double totalHours, // NEW params
                                double efficiency, double[] costs, int numIrrigations,
                                int frequency, boolean urgentNeeded) {

        emptyResultState.setVisible(false);
        emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true);
        resultContentContainer.setManaged(true);

        // Water Volume
        if (waterPerIrrigationLabel != null) waterPerIrrigationLabel.setText(formatLiters(waterPerIrrigation));

        // Removed totalWaterLabel setting
        // if (totalWaterLabel != null) totalWaterLabel.setText(formatLiters(totalWater));

        // --- TIME DISPLAY ---
        if (timePerIrrigationLabel != null) timePerIrrigationLabel.setText(formatTime(hoursPerIrrigation));

        // Removed totalTimeLabel setting
        // if (totalTimeLabel != null) totalTimeLabel.setText(formatTime(totalHours));

        if (efficiencyLabel != null) efficiencyLabel.setText(df.format(efficiency) + "%");

        // Cost and Schedule Logic (Checked against null to prevent crashing if FXML is commented out)
        if (fuelCostLabel != null) fuelCostLabel.setText("৳ " + moneyFormat.format(costs[0]));
        if (laborCostLabel != null) laborCostLabel.setText("৳ " + moneyFormat.format(costs[1]));
        if (maintenanceCostLabel != null) maintenanceCostLabel.setText("৳ " + moneyFormat.format(costs[2]));
        if (totalCostLabel != null) {
            double totalCost = costs[0] + costs[1] + costs[2];
            totalCostLabel.setText("৳ " + moneyFormat.format(totalCost));
        }

        if (scheduleContainer != null) {
            createIrrigationSchedule(numIrrigations, frequency);
        }
    }

    private String formatLiters(double liters) {
        if (liters >= 1000000) return df.format(liters / 1000000.0) + " মিলিয়ন লিটার";
        else if (liters >= 1000) return df.format(liters / 1000.0) + " হাজার লিটার";
        return df.format(liters) + " লিটার";
    }

    // --- Helper to format Time ---
    private String formatTime(double totalHours) {
        if (totalHours <= 0) return "পাম্প নির্বাচন করুন";

        int hours = (int) totalHours;
        int minutes = (int) ((totalHours - hours) * 60);

        if (hours > 0) return hours + " ঘণ্টা " + minutes + " মিনিট";
        else return minutes + " মিনিট";
    }

    private void createIrrigationSchedule(int numIrrigations, int frequency) {
        scheduleContainer.getChildren().clear();
        int[] keyStages = {1, numIrrigations / 4, numIrrigations / 2, (3 * numIrrigations) / 4, numIrrigations};
        String[] stageNames = {"প্রথম সেচ", "২য় পর্যায়", "মধ্য পর্যায়", "৩য় পর্যায়", "শেষ সেচ"};

        for (int i = 0; i < keyStages.length; i++) {
            if (keyStages[i] <= 0 || keyStages[i] > numIrrigations) continue;
            HBox scheduleItem = new HBox(15);
            scheduleItem.setAlignment(Pos.CENTER_LEFT);
            scheduleItem.setStyle("-fx-background-color: #f0f9ff; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #bae6fd; -fx-border-width: 1; -fx-border-radius: 10;");

            Label numberLabel = new Label(String.valueOf(keyStages[i]));
            numberLabel.setStyle("-fx-background-color: #0284c7; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 12; -fx-background-radius: 15; -fx-min-width: 40; -fx-alignment: center;");

            VBox textBox = new VBox(5);
            textBox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);
            Label titleLabel = new Label(stageNames[i]);
            titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #0c4a6e;");
            int daysAfter = keyStages[i] * frequency;
            Label dayLabel = new Label("বপন/রোপণের " + daysAfter + " দিন পর");
            dayLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #075985;");

            textBox.getChildren().addAll(titleLabel, dayLabel);
            scheduleItem.getChildren().addAll(numberLabel, textBox);
            scheduleContainer.getChildren().add(scheduleItem);
        }
    }

    private void resetForm() {
        cropComboBox.setValue(null);
        growthStageComboBox.getItems().clear();
        landAreaField.clear();
        unitComboBox.setValue("একর");
        soilTypeComboBox.setValue(null);
        seasonComboBox.setValue(null);
        tempModerate.setSelected(true);
        recentRainCheck.setSelected(false);
        rainfallAmount.clear();
        rainfallAmount.setDisable(true);
        methodFlood.setSelected(true);
        waterSourceComboBox.setValue(null);
        pumpPowerComboBox.setValue("নেই");
        emptyResultState.setVisible(true);
        emptyResultState.setManaged(true);
        resultContentContainer.setVisible(false);
        resultContentContainer.setManaged(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ত্রুটি");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}