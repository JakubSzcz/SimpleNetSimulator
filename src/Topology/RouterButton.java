package Topology;

import Devices.Devices.Router;
import GUI.RouterPopUp;

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

    // popup
    private final RouterPopUp router_pop_up;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterButton(Icon icon, Position position, Router router, RouterPopUp router_pop_up) {
        super(router.get_name(), icon);
        this.position = position;
        this.router = router;
        this.router_pop_up = router_pop_up;
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(Color.WHITE);
        setFocusPainted(false);


        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(router.get_name());
                router_pop_up.set_router(router);
                router_pop_up.refresh();
                router_pop_up.setVisible(true);
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
