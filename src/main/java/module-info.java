module com.example.laborator5 {
    //requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires javafx.graphics;
    requires annotations;
    requires javafx.base;
    requires java.rmi;


    opens com.example.laborator5 to javafx.fxml;
    exports com.example.laborator5;
    opens com.example.laborator5.GUI to javafx.fxml;
    opens com.example.laborator5.Domain to javafx.base;
}