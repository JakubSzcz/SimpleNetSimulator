package Topology;

import Devices.Devices.Router;
import Devices.Link;
import GUI.RouterPopUp;
import Icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

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
    public AddRouterMessages add_router(Position position, String name, int int_number, RouterPopUp router_pop_up){
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
        routers.add(new RouterButton(Icons.icon.router(), position, new Router(name, int_number), router_pop_up));
        return AddRouterMessages.is_valid;
    }

    // delete router from topology
    public void delete_router(String name){
        for(RouterButton router : routers){
            if (router.get_router().get_name().equals(name)){
                routers.remove(router);
                break;
            }
        }
    }

    // topology getter
    public static Topology get_topology(){
        return topology;
    }

    // routers getter
    public ArrayList<RouterButton> get_routers(){
        return routers;
    }

    // add router on screen
    public void refresh(JPanel panel){
        JPanel map = new JPanel(new GridLayout(20, 20));
        boolean flag;

        for (int i = 0; i < 400; i++){
            flag = false;
            // add router
            for (RouterButton router: routers){
                int index = router.get_position().get_x() + 20 * router.get_position().get_y();
                if (index == i){
                    map.add(router);
                    flag = true;
                    break;
                }
            }
            // add white space
            if (!flag){
                JPanel p = new JPanel();
                p.setForeground(Color.WHITE);
                p.setBackground(Color.WHITE);
                map.add(p);
            }
        }
        // delete old map
        panel.removeAll();
        map.setBackground(Color.WHITE);
        map.setForeground(Color.WHITE);

        // add new map
        panel.add(map);
        panel.revalidate();
    }

    public Router get_router(int ind){
        return routers.get(ind).get_router();
    }
}
