module com.felix_71241153.app.haphaphap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens com.felix_71241153.app.haphaphap to javafx.fxml;

    // TAMBAHKAN BARIS INI:
    opens controller to javafx.fxml;

    exports com.felix_71241153.app.haphaphap;
    exports controller;
    exports model;
    exports database;
    exports util;
}