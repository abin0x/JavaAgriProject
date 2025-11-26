module com.example.marketfruits {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;

    // Gson লাইব্রেরি
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // FXML এবং Gson উভয়ের জন্য প্যাকেজটি ওপেন করা হলো
    opens com.example.marketfruits to javafx.fxml, com.google.gson;

    exports com.example.marketfruits;
}