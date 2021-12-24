package Protocols;

public class SimpleP2PFrame implements Frame{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    //name of the layer 3 protocol
    private final Layer3Protocols l3_protocol;

    //packet
    private final Packet packet;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //normal case constructor
    public SimpleP2PFrame(long source_address, long destination_address, int time_to_live, Data data) {
        this.l3_protocol = Layer3Protocols.IPv4;
        this.packet = new IPv4Packet( source_address, destination_address, time_to_live, data);
    }
    public SimpleP2PFrame(Packet packet){
        this.l3_protocol = Layer3Protocols.IPv4;
        this.packet = packet;
    }
    //default constructor
    public SimpleP2PFrame() {
        this.l3_protocol = Layer3Protocols.IPv4;
        this.packet = new IPv4Packet();
    }

    //parse frame's content to string
    @Override
    public String to_string() {
        String to_return = "Simple peer to peer frame\n" + " Layer 3 ISO/OSI protocol: " + l3_protocol + "\n";
        to_return += packet.to_string();
        return to_return;
    }
}
