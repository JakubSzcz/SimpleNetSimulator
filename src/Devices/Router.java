package Devices;

public class Router extends NetworkDevice{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // routing table
    RoutingTable routing_table;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // normal case constructor
    public Router(String name, int int_number){
        super(name, int_number);
        routing_table = new RoutingTable();
    }

    // test case constructor
    public Router(String name, int int_number, boolean test){
        super(name, int_number, test);
        routing_table = new RoutingTable();
    }

    // actions taken after receiving a frame
    void handle_frame(){

    }

    // set ip address of given interface
    void set_interface_ip(int int_number, long ip_address, long mask){

    }

    // delete ip address of given interface
    void delete_interface_ip(int int_number){

    }

    // add static route to routing table
    void add_static_route(long ip_address, long mask, long gateway){

    }

    // add directly connected route to routing table
    void add_connected_route(long ip_address, long mask){

    }

    // add rip route to routing table
    void add_rip_route(long ip_address, long mask, long gateway){

    }

    // delete a route in routing table
    void delete_route(Route route){
        routing_table.delete_route(route);
    }

    public void delete_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number) {
        Route route = new Route(code, distance, metric, net, mask, gateway, int_number);
        delete_route(route);
    }
}
