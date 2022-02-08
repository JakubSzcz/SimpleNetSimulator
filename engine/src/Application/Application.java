package Application;

import java.util.ArrayDeque;
import java.util.HashMap;

public abstract class Application extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // identifier
    public final int identifier;

    // buffer
    protected ArrayDeque<HashMap<String, Object>> buffer;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    protected Application(int identifier){
        this.identifier = identifier;
        this.buffer = new ArrayDeque<>();
    }

    // add to buffer
    public void add_to_buffer(HashMap<String, Object> record){
        buffer.addLast(record);
    }
}