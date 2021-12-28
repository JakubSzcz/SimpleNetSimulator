package GUI;

import Devices.Devices.Router;
import Devices.Link;
import Protocols.Packets.IPv4;
import Topology.Topology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RouterPopUp extends JDialog {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private javax.swing.JPanel JPanel;
    private JTextField header_name;
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

    private JComboBox port_combo_box;
    private JTextField ip_address_config;
    private JTextField mask_config;
    private JButton applyButton;
    private JButton cancelButton;
    private JComboBox comboBox1;
    private JButton applyButton1;
    private JTextArea textArea1;
    private JButton showButton;
    private JButton sendButton;
    // int state
    private JButton int0_state;
    private JButton int1_state;
    private JButton int2_state;
    private JButton int3_state;
    private JButton int4_state;
    private JButton int5_state;
    private JButton int6_state;
    private JButton int7_state;

    // vars and objects
    private Router router;

    // topology
    Topology topology = Topology.get_topology();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterPopUp() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // appearance settings
        setSize(420, 620);
        setLocation(200, 100);
        setResizable(false);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void refresh(){
        // int number
        int int_number = router.get_int_number();

        // int state set
        JButton[] int_state = new JButton[]{int0_state, int1_state, int2_state, int3_state,
        int4_state, int5_state, int6_state, int7_state};

        // ip address set
        JTextField[] ip_address = new JTextField[]{int0_ip_address, int1_ip_address, int2_ip_address,
                int3_ip_address, int4_ip_address, int5_ip_address, int6_ip_address, int7_ip_address};

        // mask set
        JTextField[] mask = new JTextField[]{int0_mask, int1_mask, int2_mask, int3_mask, int4_mask,
        int5_mask, int6_mask, int7_mask};

        // link state
        JTextField[] link_state = new JTextField[]{int0_link_state, int1_link_state, int2_link_state,
        int3_link_state, int4_link_state, int5_link_state, int6_link_state, int7_link_state};

        // set header
        header_name.setText(router.get_name());

        // set ports combo box
        for (int i = 0; i < int_number; i++){
            port_combo_box.addItem(i);
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
                mask_string = IPv4.parse_to_string(router.get_interface(i).get_ip_address().get("mask"));
            }
            ip_address[i].setText(ip_string);
            mask[i].setText(mask_string);
        }

        // link state
        for (int i = 0; i < int_number; i++){
            for (Link link: topology.get_links()){

            }
        }
    }

    public void set_router(Router router){
        this.router = router;
    }
}
