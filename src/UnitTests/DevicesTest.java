package UnitTests;

import Devices.Link;
import Devices.NetworkCard;
import Protocols.SimpleP2PFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevicesTest {

    // test of connection between network cards
    // tested functions:
    // Link: all
    // NetworkInterface: add_frame(), handle_frame(), is_out_empty(), get_frame()
    // NetworkCard: add_out_traffic(), is_buffer_empty(), get_frame_from_buffer()
    @Test
    public void connection(){
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

        // frames to send
        SimpleP2PFrame frame1 = new SimpleP2PFrame();
        SimpleP2PFrame frame2 = new SimpleP2PFrame();
        SimpleP2PFrame frame3 = new SimpleP2PFrame();

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

}