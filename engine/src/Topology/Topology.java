package Topology;

import Devices.Devices.NetworkDevice;
import Devices.Devices.Router;
import Devices.Link;

import java.util.ArrayList;
import java.util.HashMap;

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

    // topology_map
    // name: device_name
    // int_number: link_id or -1
    private final ArrayList<HashMap<Object, Object>> topology_map;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Topology(){
        links = new ArrayList<>();
        devices = new ArrayList<>();
        topology_map = new ArrayList<>();
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
            case ROUTER ->{
                devices.add(new Router(name, int_number));
                HashMap<Object, Object> map = new HashMap<>();
                map.put("name", name);
                for (int i = 0; i < int_number; i++){
                    map.put(i, -1);
                }
                topology_map.add(map);
            }
        }
        return AddDeviceMessages.is_valid;
    }

    // add link to topology
    public AddLinkMessages add_link(String device1_name, int int_number1, String device2_name, int int_number2){
        // get devices
        NetworkDevice device1 = get_device(device1_name);
        NetworkDevice device2 = get_device(device2_name);

        // check if devices are not null
        if (device1 == null || device2 == null){
            return AddLinkMessages.error;
        }

        // check if link between the same device
        if (device1 == device2){
            return AddLinkMessages.same_device_chosen;
        }

        // check if given devices have given int number free
        if (!get_free_int_number(device1).contains(int_number1)){
            return AddLinkMessages.wrong_int_number;
        }
        if (!get_free_int_number(device2).contains(int_number2)){
            return AddLinkMessages.wrong_int_number;
        }

        // check if link has been already established
        // TODO

        // generate link id
        int link_id = 0;
        boolean is_id_valid = false;
        while (is_id_valid){
            is_id_valid = true;
            for (Link link: links){
                if (link.get_id() == link_id){
                    is_id_valid = false;
                    link_id++;
                    break;
                }
            }
        }

        // add new link to links
        HashMap<Object, Object> map1 = get_device_from_topology_map(device1);
        HashMap<Object, Object> map2 = get_device_from_topology_map(device2);
        map1.put(int_number1, link_id);
        map2.put(int_number2, link_id);
        links.add(new Link(device1.get_interface(int_number1), device2.get_interface(int_number2), link_id));
        return AddLinkMessages.is_valid;
    }

    /////////////////////////////////////////////////////////
    //                      getters                        //
    /////////////////////////////////////////////////////////

    // topology getter
    public static Topology get_topology(){
        return topology;
    }

    // get device by given name
    public NetworkDevice get_device(String name){
        for(NetworkDevice device: devices){
            if(name.equals(device.get_name())){
                return device;
            }
        }
        return null;
    }

    // get free interfaces in given device
    public ArrayList<Integer> get_free_int_number(String device_name){
        return get_free_int_number(get_device(device_name));
    }

    public ArrayList<Integer> get_free_int_number(NetworkDevice device){
        ArrayList<Integer> to_return = new ArrayList<>();
        HashMap<Object, Object> device_map = get_device_from_topology_map(device);
        // if device was not found
        if (device_map == null){
            return to_return;
        }

        // check interfaces
        for (int i = 0; i < device.get_int_number(); i++){
            if ((int)device_map.get(i) == -1){
                to_return.add(i);
            }
        }
        return to_return;
    }
    // get device from topology_map
    public HashMap<Object, Object> get_device_from_topology_map(String device_name){
        return get_device_from_topology_map(get_device(device_name));
    }
    public HashMap<Object, Object> get_device_from_topology_map(NetworkDevice device){
        HashMap<Object, Object> device_map = null;
        for (HashMap<Object, Object> map : topology_map){
            if(map.get("name").equals(device.get_name())){
                device_map = map;
                break;
            }
        }
        return device_map;
    }
}
