package UnitTests.Protocols;

import Protocols.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolsTest {
    @Test
    void to_string() {
    }

    @Test
    //check if the ip address in string was correctly parsed to the int type
    void parse_to_long() {
        assertEquals(3232235777L, IPv4.parse_to_long("192.168.1.1"));
        assertEquals(2888455179L, IPv4.parse_to_long("172.42.84.11"));
        assertEquals(0L, IPv4.parse_to_long("0.0.0.0"));
    }

    @Test
    void is_valid() {
    }

    @Test
    ////check if the ip address in int was correctly parsed to the string type
    void parse_to_string() {
        assertEquals("192.168.1.1", IPv4.parse_to_string(3232235777L));
        assertEquals("172.42.84.11", IPv4.parse_to_string(2888455179L));
        assertEquals("0.0.0.1", IPv4.parse_to_string(1L));
        assertEquals("0.2.0.1", IPv4.parse_to_string(131073L));
    }

    @Test
    void smask_string_to_long() {
        assertEquals(0L, IPv4.smask_string_to_long("0"));
        assertEquals(4294967295L, IPv4.smask_string_to_long("32"));
        assertEquals(4294967040L, IPv4.smask_string_to_long("24"));
        assertEquals(4294967040L, IPv4.smask_string_to_long("/24"));
    }
}