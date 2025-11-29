package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Ensure this matches your package
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LocalManagementController implements Initializable {

    // --- FXML Controls ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, addRecordBtn,btnMachinery;
    @FXML private Label totalWorkersLabel, pendingPaymentLabel, completedPaymentLabel;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private VBox workersListContainer, emptyState;

    // --- Data & Tools ---
    private List<WorkerRecord> workerRecords = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_FILE = "workers_data.json";
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Navigation: Managed centrally by Helper (Clean & Short)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery);

        // 2. Setup UI & Data
        setupFilter();
        addRecordBtn.setOnAction(e -> showAddDialog());
        loadData();
        refreshView();
    }

    // ---------------------------------------------------------
    // DATA MANAGEMENT
    // ---------------------------------------------------------
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            List<WorkerRecord> data = gson.fromJson(reader, new TypeToken<ArrayList<WorkerRecord>>(){}.getType());
            if (data != null) workerRecords = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(workerRecords, writer);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data.");
        }
    }

    // ---------------------------------------------------------
    // UI RENDERING & LOGIC
    // ---------------------------------------------------------
    private void refreshView() {
        updateStats();
        renderList();
    }

    private void updateStats() {
        double pending = workerRecords.stream().filter(r -> "pending".equals(r.status)).mapToDouble(WorkerRecord::getTotal).sum();
        double completed = workerRecords.stream().filter(r -> !"pending".equals(r.status)).mapToDouble(WorkerRecord::getTotal).sum();
        long uniqueWorkers = workerRecords.stream().map(r -> r.name).distinct().count();

        totalWorkersLabel.setText(String.valueOf(uniqueWorkers));
        pendingPaymentLabel.setText("৳" + moneyFormat.format(pending));
        completedPaymentLabel.setText("৳" + moneyFormat.format(completed));
    }

    private void renderList() {
        workersListContainer.getChildren().clear();

        // Apply Filter Logic
        String filter = filterComboBox.getValue();
        List<WorkerRecord> visibleRecords = workerRecords.stream()
                .filter(r -> filter.equals("All")
                        || (filter.equals("Pending") && "pending".equals(r.status))
                        || (filter.equals("Completed") && !"pending".equals(r.status)))
                .collect(Collectors.toList());

        boolean isEmpty = visibleRecords.isEmpty();
        emptyState.setVisible(isEmpty);
        emptyState.setManaged(isEmpty);

        visibleRecords.forEach(record -> workersListContainer.getChildren().add(createCard(record)));
    }

    private VBox createCard(WorkerRecord r) {
        VBox card = new VBox(12);
        card.getStyleClass().add("worker-card");
        card.setPadding(new Insets(20));

        // Header
        boolean isPending = "pending".equals(r.status);
        Label statusLbl = new Label(isPending ? "বাকি আছে" : "সম্পন্ন");
        statusLbl.getStyleClass().add(isPending ? "status-badge-pending" : "status-badge-completed");

        HBox header = new HBox(15, new Label("👤 " + r.name), new Region(), statusLbl);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        ((Label)header.getChildren().get(0)).getStyleClass().add("worker-name");

        // Details
        HBox row1 = createInfoRow("📞 " + r.phone, "🛠️ " + r.workType, "📅 " + r.date);
        HBox row2 = createInfoRow("⏰ " + r.hours + " h", "💰 ৳" + r.rate + "/h", "💵 Total: ৳" + moneyFormat.format(r.getTotal()));

        // Actions
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if (isPending) {
            Button payBtn = new Button("✓ Pay");
            payBtn.getStyleClass().add("pay-btn");
            payBtn.setOnAction(e -> { r.status = "completed"; saveData(); refreshView(); });
            actions.getChildren().add(payBtn);
        }

        Button delBtn = new Button("🗑️ Delete");
        delBtn.getStyleClass().add("delete-btn");
        delBtn.setOnAction(e -> confirmDelete(r));
        actions.getChildren().add(delBtn);

        card.getChildren().addAll(header, row1, row2, actions);
        if (r.notes != null && !r.notes.isEmpty()) {
            Label n = new Label("📝 " + r.notes);
            n.setWrapText(true);
            card.getChildren().add(3, n);
        }
        return card;
    }

    private HBox createInfoRow(String... texts) {
        HBox row = new HBox(30);
        for (String t : texts) row.getChildren().add(new Label(t));
        return row;
    }

    // ---------------------------------------------------------
    // DIALOGS & HELPERS
    // ---------------------------------------------------------
    private void showAddDialog() {
        Dialog<WorkerRecord> dialog = new Dialog<>();
        dialog.setTitle("New Record");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField name = new TextField(); name.setPromptText("Name");
        TextField phone = new TextField();
        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("জমি চাষ", "বীজ বপন", "সেচ", "সার প্রয়োগ", "ফসল কাটা", "অন্যান্য");
        DatePicker date = new DatePicker(LocalDate.now());
        TextField hours = new TextField(); hours.setPromptText("Hours");
        TextField rate = new TextField(); rate.setPromptText("Rate");
        TextArea notes = new TextArea(); notes.setPrefRowCount(2);

        grid.addRow(0, new Label("Name:"), name);
        grid.addRow(1, new Label("Phone:"), phone);
        grid.addRow(2, new Label("Type:"), type);
        grid.addRow(3, new Label("Date:"), date);
        grid.addRow(4, new Label("Hours:"), hours);
        grid.addRow(5, new Label("Rate:"), rate);
        grid.addRow(6, new Label("Notes:"), notes);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return new WorkerRecord(
                            UUID.randomUUID().toString(), name.getText(), phone.getText(), type.getValue(),
                            date.getValue().format(DATE_FMT), Double.parseDouble(hours.getText()),
                            Double.parseDouble(rate.getText()), "pending", notes.getText()
                    );
                } catch (Exception e) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(r -> {
            workerRecords.add(0, r);
            saveData();
            refreshView();
        });
    }

    private void confirmDelete(WorkerRecord r) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete record for " + r.name + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                workerRecords.remove(r);
                saveData();
                refreshView();
            }
        });
    }

    private void setupFilter() {
        filterComboBox.getItems().addAll("All", "Pending", "Completed");
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(e -> refreshView());
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

    // --- Model Class ---
    private static class WorkerRecord {
        String id, name, phone, workType, date, status, notes;
        double hours, rate;

        public WorkerRecord(String id, String n, String p, String w, String d, double h, double r, String s, String nt) {
            this.id = id; this.name = n; this.phone = p; this.workType = w; this.date = d;
            this.hours = h; this.rate = r; this.status = s; this.notes = nt;
        }
        public double getTotal() { return hours * rate; }
    }
}