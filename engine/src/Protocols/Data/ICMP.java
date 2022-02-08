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
    public static ICMPPacket create_echo_reply(int identifier, int sequence_number){
        return new ICMPPacket(0, identifier, sequence_number);
    }
    // create destination unreachable
    public static ICMPPacket create_dest_unreachable(){
        return new ICMPPacket(3);
    }
    public static ICMPPacket create_dest_unreachable(int identifier, int sequence_number){
        return new ICMPPacket(3, identifier, sequence_number);
    }
    // create echo request
    public static ICMPPacket create_echo_request(){
        return new ICMPPacket(8);
    }
    public static ICMPPacket create_echo_request(int identifier, int sequence_number){
        return new ICMPPacket(8, identifier, sequence_number);
    }
    // return proper message according to the ICMP packet type
    public static String get_message(ICMPPacket icmp_packet, long source_address, int ttl, long time){
        StringBuilder to_return = new StringBuilder("Replay from ");
        switch (icmp_packet.get_type()){
            // display echo replay
            case 0:
                to_return.append(IPv4.parse_to_string(source_address)).append(": bytes=32 time=")
                        .append(time).append("ms TTL=").append(ttl).append(".");
                return to_return.toString();
            // display 'Destination unreachable message'
            case 3:
                if(source_address > -1) {
                    to_return.append(IPv4.parse_to_string(source_address)).append(": Destination host unreachable.");
                    return to_return.toString();
                }else if(source_address == -1){
                    to_return.append("localhost: Destination host unreachable.");
                    return to_return.toString();
                }
            //display 'Echo request'
            case 8:
                //to_return.append(IPv4.parse_to_string(destination_address)).append(": bytes=32 TTL=").append(ttl).append(".");
                //return to_return.toString();
                return null;
        }
        return null;
    }
    public static String get_message(ICMPPacket icmp_packet, long source_address, int ttl){
        return get_message(icmp_packet, source_address, ttl, -1);
    }
}
