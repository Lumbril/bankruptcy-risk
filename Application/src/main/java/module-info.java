module com.example.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.ooxml.schemas;
    requires lombok;

    opens com.example.application to javafx.fxml;
    exports com.example.application;
    exports com.example.application.controllers;
    opens com.example.application.controllers to javafx.fxml;
}