package com.example.demo1.marketfruits;

import com.example.demo1.utils.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MachineryController implements Initializable {

    // --- Sidebar Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery;
    @FXML private Button btnCropPlan, btnMarket, btnAgriNews, btnProfitLoss, btnWeather;
    @FXML private Button btnSoilHealth, btnComments, btnProfile, btnAiHelper, btnVideoEducation, btnFarmWeather, btnAgriAnalysis;

    // --- Main Content Controls ---
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private FlowPane machineryContainer; // This must match fx:id in FXML

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Machinery Rental Page Initialized");

        // 1. Setup Navigation
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // 2. Setup Real-time Search Logic
        if (searchField != null) {
            // Listen for any key press change
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCards());
        }

        // 3. Setup Category Filter Logic
        if (categoryCombo != null) {
            categoryCombo.setOnAction(e -> filterCards());
        }
    }

    /**
     * This method loops through all cards in the FlowPane
     * and Hides/Shows them based on the Search Text and Category.
     */
    private void filterCards() {
        if (machineryContainer == null) return;

        // Get current values
        String searchText = (searchField.getText() != null) ? searchField.getText().toLowerCase().trim() : "";
        String selectedCategory = (categoryCombo.getValue() != null) ? categoryCombo.getValue() : "সব ক্যাটাগরি";

        // Loop through every node (Card) inside the FlowPane
        for (Node node : machineryContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;

                // Extract all text from this card (Title, Location, Category, etc.)
                String cardContent = extractTextFromNode(card).toLowerCase();

                // Check 1: Does it match the category?
                boolean matchesCategory = selectedCategory.equals("সব ক্যাটাগরি") || cardContent.contains(selectedCategory.toLowerCase());

                // Check 2: Does it match the search text?
                boolean matchesSearch = searchText.isEmpty() || cardContent.contains(searchText);

                // Logic: Show if BOTH match, otherwise Hide
                if (matchesCategory && matchesSearch) {
                    card.setVisible(true);
                    card.setManaged(true); // Keeps the space when visible
                } else {
                    card.setVisible(false);
                    card.setManaged(false); // Removes the space (collapses) when hidden
                }
            }
        }
    }

    /**
     * Helper method to get all text inside a Card (VBox) recursively.
     */
    private String extractTextFromNode(Parent node) {
        StringBuilder sb = new StringBuilder();

        // Loop through children to find Labels
        for (Node child : node.getChildrenUnmodifiable()) {
            if (child instanceof Label) {
                sb.append(((Label) child).getText()).append(" ");
            } else if (child instanceof Parent) {
                // Recursive call for nested layouts (HBox inside VBox)
                sb.append(extractTextFromNode((Parent) child));
            }
        }
        return sb.toString();
    }
}