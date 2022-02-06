package Devices.CLI;

import Devices.Devices.NetworkDevice;

import java.util.*;

public abstract class NetworkDeviceCLI {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // network device
    protected final NetworkDevice device;

    // cli mode
    protected CLIModes mode;

    // disable mode commands
    protected ArrayList<String> disable_commands;

    // enable mode commands
    protected ArrayList<String> enable_commands;

    // config commands
    protected ArrayList<String> config_commands;

    // enable, configure commands
    protected ArrayList<String> configure_commands;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    protected NetworkDeviceCLI(NetworkDevice device){
        // disable commands
        this.disable_commands = new ArrayList<>(
                Arrays.asList("enable", "show", "?")
        );

        // enable commands
        this.enable_commands = new ArrayList<>(
                Arrays.asList("configure", "disable", "exit", "show", "?")
        );

        // config commands
        this.config_commands = new ArrayList<>(
                Arrays.asList("do", "exit", "interface", "ip", "?")
        );

        // enable, configure commands
        this.configure_commands = new ArrayList<>(
                Arrays.asList("terminal", "?")
        );

        // mode
        this.mode = CLIModes.DISABLE;

        // network device
        this.device = device;
    }

    // execute command, main method
    public void execute_command(String command){
        ArrayList<String> commands_list = new ArrayList<>(
                Arrays.asList(command.split(" "))
        );
        if (commands_list.size() > 0){
            switch (mode){
                case DISABLE -> {
                    if (disable_commands.contains(commands_list.get(0))){
                        System.out.println("disable");
                    }else{
                        // TODO
                    }
                }
                case ENABLE -> {
                    if (enable_commands.contains(commands_list.get(0))){
                        System.out.println("enable");
                    }else{
                        // TODO
                    }
                }
                case CONFIG -> {
                    if (config_commands.contains(commands_list.get(0))){
                        System.out.println("config");
                    }else{
                        // TODO
                    }
                }
                default -> {

                }
            }
        }
    }
}
