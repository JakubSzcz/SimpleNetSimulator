package Devices.Devices;

import Application.Application;
import Application.ApplicationDataTypes;
import Application.Ping;
import Devices.CLI.RouterCLI;
import Devices.Routing.Route;
import Devices.Routing.RouteCode;
import Devices.Routing.RoutingTable;
import Protocols.Data.Data;
import Protocols.Data.ICMP;
import Protocols.Data.ICMPPacket;
import Protocols.Frame.Frame;
import Protocols.Frame.SimpleP2PFrame;
import Protocols.Packets.IPv4;
import Protocols.Packets.IPv4MessageTypes;
import Protocols.Packets.IPv4Packet;
import Protocols.Packets.Packet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Router extends NetworkDevice implements Serializable {
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
        cli = new RouterCLI(this);
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

    // actions taken if received frame uses SimpleP2P Protocol
    void handle_simpleP2P_frame(SimpleP2PFrame simpleP2P_frame){
        Packet packet = simpleP2P_frame.get_packet();
        if (packet instanceof IPv4Packet ipv4_packet){
            handle_ipv4_packet(ipv4_packet);
        }
    }

    // actions taken if received packet uses IPv4 Protocol
    void handle_ipv4_packet(IPv4Packet packet){
        // log
        System.out.println(name + ": packet received from " +
                IPv4.parse_to_string(packet.get_source_address()));

        // check if packet is for this router
        boolean for_this_router = false;
        int int_number = net_card.get_int_number();
        for (int i = 0; i < int_number; i++){
            if(packet.get_destination_address() == net_card.get_ip_address(i).get("address")){
                // if packet is for this router handle data
                if (packet.get_data() instanceof ICMPPacket icmp_packet){
                    handle_icmp_packet(icmp_packet, packet.get_source_address(), packet.get_destination_address(),
                            packet.get_time_to_live());
                }
                for_this_router = true;
                break;
            }
        }
        // if packet isn't for this router send it further
        if (!for_this_router){
            packet.reduce_ttl();
            if (packet.get_time_to_live() > 0){
                // send further
                send_ipv4_packet(packet);
            }else{
                // send destination unreachable
                Data dest_unreachable;
                if (packet.get_data() instanceof ICMPPacket icmp_packet){
                    dest_unreachable = ICMP.create_dest_unreachable(icmp_packet.get_identifier(),
                            icmp_packet.get_sequence_number());
                }else{
                    dest_unreachable = ICMP.create_dest_unreachable();
                }
                send_data(dest_unreachable, packet.get_source_address());
            }
        }

    }

    // actions taken if received data is ICMP packet
    void handle_icmp_packet(ICMPPacket packet, long source, long destination, int ttl){
        // echo reply and destination unreachable
        boolean trash = true;
        if (packet.get_type() == 0 || packet.get_type() == 3){
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", ApplicationDataTypes.ICMP);
            map.put("data", packet);
            map.put("source", source);
            map.put("ttl", ttl);
            for(Application application: applications){
                if (application.identifier == packet.get_identifier()){
                    application.add_to_buffer(map);
                    application.interrupt();
                    trash = false;
                    break;
                }
            }
            if (trash){
                add_to_trash(map);
            }
        // echo request
        }else if (packet.get_type() == 8){
            Data data = ICMP.create_echo_reply(packet.get_identifier(), packet.get_sequence_number());
            send_data(data, source, 255, false);
        }
    }

    @Override
    // set ip address of given interface
    public IPv4MessageTypes set_interface_ip(int int_number, long ip_address, long mask){
        IPv4MessageTypes message = super.set_interface_ip(int_number, ip_address, mask);
        if (message == IPv4MessageTypes.is_valid) {
            boolean is_up = net_card.is_interface_up(int_number);
            down_interface(int_number);
            net_card.get_interface(int_number).set_ip_address(ip_address, mask);
            if (is_up) {
                long net_address = ip_address & mask;
                add_connected_route(net_address, mask, int_number);
                up_interface(int_number);
            }
        }
        return message;
    }

    @Override
    // delete ip address of given interface
    public void delete_interface_ip(int int_number){
        Map<String, Long> ip_address = net_card.get_ip_address(int_number);
        long net_address = ip_address.get("address") & ip_address.get("mask");
        delete_route(RouteCode.C, 0, 0, net_address,ip_address.get("mask"),-1, int_number);
        super.delete_interface_ip(int_number);
    }

    // add static route to routing table
    public void add_static_route(long ip_address, long mask, int int_number){
        long net_address = ip_address & mask;
        routing_table.add_route(RouteCode.S, 1,0, net_address, mask, -1, int_number);
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
    public void send_data(Data data, long destination_address, int ttl, boolean show_on_monitor){
        // check if packet isn't for this router
        boolean for_this_router = false;
        int card_int_number = net_card.get_int_number();
        for (int i = 0; i < card_int_number; i++){
            if(destination_address == net_card.get_ip_address(i).get("address")){
                for_this_router = true;
                break;
            }
        }
        if(for_this_router){
            // log
            System.out.println(name + ": packet sent to " +
                    IPv4.parse_to_string(destination_address));
            // log
            System.out.println(name + ": packet received from " +
                    IPv4.parse_to_string(destination_address));

            // if packet is for this router handle data
            if (data instanceof ICMPPacket icmp_packet){
                handle_icmp_packet(icmp_packet, destination_address, destination_address, ttl);
            }
        }else{
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
                // log
                System.out.println(name + ": packet sent to " +
                        IPv4.parse_to_string(destination_address));
            }else{
                // if route was not found
                if (show_on_monitor){
                    ICMPPacket dest_unreachable;
                    if (data instanceof ICMPPacket icmp_packet) {
                        dest_unreachable = ICMP.create_dest_unreachable(icmp_packet.get_identifier(),
                                icmp_packet.get_sequence_number());
                    }else{
                        dest_unreachable = ICMP.create_dest_unreachable();
                    }
                    handle_icmp_packet(dest_unreachable, -1, destination_address, ttl);
                }
            }
        }

    }

    public void send_data(Data data, long destination_address){
        send_data(data, destination_address, 255, true);
    }

    // send ipv4 packet, use for routing purpose
    public void send_ipv4_packet(IPv4Packet packet){
        // find route to given destination
        int route_index = routing_table.find_best_route(packet.get_destination_address());
        // if route was found
        if (route_index != -1){
            Route route = routing_table.get_route(route_index);
            // find interface
            int int_number = route.int_number();
            // make frame
            Frame frame = new SimpleP2PFrame(packet);
            // send frame
            add_out_traffic(frame, int_number);
            // log
            System.out.println(name + ": packet sent to " +
                    IPv4.parse_to_string(packet.get_destination_address()));
        }else{
            // if route was not found
            ICMPPacket dest_unreachable;
            if (packet.get_data() instanceof ICMPPacket icmp_packet) {
                dest_unreachable = ICMP.create_dest_unreachable(icmp_packet.get_identifier(),
                        icmp_packet.get_sequence_number());
            }else{
                dest_unreachable = ICMP.create_dest_unreachable();
            }
            send_data(dest_unreachable, packet.get_source_address());
        }
    }

    // ping
    public void ping(long destination_address){
        applications.add(new Ping(generate_app_id(),this, destination_address));
    }

    // return routing table in string
    public String get_routing_table_string(){
        return routing_table.to_string();
    }

    // get routing table size
    public int get_routing_table_size(){
        return routing_table.get_size();
    }

    // route getter
    public Route get_route(int route_index){
        return routing_table.get_route(route_index);
    }
}
