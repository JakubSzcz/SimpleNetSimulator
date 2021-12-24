package Devices;

import Protocols.Frame;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class NetworkInterface {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // ip address
    private long address;

    // mask
    private long mask;

    // if interface is up - true
    private boolean activated;

    // out buffer for outgoing traffic
    private final ArrayDeque<Frame> out_buffer;

    // buffer for incoming traffic, reference to net card buffer
    private final ArrayDeque<Frame> buffer;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    NetworkInterface(ArrayDeque<Frame> buffer){
        this.buffer = buffer;
        out_buffer = new ArrayDeque<>();
        this.activated = false;
        address = -1;
        mask = -1;
    }

    // turn on the port
    public void up(){
        this.activated = true;
    }

    // turn off the port
    public void down(){
        this.activated = false;
        out_buffer.clear();
    }

    // configure ip address on the port
    public void set_ip_address(long address, long mask){
        this.address = address;
        this.mask = mask;
    }

    // delete ip address from port
    public void delete_ip_address(){
        this.address = -1;
        this.mask = -1;
    }

    // add frame to the out_buffer
    public void add_frame(Frame frame){
        if (activated){
            out_buffer.addLast(frame);
        }
    }

    // add frame to the buffer
    public void handle_frame(Frame frame){
        if (activated) {
            buffer.addLast(frame);
        }
    }

    // check if out buffer is empty
    public boolean is_out_empty(){
        return out_buffer.isEmpty();
    }

    // delete and return frame form out buffer
    public Frame get_frame(){
            return out_buffer.pollFirst();
    }

    // return true if ip address is set
    public boolean is_ip_set(){
        return -1 != address;
    }

    // return ip address
    public Map<String, Long> get_ip_address(){
        Map<String, Long> to_return= new HashMap<>();
        to_return.put("address", address);
        to_return.put("mask", mask);
        return to_return;
    }

    // return true if interface si up
    public boolean is_up(){
        return activated;
    }
}
