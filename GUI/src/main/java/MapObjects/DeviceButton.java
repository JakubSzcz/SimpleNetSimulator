package MapObjects;

import PopUps.AddDeviceController;
import PopUps.DeviceController;
import Topology.NetworkDevicesTypes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DeviceButton extends Button {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    public static Image ROUTER_IMAGE = new Image("file:GUI/src/main/resources/img/router.png");

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public DeviceButton(String name, double pos_x, double pos_y, NetworkDevicesTypes type){
        // name
        setText(name);

        // position
        setLayoutX(pos_x);
        setLayoutY(pos_y);

        // appearance
        setBorder(Border.EMPTY);
        setBackground(Background.EMPTY);

        // image
        switch (type){
            case ROUTER -> setGraphic(new ImageView(ROUTER_IMAGE));
        }

        // on click
        setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(AddDeviceController.class.getResource("device-pop-up.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DeviceController.device_name = name;
            Stage stage = new Stage();
            stage.setTitle(name);
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        });

        // on hover
        setOnMouseEntered(event ->{
            setCursor(Cursor.HAND);
        });
    }
}
