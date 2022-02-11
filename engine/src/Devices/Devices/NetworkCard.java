package Devices.Devices;

import Protocols.Frame.Frame;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.ArrayList;

public class NetworkCard implements Serializable {
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
            interfaces.add(new NetworkInterface(buffer, i));
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

    // turn of interface
    public void down_interface(int int_number){
        interfaces.get(int_number).down();
    }

    // turn up interface
    public void up_interface(int int_number){
        interfaces.get(int_number).up();
    }

    // return true if interface has ip address
    public boolean is_ip_set(int int_number){
        return interfaces.get(int_number).is_ip_set();
    }

    // return ip address of given interface
    public Map<String, Long> get_ip_address(int int_number){
        return interfaces.get(int_number).get_ip_address();
    }

    // return true if given interface is up
    public boolean is_interface_up(int int_number){
        return interfaces.get(int_number).is_up();
    }

    // return interfaces size
    public int get_int_number(){
        return interfaces.size();
    }
}
