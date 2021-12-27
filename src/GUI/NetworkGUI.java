package GUI;

import javax.swing.*;

public class NetworkGUI {
    private JPanel gui_panel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("NetworkGUI");
        frame.setContentPane(new NetworkGUI().gui_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
