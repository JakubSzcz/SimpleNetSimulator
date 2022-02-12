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

    // position
    private double start_x;
    private double start_y;
    private double end_x;
    private double end_y;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Link(NetworkInterface end_1, NetworkInterface end_2){
        this(end_1, end_2, -1, 0, 0, 0, 0);
    }
    public Link(NetworkInterface end_1, NetworkInterface end_2, int id,
                double start_x, double start_y, double end_x, double end_y){
        // port1
        this.end_1 = end_1;

        // port2
        this.end_2 = end_2;

        // delay
        this.delay = 200;

        // id
        this.id = id;

        // position
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;

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
                break;
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

    // position getter

    public double get_start_x() {
        return start_x;
    }

    public double get_start_y() {
        return start_y;
    }

    public double get_end_x() {
        return end_x;
    }

    public double get_end_y() {
        return end_y;
    }
}
