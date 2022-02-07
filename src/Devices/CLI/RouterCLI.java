package Devices.CLI;

import Devices.Devices.Router;
import Devices.Routing.Route;
import Devices.Routing.RouteCode;
import Protocols.Packets.IPv4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouterCLI extends NetworkDeviceCLI{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    public RouterCLI(Router router){
        // super constructor
        super(router);

        // disable commands
        disable_commands.addAll(List.of("ping"));
        Collections.sort(disable_commands);

        // enable commands
        enable_commands.addAll(List.of("ping"));
        Collections.sort(enable_commands);

        // enable commands
        show_ip_commands.addAll(List.of("route"));
        Collections.sort(show_ip_commands);

    }

    /////////////////////////////////////////////////////////
    //         abstract functions implementations          //
    /////////////////////////////////////////////////////////

    // execute mode which is possible only in child
    @Override
    protected void execute_different_mode(ArrayList<String> commands_list) {
        switch (mode){
            case CONFIG_ROUTER -> {
                // TODO
            }
        }
    }

    // execute command in disable mode which is possible only in child
    @Override
    protected void execute_disable_command(String single_command, ArrayList<String> commands_list){
        switch (single_command) {
            case "ping" -> ping(commands_list);
        }
    }

    // execute command in enable mode which is possible only in child
    @Override
    protected void execute_enable_command(String single_command, ArrayList<String> commands_list){
        switch (single_command) {
            case "ping" -> ping(commands_list);
        }
    }

    // execute command in config mode which is possible only in child
    @Override
    protected void execute_config_command(String single_command, ArrayList<String> commands_list){
        // TODO
    }

    @Override
    protected void execute_show_command(String single_command, ArrayList<String> commands_list) {
        // TODO
    }

    @Override
    protected void execute_show_ip_command(String single_command, ArrayList<String> commands_list) {
        switch (single_command) {
            case "route" -> show_ip_route(commands_list);
        }
    }

    /////////////////////////////////////////////////////////
    //                    help functions                   //
    /////////////////////////////////////////////////////////
    @Override
    protected void show_run(ArrayList<String> commands_list){
        super.show_run(commands_list);
        if (commands_list.size() == 0){
            Route route;
            for (int i = 0; i < ((Router)device).get_routing_table_size(); i++){
                route = ((Router)device).get_route(i);
                if (route.code() == RouteCode.S){
                    device.add_line_to_monitor("ip route " + IPv4.parse_to_string(route.net()) + " "
                    + IPv4.parse_mask_to_string_dot(route.net_mask()) + " interface" + route.int_number());
                }
            }
            device.add_line_to_monitor("end");
        }
    }

    // show ip route
    private void show_ip_route(ArrayList<String> commands_list){
        if (commands_list.size() == 0){
            device.add_line_to_monitor("Router routing table:");
            device.add_line_to_monitor(((Router)device).get_routing_table_string());
        }else{
            wrong_command();
        }
    }

    private void ping(ArrayList<String> commands_list){
        // TODO
    }
}
