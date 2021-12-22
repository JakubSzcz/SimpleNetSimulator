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

    //checking converting mask between '"/xx"' or '"xx"'-short mask, '"x.x.x.x"-dotted' and 'xL'-long number types

    //dotted -> long and short -> long
    @Test
    void parse_mask_to_long() {
        assertEquals(0L, IPv4.parse_mask_to_long("0.0.0.0"));
        assertEquals(0L, IPv4.parse_mask_to_long("0"));
        assertEquals(0L, IPv4.parse_mask_to_long("/0"));
        assertEquals(4294967040L, IPv4.parse_mask_to_long("255.255.255.0"));
        assertEquals(4294967040L, IPv4.parse_mask_to_long("24"));
        assertEquals(4294967040L, IPv4.parse_mask_to_long("/24"));
        assertEquals(4294967295L, IPv4.parse_mask_to_long("255.255.255.255"));
        assertEquals(4294967295L, IPv4.parse_mask_to_long("32"));
        assertEquals(4294967295L, IPv4.parse_mask_to_long("/32"));
    }
    //short -> dotted and long -> dotted
    @Test
    void parse_mask_to_string_dot() {
        assertEquals("255.255.255.255", IPv4.parse_mask_to_string_dot("/32"));
        assertEquals("255.255.255.255", IPv4.parse_mask_to_string_dot(4294967295L));
        assertEquals("0.0.0.0", IPv4.parse_mask_to_string_dot("0"));
        assertEquals("0.0.0.0", IPv4.parse_mask_to_string_dot(0));
        assertEquals("255.255.255.0", IPv4.parse_mask_to_string_dot("24"));
        assertEquals("255.255.255.0", IPv4.parse_mask_to_string_dot(4294967040L));
    }
    //long -> short and dotted -> short (with '/' and without '/')
    @Test
    void parse_mask_to_string_short() {
        assertEquals("24", IPv4.parse_mask_to_string_short(4294967040L));
        assertEquals("/24", IPv4.parse_mask_to_string_short(4294967040L,true));
        assertEquals("32", IPv4.parse_mask_to_string_short(4294967295L));
        assertEquals("/32", IPv4.parse_mask_to_string_short(4294967295L,true));
        assertEquals("0", IPv4.parse_mask_to_string_short(0L));
        assertEquals("/0", IPv4.parse_mask_to_string_short(0L,true));
        assertEquals("24", IPv4.parse_mask_to_string_short("255.255.255.0"));
        assertEquals("/24", IPv4.parse_mask_to_string_short("255.255.255.0",true));
        assertEquals("32", IPv4.parse_mask_to_string_short("255.255.255.255"));
        assertEquals("/32", IPv4.parse_mask_to_string_short("255.255.255.255",true));
        assertEquals("0", IPv4.parse_mask_to_string_short("0.0.0.0"));
        assertEquals("/0", IPv4.parse_mask_to_string_short("0.0.0.0",true));
    }
}