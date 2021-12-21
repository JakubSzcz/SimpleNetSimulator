package Devices;

import Protocols.IPv4;

public record Route(RouteCode code, int distance, int metric, long net, long net_mask, long gateway, int int_number) {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // to string method
    public String to_string() {
        // code
        String to_return = code.toString();
        // ip address
        to_return = to_return + " " + IPv4.parse_to_string(net);
        // mask
        to_return = to_return + " " + IPv4.parse_to_string(net_mask);
        // [distance/metric]
        to_return = to_return + " [" + distance + "/" + metric + "] ";
        // gateway
        to_return = to_return + "via " + IPv4.parse_to_string(gateway);
        // port
        to_return = to_return + ", interface" + int_number;
        return to_return;
    }

    // check if two Routes are identical
    public boolean is_identical(Route route){
        return route.to_string().equals(this.to_string());
    }

    // returns true if nets and mask are the same
    public boolean is_similar(Route route){
        boolean to_return = true;
        if (this.net_mask != route.net_mask){
            to_return = false;
        }else if(this.net != route.net){
            to_return = false;
        }
        return to_return;
    }
}
