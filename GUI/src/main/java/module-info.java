module GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires engine;

    opens main to javafx.fxml;
    opens PopUps to javafx.fxml;
    exports main;
    exports PopUps;
}