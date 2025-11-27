package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper; // Import your Helper
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
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement;
    @FXML private TextField searchField;
    @FXML private Button searchBtn, loadMoreBtn;
    @FXML private GridPane warehouseGrid;

    // --- Data ---
    private List<StorageFacility> allFacilities = new ArrayList<>();
    private List<StorageFacility> filteredFacilities = new ArrayList<>();
    private int displayCount = 3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Navigation (1 Line)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement);

        // 2. Initialize Logic
        loadData();
        setupActions();
        renderGrid();
    }

    // ---------------------------------------------------------
    // 1. DATA & LOGIC
    // ---------------------------------------------------------
    private void loadData() {
        allFacilities.addAll(Arrays.asList(
                new StorageFacility("কৃষি ভান্ডার ও গুদাম", "গুদাম", "নরসিংদী, শিবপুর", "মোঃ সালাম উদ্দিন", "৫০০", "৩২০", "৮৫০", 64, new String[]{"ধান", "গম", "+১"}, new String[]{"ওজন মাপা"}),
                new StorageFacility("আধুনিক শীতাতপ হিমাগার", "হিমাগার", "বগুড়া, শেরপুর", "আবু তাহের", "১০০০", "৬৫০", "৮০০", 65, new String[]{"আলু", "পেঁয়াজ", "+২"}, new String[]{"তাপমাত্রা নিয়ন্ত্রণ"}),
                new StorageFacility("রহমান কৃষি গুদাম", "গুদাম", "কুমিল্লা, চৌদ্দগ্রাম", "মোঃ রহমান", "৫০০", "২০০", "১৫০", 40, new String[]{"ধান", "ভুট্টা", "+১"}, new String[]{"লোডিং-আনলোডিং"}),
                new StorageFacility("সবুজ কৃষি হিমাগার", "হিমাগার", "দিনাজপুর, বিরামপুর", "মো. জাহাঙ্গীর", "৮০০", "৪৮০", "৩৫০", 72, new String[]{"আলু", "টমেটো", "+২"}, new String[]{"কোল্ড চেইন"}),
                new StorageFacility("মডার্ন এগ্রো স্টোরেজ", "গুদাম", "রাজশাহী, গোদাগাড়ী", "আব্দুল করিম", "৬০০", "৪২০", "২২০", 58, new String[]{"ধান", "সরিষা", "+১"}, new String[]{"শুষ্কীকরণ যন্ত্র"}),
                new StorageFacility("চট্টগ্রাম সেন্ট্রাল স্টোরেজ", "হিমাগার", "চট্টগ্রাম, হাটহাজারী", "মো. শফিকুল", "১২০০", "৮৫০", "৪৫০", 68, new String[]{"আলু", "রসুন", "+৩"}, new String[]{"অটো তাপমাত্রা"})
        ));
        filteredFacilities.addAll(allFacilities);
    }

    private void setupActions() {
        if (searchBtn != null) searchBtn.setOnAction(e -> filterData());
        if (searchField != null) searchField.setOnAction(e -> filterData());
        if (loadMoreBtn != null) loadMoreBtn.setOnAction(e -> {
            displayCount += 3;
            renderGrid();
        });
    }

    private void filterData() {
        String query = searchField.getText().toLowerCase().trim();
        filteredFacilities = allFacilities.stream()
                .filter(f -> query.isEmpty() || f.matches(query))
                .collect(Collectors.toList());
        displayCount = 3; // Reset view on search
        renderGrid();
    }

    // ---------------------------------------------------------
    // 2. UI RENDERING
    // ---------------------------------------------------------
    private void renderGrid() {
        if (warehouseGrid == null) return;
        warehouseGrid.getChildren().clear();

        int limit = Math.min(displayCount, filteredFacilities.size());
        for (int i = 0; i < limit; i++) {
            warehouseGrid.add(createCard(filteredFacilities.get(i)), i % 3, i / 3);
        }

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
        title.setWrapText(true); title.setMaxWidth(180);

        Label badge = new Label("✔ " + f.vacancyRate + "% ফাঁকা");
        badge.getStyleClass().add("badge-black");

        HBox header = new HBox(10, title, new Region(), badge);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        // Details
        VBox details = new VBox(10,
                createRow("📍", f.location),
                createRow("👤", f.owner),
                createRow("📦", f.available + "/" + f.capacity + " টন"),
                createRow("💰", "৳ " + f.price + "/টন")
        );
        details.getStyleClass().add("details-box");

        // Tags
        HBox crops = new HBox(5);
        Arrays.stream(f.crops).forEach(c -> crops.getChildren().add(createTag(c, "tag-white")));

        HBox facilities = new HBox(5);
        Arrays.stream(f.facilities).forEach(fac -> facilities.getChildren().add(createTag(fac, "tag-facility")));

        // Button
        Button contactBtn = new Button("📞 যোগাযোগ করুন");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);
        contactBtn.setOnAction(e -> showAlert(f));

        card.getChildren().addAll(header, new Label(f.type), details, new Label("ফসল:"), crops, new Label("সুবিধা:"), facilities, contactBtn);
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
        alert.setTitle("Contact");
        alert.setHeaderText(f.title);
        alert.setContentText("Owner: " + f.owner + "\nLocation: " + f.location + "\nMobile: 01712-XXXXXX");
        alert.show();
    }

    // ---------------------------------------------------------
    // INTERNAL MODEL
    // ---------------------------------------------------------
    private static class StorageFacility {
        String title, type, location, owner, capacity, available, price;
        int vacancyRate;
        String[] crops, facilities;

        StorageFacility(String t, String ty, String l, String o, String c, String a, String p, int v, String[] cr, String[] f) {
            title = t; type = ty; location = l; owner = o; capacity = c; available = a; price = p; vacancyRate = v; crops = cr; facilities = f;
        }

        boolean matches(String query) {
            return title.toLowerCase().contains(query) || location.toLowerCase().contains(query) ||
                    owner.toLowerCase().contains(query) || type.toLowerCase().contains(query);
        }
    }
}