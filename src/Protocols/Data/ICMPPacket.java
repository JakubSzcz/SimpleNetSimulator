package Protocols.Data;

public class ICMPPacket implements Data {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // basic
    private final int type;
    private final int code;
    private final int check_sum;

    // extended
    private final int identifier;
    private final int sequence_number;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // default constructor
    public ICMPPacket() {
        this(0,0,0);
    }

    // constructor
    public ICMPPacket(int type) {
        this(type, 0, 0);
    }

    public ICMPPacket(int type, int identifier, int sequence_number){
        //msg type: 0- echo reply, 3- destination unreachable, 8- echo request
        this.type = type;
        this.code = 0;
        this.check_sum = 0;
        //
        this.identifier = identifier;
        this.sequence_number = sequence_number;
    }

    // parse content to string
    @Override
    public String to_string() {
        String to_return = "Internet Control Message Protocol\n";
        to_return += "Type " + type + "\n" + "Code " + code + "\n" + "Checksum " + check_sum + "\n";
        return to_return;
    }

    // type getter
    public int get_type() {
        return type;
    }

    // code getter
    public int get_code() {
        return code;
    }

    // identifier getter
    public int get_identifier() {
        return identifier;
    }

    // sequence number getter
    public int get_sequence_number() {
        return sequence_number;
    }
}
