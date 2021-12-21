package Devices;

public record Route(String code, long net, long net_mask, long gateway, int int_number) {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public String to_string() {
        return "";
    }

}
