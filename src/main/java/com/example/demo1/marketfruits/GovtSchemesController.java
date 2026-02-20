package com.example.demo1.marketfruits;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import java.net.URL;
import java.util.ResourceBundle;

public class GovtSchemesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("✅ Govt Schemes Page Loaded Successfully.");
    }

    @FXML
    private void handleABBankInfo() {
        showDetails("AB Bank স্মার্ট কার্ড লোন",
                """
                        • এটি মূলত প্রান্তিক ও ভূমিহীন কৃষকদের জন্য।
                        • DAE (কৃষি কার্ড) ও NID বাধ্যতামূলক।
                        • এই ঋণের মাধ্যমে সরাসরি সার ও বীজ কেনা যায়।""");
    }

    @FXML
    private void handleBankAsiaInfo() {
        showDetails("Bank Asia ৪% সুদে ঋণ",
                "• ডাল, তেলবীজ, মশলা ও ভুট্টা চাষের জন্য বিশেষ ৪% সুদে লোন।\n" +
                        "• ফসল কাটার পর এককালীন পরিশোধের সুবিধা।");
    }

    @FXML
    private void handleDhakaBankInfo() {
        showDetails("Dhaka Bank সুফলা লোন",
                "• ঘরে বসে অ্যাকাউন্ট খুলে ৫,০০০-৫০,০০০ টাকা ন্যানো লোন।\n" +
                        "• কোনো জামানত ছাড়াই ক্ষুদ্র ঋণের সুবিধা।");
    }

    @FXML
    private void handleCityBankInfo() {
        showDetails("City Bank ডিজিটাল লোন",
                "• ডেটা ও ক্রপ সাইকেল বিশ্লেষণের মাধ্যমে দ্রুত লোন ডিসবার্সমেন্ট।\n" +
                        "• সরাসরি মোবাইল ব্যাংকিং অ্যাপের মাধ্যমে ঋণের আবেদন সম্ভব।");
    }

    private void showDetails(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("বিস্তারিত তথ্য");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}