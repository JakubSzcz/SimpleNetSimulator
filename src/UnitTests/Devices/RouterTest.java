package UnitTests.Devices;

import Devices.Route;
import Devices.RouteCode;
import Protocols.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    // check to_string function from Route
    @Test
    public void route_to_string(){
        Route route = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        assertEquals("C 192.168.1.0 255.255.255.0 [0/0] via 192.168.1.1, interface0", route.to_string());

        route = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                1);
        assertEquals("S 192.168.1.0 255.255.255.0 [1/0] via 192.168.1.1, interface1", route.to_string());
    }

    // check is_identical function from Route
    @Test
    public void route_is_identical(){
        Route route = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route1 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);

        Route route2 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                1);
        assertTrue(route.is_identical(route1));
        assertFalse(route.is_identical(route2));
        assertFalse(route1.is_identical(route2));
    }

    // check is_similar function from Route
    @Test
    public void route_is_similar(){
        Route route = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route1 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                1);

        Route route2 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        assertTrue(route.is_similar(route1));
        assertFalse(route.is_similar(route2));
        assertFalse(route1.is_similar(route2));
    }
}