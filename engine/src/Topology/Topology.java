package Topology;

import Devices.Devices.NetworkDevice;
import Devices.Devices.Router;
import Devices.Link;

import java.util.ArrayList;

public class Topology {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // topology object
    private static final Topology topology = new Topology();

    // max number of interfaces on router
    public final static int MAX_INT_NUMBER = 8;

    // min number of interfaces
    public final static int MIN_INT_NUMBER = 1;

    // max number of letters in name
    public final static int MAX_NAME_CHARACTERS = 10;

    // min number of letter in name
    public final static int MIN_NAME_CHARACTERS = 1;

    // links in topology
    private final ArrayList<Link> links;

    // routers in topology
    private final ArrayList<NetworkDevice> devices;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Topology(){
        links = new ArrayList<>();
        devices = new ArrayList<>();
    }

    // add router to topology
    public AddDeviceMessages add_device(String name, int int_number, NetworkDevicesTypes type){
        // check if number of interfaces is not too high
        if (int_number > MAX_INT_NUMBER){
            return AddDeviceMessages.too_many_interfaces;
            // check if number of interfaces is not too small
        }else if (int_number < MIN_INT_NUMBER){
            return AddDeviceMessages.too_less_interfaces;
            // check if name length isn't too short
        }else if(name.length() < MIN_NAME_CHARACTERS){
            return AddDeviceMessages.too_short_name;
            // check if name length isn't too long
        }else if (name.length() > MAX_NAME_CHARACTERS){
            return AddDeviceMessages.too_long_name;
        }

        // check if given name hasn't already been taken
        for(NetworkDevice device : devices){
            if (device.get_name().equals(name)){
                return AddDeviceMessages.name_is_taken;
            }
        }

        // if is valid
        switch (type){
            case ROUTER -> devices.add(new Router(name, int_number));
        }
        return AddDeviceMessages.is_valid;
    }

    /////////////////////////////////////////////////////////
    //                      getters                        //
    /////////////////////////////////////////////////////////

    // topology getter
    public static Topology get_topology(){
        return topology;
    }
}
