package Icons;

import javax.swing.*;

public class Icons {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final String PATH = "Icons/img/";
    public static Icons icon = new Icons();

    // test icon
    private final Icon test_icon = new ImageIcon(PATH + "test_icon.png");

    // router icon
    private final Icon router_icon = new ImageIcon(PATH + "test_icon.png");

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
