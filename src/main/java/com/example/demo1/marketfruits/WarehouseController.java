package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {

    // --- FXML Controls ---
    // Sidebar Buttons
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, btnLabor;

    // Search Controls
    @FXML private TextField searchField;
    @FXML private Button searchBtn, loadMoreBtn;

    // Container (Changed from GridPane to FlowPane to match your FXML)
    @FXML private FlowPane warehouseContainer;

    // --- Data ---
    private List<StorageFacility> allFacilities = new ArrayList<>();
    private List<StorageFacility> filteredFacilities = new ArrayList<>();
    private int displayCount = 6; // How many to show initially

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Navigation
        // Ensure these buttons exist in your FXML or pass null if they don't
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // 2. Initialize Logic
        loadData();
        setupActions();

        // 3. Render initial view
        renderGrid();
    }

    // ---------------------------------------------------------
    // 1. DATA LOADING
    // ---------------------------------------------------------
    private void loadData() {
        allFacilities.clear();
        allFacilities.addAll(Arrays.asList(
                new StorageFacility("কৃষি ভান্ডার ও গুদাম", "গুদাম", "নরসিংদী, শিবপুর", "মোঃ সালাম উদ্দিন", "৫০০", "৩২০", "৮৫০", 64, new String[]{"ধান", "গম", "+১"}, new String[]{"ওজন মাপা"}),
                new StorageFacility("আধুনিক শীতাতপ হিমাগার", "হিমাগার", "বগুড়া, শেরপুর", "আবু তাহের", "১০০০", "৬৫০", "৮০০", 65, new String[]{"আলু", "পেঁয়াজ", "+২"}, new String[]{"তাপমাত্রা নিয়ন্ত্রণ"}),
                new StorageFacility("রহমান কৃষি গুদাম", "গুদাম", "কুমিল্লা, চৌদ্দগ্রাম", "মোঃ রহমান", "৫০০", "২০০", "১৫০", 40, new String[]{"ধান", "ভুট্টা", "+১"}, new String[]{"লোডিং-আনলোডিং"}),
                new StorageFacility("সবুজ কৃষি হিমাগার", "হিমাগার", "দিনাজপুর, বিরামপুর", "মো. জাহাঙ্গীর", "৮০০", "৪৮০", "৩৫০", 72, new String[]{"আলু", "টমেটো", "+২"}, new String[]{"কোল্ড চেইন"}),
                new StorageFacility("মডার্ন এগ্রো স্টোরেজ", "গুদাম", "রাজশাহী, গোদাগাড়ী", "আব্দুল করিম", "৬০০", "৪২০", "২২০", 58, new String[]{"ধান", "সরিষা", "+১"}, new String[]{"শুষ্কীকরণ যন্ত্র"}),
                new StorageFacility("চট্টগ্রাম সেন্ট্রাল স্টোরেজ", "হিমাগার", "চট্টগ্রাম, হাটহাজারী", "মো. শফিকুল", "১২০০", "৮৫০", "৪৫০", 68, new String[]{"আলু", "রসুন", "+৩"}, new String[]{"অটো তাপমাত্রা"}),
                new StorageFacility("আধুনিক হিমাগার", "হিমাগার", "নরসিংদী সদর", "ইঞ্জি. সালমান চৌধুরী", "২০০", "৮০", "৩০০", 40, new String[]{"আলু", "টমেটো", "+১"}, new String[]{"তাপমাত্রা নিয়ন্ত্রণ"}),
                new StorageFacility("কৃষক সমবায় গুদাম", "গুদাম", "মুন্সিগঞ্জ, গজারিয়া", "মো. নজরুল ইসলাম", "৩০০", "০", "১২০", 0, new String[]{"ধান", "আলু"}, new String[]{"কীটনাশক স্প্রে"})
        ));
        filteredFacilities.addAll(allFacilities);
    }

    // ---------------------------------------------------------
    // 2. SEARCH ACTIONS
    // ---------------------------------------------------------
    private void setupActions() {
        // Filter when search button clicked
        if (searchBtn != null) searchBtn.setOnAction(e -> filterData());

        // Filter as you type (Real-time searching)
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterData());
        }

        // Load More button
        if (loadMoreBtn != null) loadMoreBtn.setOnAction(e -> {
            displayCount += 3;
            renderGrid();
        });
    }

    private void filterData() {
        String query = (searchField != null) ? searchField.getText().toLowerCase().trim() : "";

        filteredFacilities = allFacilities.stream()
                .filter(f -> query.isEmpty() || f.matches(query))
                .collect(Collectors.toList());

        // Reset display count or keep it? Let's reset to show results clearly
        // displayCount = filteredFacilities.size();
        renderGrid();
    }

    // ---------------------------------------------------------
    // 3. UI RENDERING
    // ---------------------------------------------------------
    private void renderGrid() {
        if (warehouseContainer == null) return;

        // Clear existing cards (removes hardcoded FXML cards and shows Java generated ones)
        warehouseContainer.getChildren().clear();

        int limit = Math.min(displayCount, filteredFacilities.size());

        for (int i = 0; i < limit; i++) {
            // Add card to FlowPane
            warehouseContainer.getChildren().add(createCard(filteredFacilities.get(i)));
        }

        // Manage Load More Button visibility
        if (loadMoreBtn != null) {
            boolean hasMore = limit < filteredFacilities.size();
            loadMoreBtn.setVisible(hasMore);
            loadMoreBtn.setManaged(hasMore);
        }
    }

    private VBox createCard(StorageFacility f) {
        VBox card = new VBox(15);
        card.getStyleClass().add("warehouse-card");

        // Header
        Label title = new Label(f.title);
        title.getStyleClass().add("card-title");
        title.setWrapText(true);
        title.setMaxWidth(180);

        // Badge Logic
        Label badge;
        if (f.vacancyRate == 0) {
            badge = new Label("🕒 পূর্ণ");
            badge.setStyle("-fx-background-color: #ef4444;"); // Red for full
        } else {
            badge = new Label("✔ " + f.vacancyRate + "% ফাঁকা");
        }
        badge.getStyleClass().add("badge-black");

        HBox header = new HBox(10, title, new Region(), badge);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        // Type Tag
        Label typeTag = new Label(f.type);
        typeTag.getStyleClass().add("tag-grey");

        // Details
        VBox details = new VBox(10,
                createRow("📍", f.location),
                createRow("👤", f.owner),
                createRow("📦", f.available + "/" + f.capacity + " টন"),
                createRow("💰", "৳ " + f.price + "/টন")
        );
        details.getStyleClass().add("details-box");

        // Crops Tags
        HBox crops = new HBox(5);
        if(f.crops != null) Arrays.stream(f.crops).forEach(c -> crops.getChildren().add(createTag(c, "tag-white")));

        // Facilities Tags
        HBox facilities = new HBox(5);
        if(f.facilities != null) Arrays.stream(f.facilities).forEach(fac -> facilities.getChildren().add(createTag(fac, "tag-facility")));

        // Contact Button
        Button contactBtn = new Button("📞 যোগাযোগ করুন");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);

        if (f.vacancyRate == 0) {
            contactBtn.setDisable(true);
            contactBtn.setStyle("-fx-background-color: #9ca3af; -fx-cursor: default;");
        } else {
            contactBtn.setOnAction(e -> showAlert(f));
        }

        card.getChildren().addAll(header, typeTag, details, new Label("ফসল:"), crops, new Label("সুবিধা:"), facilities, contactBtn);
        return card;
    }

    private HBox createRow(String icon, String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("detail-text");
        return new HBox(10, new Label(icon), lbl);
    }

    private Label createTag(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }

    private void showAlert(StorageFacility f) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact Information");
        alert.setHeaderText(f.title);
        alert.setContentText("Owner: " + f.owner + "\nLocation: " + f.location + "\nCall: 01712-000000");
        alert.show();
    }

    // ---------------------------------------------------------
    // INTERNAL MODEL CLASS
    // ---------------------------------------------------------
    private static class StorageFacility {
        String title, type, location, owner, capacity, available, price;
        int vacancyRate;
        String[] crops, facilities;

        StorageFacility(String t, String ty, String l, String o, String c, String a, String p, int v, String[] cr, String[] f) {
            title = t; type = ty; location = l; owner = o; capacity = c; available = a; price = p; vacancyRate = v; crops = cr; facilities = f;
        }

        // Search logic
        boolean matches(String query) {
            return title.toLowerCase().contains(query) ||
                    location.toLowerCase().contains(query) ||
                    owner.toLowerCase().contains(query) ||
                    type.toLowerCase().contains(query);
        }
    }
}