package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // 🛑 FXML Fix: Use absolute path for hello-view.fxml 🛑
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/example/demo1/fxml/hello-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 🛑 CSS Fix: Use absolute path for style.css 🛑
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/example/demo1/css/style.css")).toExternalForm()
        );

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}