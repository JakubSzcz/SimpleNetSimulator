package Topology;

import Devices.Devices.Router;
import Devices.Link;
import Icons.Icons;

import javax.swing.*;
import java.util.ArrayList;

public class Topology {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // routers in topology
    ArrayList<RouterButton> routers;

    // links in topology
    ArrayList<Link> links;

    // topology object
    private static final Topology topology = new Topology();

    // max number of interfaces on router
    private final int max_int_number = 8;

    // min number of interfaces
    private final int min_int_number = 1;

    // max number of letters in name
    private final int max_name_characters = 10;

    // min number of letter in name
    private final int min_name_characters = 1;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Topology(){
        routers = new ArrayList<>();
        links = new ArrayList<>();
    }

    // add router to topology
    public AddRouterMessages add_router(Position position, String name, int int_number){
        // check if number of interfaces is not too high
        if (int_number > max_int_number){
            return AddRouterMessages.too_many_interfaces;
        }else if (int_number < min_int_number){
            return AddRouterMessages.too_less_interfaces;
        }else if(name.length() < min_name_characters){
            return AddRouterMessages.too_short_name;
        }else if (name.length() > max_name_characters){
            return AddRouterMessages.too_long_name;
        }

        // check if given name hasn't already been taken
        for(RouterButton router : routers){
            if (router.get_router().get_name().equals(name)){
                return AddRouterMessages.name_is_taken;
            }
        }

        // if is valid
        routers.add(new RouterButton(Icons.icon.test(), position, new Router(name, int_number)));
        return AddRouterMessages.is_valid;
    }

    // delete router from topology
    public void delete_router(String name){

    }

    // topology getter
    public static Topology get_topology(){
        return topology;
    }

    // routers getter
    public ArrayList<RouterButton> get_routers(){
        return routers;
    }
}
