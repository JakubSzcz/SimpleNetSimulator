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

    // turn on the port
    public void up(){

    }

    // turn off the port
    public void down(){

    }

    // configure ip address on the port
    public void set_ip_address(){

    }

    // delete ip address from port
    public void delete_ip_address(){

    }

    // add frame to the out_buffer
    public void add_frame(Frame frame){
        out_buffer.addLast(frame);
    }

    // add frame to the buffer
    public void handle_frame(Frame frame){
        buffer.addLast(frame);
    }

    // check if out buffer is empty
    public boolean is_out_empty(){
        return out_buffer.isEmpty();
    }

    // delete and return frame form out buffer
    public Frame get_frame(){
        return out_buffer.pollFirst();
    }
}
