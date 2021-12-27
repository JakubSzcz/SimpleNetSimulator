package Topology;

import Devices.Devices.Router;
import Icons.Icons;

import javax.swing.*;

public class RouterButton extends JButton {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // position on map
    Position position;

    // actual router
    Router router;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterButton(Position position, Router router) {
        super(router.get_name(), Icons.icon.test());
        this.position = position;
        this.router = router;
    }
}
