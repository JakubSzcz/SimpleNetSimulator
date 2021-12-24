package UnitTests.Devices;

import Devices.*;
import Protocols.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoutingTableTest {

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

        route = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), -1,
                1);
        assertEquals("S 192.168.1.0 255.255.255.0 [1/0] via ---, interface1", route.to_string());
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

    // check RoutingTable add_route, find_similar_routes and delete_route methods
    @Test
    public void routing_table_add_route(){
        RoutingTable routing_table = new RoutingTable();
        Route route1 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.252"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route2 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        Route route3 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route4 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        Route route5 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.0.0"),
                IPv4.parse_to_long("255.255.0.0"), IPv4.parse_to_long("192.168.0.1"),
                0);
        Route route6 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.252"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route7 = new Route(RouteCode.R, 120, 10, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        routing_table.add_route(route1);
        routing_table.add_route(route2);
        routing_table.add_route(route3);
        routing_table.add_route(route4);
        routing_table.add_route(route5);
        routing_table.add_route(route6);
        routing_table.add_route(route7);

        // expected table
        String expected_table = route1.to_string() + "\n";
        expected_table = expected_table + route6.to_string() + "\n";
        expected_table = expected_table + route3.to_string() + "\n";
        expected_table = expected_table + route2.to_string() + "\n";
        expected_table = expected_table + route4.to_string() + "\n";
        expected_table = expected_table + route7.to_string() + "\n";
        expected_table = expected_table + route5.to_string() + "\n";

        // test
        assertEquals(expected_table, routing_table.to_string());

        // delete routes
        routing_table.delete_route(route1);
        routing_table.delete_route(route4);
        routing_table.delete_route(route7);
        routing_table.delete_route(route5);

        // expected table
        expected_table = route6.to_string() + "\n";
        expected_table = expected_table + route3.to_string() + "\n";
        expected_table = expected_table + route2.to_string() + "\n";

        // test
        assertEquals(expected_table, routing_table.to_string());
    }

    // check find_best_route method
    @Test
    public void routing_table_find_best_route() {
        // routing table
        RoutingTable routing_table = new RoutingTable();
        Route route1 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.252"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route2 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        Route route3 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route4 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        Route route5 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.0.0"),
                IPv4.parse_to_long("255.255.0.0"), IPv4.parse_to_long("192.168.0.1"),
                0);
        Route route6 = new Route(RouteCode.S, 1, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_to_long("255.255.255.252"), IPv4.parse_to_long("192.168.1.1"),
                0);
        Route route7 = new Route(RouteCode.R, 120, 10, IPv4.parse_to_long("192.168.2.0"),
                IPv4.parse_to_long("255.255.255.0"), IPv4.parse_to_long("192.168.2.1"),
                0);
        routing_table.add_route(route1);
        routing_table.add_route(route2);
        routing_table.add_route(route3);
        routing_table.add_route(route4);
        routing_table.add_route(route5);
        routing_table.add_route(route6);
        routing_table.add_route(route7);

        // tests
        int best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.1.1"));
        assertEquals(0, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("10.10.10.1"));
        assertEquals(-1, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.1.5"));
        assertEquals(2, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.2.190"));
        assertEquals(3, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.0.190"));
        assertEquals(6, best_route_index);

        // add default route
        routing_table.add_route(RouteCode.S, 1, 0, 0,
                0,IPv4.parse_to_long("192.168.1.1"), 0);

        // tests
        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.1.1"));
        assertEquals(0, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("10.10.10.1"));
        assertEquals(7, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.1.5"));
        assertEquals(2, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.2.190"));
        assertEquals(3, best_route_index);

        best_route_index = routing_table.find_best_route(IPv4.parse_to_long("192.168.0.190"));
        assertEquals(6, best_route_index);
    }

    // test of interfaces on routers
    // tested functions:
    // Router: add_connected_route, delete_route, up/down_interface
    // set_interface_ip, delete_interface_ip
    // NetworkCard: is_ip_set, get_ip_address
    // NetworkInterface: is_ip_set, get_ip_address, set_ip_address, delete_ip_address
    @Test
    public void route_add_after_interface_change() {
        // test Routers
        Router r1 = new Router("R1", 1, true);
        Router r2 = new Router("R2", 1, true);

        // link between R1 and R2
        Link link = new Link(r1.get_interface(0), r2.get_interface(0));

        // set ip on r1
        r1.set_interface_ip(0, IPv4.parse_to_long("192.168.1.1"),
                IPv4.parse_mask_to_long("/24"));

        // set ip on r2
        r2.set_interface_ip(0, IPv4.parse_to_long("192.168.1.2"),
                IPv4.parse_mask_to_long("/24"));

        // expected route
        Route route1 = new Route(RouteCode.C, 0, 0, IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_mask_to_long("24"), -1, 0);

        // tests
        assertEquals(route1.to_string() + "\n", r1.get_routing_table());
        assertEquals(route1.to_string() + "\n", r2.get_routing_table());

        // interfaces down
        r1.down_interface(0);
        r2.down_interface(0);

        // tests
        assertEquals("", r1.get_routing_table());
        assertEquals("", r2.get_routing_table());

        // interfaces up
        r1.up_interface(0);
        r2.up_interface(0);

        // tests
        assertEquals(route1.to_string() + "\n", r1.get_routing_table());
        assertEquals(route1.to_string() + "\n", r2.get_routing_table());
    }
}