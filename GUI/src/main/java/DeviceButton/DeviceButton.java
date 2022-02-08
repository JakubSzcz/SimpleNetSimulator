package DeviceButton;

import javafx.scene.control.Button;

public class DeviceButton extends Button {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public DeviceButton(String name, double pos_x, double pos_y){
        setText(name);
        setLayoutX(pos_x);
        setLayoutY(pos_y);
    }
}
