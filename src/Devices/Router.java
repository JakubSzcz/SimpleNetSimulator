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
    void handle_frame(Frame frame){
        if (frame instanceof SimpleP2PFrame simpleP2P_frame){
            handle_simpleP2P_frame(simpleP2P_frame);
        }
    }

    // handle SimpleP2P frame
    void handle_simpleP2P_frame(SimpleP2PFrame simpleP2P_frame){
        Packet packet = simpleP2P_frame.get_packet();
        if (packet instanceof IPv4Packet ipv4_packet){
            handle_ipv4_packet(ipv4_packet);
        }
    }

    // handle ipv4_packet
    void handle_ipv4_packet(IPv4Packet packet){
        // check if packet is for this router
        boolean for_this_router = false;
        int int_number = net_card.get_int_number();
        for (int i = 0; i < int_number; i++){

        }

    }

    // handle icmp_packet
    void handle_icmp_packet(ICMPPacket packet){

    }

    // set ip address of given interface
    public void set_interface_ip(int int_number, long ip_address, long mask){
        net_card.get_interface(int_number).set_ip_address(ip_address, mask);
        if (net_card.is_interface_up(int_number)){
            long net_address = ip_address & mask;
            add_connected_route(net_address, mask, int_number);
        }
    }

    // delete ip address of given interface
    public void delete_interface_ip(int int_number){
        Map<String, Long> ip_address = net_card.get_ip_address(int_number);
        long net_address = ip_address.get("address") & ip_address.get("mask");
        delete_route(RouteCode.C, 0, 0, net_address,ip_address.get("mask"),-1, int_number);
        net_card.get_interface(int_number).set_ip_address(-1, -1);
    }

    // add static route to routing table
    public void add_static_route(long ip_address, long mask, int int_number){
        routing_table.add_route(RouteCode.S, 1,0, ip_address, mask, -1, int_number);
    }

    // add directly connected route to routing table
    private void add_connected_route(long ip_address, long mask, int int_number){
        routing_table.add_route(RouteCode.C, 0, 0, ip_address,mask, -1, int_number);
    }

    // add rip route to routing table
    private void add_rip_route(long ip_address, long mask, long gateway, int int_number, int metric){
        routing_table.add_route(RouteCode.R,120, metric, ip_address,mask, -1, int_number);
    }

    // delete a route in routing table
    public void delete_route(Route route){
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
    public void send_data(Data data, long destination_address, int ttl){
        // find route to given destination
        int route_index = routing_table.find_best_route(destination_address);
        // if route was found
        if (route_index != -1){
            Route route = routing_table.get_route(route_index);
            // find source address and interface
            int int_number = route.int_number();
            Map<String, Long> address= net_card.get_ip_address(int_number);
            // make IPv4 packet
            Packet packet = IPv4.create_packet(address.get("address"),destination_address,ttl,data);
            // make frame
            Frame frame = new SimpleP2PFrame(packet);
            // send frame
            add_out_traffic(frame, int_number);
        }
    }

    public void send_data(Data data, long destination_address){
        send_data(data, destination_address, 255);
    }

    // return routing table in string
    public String get_routing_table(){
        return routing_table.to_string();
    }
}
