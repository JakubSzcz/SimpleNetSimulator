package UnitTests.CLI;

import Devices.CLI.CLIModes;
import Devices.Devices.Router;
import Devices.Routing.Route;
import Devices.Routing.RouteCode;
import Protocols.Packets.IPv4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    public void cli_question_mark(){
        Router router = new Router("router", 1);
        StringBuilder expected = new StringBuilder();
        // disable
        router.execute_command("?");
        for (String word : router.get_all_commands().get("disable")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("disable").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

        // enable
        router.clear_monitor();
        expected = new StringBuilder();
        router.execute_command("enable");
        assertEquals(CLIModes.ENABLE, router.get_cli_mode());
        router.execute_command("?");

        for (String word : router.get_all_commands().get("enable")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("enable").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

        // config
        router.clear_monitor();
        expected = new StringBuilder();
        router.execute_command("configure terminal");
        assertEquals(CLIModes.CONFIG, router.get_cli_mode());
        router.execute_command("?");

        for (String word : router.get_all_commands().get("config")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("config").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

        // config-if
        router.clear_monitor();
        expected = new StringBuilder();
        router.execute_command("interface interface0");
        System.out.println(router.get_monitor());
        assertEquals(CLIModes.CONFIG_IF, router.get_cli_mode());
        router.execute_command("?");

        for (String word : router.get_all_commands().get("config-if")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("config-if").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());
    }

    @Test
    public void show_ip_route(){
        Router router = new Router("router", 1);

        // empty
        router.execute_command("enable");
        router.execute_command("show ip route");
        assertEquals("Router routing table:\n\n", router.get_monitor());

        // add routes
        router.clear_monitor();
        Route route = new Route(RouteCode.S, 1, 0,
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("24"),
                -1, 0);
        router.add_static_route(IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_mask_to_long("24"), 0);
        router.execute_command("show ip route");
        assertEquals("Router routing table:\n" + route.to_string() + "\n\n", router.get_monitor());

        // wrong command
        router.clear_monitor();
        router.execute_command("show ip route a");
        assertEquals("\nInvalid input detected\n\n", router.get_monitor());
    }

    @Test
    public void show_run(){
        Router router = new Router("router", 1);

        // empty
        router.execute_command("enable");
        router.execute_command("show running-config");
        StringBuilder expected = new StringBuilder();
        expected.append("Building configuration...\n\n");
        expected.append("hostname: router\n!\n");
        expected.append("interface interface0\n");
        expected.append(" no ip address\n shutdown\n!\n");
        assertEquals(expected + "end\n", router.get_monitor());

        // add routes
        router.clear_monitor();
        Route route = new Route(RouteCode.S, 1, 0,
                IPv4.parse_to_long("192.168.1.0"), IPv4.parse_mask_to_long("24"),
                -1, 0);
        router.add_static_route(IPv4.parse_to_long("192.168.1.0"),
                IPv4.parse_mask_to_long("24"), 0);
        router.execute_command("show running-config");
        expected.append("ip route ");
        expected.append(IPv4.parse_to_string(route.net()));
        expected.append(" ");
        expected.append(IPv4.parse_mask_to_string_dot(route.net_mask()));
        expected.append(" interface");
        expected.append(route.int_number());
        assertEquals(expected + "\nend\n", router.get_monitor());
    }

    @Test
    public void do$(){
        Router router = new Router("router", 1);
        router.execute_command("enable");
        router.execute_command("configure terminal");
        StringBuilder expected = new StringBuilder();

        // enable
        router.execute_command("do ?");
        for (String word : router.get_all_commands().get("enable")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("enable").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

        // config
        router.clear_monitor();
        expected = new StringBuilder();
        router.execute_command("?");
        for (String word : router.get_all_commands().get("config")){
            expected.append(word).append("\t\t\t").append(router.get_all_commands_info()
                    .get("config").get(word)).append("\n");
        }
        expected.delete(0, 4);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

    }

    @Test
    public void interface_ip_address() {
        Router router = new Router("router", 1);
        router.execute_command("enable");
        router.execute_command("configure terminal");
        router.execute_command("interface interface0");
        router.execute_command("ip address 192.168.1.1 255.255.255.0");
        long ip_address = router.get_interface(0).get_ip_address().get("address");
        long mask = router.get_interface(0).get_ip_address().get("mask");
        assertEquals(IPv4.parse_to_long("192.168.1.1"), ip_address);
        assertEquals(IPv4.parse_mask_to_long("/24"), mask);
    }
}