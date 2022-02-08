package Application;

import Devices.Devices.Router;
import Protocols.Data.ICMP;
import Protocols.Data.ICMPPacket;
import Protocols.Packets.IPv4;

import java.util.ArrayList;
import java.util.Collections;
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

    // how many ping are to send
    private final int how_many;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public Ping(int identifier, Router parent_router, long destination_address, int wait_time) {
        super(identifier);
        this.parent_router = parent_router;
        this.destination_address = destination_address;
        this.wait_time = wait_time;
        this.how_many = 4;
        start();
    }

    public Ping(int identifier, Router parent_router, long destination_address){
        this(identifier, parent_router, destination_address, 5);
    }

    // run
    @Override
    public void run() {
        parent_router.add_line_to_monitor("Pinging with 32 bytes of data:");
        long start;
        long stop;
        long elapsed;
        long received = 0;
        ArrayList<Long> times = new ArrayList<>();
        OUTER: for (int i = 0; i < how_many; i++){
            // send icmp request
            parent_router.send_data(ICMP.create_echo_request(identifier, i + 1), destination_address);
            start = System.currentTimeMillis();
            try {
                Thread.sleep(wait_time * 1000);
            // if is echo replay
            } catch (InterruptedException e) {
                for (HashMap<String, Object> packet: buffer){
                    if (packet.get("data") instanceof ICMPPacket icmp_packet){
                        if (icmp_packet.get_type() == 0 || icmp_packet.get_type() == 3){
                            stop = System.currentTimeMillis();
                            elapsed = stop - start;
                            times.add(elapsed);
                            parent_router.add_line_to_monitor(ICMP.get_message(icmp_packet,
                                    (long)packet.get("source"), (int)packet.get("ttl"), elapsed));
                            buffer.clear();
                            received++;
                            continue OUTER;
                        }
                    }
                }
            }
            parent_router.add_line_to_monitor("Request time out.");
        }
        parent_router.add_line_to_monitor(String.format("\nPing statistics for %s:",
                IPv4.parse_to_string(destination_address)));
        double loss = (double)(how_many - received) / how_many * 100;
        parent_router.add_line_to_monitor(String.format("Packets: Sent = %d, Received = %d, " +
                "Lost = %d (%d%% loss)", how_many, received, how_many - received,
                (int)loss));
        if (times.size() > 0){
            Collections.sort(times);
            long average = 0;
            for (long time: times){
                average += time;
            }
            average /= times.size();
            parent_router.add_line_to_monitor("Approximate round trip times in milli-seconds:");
            parent_router.add_line_to_monitor(String.format("Minimum = %dms, Maximum = %dms, Average = %dms\n",
                    times.get(0), times.get(times.size() - 1), average));
        }
        parent_router.remove_application(identifier);
    }
}
