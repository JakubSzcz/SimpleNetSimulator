package GUI;

import Devices.Devices.Router;
import Topology.Topology;
import Topology.Position;
import Topology.RouterButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NetworkGUI {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel gui_panel;
    private JButton add_link;
    private JButton delete_link;
    private JButton add_router;
    private JButton delete_router;
    private JButton save_topology;
    private JButton load_topology;
    private JPanel buttons;
    private JPanel down_margin;
    private JPanel left_margin;
    private JPanel right_margin;
    private JPanel topology_map;

    // popups
    private final AddRouterPopUp add_router_pop_up;

    // vars and objects
    private boolean flag;
    private final Topology topology = Topology.get_topology();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public NetworkGUI() {
        // appearance
        this.add_router.setBorder(BorderFactory.createEmptyBorder());

        // popups
        this.add_router_pop_up = new AddRouterPopUp();

        // vars
        this.flag = false;

        //                      listeners                      //
        // add router button
        add_router.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = true;
            }
        });

        // map click
        topology_map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(flag){
                    add_router_pop_up.set_mouse_position(new Position(e.getX(), e.getY()));
                    add_router_pop_up.setVisible(true);
                    flag = false;
                }

            }
        });
    }

    // refresh
    public void refresh(){
        for(RouterButton router: topology.get_routers())
        topology_map.add(router);
    }

    // main
    public static void main(String[] args) {
        JFrame frame = new JFrame("Network Topology");
        frame.setContentPane(new NetworkGUI().gui_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}