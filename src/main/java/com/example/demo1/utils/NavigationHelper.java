package com.example.demo1.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class NavigationHelper {

    // --- 1. সব পেজের পাথ কনস্ট্যান্ট (এক জায়গায় সব পাথ) ---
    private static final String HOME_FXML = "/com/example/demo1/fxml/dashboard.fxml";

    // Advisory & Calculators
    private static final String ADVISORY_FXML = "/com/example/demo1/fxml/CropAdvisory.fxml";
    private static final String ADVISORY_CSS = "CropAdvisory.css";

    private static final String FERTILIZER_FXML = "/com/example/demo1/fxml/FertilizerCalculator.fxml";
    private static final String FERTILIZER_CSS = "FertilizerCalculator.css";

    private static final String IRRIGATION_FXML = "/com/example/demo1/fxml/IrrigationCalculator.fxml";
    private static final String IRRIGATION_CSS = "IrrigationCalculator.css";

    private static final String ROTATION_FXML = "/com/example/demo1/fxml/CropRotation.fxml";
    // CropRotation এর যদি আলাদা CSS না থাকে, তবে null পাঠানো হবে

    // Storage & Local
    private static final String STORAGE_FXML = "/com/example/demo1/fxml/WarehouseView.fxml";
    private static final String LOCAL_FXML = "/com/example/demo1/fxml/LocalManagement.fxml";
    private static final String LOCAL_CSS = "LocalManagement.css";


    // --- 2. সাইডবার সেটআপ মেথড ---
    public static void setupSidebar(Button btnHome, Button btnAdvisory, Button btnStorage, Button btnLocal) {
        assignAction(btnHome, HOME_FXML, null);
        assignAction(btnAdvisory, ADVISORY_FXML, ADVISORY_CSS);
        assignAction(btnStorage, STORAGE_FXML, null);
        assignAction(btnLocal, LOCAL_FXML, LOCAL_CSS);
    }

    // --- 3. নতুন: টপ ন্যাভিগেশন বার (Guide, Fertilizer, Irrigation, Rotation) সেটআপ মেথড ---
    public static void setupAdvisoryNav(Button btnGuide, Button btnFertilizer, Button btnIrrigation, Button btnRotation) {
        assignAction(btnGuide, ADVISORY_FXML, ADVISORY_CSS);
        assignAction(btnFertilizer, FERTILIZER_FXML, FERTILIZER_CSS);
        assignAction(btnIrrigation, IRRIGATION_FXML, IRRIGATION_CSS);
        assignAction(btnRotation, ROTATION_FXML, null);
    }

    // --- 4. প্রাইভেট মেথড বাটন সেট করার জন্য ---
    private static void assignAction(Button btn, String fxmlPath, String cssName) {
        if (btn != null) {
            btn.setOnAction(event -> switchScene(event, fxmlPath, cssName));
        }
    }

    // --- 5. সিন সুইচ করার মেইন লজিক ---
    public static void switchScene(ActionEvent event, String fxmlPath, String cssName) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // FXML লোড করা
            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // গ্লোবাল ড্যাশবোর্ড CSS লোড করা (সব পেজের জন্য কমন)
            URL globalCssUrl = NavigationHelper.class.getResource("/com/example/demo1/css/dashboard.css");
            if (globalCssUrl != null) {
                scene.getStylesheets().add(globalCssUrl.toExternalForm());
            }

            // পেজ স্পেসিফিক CSS লোড করা (যদি থাকে)
            if (cssName != null) {
                URL pageCssUrl = NavigationHelper.class.getResource("/com/example/demo1/css/" + cssName);
                if (pageCssUrl != null) {
                    scene.getStylesheets().add(pageCssUrl.toExternalForm());
                }
            }

            stage.setScene(scene);
            stage.show();

        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading page: " + fxmlPath);
            e.printStackTrace();
        }
    }
}