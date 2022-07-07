module ComponentesApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires javafx.graphicsEmpty;
    requires java.base;
    requires org.controlsfx.controls;

    opens ComponentesApp to javafx.fxml;
    exports ComponentesApp;
}
