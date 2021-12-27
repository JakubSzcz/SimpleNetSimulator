package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NetworkGUI {
    private JPanel gui_panel;
    private JButton add_link;
    private JButton delete_link;
    private JButton add_router;
    private JButton delete_router;
    private JButton save_topology;
    private JButton load_topology;
    private JPanel buttons;
    private JPanel down_margin;
    private JPanel left_margin;
    private JPanel right_margin;
    private JPanel topology_map;
    private final AddRouterPopUp add_router_pop_up;
    private boolean flag;

    public NetworkGUI() {
        this.add_router.setBorder(BorderFactory.createEmptyBorder());
        this.add_router_pop_up = new AddRouterPopUp();
        this.flag = false;
        add_router.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_router_pop_up.setVisible(true);
                flag = true;
            }
        });
        topology_map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(flag){
                    //dodaj ruter w to miejsce
                    flag = false;
                    System.out.println(flag);
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Network Topology");
        frame.setContentPane(new NetworkGUI().gui_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
