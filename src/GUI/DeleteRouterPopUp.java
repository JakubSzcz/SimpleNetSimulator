package GUI;

import Topology.Topology;
import Topology.RouterButton;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DeleteRouterPopUp extends JDialog {
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
    private ArrayList<RouterButton> routers;
    // topology_map
    JPanel panel;

    public DeleteRouterPopUp(JPanel panel) {

        setContentPane(delete_router_content);
        setModal(true);
        getRootPane().setDefaultButton(ok_button);
        routers = topology.get_routers();
        this.panel = panel;
        setSize(400, 150);
        setResizable(false);
        setLocation(200, 200);

        ok_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancel_button.addActionListener(new ActionListener() {
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
        delete_router_content.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // deleting router from a list and map
        String to_delete = router_to_delete.getSelectedItem().toString();
        topology.delete_router(to_delete);
        topology.refresh(panel);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void refresh(){
        int int_number;
        router_to_delete.removeAllItems();
        for (RouterButton router: routers){
            String item = router.get_router().get_name();
            router_to_delete.addItem(item);
        }
    }
}
