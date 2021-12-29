package GUI.Forms;

import GUI.Topology.Topology;
import GUI.Topology.Position;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.io.File;
import java.util.Locale;

public class NetworkGUI extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // swing
    private JPanel gui_panel;
    private JButton add_link;
    private JButton delete_link;
    private JButton add_router;
    private JButton delete_router;
    private JButton save_topology;
    private JButton open_topology;
    private JPanel buttons;
    private JPanel down_margin;
    private JPanel left_margin;
    private JPanel right_margin;
    private JPanel topology_map;

    // popups
    private final AddRouterPopUp add_router_pop_up;
    private final RouterPopUp router_pop_up;
    private final AddLinkPopUp add_link_pop_up;
    private final DeleteRouterPopUp delete_router_pop_up;

    // vars and objects
    private boolean flag;
    private final Topology topology = Topology.get_topology();

    // File Filter
    private final FileFilter sns_file_filter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()){
                return true;
            }
            String filename = f.getName().toLowerCase();
            return filename.endsWith(".sns");
        }

        @Override
        public String getDescription() {
            return "Simple Net Simulator (* sns)";
        }
    };

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public NetworkGUI() {
        // popups
        this.router_pop_up = new RouterPopUp();
        this.add_router_pop_up = new AddRouterPopUp(topology_map, router_pop_up);
        this.add_link_pop_up = new AddLinkPopUp(topology_map);
        this.delete_router_pop_up = new DeleteRouterPopUp(topology_map);

        // add router flag
        this.flag = false;

        // set topology_map layout to grid
        topology_map.setLayout(new GridLayout(1, 1 ));

        // add router button
        add_router.addActionListener(e -> {
            flag = true;
            Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
            gui_panel.setCursor(cursor);
        });

        // map click
        topology_map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(flag){
                    // add router popup
                    add_router_pop_up.set_mouse_position(new Position(e.getX(), e.getY()));
                    add_router_pop_up.setVisible(true);

                    // change flag back to false
                    flag = false;

                    // thread start
                    if (!isAlive()){
                        start();
                    }

                    // change cursor
                    Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    gui_panel.setCursor(cursor);
                }

            }
        });

        // add link button
        add_link.addActionListener(e -> {
            add_link_pop_up.refresh();
            add_link_pop_up.setVisible(true);
        });

        //delete button
        delete_router.addActionListener(e -> {
            delete_router_pop_up.refresh();
            delete_router_pop_up.setVisible(true);
        });

        // save button
        save_topology.addActionListener(e -> {
            // create new file chooser
            JFileChooser file_chooser = new JFileChooser();
            file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // allow files with sns extension only
            file_chooser.setFileFilter(sns_file_filter);
            file_chooser.setDialogTitle("Choose a file");
            int user_selection = file_chooser.showSaveDialog(gui_panel);

            // if user chose a file
            if (user_selection == JFileChooser.APPROVE_OPTION){
                File file_to_save = file_chooser.getSelectedFile();
                topology.save(file_to_save.getAbsolutePath() + ".sns");
            }
        });

        // open button
        open_topology.addActionListener(e -> {
            // create new file chooser
            JFileChooser file_chooser = new JFileChooser();
            file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // allow files with sns extension only
            file_chooser.setFileFilter(sns_file_filter);
            file_chooser.setDialogTitle("Choose a file");
            int user_selection = file_chooser.showOpenDialog(gui_panel);

            // if user chose a file
            if (user_selection == JFileChooser.APPROVE_OPTION){
                File file_to_open = file_chooser.getSelectedFile();
                topology.open(file_to_open.getAbsolutePath() + ".sns");
            }
        });
    }

    // thread, draw links
    @Override
    public void run() {
        while (true){
            topology.draw_links(topology_map);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // main
    public static void main(String[] args) {
        JFrame frame = new JFrame("Network GUI.Topology");
        frame.setContentPane(new NetworkGUI().gui_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}