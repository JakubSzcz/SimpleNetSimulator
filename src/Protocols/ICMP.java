package Protocols;

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
    public static String get_message(ICMPPacket icmp_packet, long destination_address, long source_address, int ttl){
        StringBuilder to_return = new StringBuilder("Pinging ");
        to_return.append(destination_address).append(" with 32 bytes of data\n").append("Replay from ");
        switch (icmp_packet.get_type()){
            // display echo replay
            case 0:
                to_return.append(destination_address).append(": bytes=32 TTL=").append(ttl).append(".");
                return to_return.toString();
            // display 'Destination unreachable message'
            case 3:
                to_return.append(source_address).append(": Destination host unreachable.");
                return  to_return.toString();
            //display 'Echo request'
            case 8:
                return null;
        }
        return null;
    }
}
