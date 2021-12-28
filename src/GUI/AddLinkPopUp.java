package GUI;

import Topology.Topology;
import Topology.AddLinkMessages;
import Topology.RouterButton;

import javax.swing.*;
import java.awt.event.*;

public class AddLinkPopUp extends JDialog {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel contentPane;
    private JButton buttonAdd;
    private JButton buttonCancel;
    private JComboBox end1_combo_box;
    private JComboBox end2_combo_box;
    private JTextField warning_text_field;

    // vars and objects
    private Topology topology = Topology.get_topology();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public AddLinkPopUp() {
        // swing
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAdd);

        // appearance settings
        setSize(200, 200);
        setResizable(false);
        setLocation(200, 200);
        warning_text_field.setBorder(BorderFactory.createEmptyBorder());

        // button Add
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });

        // button Add
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

    private void onAdd() {
        // add link
        AddLinkMessages message = topology.add_link(end1_combo_box.getSelectedItem().toString(),
                end2_combo_box.getSelectedItem().toString());

        if (message == AddLinkMessages.same_router_chosen){
            warning_text_field.setText(message.toString());
        }

        // window terminate
        if (message == AddLinkMessages.is_valid){
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void refresh(){
        int int_number;
        String item;
        end1_combo_box.removeAllItems();
        end2_combo_box.removeAllItems();
        for (RouterButton router: topology.get_routers()){
            int_number = router.get_router().get_int_number();
            for (int i = 0; i < int_number; i++){
                item = router.get_router().get_name() + ": " + i;
                end1_combo_box.addItem(item);
                end2_combo_box.addItem(item);
            }
        }
    }
}
