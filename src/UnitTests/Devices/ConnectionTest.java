package UnitTests.Devices;

import Devices.*;
import Protocols.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {
    // test cards
    NetworkCard network_card1 = new NetworkCard(2);
    NetworkCard network_card2 = new NetworkCard(1);
    NetworkCard network_card3 = new NetworkCard(1);

    // connection between card1 and card2
    Link link1 = new Link(network_card1.get_interface(0),
            network_card2.get_interface(0));

    // connection between card1 and card3
    Link link2 = new Link(network_card1.get_interface(1),
            network_card3.get_interface(0));

    // test frames to send
    SimpleP2PFrame frame1 = new SimpleP2PFrame();
    SimpleP2PFrame frame2 = new SimpleP2PFrame();
    SimpleP2PFrame frame3 = new SimpleP2PFrame();

    // test of connection between network cards
    // tested functions:
    // Link: all
    // NetworkInterface: add_frame(), handle_frame(), is_out_empty(), get_frame()
    // NetworkCard: add_out_traffic(), is_buffer_empty(), get_frame_from_buffer(),
    @Test
    public void connection(){
        // card1 sends frame1 to card2
        network_card1.add_out_traffic(frame1, 0);

        // card1 sends frame2 to card3
        network_card1.add_out_traffic(frame2, 1);

        // card2 sends frame3 to card1
        network_card2.add_out_traffic(frame3, 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check if sth was sent
        assertFalse(network_card1.is_buffer_empty());
        assertFalse(network_card2.is_buffer_empty());
        assertFalse(network_card3.is_buffer_empty());

        // check if right frame was sent
        assertEquals(frame1, network_card2.get_frame_from_buffer());
        assertEquals(frame2, network_card3.get_frame_from_buffer());
        assertEquals(frame3, network_card1.get_frame_from_buffer());
    }


    // test of interface up and down functions
    // tested functions:
    // NetworkInterface: up(), down()
    // NetworkCard: down_interface(), up_interface()
    @Test
    public void up_down_int(){
        // turn off ports in card1
        network_card1.down_interface(0);
        network_card1.down_interface(1);

        // card2 sends frame3 to card1
        network_card2.add_out_traffic(frame3, 0);

        // card3 sends frame1 to card1
        network_card3.add_out_traffic(frame1, 0);

        // card1 sends frame2 to card2
        network_card1.add_out_traffic(frame2, 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ports are down buffers should be empty
        assertTrue(network_card1.is_buffer_empty());
        assertTrue(network_card2.is_buffer_empty());


        // turn up ports in card1
        network_card1.up_interface(0);
        network_card1.up_interface(1);

        // card2 sends frame3 to card1
        network_card2.add_out_traffic(frame3, 0);

        // card3 sends frame1 to card1
        network_card3.add_out_traffic(frame1, 0);

        // card1 sends frame2 to card2
        network_card1.add_out_traffic(frame2, 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ports are up buffers should not be empty
        assertFalse(network_card1.is_buffer_empty());
        assertFalse(network_card2.is_buffer_empty());
        assertEquals(frame2, network_card2.get_frame_from_buffer());
    }

    // test of connection between routers
    // tested functions:
    // Router:
    @Test
    public void connection_between_routers(){
        // test Routers
        Router r1 = new Router("R1",2, true);
        Router r2 = new Router("R2",1, true);
        Router r3 = new Router("R3",1, true);

        // link between R1 and R2
        Link link3 = new Link(r1.get_interface(0), r2.get_interface(0));

        // link between R1 and R3
        Link link4 = new Link(r1.get_interface(1), r3.get_interface(0));

        // check if buffers are clear before test
        assertTrue(r1.is_buffer_empty());
        assertTrue(r2.is_buffer_empty());
        assertTrue(r3.is_buffer_empty());

        // R1 sends frame1 to R2
        r1.add_out_traffic(frame1, 0);

        // R1 sends frame2 to R3
        r1.add_out_traffic(frame2, 1);

        // R2 sends frame3 to R1
        r2.add_out_traffic(frame3, 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check if sth was sent
        assertFalse(r1.is_buffer_empty());
        assertFalse(r2.is_buffer_empty());
        assertFalse(r3.is_buffer_empty());
    }

    // test of connection between routers
    // tested functions:
    // Router: send_data, handle_frame, handle_simple_P2P_frame, handle_ipv4_packet
    // handle_icmp_packet, send_ipv4_packet
    @Test
    public void routing_between_routers(){
        Router r1 = new Router("R1",1);
        Router r2 = new Router("R2",2);
        Router r3 = new Router("R3",1);

        // link between R1 and R2, net 192.168.0.0/24
        Link link3 = new Link(r1.get_interface(0), r2.get_interface(0));

        // link between R2 and R3, net 192.168.1.0/24
        Link link4 = new Link(r2.get_interface(1), r3.get_interface(0));

        // set ip addresses
        r1.set_interface_ip(0, IPv4.parse_to_long("192.168.0.1"),
                IPv4.parse_mask_to_long("24"));

        r2.set_interface_ip(0, IPv4.parse_to_long("192.168.0.2"),
                IPv4.parse_mask_to_long("24"));
        r2.set_interface_ip(1, IPv4.parse_to_long("192.168.1.2"),
                IPv4.parse_mask_to_long("24"));
        r3.set_interface_ip(0, IPv4.parse_to_long("192.168.1.1"),
                IPv4.parse_mask_to_long("24"));

        // data to send
        Data data = ICMP.create_echo_request();

        // send data from r1 to r1
        r1.send_data(data, IPv4.parse_to_long("192.168.0.1"));

        // test
        assertEquals(data.to_string() + "\n", r1.get_monitor());

        // send data from r1 to r2
        r1.send_data(data, IPv4.parse_to_long("192.168.0.2"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // test
        assertEquals(data.to_string() + "\n", r2.get_monitor());

        // add static route on r1 to r3
        r1.add_static_route(IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_mask_to_long("24"),0);

        // send data from r1 to r2 interface 1
        r1.send_data(data, IPv4.parse_to_long("192.168.1.2"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // test
        assertEquals(data.to_string() + "\n" + data.to_string() + "\n", r2.get_monitor());

        // send data from r1 to r3
        r1.send_data(data, IPv4.parse_to_long("192.168.1.1"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // test
        assertEquals(data.to_string() + "\n", r3.get_monitor());
    }
}