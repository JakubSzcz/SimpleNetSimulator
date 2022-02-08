package Application;

import java.util.ArrayDeque;
import java.util.HashMap;

public class Trash extends Application{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Trash(){
        super(0);
    }

    // get buffer
    public ArrayDeque<HashMap<String, Object>> get_buffer(){
        return buffer;
    }

}
