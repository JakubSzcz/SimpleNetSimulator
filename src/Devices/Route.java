package Devices;

public record Route(RouteCode code, int distance, long net, long net_mask, long gateway, int int_number) {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public String to_string() {
        String to_return = code.toString();
        to_return = to_return + " ";
        return to_return;
    }
}
