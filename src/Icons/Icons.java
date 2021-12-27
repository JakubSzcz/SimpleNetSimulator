package Icons;

import javax.swing.*;

public class Icons {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final String PATH = "Icons/img/";
    public static Icons icon = new Icons();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Icons(){}

    // test icon
    public ImageIcon test(){
        return new ImageIcon(getClass().getResource(PATH + "test_icon.png"));
    }

    // router icon
    public ImageIcon router(){
        return new ImageIcon(getClass().getResource(PATH + "router.png"));
    }


}
