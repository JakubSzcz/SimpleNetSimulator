package Devices;

import Protocols.Frame;

import java.util.ArrayDeque;

public class NetworkInterface {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // ip address
    private int address;

    // mask
    private int mask;

    // if interface is up - true
    private boolean activated;

    // out buffer for outgoing traffic
    private ArrayDeque<Frame> out_buffer;

    // buffer for incoming traffic, reference to net card buffer
    private ArrayDeque<Frame> buffer;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    NetworkInterface(ArrayDeque<Frame> buffer){
        this.buffer = buffer;
    }
}
