package Devices.CLI;

import Devices.Devices.Router;

import java.util.Arrays;
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
}
