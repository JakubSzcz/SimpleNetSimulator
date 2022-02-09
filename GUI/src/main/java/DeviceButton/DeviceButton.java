package DeviceButton;

import Topology.NetworkDevicesTypes;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;

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

        switch (type){
            case ROUTER -> setGraphic(new ImageView(ROUTER_IMAGE));
        }
    }
}
