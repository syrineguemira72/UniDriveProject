module UniDriveProject {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    opens edu.unidrive.controllers;
    opens edu.unidrive.entities;
     opens edu.unidrive.tools;
    opens edu.unidrive.services;
    opens edu.unidrive.tests;
}