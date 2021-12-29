package GUI.Forms;

import Devices.Link;
import GUI.Topology.Topology;
import GUI.Topology.AddLinkMessages;
import GUI.Topology.RouterButton;

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
    private final Topology topology = Topology.get_topology();

    // panel
    JPanel panel;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public AddLinkPopUp(JPanel panel) {
        // swing
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAdd);

        // initialization
        this.panel = panel;

        // appearance settings
        setSize(200, 200);
        setResizable(false);
        setLocation(200, 200);
        warning_text_field.setBorder(BorderFactory.createEmptyBorder());

        // button Add
        buttonAdd.addActionListener(e -> {
            if(topology.get_routers().size() > 1 ) {
                onAdd();
            }
        });

        // button Add
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onAdd() {
        // add link, selected item = [name: int_number]
        AddLinkMessages message = topology.add_link(end1_combo_box.getSelectedItem().toString(),
                end2_combo_box.getSelectedItem().toString());

        // if same router was chosen
        if (message == AddLinkMessages.same_router_chosen){
            warning_text_field.setText("You must choose different routers interfaces");
        // if link has already been established
        }else if (message == AddLinkMessages.link_already_established){
            warning_text_field.setText("Link between this routers has already been established");
        // if there is any other error
        }else if (message == AddLinkMessages.error){
            warning_text_field.setText("Error");
        }

        // window terminate
        if (message == AddLinkMessages.is_valid){
            dispose();
        }
    }

    // if cancel button was clicked
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // refresh content of popup
    public void refresh(){
        // interfaces number
        int int_number;

        // item to add
        String item;

        // remove all items from combo boxes
        end1_combo_box.removeAllItems();
        end2_combo_box.removeAllItems();

        for (RouterButton router: topology.get_routers()){
            // number interfaces on router
            int_number = router.get_router().get_int_number();
            for (int i = 0; i < int_number; i++){
                boolean taken = false;

                // check if interfaces of router are connected to any link
                for (Link link : topology.get_links()){
                    if (router.get_router().get_interface(i) == link.get_end1()){
                        taken = true;
                        break;
                    }else if (router.get_router().get_interface(i) == link.get_end2()){
                        taken = true;
                        break;
                    }
                }

                // if interface hasn't been connected to any link, add to combo box
                if (!taken){
                    item = router.get_router().get_name() + ": " + i;
                    end1_combo_box.addItem(item);
                    end2_combo_box.addItem(item);
                }
            }
        }
    }
}
