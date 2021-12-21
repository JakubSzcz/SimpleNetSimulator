package Devices;

import Protocols.Frame;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class NetworkCard {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // interfaces
    private final ArrayList<NetworkInterface> interfaces;

    // buffer for incoming traffic
    private final ArrayDeque<Frame> buffer;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public NetworkCard(int int_number){
        buffer = new ArrayDeque<>();
        interfaces = new ArrayList<>();
        for (int i=0; i<int_number; i++){
            interfaces.add(new NetworkInterface(buffer));
        }
    }

    // add outgoing traffic to an interface
    public void add_out_traffic(Frame frame, int int_number){
        interfaces.get(int_number).add_frame(frame);
    }

    // returns interface, link needs it
    public NetworkInterface get_interface(int int_number){
        return interfaces.get(int_number);
    }

    // return true if buffer is empty
    public boolean is_buffer_empty(){
        return buffer.isEmpty();
    }

    // returns and deletes first frame in buffer
    public Frame get_frame_from_buffer(){
        return buffer.pollFirst();
    }
}
