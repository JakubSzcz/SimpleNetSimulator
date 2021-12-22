package Devices;

import java.util.ArrayList;

public class Monitor {
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
    public String to_string(){
        String to_return = "";
        for (String line : memory){
            to_return = to_return + line + "\n";
        }
        return to_return;
    }
}
