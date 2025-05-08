module org.example.unidriva {
    requires javafx.fxml;
    requires stripe.java;
    requires jjwt.api;
    requires com.google.gson;
    requires java.sql;
    requires twilio;
    requires jbcrypt;
    requires org.controlsfx.controls;
    requires spring.web;
    requires cloudinary.core;
    requires unirest.java;
    requires jakarta.mail;
    requires javafx.web;
    requires org.apache.pdfbox;
    requires java.net.http;
    requires java.desktop;
    opens edu.unidrive.entities to javafx.base,javafx.fxml;

    opens edu.unidrive.controllers to javafx.fxml, javafx.graphics;
    exports edu.unidrive.controllers;

}