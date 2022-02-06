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
                        switch (single_command) {
                            case "enable" -> mode = CLIModes.ENABLE;
                            case "show" -> show(commands_list);
                            case "?" -> question_mark(disable_commands);
                            default -> execute_disable_command(single_command, commands_list);
                        }
                    }else{
                        wrong_command();
                    }
                }
                case ENABLE -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (enable_commands.contains(single_command)){
                        switch (single_command) {
                            case "configure" -> configure(commands_list);
                            case "disable", "exit" -> mode = CLIModes.DISABLE;
                            case "show" -> show(commands_list);
                            case "?" -> question_mark(enable_commands);
                            default -> execute_enable_command(single_command, commands_list);
                        }
                    }else{
                        wrong_command();
                    }
                }
                case CONFIG -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (config_commands.contains(single_command)){
                        switch (single_command) {
                            case "do" -> do$(commands_list);
                            case "exit" -> mode = CLIModes.ENABLE;
                            case "interface" -> interface$(commands_list);
                            case "ip" -> ip(commands_list);
                            case "?" -> question_mark(config_commands);
                            default -> execute_config_command(single_command, commands_list);
                        }
                    }else{
                        wrong_command();
                    }
                }
                default -> execute_different_mode(commands_list);
            }
        }
    }

    // prompt
    public String get_prompt(){
        StringBuilder prompt = new StringBuilder();
        prompt.append(device.get_name());
        if (mode == CLIModes.DISABLE){
            prompt.append(">");
        }else if (mode == CLIModes.ENABLE){
            prompt.append("#");
        }else if (mode == CLIModes.CONFIG){
            prompt.append("(config)#");
        }else if (mode == CLIModes.CONFIG_IF){
            prompt.append("(config-if)#");
        }else if (mode == CLIModes.CONFIG_ROUTER){
            prompt.append("(config-router)#");
        }
        return prompt.toString();
    }

    /////////////////////////////////////////////////////////
    //                    help functions                   //
    /////////////////////////////////////////////////////////

    // wrong command
    protected void wrong_command(){
        device.add_line_to_monitor("\nInvalid input detected\n");
    }

    // question mark command, add possible commands to monitor
    protected void question_mark(ArrayList<String> possible_commands){
        StringBuilder line = new StringBuilder();
        for (String word : possible_commands){
            line.append(word).append("\n");
        }
        line.delete(0, 2);
        device.add_line_to_monitor(line.toString());
    }

    // enable configure command
    private void configure(ArrayList<String> commands_list){
        if (commands_list.size() == 1){
            if (commands_list.get(0).equals("terminal")){
                mode = CLIModes.CONFIG;
            }else{
                wrong_command();
            }
        }else{
            wrong_command();
        }
    }

    // disable, enable show command
    private void show(ArrayList<String> commands_list){
        // TODO
    }

    // config do command
    private void do$(ArrayList<String> commands_list){
        // TODO
    }

    // config interface command
    private void interface$(ArrayList<String> commands_list){
        // TODO
    }

    // config ip command
    private void ip(ArrayList<String> commands_list){
        // TODO
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

    // return all possible commands in HashMap
    public abstract HashMap<String, ArrayList<String>> get_all_commands();
}
