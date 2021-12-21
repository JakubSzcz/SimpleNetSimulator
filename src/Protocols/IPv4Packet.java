package Protocols;

public class IPv4Packet implements Packet{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    //contents of IPv4 packet
    final private int version;
    final private int ihl;
    final private int dscp;
    final private int enc;
    final private int total_length;
    final private int identification;
    final private int falgs;
    final private int fragment_offset;
    final private int time_to_live;
    final private int protocol;
    final private int header_checksum;
    final private long source_address;
    final private long destination_address;
    final private int option;
    final private Data data;


    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //default constructor
    public IPv4Packet() {
        //ip addresses
        this.source_address = 0;
        this.destination_address = 1;
        //ttl value
        this.time_to_live = 255;
        //data
        this.data = new ICMPPacket();
        //default values
        this.version = 4;
        this.ihl = 0;
        this.dscp = 0;
        this.enc = 0;
        this.total_length = 0;
        this.identification = 0;
        this.falgs = 0;
        this.fragment_offset = 0;
        this.protocol = 0;
        this.header_checksum = 0;
        this.option = 0;
    }

    //constructor
    public IPv4Packet(long source_address, long destination_address, int time_to_live, Data data) {
        //ip addresses
        this.source_address = source_address;
        this.destination_address = destination_address;
        //ttl value
        this.time_to_live = time_to_live;
        //data
        this.data = data;
        //default values
        this.version = 4;
        this.ihl = 0;
        this.dscp = 0;
        this.enc = 0;
        this.total_length = 0;
        this.identification = 0;
        this.falgs = 0;
        this.fragment_offset = 0;
        this.protocol = 0;
        this.header_checksum = 0;
        this.option = 0;
    }


    @Override
    public String to_string() {
        return null;
    }
}
