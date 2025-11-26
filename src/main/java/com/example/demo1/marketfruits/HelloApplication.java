package com.example.demo1.marketfruits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // FXML ফাইল লোড করুন
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/example/demo1/fxml/dashboard.fxml")
        );

        if (fxmlLoader.getLocation() == null) {
            System.out.println("FXML ফাইল পাওয়া যায়নি! পাথ চেক করুন।");
            return;
        }

        // Scene তৈরি করুন - প্রাথমিক আকার সেট করুন
        Scene scene = new Scene(fxmlLoader.load());

        // CSS লোড করুন
        String css = getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.out.println("CSS ফাইল পাওয়া যায়নি!");
        }

        // Stage কনফিগার করুন
        stage.setTitle("কৃষি সাখী - Krishi Sakhi");
        stage.setScene(scene);

        // স্টেজের আকার সেট করুন (কিন্তু Scene এর নয়!)
        stage.setWidth(1400);
        stage.setHeight(900);

        // ব্যবহারকারী স্টেজ রিসাইজ করতে পারবে
        stage.setResizable(true);

        // মিনিমাম আকার সেট করুন
        stage.setMinWidth(1200);
        stage.setMinHeight(700);

        // স্টেজ সেন্টার করুন
        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}