package Topology;

import Devices.Devices.Router;
import Icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RouterButton extends JButton {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // position on map
    private final Position position;

    // actual router
    private final Router router;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterButton(Icon icon, Position position, Router router) {
        super(router.get_name(), icon);
        // super(router.get_name());
        this.position = position;
        this.router = router;
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(Color.WHITE);
        setFocusPainted(false);


        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(router.get_name());
            }
        });
    }

    // position getter
    public Position get_position() {
        return position;
    }

    // router getter
    public Router get_router() {
        return router;
    }
}
