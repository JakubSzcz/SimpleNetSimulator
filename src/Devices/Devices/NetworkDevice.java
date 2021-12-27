package Devices.Devices;

import Protocols.Frame.Frame;

public abstract class NetworkDevice extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // name of the device
    protected String name;

    // network card
    protected NetworkCard net_card;

    // monitor
    protected Monitor monitor;

    // if device is turned on - true
    protected boolean turned_on;

    // sleep time in run method
    protected int router_speed;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // normal case constructor
    public NetworkDevice(String name, int int_number){
        constructor(name,int_number, 200, false);
    }

    public NetworkDevice(String name, int int_number, int clock_period){
        constructor(name,int_number, clock_period, false);
    }

    // test case constructor, Network device for tests without thread running
    public NetworkDevice(String name, int int_number, Boolean test){
        constructor(name, int_number, 200, test);
    }

    public NetworkDevice(String name, int int_number, int clock_period, Boolean test){
        constructor(name, int_number, clock_period, test);
    }

    // constructor
    private void constructor(String name,int int_number, int clock_period, Boolean test){
        this.name = name;
        this.net_card = new NetworkCard(int_number);
        this.monitor = new Monitor();
        this.turned_on = true;
        this.router_speed = clock_period;
        if (!test){
            start();
        }
    }

    // turns on the device
    public void turn_on(){
        turned_on = true;
    }

    // turns off the device
    public void turn_of(){
        turned_on = false;
    }

    // sends a frame by given interface
    public void add_out_traffic(Frame frame, int int_number){
        net_card.add_out_traffic(frame, int_number);
    }

    // checks if there are frames in buffer
    public void run(){
        while (true){
            if (!net_card.is_buffer_empty()){
                handle_frame(net_card.get_frame_from_buffer());
            }

            try {
                Thread.sleep(router_speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // actions taken after receiving a frame
    abstract void handle_frame(Frame frame);

    // returns interface, link needs it
    public NetworkInterface get_interface(int int_number){
        return net_card.get_interface(int_number);
    }

    // turn of interface
    public void down_interface(int int_number){
        net_card.down_interface(int_number);
    }

    // turn up interface
    public void up_interface(int int_number){
        net_card.up_interface(int_number);
    }


    // check if card buffer is empty - only for tests
    public boolean is_buffer_empty(){
        return net_card.is_buffer_empty();
    }

    // return monitor to string
    public String get_monitor(){
        return monitor.to_string();
    }

    // clear monitor
    public void clear_monitor(){
        monitor.clear();
    }

    // name getter
    public String get_name() {
        return name;
    }
}
