package Protocols;

public class SimpleP2P {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // creates default SimpleP2PFrame
    public static SimpleP2PFrame create_frame(){
        return new SimpleP2PFrame();
    }

    // creates SimpleP2PFrame
    public static SimpleP2PFrame create_frame(long source_address, long destination_address, int time_to_live, Data data){
        return new SimpleP2PFrame(source_address, destination_address, time_to_live, data);
    }
}
