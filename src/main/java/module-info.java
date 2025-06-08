module org.example.jsonschemagui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.example to javafx.fxml;
    exports org.example;
}
