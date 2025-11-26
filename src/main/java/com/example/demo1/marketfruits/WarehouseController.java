package com.example.demo1.marketfruits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WarehouseController implements Initializable {

    // Sidebar buttons
    @FXML private Button btnHome, btnAdvisory, btnStorage;

    // Search
    @FXML private TextField searchField;
    @FXML private Button searchBtn, loadMoreBtn;

    // Grid for cards
    @FXML private GridPane warehouseGrid; // Ensure your FXML uses fx:id="warehouseGrid" inside the FlowPane/GridPane container

    // Data structure for storage facilities
    private List<StorageFacility> allFacilities;
    private List<StorageFacility> filteredFacilities;
    private int currentDisplayCount = 3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Warehouse page initialized!");

        // FIXED: Uncommented these so data actually loads
        initializeFacilityData();
        setupEventHandlers();
        setupNavigationHandlers();
        displayFacilities();
    }

    // Inner class to hold Warehouse data
    private static class StorageFacility {
        String title;
        String type;
        String location;
        String ownerName;
        String totalCapacity;
        String availableStock;
        String price;
        int vacancyRate;
        String[] acceptableCrops;
        String[] facilities;

        StorageFacility(String title, String type, String location, String ownerName,
                        String totalCapacity, String availableStock, String price, int vacancyRate,
                        String[] crops, String[] facilities) {
            this.title = title;
            this.type = type;
            this.location = location;
            this.ownerName = ownerName;
            this.totalCapacity = totalCapacity;
            this.availableStock = availableStock;
            this.price = price;
            this.vacancyRate = vacancyRate;
            this.acceptableCrops = crops;
            this.facilities = facilities;
        }
    }

    private void initializeFacilityData() {
        allFacilities = new ArrayList<>();

        allFacilities.add(new StorageFacility(
                "কৃষি ভান্ডার ও গুদাম", "গুদাম", "নরসিংদী, শিবপুর", "মোঃ সালাম উদ্দিন",
                "৫০০", "৩২০", "৮৫০", 64,
                new String[]{"ধান", "গম", "ভুট্টা", "+১"},
                new String[]{"ওজন মাপা", "লোডিং-আনলোডিং"}
        ));

        allFacilities.add(new StorageFacility(
                "আধুনিক শীতাতপ নিয়ন্ত্রিত গুদাম", "হিমাগার", "বগুড়া, শেরপুর", "আবু তাহের",
                "১০০০", "৬৫০", "৮০০", 65,
                new String[]{"আলু", "পেঁয়াজ", "রসুন", "+২"},
                new String[]{"তাপমাত্রা নিয়ন্ত্রণ", "আর্দ্রতা নিয়ন্ত্রণ"}
        ));

        allFacilities.add(new StorageFacility(
                "রহমান কৃষি গুদাম", "গুদাম", "কুমিল্লা, চৌদ্দগ্রাম", "মোঃ আব্দুর রহমান",
                "৫০০", "২০০", "১৫০", 40,
                new String[]{"ধান", "গম", "ভুট্টা", "+১"},
                new String[]{"ওজন করার সুবিধা", "লোডিং-আনলোডিং"}
        ));

        // Load More Data
        allFacilities.add(new StorageFacility(
                "সবুজ কৃষি হিমাগার", "হিমাগার", "দিনাজপুর, বিরামপুর", "মো. জাহাঙ্গীর আলম",
                "৮০০", "৪৮০", "৩৫০", 72,
                new String[]{"আলু", "টমেটো", "শসা", "+২"},
                new String[]{"কোল্ড চেইন", "প্যাকেজিং সুবিধা"}
        ));

        allFacilities.add(new StorageFacility(
                "মডার্ন এগ্রো স্টোরেজ", "গুদাম", "রাজশাহী, গোদাগাড়ী", "আব্দুল করিম",
                "৬০০", "৪২০", "২২০", 58,
                new String[]{"ধান", "পাট", "সরিষা", "+১"},
                new String[]{"শুষ্কীকরণ যন্ত্র", "কীটনাশক স্প্রে"}
        ));

        allFacilities.add(new StorageFacility(
                "চট্টগ্রাম সেন্ট্রাল কোল্ড স্টোরেজ", "হিমাগার", "চট্টগ্রাম, হাটহাজারী", "মো. শফিকুল ইসলাম",
                "১২০০", "৮৫০", "৪৫০", 68,
                new String[]{"আলু", "পেঁয়াজ", "রসুন", "+৩"},
                new String[]{"অটো তাপমাত্রা", "লোডিং সুবিধা"}
        ));

        filteredFacilities = new ArrayList<>(allFacilities);
    }

    private void setupEventHandlers() {
        if(searchBtn != null) searchBtn.setOnAction(e -> performSearch());
        if(searchField != null) searchField.setOnAction(e -> performSearch());
        if(loadMoreBtn != null) loadMoreBtn.setOnAction(e -> loadMoreFacilities());
    }

    private void setupNavigationHandlers() {
        // FIXED: Removed the 'event' argument from the loadPage calls
        if (btnHome != null) {
            btnHome.setOnAction(event -> loadPage("/com/example/demo1/fxml/dashboard.fxml"));
        }
        if (btnAdvisory != null) {
            btnAdvisory.setOnAction(event -> loadPage("/com/example/demo1/fxml/CropAdvisory.fxml"));
        }
        if (btnStorage != null) {
            btnStorage.setOnAction(event -> loadPage("/com/example/demo1/fxml/Warehouse.fxml"));
        }
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            filteredFacilities = new ArrayList<>(allFacilities);
        } else {
            filteredFacilities = new ArrayList<>();
            for (StorageFacility facility : allFacilities) {
                if (facility.title.toLowerCase().contains(query) ||
                        facility.location.toLowerCase().contains(query) ||
                        facility.ownerName.toLowerCase().contains(query) ||
                        facility.type.toLowerCase().contains(query)) {
                    filteredFacilities.add(facility);
                }
            }
        }

        currentDisplayCount = 3;
        displayFacilities();
    }

    private void loadMoreFacilities() {
        currentDisplayCount += 3;
        displayFacilities();
    }

    private void displayFacilities() {
        if(warehouseGrid == null) return;

        warehouseGrid.getChildren().clear();

        int displayLimit = Math.min(currentDisplayCount, filteredFacilities.size());

        for (int i = 0; i < displayLimit; i++) {
            StorageFacility facility = filteredFacilities.get(i);
            VBox facilityCard = createFacilityCard(facility);

            // Grid logic: 3 columns
            int col = i % 3;
            int row = i / 3;

            warehouseGrid.add(facilityCard, col, row);
        }

        if (loadMoreBtn != null) {
            if (displayLimit >= filteredFacilities.size()) {
                loadMoreBtn.setVisible(false);
                loadMoreBtn.setManaged(false);
            } else {
                loadMoreBtn.setVisible(true);
                loadMoreBtn.setManaged(true);
            }
        }
    }

    private VBox createFacilityCard(StorageFacility facility) {
        VBox card = new VBox(15);
        // FIXED: Changed "expert-card" to "warehouse-card" to match the CSS provided
        card.getStyleClass().add("warehouse-card");

        // --- Header Section ---
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(facility.title);
        title.getStyleClass().add("card-title");
        title.setWrapText(true);
        title.setMaxWidth(180);

        StackPane badgePane = new StackPane();
        badgePane.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(badgePane, Priority.ALWAYS);

        Label badge = new Label("✔ " + facility.vacancyRate + "% ফাঁকা");
        badge.getStyleClass().add("badge-black");
        badgePane.getChildren().add(badge);

        header.getChildren().addAll(title, badgePane);

        // --- Type Tag ---
        Label typeLabel = new Label(facility.type);
        typeLabel.getStyleClass().add("tag-grey");

        // --- Details Section ---
        VBox details = new VBox(10);
        details.getStyleClass().add("details-box");
        details.getChildren().addAll(
                createDetailRow("📍", facility.location),
                createDetailRow("👤", facility.ownerName),
                createDetailRow("📦", facility.availableStock + "/" + facility.totalCapacity + " টন উপলব্ধ"),
                createDetailRow("💰", "৳ " + facility.price + "/টন/মাস")
        );

        // --- Crops Section ---
        Label cropsHeader = new Label("গ্রহণযোগ্য ফসল:");
        cropsHeader.getStyleClass().add("section-header");

        // Used FlowPane logic but inside VBox for simplicity in code generation
        HBox cropsBox = new HBox(8);
        for (String crop : facility.acceptableCrops) {
            Label cropTag = new Label(crop);
            cropTag.getStyleClass().add("tag-white");
            cropsBox.getChildren().add(cropTag);
        }

        // --- Facilities Section ---
        Label facilityHeader = new Label("সুবিধাসমূহ:");
        facilityHeader.getStyleClass().add("section-header");

        HBox facilitiesBox = new HBox(8);
        for (String fac : facility.facilities) {
            Label facTag = new Label(fac);
            facTag.getStyleClass().add("tag-facility");
            facilitiesBox.getChildren().add(facTag);
        }

        // --- Contact Button ---
        Button contactBtn = new Button("📞 যোগাযোগ করুন");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);
        contactBtn.setOnAction(e -> showContactInfo(facility));

        card.getChildren().addAll(
                header,
                typeLabel,
                details,
                cropsHeader,
                cropsBox,
                facilityHeader,
                facilitiesBox,
                contactBtn
        );

        return card;
    }

    private HBox createDetailRow(String icon, String text) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("detail-icon");

        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("detail-text");
        textLabel.setWrapText(true);

        row.getChildren().addAll(iconLabel, textLabel);
        return row;
    }

    private void showContactInfo(StorageFacility facility) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("যোগাযোগের তথ্য");
        alert.setHeaderText(facility.title);
        alert.setContentText(
                "মালিক: " + facility.ownerName + "\n" +
                        "স্থান: " + facility.location + "\n" +
                        "মোবাইল: ০১৭১২-৩৪৫৬৭৮\n" +
                        "ভাড়া: ৳ " + facility.price + "/টন/মাস"
        );
        alert.showAndWait();
    }

    private void loadPage(String fxmlPath) {
        try {
            // Check if btnStorage is available to get the stage, otherwise try btnHome
            Button sourceButton = (btnStorage != null) ? btnStorage : btnHome;
            if (sourceButton == null) return; // Safety check

            Stage stage = (Stage) sourceButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Set styles
            // Note: Update "style.css" to the actual name of your CSS file
            URL cssUrl = getClass().getResource("/com/example/demo1/css/dashboard.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load page: " + fxmlPath);
        }
    }
}