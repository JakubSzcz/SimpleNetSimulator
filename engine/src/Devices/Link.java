package Devices;

import Devices.Devices.NetworkInterface;

import java.io.Serializable;

public class Link extends Thread implements Serializable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // ends of link
    private final NetworkInterface end_1;
    private final NetworkInterface end_2;

    // delay in ms
    private final int delay;

    // link id
    private final int id;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Link(NetworkInterface end_1, NetworkInterface end_2){
        this(end_1, end_2, -1);
    }
    public Link(NetworkInterface end_1, NetworkInterface end_2, int id){
        // port1
        this.end_1 = end_1;

        // port2
        this.end_2 = end_2;

        // delay
        this.delay = 200;

        // id
        this.id = id;

        // Thread start
        start();
    }

    public void run(){
        while (true){
            if (!end_1.is_out_empty()){
                end_2.handle_frame(end_1.get_frame());
            }
            if (!end_2.is_out_empty()){
                end_1.handle_frame(end_2.get_frame());
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // end1 getter
    public NetworkInterface get_end1(){
        return end_1;
    }

    // end2 getter
    public NetworkInterface get_end2(){
        return end_2;
    }

    // id getter
    public int get_id(){
        return id;
    }
}
