package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkGUI {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // Elements
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

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public NetworkGUI() {
        // popups
        this.add_router_pop_up = new AddRouterPopUp();

        // buttons appearance
        this.add_router.setBorder(BorderFactory.createEmptyBorder());

        //                     listeners                       //

        // add router button
        add_router.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_router_pop_up.setVisible(true);
            }
        });
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
