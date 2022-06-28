module glorona.eddproyectog4 {
    requires javafx.controls;
    requires javafx.fxml;

    opens glorona.eddproyectog4 to javafx.fxml;
    exports ComponentesApp;
}
