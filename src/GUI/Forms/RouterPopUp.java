package GUI.Forms;

import Devices.Devices.Router;
import Devices.Link;
import Devices.Routing.Route;
import Devices.Routing.RouteCode;
import Protocols.Data.ICMP;
import Protocols.Packets.IPv4;
import Protocols.Packets.IPv4MessageTypes;
import GUI.Topology.Topology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// item for delete route combo box
class RouteItem{
    public Route route;

    @Override
    public String toString(){
        return route.to_string();
    }

}

public class RouterPopUp extends JDialog implements Runnable{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private javax.swing.JPanel JPanel;
    private JTextField header_name;
    private JComboBox port_combo_box;
    private JTextField ip_address_config;
    private JTextField mask_config;
    private JButton ip_address_apply;
    private JButton ip_address_delete;
    private JComboBox delete_route_combo_box;
    private JButton delete_route_button;
    private JTextArea monitor_text_area;
    private JButton show_routing_table_button;
    private JButton send_ping_button;
    // ip address
    private JTextField int0_ip_address;
    private JTextField int1_ip_address;
    private JTextField int2_ip_address;
    private JTextField int3_ip_address;
    private JTextField int4_ip_address;
    private JTextField int5_ip_address;
    private JTextField int6_ip_address;
    private JTextField int7_ip_address;
    // mask
    private JTextField int0_mask;
    private JTextField int1_mask;
    private JTextField int2_mask;
    private JTextField int3_mask;
    private JTextField int4_mask;
    private JTextField int5_mask;
    private JTextField int6_mask;
    private JTextField int7_mask;
    // link state
    private JTextField int0_link_state;
    private JTextField int1_link_state;
    private JTextField int2_link_state;
    private JTextField int3_link_state;
    private JTextField int4_link_state;
    private JTextField int5_link_state;
    private JTextField int6_link_state;
    private JTextField int7_link_state;
    // int state
    private JButton int0_state;
    private JButton int1_state;
    private JButton int2_state;
    private JButton int3_state;
    private JButton int4_state;
    private JButton int5_state;
    private JButton int6_state;
    private JButton int7_state;
    // int panels
    private JPanel int0_panel;
    private JPanel int1_panel;
    private JPanel int2_panel;
    private JPanel int3_panel;
    private JPanel int4_panel;
    private JPanel int5_panel;
    private JPanel int6_panel;
    private JPanel int7_panel;
    private JButton route_add;
    private JTextField ip_address_route_add;
    private JTextField mask_route_add;
    private JComboBox add_route_combo_box;
    private JScrollPane scroll;
    private JTextField ip_address_ping;
    private JButton clear_monitor_button;

    // int state set
    JButton[] int_state = new JButton[]{int0_state, int1_state, int2_state, int3_state,
            int4_state, int5_state, int6_state, int7_state};

    // vars and objects
    private Router router;

