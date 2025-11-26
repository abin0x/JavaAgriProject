package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // 🛑 FIX 1: Use the correct path relative to the class path root 🛑
        // The path must start with a leading slash '/' to be absolute from the resources folder.
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/example/demo1/fxml/hello-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 🛑 FIX 2: Use the correct path for CSS 🛑
        // Start with '/' and ensure the path matches your resources structure (image_9f4ac2.png).
        scene.getStylesheets().add(
                getClass().getResource("/com/example/demo1/css/dashboard.css").toExternalForm() // Assuming your CSS is dashboard.css in the css folder
                // OR use this if your main login style is 'style.css' in the parent folder:
                // getClass().getResource("/com/example/demo1/style.css").toExternalForm()
        );

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}