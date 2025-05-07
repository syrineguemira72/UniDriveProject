module org.example.unidriva {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.unidriva to javafx.fxml;
    exports org.example.unidriva;
}