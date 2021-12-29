package GUI.Forms;

import GUI.Topology.Topology;
import GUI.Topology.RouterButton;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DeleteRouterPopUp extends JDialog {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel delete_router_content;
    private JButton ok_button;
    private JButton cancel_button;
    private JPanel buttons_big;
    private JPanel buttons_small;
    private JPanel field;
    private JTextField information;
    private JComboBox router_to_delete;

    // topology
    Topology topology = Topology.get_topology();

    // routers list
    private final ArrayList<RouterButton> routers;

    // topology_map
    JPanel panel;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public DeleteRouterPopUp(JPanel panel) {
        // swing
        setContentPane(delete_router_content);
        setModal(true);
        getRootPane().setDefaultButton(ok_button);

        // init
        this.routers = topology.get_routers();
        this.panel = panel;

        // appearance settings
        setSize(400, 150);
        setResizable(false);
        setLocation(200, 200);

        // ok button
        ok_button.addActionListener(e -> {
            if(!routers.isEmpty()) {
                onOK();
            }
        });

        // cancel button
        cancel_button.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        delete_router_content.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // if ok button was clicked
    private void onOK() {
        // deleting router from a list and map
        String to_delete = router_to_delete.getSelectedItem().toString();
        topology.delete_router(to_delete);
        topology.refresh(panel);

        // window terminate
        dispose();
    }

    // if cancel button was clicked
    private void onCancel() {
        // window terminate
        dispose();
    }

    // refresh window content
    public void refresh(){
        router_to_delete.removeAllItems();
        for (RouterButton router: routers){
            String item = router.get_router().get_name();
            router_to_delete.addItem(item);
        }
    }
}
