package Protocols.Data;

import Protocols.Data.Data;

public class ICMPPacket implements Data {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final int type;
    private final int code;
    private final int check_sum;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // default constructor
    public ICMPPacket() {
        //msg type: 0- echo reply, 3- destination unreachable, 8- echo request
        this.type = 0;
        this.code = 0;
        this.check_sum = 0;
    }

    // constructor
    public ICMPPacket(int type) {
        //msg type: 0- echo reply, 3- destination unreachable, 8- echo request
        this.type = type;
        this.code = 0;
        this.check_sum = 0;
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
}
