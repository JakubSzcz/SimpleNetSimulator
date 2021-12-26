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
}
