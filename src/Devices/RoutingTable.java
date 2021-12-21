package Devices;

import java.util.*;

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
    public RoutingTable(){
        routes = new ArrayList<>();
    }

    // add route to routing table list
    public void add_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number){
        Route new_route = new Route(code, distance, metric, net, mask, gateway, int_number);
        if (routes.isEmpty()){
            routes.add(new_route);
        }else{
            Map<Integer, Route> similar_routes = this.find_similar_routes(new_route);
            if (similar_routes.isEmpty()){
                int pointer = 0;
                while (pointer < routes.size() && routes.get(pointer).net_mask() > mask){
                    pointer++;
                }
                routes.add(pointer, new_route);
            }else{
                List<Integer> keys = List.copyOf(similar_routes.keySet());
                ArrayList<Integer> keys2 = new ArrayList<>(keys);
                Collections.sort(keys2);
                int position = keys.get(keys2.size() - 1) + 1;
                boolean identity_flag = false;
                for (int key : similar_routes.keySet()){
                    if (similar_routes.get(key).is_identical(new_route)){
                        identity_flag = true;
                        break;
                    }
                }
                if (!identity_flag){
                    for (int key : similar_routes.keySet()){
                        if (new_route.distance() <= similar_routes.get(key).distance()){
                            position = key;
                            System.out.println(key + ": " + similar_routes.get(key).to_string());
                        }
                    }
                    routes.add(position, new_route);
                }

            }

        }
    }

    // deletes route from routing table list
    public void delete_route(){

    }

    // find routes that are similar to given one
    public Map<Integer, Route> find_similar_routes(Route route){
        Map<Integer, Route> to_return = new HashMap<>();
        for (int i = 0; i < routes.size(); i++){
            if(routes.get(i).is_similar(route)){
                to_return.put(i, routes.get(i));
            }
        }
        return to_return;
    }

    // to string method
    public String to_string(){
        String to_return = "";
        for (Route route : routes){
            to_return = to_return + route.to_string() + "\n";
        }
        return to_return;
    }
}
