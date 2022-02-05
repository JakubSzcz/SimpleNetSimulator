package Application;

import Devices.Devices.Router;
import Protocols.Data.ICMP;
import Protocols.Data.ICMPPacket;
import java.util.HashMap;


public class Ping extends Application{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // destination
    private final long destination_address;

    // parent_router
    private final Router parent_router;

    // wait time in seconds
    private final int wait_time;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Ping(int identifier, Router parent_router, long destination_address, int wait_time) {
        super(identifier);
        this.parent_router = parent_router;
        this.destination_address = destination_address;
        this.wait_time = wait_time;
        start();
    }

    public Ping(int identifier, Router parent_router, long destination_address){
        this(identifier, parent_router, destination_address, 5);
    }

    // run
    @Override
    public void run() {
        parent_router.add_line_to_monitor("Pinging with 32 bytes of data:");
        OUTER: for (int i = 0; i < 4; i++){
            // send icmp request
            parent_router.send_data(ICMP.create_echo_request(identifier, i + 1), destination_address);
            try {
                Thread.sleep(wait_time * 1000);
            // if is echo replay
            } catch (InterruptedException e) {
                for (HashMap<String, Object> packet: buffer){
                    if (packet.get("data") instanceof ICMPPacket icmp_packet){
                        if (icmp_packet.get_type() == 0 || icmp_packet.get_type() == 3){
                            parent_router.add_line_to_monitor(ICMP.get_message(icmp_packet,
                                    (long)packet.get("source"), (int)packet.get("ttl")));
                            buffer.clear();
                            continue OUTER;
                        }
                    }
                }
            }
            parent_router.add_line_to_monitor("Request time out.");
        }
        parent_router.remove_application(identifier);
    }
}
