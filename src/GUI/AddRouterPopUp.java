package GUI;

import Topology.Topology;
import Topology.AddRouterMessages;
import Topology.Position;

import javax.swing.*;
import java.awt.event.*;

public class AddRouterPopUp extends JDialog {
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

    // constructor
    public AddRouterPopUp() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAdd);
        setLocation(200, 200);

        // appearance settings
        setSize(400, 150);
        setResizable(false);
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
            AddRouterMessages message = topology.add_router(new Position(mouse_position.get_x(), mouse_position.get_y()),
                    name, int_number);
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
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
