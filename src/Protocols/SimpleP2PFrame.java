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
    //default constructor
    public SimpleP2PFrame() {
        this.l3_protocol = Layer3Protocols.IPv4;
        this.packet = new IPv4Packet();
    }

    //parse frame's content
    @Override
    public String to_string() {
        return null;
    }
}
