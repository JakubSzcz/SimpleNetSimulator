package Protocols.Data;

import Protocols.Packets.IPv4;

public class ICMP {
    // create default ICMP packet
    public static ICMPPacket create_packet(){
        return new ICMPPacket();
    }
    // create echo reply
    public static ICMPPacket create_echo_reply(){
        return new ICMPPacket(0);
    }
    // create destination unreachable
    public static ICMPPacket create_dest_unreachable(){
        return new ICMPPacket(3);
    }
    // create echo request
    public static ICMPPacket create_echo_request(){
        return new ICMPPacket(8);
    }
    // return proper message according to the ICMP packet type
    public static String get_message(ICMPPacket icmp_packet, long source_address, int ttl){
        StringBuilder to_return = new StringBuilder("Replay from ");
        switch (icmp_packet.get_type()){
            // display echo replay
            case 0:
                to_return.append(IPv4.parse_to_string(source_address)).append(": bytes=32 TTL=").append(ttl).append(".");
                return to_return.toString();
            // display 'Destination unreachable message'
            case 3:
                if(source_address > -1) {
                    to_return.append(IPv4.parse_to_string(source_address)).append(": Destination host unreachable.");
                    return to_return.toString();
                }else if(source_address == -1){
                    to_return.append("localhost: Destination host unreachable.");
                }
            //display 'Echo request'
            case 8:
                //to_return.append(IPv4.parse_to_string(destination_address)).append(": bytes=32 TTL=").append(ttl).append(".");
                //return to_return.toString();
                return null;
        }
        return null;
    }
}