    // topology
    Topology topology = Topology.get_topology();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterPopUp() {
        // swing
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // appearance settings
        setLocation(200, 100);
        setResizable(false);
        header_name.setBorder(BorderFactory.createEmptyBorder());

        // start thread, for links drawing
        Thread t = new Thread(this);
        t.start();

        // button ok
        buttonOK.addActionListener(e -> onOK());

        // button cancel
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // up/down interface 0
        int0_state.addActionListener(e -> up_down_interface(0));

        // up/down interface 1
        int1_state.addActionListener(e -> up_down_interface(1));

        // up/down interface 2
        int2_state.addActionListener(e -> up_down_interface(2));

        // up/down interface 3
        int3_state.addActionListener(e -> up_down_interface(3));

        // up/down interface 4
        int4_state.addActionListener(e -> up_down_interface(4));

        // up/down interface 5
        int5_state.addActionListener(e -> up_down_interface(5));

        // up/down interface 6
        int6_state.addActionListener(e -> up_down_interface(6));

        // up/down interface 7
        int7_state.addActionListener(e -> up_down_interface(7));

        // ip address apply
        ip_address_apply.addActionListener(e -> {
            // get interface
            String int_number_string = port_combo_box.getSelectedItem().toString();
            int int_number = Integer.parseInt(int_number_string);

            // get ip and mask
            String ip_address = ip_address_config.getText();
            String mask = mask_config.getText();

            // valid ip and mask
            IPv4MessageTypes ip_message = IPv4.is_ip_valid(ip_address);
            IPv4MessageTypes mask_message = IPv4.is_mask_valid(mask);

            // if valid apply
            if (ip_message == IPv4MessageTypes.is_valid && mask_message == IPv4MessageTypes.is_valid){
                router.set_interface_ip(int_number, IPv4.parse_to_long(ip_address),
                        IPv4.parse_mask_to_long(mask));
                refresh();
            }else{
                // if not valid add message to router monitor
                if (ip_message != IPv4MessageTypes.is_valid){
                    router.add_line_to_monitor("Wrong ip address");
                }
                if (mask_message != IPv4MessageTypes.is_valid){
                    router.add_line_to_monitor("Wrong mask");
                }
            }
        });

        // delete ip
        ip_address_delete.addActionListener(e -> {
            // get interface
            String int_number_string = port_combo_box.getSelectedItem().toString();
            int int_number = Integer.parseInt(int_number_string);
            router.set_interface_ip(int_number, -1, -1);
            refresh();
        });

        // route add
        route_add.addActionListener(e -> {
            // get interface
            String int_number_string = add_route_combo_box.getSelectedItem().toString();
            int int_number = Integer.parseInt(int_number_string);

            // get ip and mask
            String ip_address = ip_address_route_add.getText();
            String mask = mask_route_add.getText();

            // valid ip and mask
            IPv4MessageTypes ip_message = IPv4.is_ip_valid(ip_address);
            IPv4MessageTypes mask_message = IPv4.is_mask_valid(mask);

            if (ip_message == IPv4MessageTypes.is_valid && mask_message == IPv4MessageTypes.is_valid){
                System.out.println("true");
                router.add_static_route(IPv4.parse_to_long(ip_address),IPv4.parse_mask_to_long(mask), int_number);
                refresh();
            }else{
                if (ip_message != IPv4MessageTypes.is_valid){
                    router.add_line_to_monitor("Wrong ip address");
                }
                if (mask_message != IPv4MessageTypes.is_valid){
                    router.add_line_to_monitor("Wrong mask");
                }

            }
        });

        // delete route button
        delete_route_button.addActionListener(e -> {
            RouteItem route_item = (RouteItem) delete_route_combo_box.getSelectedItem();
            router.delete_route(route_item.route);
            refresh();
        });

        // show routing table button
        show_routing_table_button.addActionListener(e -> {
            router.add_line_to_monitor("Router routing table:");
            router.add_line_to_monitor(router.get_routing_table_string());
        });

        // ping button
        send_ping_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get ip and mask
                String ip_address = ip_address_ping.getText();

                // valid ip and mask
                IPv4MessageTypes ip_message = IPv4.is_ip_valid(ip_address);

                // if is valid
                if (ip_message == IPv4MessageTypes.is_valid){
                    router.add_line_to_monitor("Pinging with 32 bytes of data:");
                    for (int i = 0; i < 4; i++){
                        router.send_data(ICMP.create_echo_request(), IPv4.parse_to_long(ip_address));
                    }
                }else{
                    if (ip_message != IPv4MessageTypes.is_valid){
                        router.add_line_to_monitor("Wrong ip address");
                    }
                }
            }
        });
        // clear monitor button
        clear_monitor_button.addActionListener(e -> {
            router.clear_monitor();
        });
    }

    // actions after clicking up down button on interfaces
    private void up_down_interface(int int_number){
        if (router.get_interface(int_number).is_up()){
            router.down_interface(int_number);
        }else{
            router.up_interface(int_number);
        }
        refresh();
    }

    // after clicking OK button
    private void onOK() {
        // add your code here
        dispose();
    }

    // after clicking Cancel button
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // refresh window content
    public void refresh(){
        // int number
        int int_number = router.get_int_number();

        // set window size
        int width = 450;
        int height = 550 + 25 * (int_number - 1);
        setSize(width, height);

        // clear fields
        ip_address_route_add.setText("");
        mask_route_add.setText("");
        ip_address_config.setText("");
        mask_config.setText("");
        ip_address_ping.setText("");

        // ip address set
        JTextField[] ip_address = new JTextField[]{int0_ip_address, int1_ip_address, int2_ip_address,
                int3_ip_address, int4_ip_address, int5_ip_address, int6_ip_address, int7_ip_address};

        // mask set
        JTextField[] mask = new JTextField[]{int0_mask, int1_mask, int2_mask, int3_mask, int4_mask,
        int5_mask, int6_mask, int7_mask};

        // link state set
        JTextField[] link_state = new JTextField[]{int0_link_state, int1_link_state, int2_link_state,
        int3_link_state, int4_link_state, int5_link_state, int6_link_state, int7_link_state};

        // int panel set
        JPanel[] panel = new JPanel[]{int0_panel, int1_panel, int2_panel, int3_panel,
        int4_panel, int5_panel, int6_panel, int7_panel};

        // set header
        header_name.setText(router.get_name());

        // set ports combo box
        port_combo_box.removeAllItems();
        add_route_combo_box.removeAllItems();
        for (int i = 0; i < int_number; i++){
            port_combo_box.addItem(i);
            add_route_combo_box.addItem(i);
        }

        // set delete route combo box content
        RouteItem item;
        delete_route_combo_box.removeAllItems();
        for (int i = 0; i < router.get_routing_table_size(); i++){
            Route route = router.get_route(i);
            if (route.code() == RouteCode.S){
                item = new RouteItem();
                item.route = route;
                delete_route_combo_box.addItem(item);
            }
        }

        // set ip int states
        for (int i = 0; i < int_number; i++){
            if(router.get_interface(i).is_up()){
                int_state[i].setBackground(Color.green);
            }else{
                int_state[i].setBackground(Color.red);
            }
        }

        // set ip addresses and masks
        for (int i = 0; i < int_number; i++){
            String ip_string = "";
            String mask_string = "";
            if (router.get_interface(i).get_ip_address().get("address") != -1){
                ip_string = IPv4.parse_to_string(router.get_interface(i).get_ip_address().get("address"));
                mask_string = IPv4.parse_mask_to_string_short(router.get_interface(i).get_ip_address().get("mask"));
            }
            ip_address[i].setText(ip_string);
            mask[i].setText(mask_string);
        }

        // link state
        for (int i = 0; i < int_number; i++){
            link_state[i].setText("unconnected");
            for (int j = 0; j < topology.get_flinks().size(); j++){
                if (topology.get_flinks().get(j).get_link().get_end1() == router.get_interface(i)){
                    link_state[i].setText("link " + topology.get_flinks().get(j).get_name());
                    break;
                }else if (topology.get_flinks().get(j).get_link().get_end2() == router.get_interface(i)){
                    link_state[i].setText("link " + topology.get_flinks().get(j).get_name());
                    break;
                }
            }
        }

        // delete ports
        for (int i = 0; i < topology.get_max_int_number(); i++){
            panel[i].setVisible(i < int_number);
        }
    }

    // router setter
    public void set_router(Router router){
        this.router = router;
    }

    // Thread for monitor refreshing
    @Override
    public void run() {
        while (true){
            if (router != null){
                String old_text = monitor_text_area.getText();
                if (!old_text.trim().equals(router.get_monitor().trim())){
                    scroll.getVerticalScrollBar().setValue(monitor_text_area.getHeight());
                }
                monitor_text_area.setText(router.get_monitor());
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
