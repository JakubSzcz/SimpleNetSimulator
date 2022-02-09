package UnitTests.Topology;

import Topology.Topology;
import Topology.NetworkDevicesTypes;
import Topology.AddLinkMessages;
import Topology.AddDeviceMessages;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TopologyTest {
    // test of add device, add link, get free int number
    @Test
    public void test1(){
        // add routers
        assertEquals(AddDeviceMessages.is_valid,Topology.get_topology().
                add_device("r1", 3, NetworkDevicesTypes.ROUTER));
        assertEquals(AddDeviceMessages.is_valid,Topology.get_topology().
                add_device("r2", 5, NetworkDevicesTypes.ROUTER));
        assertEquals(AddDeviceMessages.name_is_taken,Topology.get_topology().
                add_device("r2", 5, NetworkDevicesTypes.ROUTER));
        assertEquals(AddDeviceMessages.too_many_interfaces,Topology.get_topology().
                add_device("r3", Topology.MAX_INT_NUMBER + 1, NetworkDevicesTypes.ROUTER));

        // test, free int number
        assertEquals(Arrays.asList(0, 1, 2), Topology.get_topology().get_free_int_number("r1"));
        assertEquals(Arrays.asList(0, 1, 2, 3, 4), Topology.get_topology().get_free_int_number("r2"));

        // add link
        assertEquals(AddLinkMessages.is_valid,Topology.get_topology().
                add_link("r1", 0, "r2", 3));

        // test, free int number
        assertEquals(Arrays.asList(1, 2), Topology.get_topology().get_free_int_number("r1"));
        assertEquals(Arrays.asList(0, 1, 2, 4), Topology.get_topology().get_free_int_number("r2"));

    }

}