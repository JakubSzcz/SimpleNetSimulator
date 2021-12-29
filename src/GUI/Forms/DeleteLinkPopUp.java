package GUI.Forms;

import Devices.Link;
import GUI.Topology.Position;
import GUI.Topology.Topology;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DeleteLinkPopUp extends JDialog {
    private JPanel contentPane;
    private JButton ok_button;
    private JButton cancel_button;
    private JPanel buttons;
    private JPanel down_field;
    private JPanel main_panel;
    private JTextField information;
    private JComboBox link_to_delete;
    // topology
    Topology topology = Topology.get_topology();
    //topology map
    private JPanel panel;
    // links
    private ArrayList<Link> links;
    // links position
    private ArrayList<Position[]> link_positions;

    // constructor
    public DeleteLinkPopUp(JPanel panel) {
        // swing
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(ok_button);

        // appearance settings
        setSize(400, 150);
        setResizable(false);
        setLocation(200, 200);

        // initialization
        this.panel = panel;
        this.links = topology.get_links();
        this.link_positions = topology.get_link_positions();

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
}
