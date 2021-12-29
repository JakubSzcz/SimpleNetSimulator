package Devices.Devices;

import java.io.Serializable;
import java.util.ArrayList;

public class Monitor implements Serializable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////
    private ArrayList<String> memory;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    Monitor(){
        memory = new ArrayList<>();
    }

    // add line to memory
    public void add_line(String line){
        memory.add(line);
    }

    // clear memory
    public void clear(){
        memory.clear();
    }

    // return memory in string
    /*public String to_string(){
        String to_return = "";
        for (String line : memory){
            to_return = to_return + line + "\n";
        }
        return to_return;
    }*/

    // return memory in string
    public String to_string(){
        StringBuilder to_return = new StringBuilder();
        for(String line : memory){
            to_return.append(line);
            to_return.append("\n");
        }
        return to_return.toString();
    }
}
