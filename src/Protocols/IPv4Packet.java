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
    final private int flags;
    final private int fragment_offset;
    private int time_to_live;
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
        this.flags = 0;
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
        this.flags = 0;
        this.fragment_offset = 0;
        this.protocol = 0;
        this.header_checksum = 0;
        this.option = 0;
    }

    //parse packet's content to string
    @Override
    public String to_string() {
        String to_return = "Internet Protocol Version: " + version + "\n" + "Version: " + version + "\n";
        to_return += "Differentiated Services Field: (DSCP: " + dscp + "; ENC: " + enc + ";\n";
        to_return += "Total Length: " + total_length + "\n" + "Identification: " + identification + "\n";
        to_return += "Flags: " + flags + "\n" + "Fragment Offset: " + fragment_offset + "\n";
        to_return += "Time to live: " + time_to_live + "\n" + "Protocol: " + protocol + "\n";
        to_return += "Internet Header Length: " + ihl + "\n "+ "Header checksum: " + header_checksum + "\n";
        to_return += "Option: " + option + "\n";
        to_return += "Source: " + source_address + "\n" + "Destination: " + destination_address + "\n";
        to_return += data.to_string();
        return to_return;
    }

    // data getter
    public Data get_data(){
        return data;
    }

    // ttl getter
    public int get_time_to_live() {
        return time_to_live;
    }

    // source getter
    public long get_source_address() {
        return source_address;
    }

    // destination getter
    public long get_destination_address() {
        return destination_address;
    }

    // reduce ttl
    public void reduce_ttl() {
        this.time_to_live = time_to_live - 1;
    }
}
