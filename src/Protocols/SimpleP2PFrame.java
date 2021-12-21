package Protocols;

public class SimpleP2PFrame implements Frame{

    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    //name of the layer 3 protocol
    private final Layer3Protocols l3_protocol;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //constructor
    public SimpleP2PFrame() {
        this.l3_protocol = Layer3Protocols.IPv4;
    }

    //print frame's content
    @Override
    public String to_string() {
        return null;
    }
}
