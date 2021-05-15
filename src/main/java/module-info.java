module com.udg {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.udg to javafx.fxml;
    exports com.udg;
    exports com.udg.controller;
    exports  com.udg.model;
}