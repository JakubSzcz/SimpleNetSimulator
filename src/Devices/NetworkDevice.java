package Devices;

public class NetworkDevice extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // name of the device
    protected String name;

    // network card
    NetworkCard net_card;

    // if device is turned on - true
    protected boolean turned_on;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    NetworkDevice(String name){
        this.name = name;
    }

    // turns on the device
    public void turn_on(){

    }

    // turns off the device
    public void turn_of(){

    }

    // sends a frame by given interface
    public void send(){

    }

    // checks if there are frames in buffer
    public void run(){

    }
}
