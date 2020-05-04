module com.udg {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.udg to javafx.fxml;
    opens com.udg.model to com.google.gson;
    exports com.udg;
    exports com.udg.controller;
    exports  com.udg.model;
}