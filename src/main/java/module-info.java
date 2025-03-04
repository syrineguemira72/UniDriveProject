module edu.unidrive {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Add this line to require the Twilio module
    requires twilio;
    requires java.desktop;
    requires mysql.connector.j;
    requires javax.mail.api;

    // Add the ControlsFX module for Notifications
    requires org.controlsfx.controls;

    // Open packages for JavaFX reflection
    opens edu.unidrive.controllers to javafx.fxml;
    opens edu.unidrive.entities to javafx.base;
    opens edu.unidrive to javafx.fxml;

    // Export main packages
    exports edu.unidrive;
    exports edu.unidrive.controllers;
    exports edu.unidrive.entities;
}
