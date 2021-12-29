package GUI.Icons;

import javax.swing.*;

public class Icons {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final String PATH = "/GUI/Icons/img/";
    public static Icons icon = new Icons();

    // test icon
    private final Icon test_icon = new ImageIcon(getClass().getResource(PATH + "test_icon.png"));

    // router icon
    private final Icon router_icon = new ImageIcon(getClass().getResource(PATH + "router.png"));

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Icons(){}

    // test icon
    public Icon test(){
        return test_icon;
    }

    // router icon
    public Icon router(){
        return router_icon;
    }
}
