package UnitTests.Devices;

import Devices.Route;
import Devices.RouteCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {
    @Test
    public void route_to_string(){
        Route route = new Route(RouteCode.C, 0,1,1,1,0);
        assertEquals("C ", route.to_string());
    }
}