package Devices;

import Protocols.*;

import java.util.Map;

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
        net_card.get_interface(int_number).set_ip_address(ip_address, mask);
    }

    // delete ip address of given interface
    void delete_interface_ip(int int_number){
        net_card.get_interface(int_number).set_ip_address(-1, -1);
    }

    // add static route to routing table
    void add_static_route(long ip_address, long mask, int int_number){
        routing_table.add_route(RouteCode.S, 1,0, ip_address, mask, -1, int_number);
    }

    // add directly connected route to routing table
    void add_connected_route(long ip_address, long mask, int int_number){
        routing_table.add_route(RouteCode.C, 0, 0, ip_address,mask, -1, int_number);
    }

    // add rip route to routing table
    void add_rip_route(long ip_address, long mask, long gateway, int int_number, int metric){
        routing_table.add_route(RouteCode.R,120, metric, ip_address,mask, -1, int_number);
    }

    // delete a route in routing table
    void delete_route(Route route){
        routing_table.delete_route(route);
    }

    public void delete_route(RouteCode code, int distance, int metric, long net, long mask, long gateway, int int_number) {
        Route route = new Route(code, distance, metric, net, mask, gateway, int_number);
        delete_route(route);
    }

    // turn on given interface
    public void up_interface(int int_number){
        super.up_interface(int_number);
        if (net_card.is_ip_set(int_number)){
            Map<String, Long> ip_address = net_card.get_ip_address(int_number);
            long net_address = ip_address.get("address") & ip_address.get("mask");
            add_connected_route(net_address, ip_address.get("mask"), int_number);
        }
    }

    // turn off given interface
    public void down_interface(int int_number){
        if (net_card.is_ip_set(int_number)){
            Map<String, Long> ip_address = net_card.get_ip_address(int_number);
            long net_address = ip_address.get("address") & ip_address.get("mask");
            delete_route(RouteCode.C, 0, 0, net_address,ip_address.get("mask"),-1, int_number);
        }
        super.down_interface(int_number);
    }

    // sends data to given ip address
    public void send_data(Data data, long destination_address){

    }
}
