package Topology;

import Devices.Devices.Router;
import Devices.Link;

import java.util.ArrayList;
import java.util.HashMap;

public class Topology {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // routers in topology
    ArrayList<HashMap<String, Object>> routers;

    // links in topology
    ArrayList<Link> links;

    // topology object
    public Topology topology = new Topology();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Topology(){
        routers = new ArrayList<>();
        links = new ArrayList<>();
    }

    // add router to topology
    public void add_router(Position position, String name, int int_number){
        HashMap<String, Object> dict = new HashMap<>();
        dict.put("position", position);
        Router router = new Router(name, int_number);
        dict.put("router", router);
        routers.add(dict);
    }

    // delete router from topology
    public void delete_router(String name){

    }
}
