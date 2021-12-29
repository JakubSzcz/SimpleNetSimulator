package GUI.Forms;

import GUI.Topology.Topology;
import GUI.Topology.AddRouterMessages;
import GUI.Topology.Position;

import javax.swing.*;
import java.awt.event.*;

public class AddRouterPopUp extends JDialog {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // form objects
    private JPanel contentPane;
    private JButton buttonAdd;
    private JButton buttonCancel;
    private JTextField name_text_field;
    private JTextField int_number_text_field;
    private JTextField warning_text_field;

    // topology
    Topology topology = Topology.get_topology();
    private Position mouse_position;

    // topology panel
    JPanel panel;

    // popup for routers
    RouterPopUp router_pop_up;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public AddRouterPopUp(JPanel panel, RouterPopUp router_pop_up) {
        this.router_pop_up = router_pop_up;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAdd);
        this.panel = panel;

        // appearance settings
        setSize(400, 150);
        setResizable(false);
        setLocation(200, 200);
        warning_text_field.setBorder(BorderFactory.createEmptyBorder());

        buttonAdd.addActionListener(new ActionListener() {
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
    // set mouse position

    public void set_mouse_position(Position position){
        mouse_position = position;
    }

    private void onOK() {
        // add router
        boolean is_valid = true;
        // name
        String name = name_text_field.getText();
        // int number
        String string_int_number = int_number_text_field.getText();
        int int_number;
        // check if int number is integer
        try{
            int_number = Integer.parseInt(string_int_number);
            float index_x = (float)mouse_position.get_x() / panel.getWidth() * 20;
            float index_y = (float)mouse_position.get_y() / panel.getHeight() * 20;
            AddRouterMessages message = topology.add_router(
                    new Position((int)index_x, (int)index_y),name, int_number, router_pop_up);
            if (message == AddRouterMessages.name_is_taken){
                is_valid = false;
                warning_text_field.setText(message.toString());
            }else if (message == AddRouterMessages.too_many_interfaces){
                is_valid = false;
                warning_text_field.setText(message.toString());
            }else if (message == AddRouterMessages.too_less_interfaces){
                is_valid = false;
                warning_text_field.setText(message.toString());
            }else if (message == AddRouterMessages.too_short_name){
                is_valid = false;
                warning_text_field.setText(message.toString());
            }else if (message == AddRouterMessages.too_long_name){
                is_valid = false;
                warning_text_field.setText(message.toString());
            }
        }catch (NumberFormatException e){
            is_valid = false;
            warning_text_field.setText("Number of interfaces must be integer");
        }

        // window terminate
        if (is_valid){
            topology.refresh(panel);
            name_text_field.setText("");
            int_number_text_field.setText("");
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
