package Protocols;

public class ICMPPacket implements Data{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    private final int type;
    private final int code;
    private final int check_sum;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //default constructor
    public ICMPPacket() {
        //msg type: 0- echo reply, 3- destination unreachable, 8- echo request
        this.type = 0;
        this.code = 0;
        this.check_sum = 0;
    }

    //constructor
    public ICMPPacket(int type) {
        //msg type: 0- echo reply, 3- destination unreachable, 8- echo request
        this.type = type;
        this.code = 0;
        this.check_sum = 0;
    }


    @Override
    public String to_string() {
        return null;
    }
}
