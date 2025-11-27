module com.example.demo1.marketfruits {

    // --- REQUIRED DEPENDENCIES ---
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.swing;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;



    // 1. Export the main package where the Application Launcher is located.
    exports com.example.demo1;

    // 2. Open the main package for FXML (LoginController, RegisterController).
    opens com.example.demo1 to javafx.fxml;

    // এখানে demo1 যুক্ত করতে হবে
    opens com.example.demo1.marketfruits to javafx.fxml, com.google.gson;

    // 4. Export the sub-package to make its public classes available.
    exports com.example.demo1.marketfruits;
    exports com.example.demo1.utils;
    opens com.example.demo1.utils to javafx.fxml;


}