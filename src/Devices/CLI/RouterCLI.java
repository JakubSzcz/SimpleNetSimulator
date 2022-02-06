package Devices.CLI;

import Devices.Devices.Router;

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
        // TODO
    }

    // execute command in enable mode which is possible only in child
    @Override
    protected void execute_enable_command(String single_command, ArrayList<String> commands_list){
        // TODO
    }

    // execute command in config mode which is possible only in child
    @Override
    protected void execute_config_command(String single_command, ArrayList<String> commands_list){
        // TODO
    }
}
