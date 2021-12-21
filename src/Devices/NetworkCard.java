package Devices;

import Protocols.Frame;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class NetworkCard {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // interfaces
    private ArrayList<NetworkInterface> interfaces;

    // buffer for incoming traffic
    private ArrayDeque<Frame> buffer;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public NetworkCard(int int_number){
        interfaces = new ArrayList<>();
        for (int i=0; i<int_number; i++){
            interfaces.add(new NetworkInterface(buffer));
        }
    }

    // add outgoing traffic to an interface
    public void add_out_traffic(Frame frame, int int_number){
        interfaces.get(int_number).add_frame(frame);
    }
}
