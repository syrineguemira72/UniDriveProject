module org.example.unidrivefinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.unidrivefinal to javafx.fxml;
    exports org.example.unidrivefinal;
}