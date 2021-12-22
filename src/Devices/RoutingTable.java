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
    public void add_route(Route new_route){
        // add route if routes list is empty
        if (routes.isEmpty()){
            routes.add(new_route);
        }else{
            // check if similar routes have already been added
            Map<Integer, Route> similar_routes = this.find_similar_routes(new_route);
            if (similar_routes.isEmpty()){
                // add route to the list, position based on mask
                int pointer = 0;
                while (pointer < routes.size() && routes.get(pointer).net_mask() > new_route.net_mask()){
                    pointer++;
                }
                routes.add(pointer, new_route);
            }else{
                // change similar_rotes keys Collection from set to ArrayList
                List<Integer> keys = List.copyOf(similar_routes.keySet());
                ArrayList<Integer> keys2 = new ArrayList<>(keys);

                // sort keys in ascending order
                Collections.sort(keys2);

                // gives default position, one higher than the max of keys
                int position = keys.get(keys2.size() - 1) + 1;
                boolean identity_flag = false;

                // check if one of similar routes is identical
                for (int key : similar_routes.keySet()){
                    if (similar_routes.get(key).is_identical(new_route)){
                        identity_flag = true;
                        break;
                    }
                }

                // if neither of similar routes is identical find position of the new route based on distance
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

    public void add_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number){
        Route new_route = new Route(code, distance, metric, net, mask, gateway, int_number);
        add_route(new_route);
    }

    // deletes route from routing table list
    public void delete_route(Route route){
        for (int i = 0; i < routes.size(); i++){
            if (routes.get(i).is_identical(route)){
                routes.remove(i);
                break;
            }
        }
    }

    public void delete_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number){
        Route route = new Route(code, distance, metric, net, mask, gateway, int_number);
        delete_route(route);
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

    // finds index of best route for given ip address
    // returns -1 if route doesn't exist
    public int find_best_route(long ip_address){
        int route_index = -1;
        for (int i = 0; i < routes.size(); i++){
            long net_address = ip_address & routes.get(i).net_mask();
            if (net_address == routes.get(i).net()){
                route_index = i;
                break;
            }
        }
        return route_index;
    }

    // returns route by given index
    public Route get_route(int route_index){
        return routes.get(route_index);
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
