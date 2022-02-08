package UnitTests.Application;

import Devices.Devices.Router;
import Devices.Link;
import Protocols.Data.ICMP;
import Protocols.Data.ICMPPacket;
import Protocols.Packets.IPv4;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PingTest {
    @Test
    public void simple_ping(){
        Router r1 = new Router("R1", 1);
        Router r2 = new Router("R2", 1);
        Link link = new Link(r1.get_interface(0), r2.get_interface(0));
        r1.set_interface_ip(0, IPv4.parse_to_long("192.168.1.1"),
                IPv4.parse_mask_to_long("24"));
        r2.set_interface_ip(0, IPv4.parse_to_long("192.168.1.2"),
                IPv4.parse_mask_to_long("24"));
        r1.ping(IPv4.parse_to_long("192.168.1.2"));
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuilder expected = new StringBuilder();
        expected.append("Pinging with 32 bytes of data:\n");
        ICMPPacket packet = ICMP.create_echo_reply();
        String icmp_message = ICMP.get_message(packet, IPv4.parse_to_long("192.168.1.2"),
                255);
        expected.append(icmp_message).append("\n");
        expected.append(icmp_message).append("\n");
        expected.append(icmp_message).append("\n");
        expected.append(icmp_message).append("\n");
        // check
        assertEquals(expected.toString(), r1.get_monitor());
    }
}