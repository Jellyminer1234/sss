module com.example.sss {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sss to javafx.fxml;
    exports com.example.sss;
}