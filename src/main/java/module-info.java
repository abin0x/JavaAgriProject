module com.example.demo1.marketfruits {

    // --- REQUIRED DEPENDENCIES ---

    // Core JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;

    // Gson লাইব্রেরি
    requires com.google.gson;


    // Jackson JSON Library (REQUIRED for JsonDbService, fixes "module not found" error)
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    // Additional Libraries (from your pom.xml)
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all; // Generic module name for fxgl
    // --- PACKAGE ACCESS / EXPORTS ---

    // 1. Export the main package where the Application Launcher is located.
    exports com.example.demo1;

    // 2. Open the main package for FXML (LoginController, RegisterController).
    opens com.example.demo1 to javafx.fxml;

    // 3. Open the sub-package for FXML (DashboardController, fixes IllegalAccessException).
    opens com.example.demo1.marketfruits to javafx.fxml;

    // 4. Export the sub-package to make its public classes available.
    exports com.example.demo1.marketfruits;



    // FXML এবং Gson উভয়ের জন্য প্যাকেজটি ওপেন করা হলো
    opens com.example.marketfruits to javafx.fxml, com.google.gson;

    exports com.example.marketfruits;

}