module org.example.unidriveapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.unidriveapp to javafx.fxml;
    exports org.example.unidriveapp;
}