package UnitTests;

import Devices.NetworkCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevicesTest {

    // test of connection between network cards
    // tested functions:
    // Link: all
    // NetworkInterface: add_frame(), handle_frame(), is_out_empty(), get_frame()
    // NetworkCard: add_out_traffic()
    @Test
    public void connection(){
        // test cards
        NetworkCard network_card1 = new NetworkCard(2);
        NetworkCard network_card2 = new NetworkCard(1);
        NetworkCard network_card3 = new NetworkCard(1);
    }

}