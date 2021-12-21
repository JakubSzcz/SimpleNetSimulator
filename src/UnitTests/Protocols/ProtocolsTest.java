package UnitTests.Protocols;

import Protocols.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolsTest {

    @Test
    void to_string() {
    }

    @Test
    //check if the string was correctly parsed to int
    void parse_to_long() {
        IPv4 ipv4 = new IPv4();
        assertEquals(3232235777l, ipv4.parse_to_long("192.168.1.1"));
        assertEquals(2888455179l, ipv4.parse_to_long("172.42.84.11"));
        assertEquals(0l, ipv4.parse_to_long("0.0.0.0"));
    }
}