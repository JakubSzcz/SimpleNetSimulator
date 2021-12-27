package Topology;

public class Position {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////
    private int x;
    private int y;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    Position(int x, int y){
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
