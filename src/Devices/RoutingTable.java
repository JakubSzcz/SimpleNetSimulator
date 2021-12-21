package Devices;

import java.util.ArrayList;

public class RoutingTable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // routing table
    ArrayList<Route> routes;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    RoutingTable(){
        routes = new ArrayList<>();
    }

    // add route to routing table list
    public void add_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number){
        Route new_route = new Route(code, distance, metric, net, mask, gateway, int_number);
        if (routes.isEmpty()){
            routes.add(new_route);
        }else{
            int pointer = 0;
            while (pointer < routes.size() && routes.get(pointer).net_mask() < mask){
                pointer++;
            }
            routes.add(pointer, new_route);
        }
    }
}
