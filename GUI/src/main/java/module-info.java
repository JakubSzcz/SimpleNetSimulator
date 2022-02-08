module GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires engine;

    opens main to javafx.fxml;
    exports main;
}