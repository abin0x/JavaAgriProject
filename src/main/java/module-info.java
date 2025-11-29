// src/main/java/com/example/demo1/module-info.java

module com.example.demo.marketfruits {

    // --- REQUIRED DEPENDENCIES ---

    // Core JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Jackson JSON Library (REQUIRED for JsonDbService)
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core; // Jackson Core is often needed explicitly

    // Additional Libraries (from your pom.xml, ensuring all are present)
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;

    // --- PACKAGE ACCESS / EXPORTS (Crucial for FXML) ---

    // 🛑 FXML Controller Access Fix: Open the main package to FXML loader 🛑
    opens com.example.demo1 to javafx.fxml;

    // 🛑 FXML Controller Access Fix: Open the sub-package to FXML loader 🛑
    opens com.example.demo1.marketfruits to javafx.fxml;

    // Export the main package
    exports com.example.demo1;
    // Export the sub-package
    exports com.example.demo1.marketfruits;

}