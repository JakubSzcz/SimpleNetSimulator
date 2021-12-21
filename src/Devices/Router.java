package Devices;

public class Router extends NetworkDevice{
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // normal case constructor
    public Router(String name, int int_number){
        super(name, int_number);
    }

    // test case constructor
    public Router(String name, int int_number, boolean test){
        super(name, int_number, test);
    }

    // actions taken after receiving a frame
    void handle_frame(){

    }

}
