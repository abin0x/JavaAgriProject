module com.example.demo.marketfruits {

    // --- REQUIRED DEPENDENCIES ---
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;


    // --- PACKAGE ACCESS / OPENS ---

    // FXML Access for main package (LoginApplication, LoginController etc.)
    opens com.example.demo1 to javafx.fxml;

    // FXML and GSON access for marketfruits package (LocalManagementController, WorkerRecord etc.)
    // 🛑 নিশ্চিত করুন যে এখানে শুধু কমা (,) আছে, অন্য কোনো ক্যারেক্টার নেই 🛑
    opens com.example.demo1.marketfruits to javafx.fxml, com.google.gson;

    // FXML and GSON access for other packages (like utils) if needed
    // opens com.example.demo1.utils to com.google.gson;


    // --- EXPORTS ---

    exports com.example.demo1;
    exports com.example.demo1.marketfruits;
}