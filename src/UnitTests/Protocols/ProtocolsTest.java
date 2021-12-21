package UnitTests.Protocols;

import Protocols.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolsTest {
    //instance of IPv4 class
    IPv4 ipv4 = new IPv4();
    @Test
    void to_string() {
    }

    @Test
    //check if the ip address in string was correctly parsed to the int type
    void parse_to_long() {
        assertEquals(3232235777l, ipv4.parse_to_long("192.168.1.1"));
        assertEquals(2888455179l, ipv4.parse_to_long("172.42.84.11"));
        assertEquals(0l, ipv4.parse_to_long("0.0.0.0"));
    }

    @Test
    void is_valid() {
    }

    @Test
    ////check if the ip address in int was correctly parsed to the string type
    void parse_to_string() {
        assertEquals("192.168.1.1", ipv4.parse_to_string(3232235777l));
        assertEquals("172.42.84.11", ipv4.parse_to_string(2888455179l));
        assertEquals("0.0.0.1", ipv4.parse_to_string(1l));
        assertEquals("0.2.0.1", ipv4.parse_to_string(131073l));
    }
}