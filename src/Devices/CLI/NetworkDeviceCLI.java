package Devices.CLI;

import Devices.Devices.NetworkDevice;
import Protocols.Packets.IPv4;

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

    // show commands
    protected ArrayList<String> show_commands;

    // show ip commands
    protected ArrayList<String> show_ip_commands;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    protected NetworkDeviceCLI(NetworkDevice device){
        // disable commands
        this.disable_commands = new ArrayList<>(
                Arrays.asList("enable", "?")
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

        // show commands
        this.show_commands = new ArrayList<>(
                Arrays.asList("ip","running-config", "?")
        );

        // show ip commands
        this.show_ip_commands = new ArrayList<>(
                List.of("?")
        );

        // mode
        this.mode = CLIModes.DISABLE;

        // network device
        this.device = device;
    }

    // execute command, main method
    public final void execute_command(String command){
        execute_command(command, false);
    }
    public final void execute_command(String command, boolean add_command_to_monitor){
        // single command to process
        String single_command;

        // commands in list
        ArrayList<String> commands_list = new ArrayList<>(
                Arrays.asList(command.split(" "))
        );

        if (add_command_to_monitor){
            device.add_line_to_monitor(get_prompt() + command);
        }

        // check cli mode
        if (commands_list.size() > 0){
            switch (mode){
                case DISABLE -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (disable_commands.contains(single_command)){
                        switch (single_command) {
                            case "enable" -> mode = CLIModes.ENABLE;
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

    // return all possible commands in HashMap
    public HashMap<String, ArrayList<String>> get_all_commands(){
        HashMap<String, ArrayList<String>> to_return = new HashMap<>();
        to_return.put("disable", disable_commands);
        to_return.put("enable", enable_commands);
        to_return.put("config", config_commands);
        to_return.put("configure", configure_commands);
        to_return.put("show", show_commands);
        to_return.put("show_ip", show_ip_commands);
        return to_return;
    }

    /////////////////////////////////////////////////////////
    //                    help functions                   //
    /////////////////////////////////////////////////////////

    // wrong command
    protected void wrong_command(){
        device.add_line_to_monitor("\nInvalid input detected\n");
    }
    protected void incomplete_command(){
        device.add_line_to_monitor("\nIncomplete command\n");
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
        }else if (commands_list.size() > 1){
            wrong_command();
        }else{
            incomplete_command();
        }
    }

    // disable, enable show command
    private void show(ArrayList<String> commands_list){
        String single_command;
        if (commands_list.size() > 0){
            single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (show_commands.contains(single_command)){
                switch (single_command){
                    case "ip" -> show_ip(commands_list);
                    case "running-config" -> show_run(commands_list);
                    case "?" -> question_mark(show_commands);
                    default -> execute_show_command(single_command, commands_list);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // show ip command
    private void show_ip(ArrayList<String> commands_list){
        String single_command;
        if (commands_list.size() > 0){
            single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (show_ip_commands.contains(single_command)){
                if ("?".equals(single_command)) {
                    question_mark(show_commands);
                } else {
                    execute_show_ip_command(single_command, commands_list);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // show running-config
    protected void show_run(ArrayList<String> commands_list){
        if (commands_list.size() == 0){
            device.add_line_to_monitor("Building configuration...\n");
            device.add_line_to_monitor("hostname: " + device.get_name() + "\n!");
            for (int i = 0; i < device.get_int_number(); i++){
                device.add_line_to_monitor("interface interface" + i);
                if (device.get_interface(i).get_ip_address().get("address") != -1){
                    long ip_address = device.get_interface(i).get_ip_address().get("address");
                    long mask = device.get_interface(i).get_ip_address().get("mask");
                    device.add_line_to_monitor(" ip address " + IPv4.parse_to_string(ip_address) +
                            " " + IPv4.parse_mask_to_string_dot(mask));
                }else{
                    device.add_line_to_monitor(" no ip address");
                }
                if (device.get_interface(i).is_up()){
                    device.add_line_to_monitor(" no shutdown");
                }else{
                    device.add_line_to_monitor(" shutdown");
                }
                device.add_line_to_monitor("!");
            }
        }else{
            wrong_command();
        }
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

    // execute show command which is possible only in child
    protected abstract void execute_show_command(String single_command,
                                                   ArrayList<String> commands_list);

    // execute show ip command which is possible only in child
    protected abstract void execute_show_ip_command(String single_command,
                                                 ArrayList<String> commands_list);
}
