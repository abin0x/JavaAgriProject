package com.example.demo1.marketfruits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent; // Import added
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node; // Import added
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LocalManagementController implements Initializable {

    // Sidebar buttons
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement;

    // Header button
    @FXML private Button addRecordBtn;

    // Statistics labels
    @FXML private Label totalWorkersLabel, pendingPaymentLabel, completedPaymentLabel;

    // Filter and list
    @FXML private ComboBox<String> filterComboBox;
    @FXML private VBox workersListContainer, emptyState;

    // Data
    private List<WorkerRecord> workerRecords;
    private Gson gson;
    private static final String DATA_FILE = "workers_data.json";
    private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("শ্রমিক ব্যবস্থাপনা পেজ চালু হয়েছে!");

        gson = new GsonBuilder().setPrettyPrinting().create();

        setupFilterComboBox();
        loadWorkersData();
        setupEventHandlers();
        setupNavigationHandlers();
        updateStatistics();
        displayWorkers();
    }

    // ---------------------------------------------------------
    // NAVIGATION LOGIC (FIXED SECTION)
    // ---------------------------------------------------------
    private void setupNavigationHandlers() {
        // Home Button
        if (btnHome != null) {
            btnHome.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/dashboard.fxml"));
        }

        // Crop Advisory Button
        if (btnAdvisory != null) {
            btnAdvisory.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/CropAdvisory.fxml"));
        }

        // Storage/Warehouse Button
        if (btnStorage != null) {
            btnStorage.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/WarehouseView.fxml"));
        }

        // Current Page Button (Optional: Refresh the page)
        if (btnLocalManagement != null) {
            btnLocalManagement.setOnAction(event -> loadPage(event, "/com/example/demo1/fxml/LocalManagement.fxml"));
        }
    }

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            // 1. Get the Stage from the button that was clicked (Safer than hardcoding btnHome)
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // 2. Load FXML
            URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                throw new FileNotFoundException("FXML file not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // 3. Load CSS (Global Dashboard CSS + Page Specific CSS)
            String dashboardCss = getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm();
            if (dashboardCss != null) scene.getStylesheets().add(dashboardCss);

            // Logic to add specific CSS based on file path
            if (fxmlPath.contains("LocalManagement")) {
                String localCss = getClass().getResource("/com/example/demo1/css/LocalManagement.css").toExternalForm();
                if (localCss != null) scene.getStylesheets().add(localCss);
            } else if (fxmlPath.contains("CropAdvisory")) {
                String cropCss = getClass().getResource("/com/example/demo1/css/CropAdvisory.css").toExternalForm();
                if (cropCss != null) scene.getStylesheets().add(cropCss);
            }

            // 4. Set Scene
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading page: " + fxmlPath);
            e.printStackTrace();
            showError("পেজ লোড করতে সমস্যা হয়েছে: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // DATA & LOGIC (UNCHANGED)
    // ---------------------------------------------------------

    // Worker Record Class
    public static class WorkerRecord {
        private String id;
        private String name;
        private String phone;
        private String workType;
        private String date;
        private double hours;
        private double ratePerHour;
        private double totalAmount;
        private String paymentStatus;
        private String notes;

        public WorkerRecord(String id, String name, String phone, String workType,
                            String date, double hours, double ratePerHour,
                            String paymentStatus, String notes) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.workType = workType;
            this.date = date;
            this.hours = hours;
            this.ratePerHour = ratePerHour;
            this.totalAmount = hours * ratePerHour;
            this.paymentStatus = paymentStatus;
            this.notes = notes;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getWorkType() { return workType; }
        public String getDate() { return date; }
        public double getHours() { return hours; }
        public double getRatePerHour() { return ratePerHour; }
        public double getTotalAmount() { return totalAmount; }
        public String getPaymentStatus() { return paymentStatus; }
        public String getNotes() { return notes; }
        public void setPaymentStatus(String status) { this.paymentStatus = status; }
    }

    private void setupFilterComboBox() {
        filterComboBox.getItems().addAll("সব দেখুন", "বাকি পেমেন্ট", "সম্পন্ন পেমেন্ট", "আজকের কাজ", "এই সপ্তাহ");
        filterComboBox.setValue("সব দেখুন");
        filterComboBox.setOnAction(e -> applyFilter());
    }

    private void setupEventHandlers() {
        addRecordBtn.setOnAction(e -> showAddRecordDialog());
    }

    private void loadWorkersData() {
        workerRecords = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<WorkerRecord>>(){}.getType();
                List<WorkerRecord> loadedRecords = gson.fromJson(reader, listType);
                if (loadedRecords != null) {
                    workerRecords = loadedRecords;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveWorkersData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(workerRecords, writer);
        } catch (IOException e) {
            showError("ডেটা সংরক্ষণে সমস্যা হয়েছে");
        }
    }

    private void updateStatistics() {
        Set<String> uniqueWorkers = new HashSet<>();
        double pendingTotal = 0;
        double completedTotal = 0;

        for (WorkerRecord record : workerRecords) {
            uniqueWorkers.add(record.getName());
            if ("pending".equals(record.getPaymentStatus())) {
                pendingTotal += record.getTotalAmount();
            } else {
                completedTotal += record.getTotalAmount();
            }
        }

        totalWorkersLabel.setText(String.valueOf(uniqueWorkers.size()));
        pendingPaymentLabel.setText("৳" + moneyFormat.format(pendingTotal));
        completedPaymentLabel.setText("৳" + moneyFormat.format(completedTotal));
    }

    private void displayWorkers() {
        workersListContainer.getChildren().clear();
        if (workerRecords.isEmpty()) {
            emptyState.setVisible(true);
            emptyState.setManaged(true);
        } else {
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            for (WorkerRecord record : workerRecords) {
                workersListContainer.getChildren().add(createWorkerCard(record));
            }
        }
    }

    private VBox createWorkerCard(WorkerRecord record) {
        VBox card = new VBox(12);
        card.getStyleClass().add("worker-card");
        card.setPadding(new Insets(20));

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("👤 " + record.getName());
        nameLabel.getStyleClass().add("worker-name");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Label statusBadge = new Label("pending".equals(record.getPaymentStatus()) ? "বাকি আছে" : "সম্পন্ন");
        statusBadge.getStyleClass().add("pending".equals(record.getPaymentStatus()) ? "status-badge-pending" : "status-badge-completed");
        header.getChildren().addAll(nameLabel, spacer, statusBadge);

        HBox details1 = new HBox(30);
        details1.getChildren().addAll(
                createDetailItem("📞", record.getPhone()),
                createDetailItem("🛠️", record.getWorkType()),
                createDetailItem("📅", record.getDate())
        );

        HBox details2 = new HBox(30);
        details2.getChildren().addAll(
                createDetailItem("⏰", record.getHours() + " ঘণ্টা"),
                createDetailItem("💰", "৳" + moneyFormat.format(record.getRatePerHour()) + "/ঘণ্টা"),
                createDetailItem("💵", "মোট: ৳" + moneyFormat.format(record.getTotalAmount()))
        );

        VBox notesSection = new VBox(5);
        if (record.getNotes() != null && !record.getNotes().isEmpty()) {
            Label notesLabel = new Label("📝 নোট:");
            notesLabel.getStyleClass().add("notes-label");
            Label notesText = new Label(record.getNotes());
            notesText.getStyleClass().add("notes-text");
            notesText.setWrapText(true);
            notesSection.getChildren().addAll(notesLabel, notesText);
        }

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        if ("pending".equals(record.getPaymentStatus())) {
            Button payButton = new Button("✓ পেমেন্ট সম্পন্ন");
            payButton.getStyleClass().add("pay-btn");
            payButton.setOnAction(e -> markAsPaid(record));
            actions.getChildren().add(payButton);
        }
        Button deleteButton = new Button("🗑️ মুছুন");
        deleteButton.getStyleClass().add("delete-btn");
        deleteButton.setOnAction(e -> deleteRecord(record));
        actions.getChildren().add(deleteButton);

        card.getChildren().addAll(header, details1, details2, notesSection, actions);
        return card;
    }

    private VBox createDetailItem(String icon, String text) {
        VBox item = new VBox(3);
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");
        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("detail-value");
        item.getChildren().addAll(iconLabel, textLabel);
        return item;
    }

    private void markAsPaid(WorkerRecord record) {
        record.setPaymentStatus("completed");
        saveWorkersData();
        updateStatistics();
        displayWorkers();
        showSuccess("পেমেন্ট সম্পন্ন হয়েছে!");
    }

    private void deleteRecord(WorkerRecord record) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("নিশ্চিত করুন");
        confirm.setHeaderText("এই রেকর্ড মুছতে চান?");
        confirm.setContentText(record.getName() + " - " + record.getDate());
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                workerRecords.remove(record);
                saveWorkersData();
                updateStatistics();
                displayWorkers();
                showSuccess("রেকর্ড মুছে ফেলা হয়েছে");
            }
        });
    }

    private void showAddRecordDialog() {
        Dialog<WorkerRecord> dialog = new Dialog<>();
        dialog.setTitle("নতুন শ্রমিক রেকর্ড");
        dialog.setHeaderText("শ্রমিকের তথ্য দিন");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(); nameField.setPromptText("নাম");
        TextField phoneField = new TextField(); phoneField.setPromptText("মোবাইল নম্বর");
        ComboBox<String> workTypeCombo = new ComboBox<>();
        workTypeCombo.getItems().addAll("জমি চাষ", "বীজ বপন", "সেচ", "সার প্রয়োগ", "আগাছা পরিষ্কার", "ফসল কাটা", "অন্যান্য");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField hoursField = new TextField(); hoursField.setPromptText("ঘণ্টা");
        TextField rateField = new TextField(); rateField.setPromptText("টাকা/ঘণ্টা");
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("বাকি আছে", "সম্পন্ন"); statusCombo.setValue("বাকি আছে");
        TextArea notesArea = new TextArea(); notesArea.setPromptText("নোট");

        grid.add(new Label("নাম:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("মোবাইল:"), 0, 1); grid.add(phoneField, 1, 1);
        grid.add(new Label("কাজের ধরন:"), 0, 2); grid.add(workTypeCombo, 1, 2);
        grid.add(new Label("তারিখ:"), 0, 3); grid.add(datePicker, 1, 3);
        grid.add(new Label("ঘণ্টা:"), 0, 4); grid.add(hoursField, 1, 4);
        grid.add(new Label("টাকা/ঘণ্টা:"), 0, 5); grid.add(rateField, 1, 5);
        grid.add(new Label("পেমেন্ট:"), 0, 6); grid.add(statusCombo, 1, 6);
        grid.add(new Label("নোট:"), 0, 7); grid.add(notesArea, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String id = UUID.randomUUID().toString();
                    String name = nameField.getText().trim();
                    String phone = phoneField.getText().trim();
                    String workType = workTypeCombo.getValue();
                    String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    double hours = Double.parseDouble(hoursField.getText().trim());
                    double rate = Double.parseDouble(rateField.getText().trim());
                    String status = statusCombo.getValue();
                    String notes = notesArea.getText().trim();
                    if (name.isEmpty() || workType == null) return null;
                    return new WorkerRecord(id, name, phone, workType, date, hours, rate, status, notes);
                } catch (Exception e) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(record -> {
            workerRecords.add(0, record);
            saveWorkersData();
            updateStatistics();
            displayWorkers();
            showSuccess("নতুন রেকর্ড যোগ হয়েছে!");
        });
    }

    private void applyFilter() {
        // Filtering logic placeholder
        displayWorkers();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("সফল");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ত্রুটি");
        alert.setContentText(message);
        alert.showAndWait();
    }
}