package GUI.Topology;

import java.io.Serializable;

public class Position implements Serializable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////
    private int x;
    private int y;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    // x getter
    public int get_x() {
        return x;
    }

    // y getter
    public int get_y() {
        return y;
    }
}
