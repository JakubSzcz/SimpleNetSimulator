package UnitTests.Protocols;

import Protocols.Packets.IPv4;
import Protocols.Packets.IPv4MessageTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolsTest {

    @Test
    //check if the ip address in string was correctly parsed to the int type
    void parse_to_long() {
        assertEquals(3232235777L, IPv4.parse_to_long("192.168.1.1"));
        assertEquals(2888455179L, IPv4.parse_to_long("172.42.84.11"));
        assertEquals(0L, IPv4.parse_to_long("0.0.0.0"));
        assertEquals(168430081L, IPv4.parse_to_long("10.10.10.1"));
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

    //checks if ip address is valid
    @Test
    void is_ip_valid() {
        //is valid
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_ip_valid("255.255.255.0"));
        //is dotted format
        assertEquals(IPv4MessageTypes.is_not_dotted_format, IPv4.is_ip_valid("2552552550"));
        assertEquals(IPv4MessageTypes.is_not_dotted_format, IPv4.is_ip_valid(".2552552..550"));
        //must be int
        assertEquals(IPv4MessageTypes.octet_value_must_be_int, IPv4.is_ip_valid("255.255.255.test"));
        assertEquals(IPv4MessageTypes.octet_value_must_be_int, IPv4.is_ip_valid("Lorem .ipsum .dolor .sit"));
        assertEquals(IPv4MessageTypes.octet_value_must_be_int, IPv4.is_ip_valid("0,5.3/2.0,02.test"));
        assertEquals(IPv4MessageTypes.octet_value_must_be_int, IPv4.is_ip_valid("0,523.0.242.12,423"));
        //must be positive
        assertEquals(IPv4MessageTypes.octet_value_must_be_positive, IPv4.is_ip_valid("192.168.1.-2"));
        assertEquals(IPv4MessageTypes.octet_value_must_be_positive, IPv4.is_ip_valid("192.-168.1.2"));
        //is too big
        assertEquals(IPv4MessageTypes.octet_value_is_too_big, IPv4.is_ip_valid("192.4258.1.2"));
    }

    //check if net mask is valid
    @Test
    void is_mask_valid() {
        //is valid
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_mask_valid("192.0.0.0"));
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_mask_valid("255.255.255.0"));
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_mask_valid("24"));
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_mask_valid("/24"));
        // is not int
        assertEquals(IPv4MessageTypes.mask_value_must_be_int, IPv4.is_mask_valid("abc"));
        assertEquals(IPv4MessageTypes.mask_value_must_be_int, IPv4.is_mask_valid("a24"));
        assertEquals(IPv4MessageTypes.mask_value_must_be_int, IPv4.is_mask_valid("/bc"));
        assertEquals(IPv4MessageTypes.mask_value_must_be_int, IPv4.is_mask_valid("0.5"));
        //is too big
        assertEquals(IPv4MessageTypes.mask_value_is_to_big, IPv4.is_mask_valid("33"));
        assertEquals(IPv4MessageTypes.mask_value_is_to_big, IPv4.is_mask_valid("/33"));
        //must be positive
        assertEquals(IPv4MessageTypes.mask_value_must_be_positive, IPv4.is_mask_valid("-24"));
        assertEquals(IPv4MessageTypes.mask_value_must_be_positive, IPv4.is_mask_valid("/-8"));
        //is not mask
        assertEquals(IPv4MessageTypes.mask_value_is_incorrect, IPv4.is_mask_valid("192.255.255.0"));
        assertEquals(IPv4MessageTypes.mask_value_is_incorrect, IPv4.is_mask_valid("7.5.0.0"));
        assertEquals(IPv4MessageTypes.octet_value_is_too_big, IPv4.is_mask_valid("256.0.255.0"));
        assertEquals(IPv4MessageTypes.octet_value_must_be_int, IPv4.is_mask_valid("abc.0.255.0"));
    }

    @Test
    void is_interface_ip_valid(){
        // is_net_address
        assertEquals(IPv4MessageTypes.is_net_address, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("/24")));
        assertEquals(IPv4MessageTypes.is_net_address, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.68"), IPv4.parse_mask_to_long("/30")));

        // mask_is_over_30
        assertEquals(IPv4MessageTypes.mask_is_over_30, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("31")));
        assertEquals(IPv4MessageTypes.mask_is_over_30, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("/32")));
        assertEquals(IPv4MessageTypes.mask_is_over_30, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("/40")));

        // is_broadcast_address
        assertEquals(IPv4MessageTypes.is_broadcast_address, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.255"), IPv4.parse_mask_to_long("/24")));
        assertEquals(IPv4MessageTypes.is_broadcast_address, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.71"), IPv4.parse_mask_to_long("/30")));

        // is valid
        // is_net_address
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.2"), IPv4.parse_mask_to_long("/24")));
        assertEquals(IPv4MessageTypes.is_valid, IPv4.is_interface_ip_valid(
                IPv4.parse_to_long("192.168.1.70"), IPv4.parse_mask_to_long("/30")));
    }
}
