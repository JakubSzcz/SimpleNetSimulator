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
    public final void execute_command(String command){
        // single command to process
        String single_command;

        // commands in list
        ArrayList<String> commands_list = new ArrayList<>(
                Arrays.asList(command.split(" "))
        );

        // check cli mode
        if (commands_list.size() > 0){
            switch (mode){
                case DISABLE -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (disable_commands.contains(single_command)){
                        if (single_command.equals("enable")){
                            mode = CLIModes.ENABLE;
                        }else if (single_command.equals("show")){
                            // TODO
                        }else if (single_command.equals("?")){
                            StringBuilder line = new StringBuilder();
                            for (String word : disable_commands){
                                line.append(word).append("\n");
                            }
                            device.add_line_to_monitor(line.toString());
                        }else{
                            execute_disable_command(single_command, commands_list);
                        }
                    }else{
                        // TODO
                    }
                }
                case ENABLE -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (enable_commands.contains(single_command)){
                        System.out.println("enable");
                    }else{
                        // TODO
                    }
                }
                case CONFIG -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (config_commands.contains(single_command)){
                        System.out.println("config");
                    }else{
                        // TODO
                    }
                }
                default -> execute_different_mode(commands_list);
            }
        }
    }

    /////////////////////////////////////////////////////////
    //                abstract functions                   //
    /////////////////////////////////////////////////////////

    // execute mode which is possible only in child
    protected abstract void execute_different_mode(ArrayList<String> commands_list);

    // execute command in disable mode which is possible only in child
    protected abstract void execute_disable_command(String single_command,
                                                    ArrayList<String> commands_list);

    // execute command in enable mode which is possible only in child
    protected abstract void execute_enable_command(String single_command,
                                                    ArrayList<String> commands_list);

    // execute command in config mode which is possible only in child
    protected abstract void execute_config_command(String single_command,
                                                    ArrayList<String> commands_list);
}
