package Devices.Routing;

import Protocols.Packets.IPv4;

public record Route(RouteCode code, int distance, int metric, long net, long net_mask, long gateway, int int_number) {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // to string method
    public String to_string() {
        StringBuilder to_return = new StringBuilder();
        // code
        to_return.append(code.toString());
        // ip address
        to_return.append(" ");
        to_return.append(IPv4.parse_to_string(net));
        // mask
        to_return.append(" ");
        to_return.append(IPv4.parse_to_string(net_mask));
        // [distance/metric]
        to_return.append(" [");
        to_return.append(distance);
        to_return.append("/");
        to_return.append(metric);
        to_return.append("] ");
        // gateway
        to_return.append("via ");
        if (gateway == -1){
            to_return.append("---");
        }else{
            to_return.append(IPv4.parse_to_string(gateway));
        }
        // port
        to_return.append(", interface");
        to_return.append(int_number);
        return to_return.toString();
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
