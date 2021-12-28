package GUI;

import Devices.Devices.Router;

import javax.swing.*;
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
    // interface state
    private JPanel int0_state;
    private JPanel int1_state;
    private JPanel int2_state;
    private JPanel int3_state;
    private JPanel int4_state;
    private JPanel int5_state;
    private JPanel int6_state;
    private JPanel int7_state;
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

    // vars and objects
    private Router router;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public RouterPopUp() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // appearance settings
        setSize(500, 630);
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
        // set header
        header_name.setText(router.get_name());

        // set ports combo box
        int int_number = router.get_int_number();
        for (int i = 0; i < int_number; i++){
            port_combo_box.addItem(i);
        }
    }

    public void set_router(Router router){
        this.router = router;
    }
}
