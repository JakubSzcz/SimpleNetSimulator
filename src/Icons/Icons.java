package Icons;

import javax.swing.*;

public class Icons {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final String PATH = "Icons/img/";
    public static Icons icon = new Icons();

    // test icon
    private final ImageIcon test_icon = new ImageIcon(getClass().getResource(PATH + "test_icon.png"));

    // router
    private final ImageIcon router_icon = new ImageIcon(getClass().getResource(PATH + "router.png"));

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Icons(){}

    // test icon
    public ImageIcon test(){
        return test_icon;
    }

    // router icon
    public ImageIcon router(){
        return router_icon;
    }
}
