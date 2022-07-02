module ComponentesApp {
    requires javafx.controls;
    requires javafx.fxml;

    opens ComponentesApp to javafx.fxml;
    exports ComponentesApp;
}
