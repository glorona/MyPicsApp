module ComponentesApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens ComponentesApp to javafx.fxml;
    exports ComponentesApp;
}
