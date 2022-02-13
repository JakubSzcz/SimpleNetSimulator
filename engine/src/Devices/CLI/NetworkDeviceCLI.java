package Devices.CLI;

import Devices.Devices.NetworkDevice;
import Protocols.Packets.IPv4;
import Protocols.Packets.IPv4MessageTypes;

import java.io.Serializable;
import java.util.*;

public abstract class NetworkDeviceCLI implements Serializable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // network device
    protected final NetworkDevice device;

    // cli mode
    protected CLIModes mode;

    // application block input
    protected boolean application_block_input;

    // commands history
    protected ArrayList<String> history;

    // disable mode commands
    protected ArrayList<String> disable_commands;
    protected HashMap<String, String> disable_commands_info;

    // enable mode commands
    protected ArrayList<String> enable_commands;
    protected HashMap<String, String> enable_commands_info;

    // config commands
    protected ArrayList<String> config_commands;
    protected ArrayList<String> config_no_commands;
    protected HashMap<String, String> config_commands_info;

    // config-if commands
    protected ArrayList<String> config_if_commands;
    protected ArrayList<String> config_if_no_commands;
    protected HashMap<String, String> config_if_commands_info;

    // config-if ip commands
    protected ArrayList<String> interface_ip_commands;
    protected HashMap<String, String> interface_ip_commands_info;

    // enable, configure commands
    protected ArrayList<String> configure_commands;
    protected HashMap<String, String> configure_commands_info;

    // show commands
    protected ArrayList<String> show_commands;
    protected HashMap<String, String> show_commands_info;

    // show ip commands
    protected ArrayList<String> show_ip_commands;
    protected HashMap<String, String> show_ip_commands_info;

    // interface in config-if mode
    protected int active_interface;

    /////////////////////////////////////////////////////////
    //                    constructor                      //
    /////////////////////////////////////////////////////////

    // constructor
    protected NetworkDeviceCLI(NetworkDevice device){
        // disable commands
        this.disable_commands = new ArrayList<>(
                Arrays.asList("enable", "?")
        );
        this.disable_commands_info = new HashMap<>(){{
            put("enable", "Turn on privileged commands");
            put("?", "");
        }};
        Collections.sort(disable_commands);

        // enable commands
        this.enable_commands = new ArrayList<>(
                Arrays.asList("configure", "disable", "exit", "show", "?")
        );
        this.enable_commands_info = new HashMap<>(){{
            put("configure", "Enter configuration mode");
            put("disable", "Turn off privileged commands");
            put("exit", "Exit from the EXEC");
            put("show", "Show running system information");
            put("?", "");
        }};
        Collections.sort(enable_commands);

        // config commands
        this.config_commands = new ArrayList<>(
                Arrays.asList("do", "exit", "interface", "ip", "no", "?")
        );
        this.config_no_commands = new ArrayList<>(
                Arrays.asList("interface", "ip", "?")
        );
        this.config_commands_info = new HashMap<>(){{
            put("do", "To run exec commands in config mode");
            put("exit", "Exit from configure mode");
            put("interface", "Select an interface to configure");
            put("ip", "Global IP configuration subcommands");
            put("no", "Negate a command or set its defaults");
            put("?", "");
        }};
        Collections.sort(config_commands);

        // config-if commands
        this.config_if_commands = new ArrayList<>(
                Arrays.asList("do", "exit", "ip","no", "shutdown", "?")
        );
        this.config_if_no_commands = new ArrayList<>(
                Arrays.asList("ip", "shutdown", "?")
        );
        this.config_if_commands_info = new HashMap<>(){{
            put("do", "To run exec commands in config-if mode");
            put("exit", "Exit from interface configuration mode");
            put("ip", "Interface Internet Protocol config commands");
            put("no", "Negate a command or set its defaults");
            put("shutdown", "Shutdown the selected interface");
            put("?", "");
        }};
        Collections.sort(config_if_commands);

        // config-if ip commands
        this.interface_ip_commands = new ArrayList<>(
                Arrays.asList("address", "?")
        );
        this.interface_ip_commands_info = new HashMap<>(){{
            put("address", "Set the IP address of an interface");
            put("?", "");
        }};
        Collections.sort(interface_ip_commands);

        // enable, configure commands
        this.configure_commands = new ArrayList<>(
                Arrays.asList("terminal", "?")
        );
        this.configure_commands_info = new HashMap<>(){{
            put("terminal", "Configure from the terminal");
            put("?", "");
        }};
        Collections.sort(configure_commands);

        // show commands
        this.show_commands = new ArrayList<>(
                Arrays.asList("ip","running-config", "?")
        );
        this.show_commands_info = new HashMap<>(){{
            put("ip", "IP information");
            put("running-config", "Current operating configuration");
            put("?", "");
        }};
        Collections.sort(show_commands);

        // show ip commands
        this.show_ip_commands = new ArrayList<>(
                List.of("?")
        );
        this.show_ip_commands_info = new HashMap<>(){{
            put("?", "");
        }};
        Collections.sort(show_ip_commands);

        // init
        this.mode = CLIModes.DISABLE;
        this.device = device;
        this.active_interface = -1;
        this.application_block_input = false;
        this.history = new ArrayList<>();
    }

    /////////////////////////////////////////////////////////
    //                  execute command                    //
    /////////////////////////////////////////////////////////

    // execute command, main method
    public final void execute_command(String command){
        execute_command(command, false);
    }
    public final void execute_command(String command, boolean add_command_to_monitor){
        // add to monitor
        if (add_command_to_monitor){
            device.add_line_to_monitor(get_prompt() + command);
            if (!command.equals("")){
                history.add(command);
            }
        }

        // command trimmed
        String command_trimmed = command.trim();

        // commands in list
        ArrayList<String> commands_list = new ArrayList<>(
                Arrays.asList(command_trimmed.split(" "))
        );
        if (!command_trimmed.equals("")){
            execute_command(commands_list);
        }
    }
    public final void execute_command(ArrayList<String> commands_list){
        // single command to process
        String single_command;

        if (!commands_list.isEmpty()){
            // check cli mode
            switch (mode){
                case DISABLE -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (disable_commands.contains(single_command)){
                        switch (single_command) {
                            case "enable" -> mode = CLIModes.ENABLE;
                            case "?" -> question_mark(disable_commands, disable_commands_info);
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
                            case "?" -> question_mark(enable_commands, enable_commands_info);
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
                            case "no" -> config_no(commands_list);
                            case "?" -> question_mark(config_commands, config_commands_info);
                            default -> execute_config_command(single_command, commands_list);
                        }
                    }else{
                        wrong_command();
                    }
                }
                case CONFIG_IF -> {
                    single_command = commands_list.get(0);
                    commands_list.remove(single_command);
                    if (config_if_commands.contains(single_command)) {
                        switch (single_command) {
                            case "do" -> do$(commands_list);
                            case "exit" -> {
                                active_interface = -1;
                                mode = CLIModes.CONFIG;
                            }
                            case "ip" -> interface_ip(commands_list);
                            case "no" -> interface_no(commands_list);
                            case "shutdown" -> interface_shutdown(commands_list);
                            case "?" -> question_mark(config_if_commands, config_if_commands_info);
                            default -> execute_config_command(single_command, commands_list);
                        }
                    }
                }
                default -> execute_different_mode(commands_list);
            }
        }
    }

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // get prompt
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
        to_return.put("config-if", config_if_commands);
        to_return.put("interface_ip", interface_ip_commands);
        to_return.put("configure", configure_commands);
        to_return.put("show", show_commands);
        to_return.put("show_ip", show_ip_commands);
        return to_return;
    }

    // return all possible commands info in HashMap
    public HashMap<String, HashMap<String, String>> get_all_commands_info(){
        HashMap<String, HashMap<String, String>> to_return = new HashMap<>();
        to_return.put("disable", disable_commands_info);
        to_return.put("enable", enable_commands_info);
        to_return.put("config", config_commands_info);
        to_return.put("config-if", config_if_commands_info);
        to_return.put("interface_ip", interface_ip_commands_info);
        to_return.put("configure", configure_commands_info);
        to_return.put("show", show_commands_info);
        to_return.put("show_ip", show_ip_commands_info);
        return to_return;
    }

    // return cli mode
    public CLIModes get_mode() {
        return mode;
    }

    // block cmd
    public void block(){
        application_block_input = true;
    }

    public void unblock(){
        application_block_input = false;
    }

    public boolean is_input_blocked() {
        return application_block_input;
    }

    // history getter
    public ArrayList<String> get_history() {
        return history;
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
    protected void question_mark(ArrayList<String> possible_commands,
                                 HashMap<String, String> possible_commands_info){
        StringBuilder line = new StringBuilder();
        for (String word : possible_commands){
            line.append(word).append("\t\t").append(possible_commands_info.get(word))
                    .append("\n");
        }
        line.delete(0, 4);
        device.add_line_to_monitor(line.toString());
    }

    /////////////////////////////////////////////////////////
    //               disable and enable modes              //
    /////////////////////////////////////////////////////////

    // # configure ...
    private void configure(ArrayList<String> commands_list){
        if (commands_list.size() == 1){
            switch (commands_list.get(0)) {
                case "terminal" -> mode = CLIModes.CONFIG;
                case "?" -> question_mark(configure_commands, configure_commands_info);
                default -> wrong_command();
            }
        }else if (commands_list.size() > 1){
            wrong_command();
        }else{
            incomplete_command();
        }
    }

    // # show ...
    private void show(ArrayList<String> commands_list){
        String single_command;
        if (commands_list.size() > 0){
            single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (show_commands.contains(single_command)){
                switch (single_command){
                    case "ip" -> show_ip(commands_list);
                    case "running-config" -> show_run(commands_list);
                    case "?" -> question_mark(show_commands, show_commands_info);
                    default -> execute_show_command(single_command, commands_list);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // # show ip ...
    private void show_ip(ArrayList<String> commands_list){
        String single_command;
        if (commands_list.size() > 0){
            single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (show_ip_commands.contains(single_command)){
                if ("?".equals(single_command)) {
                    question_mark(show_ip_commands,  show_ip_commands_info);
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

    // # show running-config
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

    /////////////////////////////////////////////////////////
    //                    config mode                      //
    /////////////////////////////////////////////////////////

    // (config)# do ...
    private void do$(ArrayList<String> commands_list){
        if (commands_list.size() > 0){
            CLIModes current_mode = mode;
            mode = CLIModes.ENABLE;
            execute_command(commands_list);
            mode = current_mode;
        }else{
            incomplete_command();
        }

    }

    // (config)# interface <int number>
    private void interface$(ArrayList<String> commands_list){
        interface$(commands_list, false);
    }
    // (config)# interface <int number>
    private void interface$(ArrayList<String> commands_list, boolean no){
        switch (commands_list.size()) {
            case 1 -> {
                String int_number_string = commands_list.get(0).replace("interface", "");
                try {
                    int int_number = Integer.parseInt(int_number_string);
                    if (!no){
                        interface$(int_number);
                    }else{
                        no_interface(int_number);
                    }
                } catch (NumberFormatException e) {
                    wrong_command();
                }
            }
            case 2 -> {
                if (commands_list.get(0).equals("interface")){
                    try {
                        int int_number = Integer.parseInt(commands_list.get(1));
                        if (!no){
                            interface$(int_number);
                        }else{
                            no_interface(int_number);
                        }
                    }catch (NumberFormatException e){
                        wrong_command();
                    }
                }else{
                    wrong_command();
                }
            }
            case 0 -> {
                incomplete_command();
            }
            default -> wrong_command();
        }
    }

    // (config)# interface <int number>
    private void interface$(int int_number){
        if (int_number >= 0 && int_number < device.get_int_number()){
            active_interface = int_number;
            mode = CLIModes.CONFIG_IF;
        }else{
            wrong_command();
        }
    }

    // (config)# no interface <int number>
    private void no_interface(int int_number){
        if (int_number >= 0 && int_number < device.get_int_number()){
            device.down_interface(int_number);
            device.delete_interface_ip(int_number);
        }else{
            wrong_command();
        }
    }

    // (config)# no ...
    private void config_no(ArrayList<String> commands_list){
        if (commands_list.size() > 0){
            String single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (config_commands.contains(single_command)){
                switch (single_command) {
                    case "interface" -> interface$(commands_list);
                    case "ip" -> no_ip(commands_list);
                    case "?" -> question_mark(config_no_commands, config_commands_info);
                    default -> execute_config_no_command(single_command, commands_list);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // (config)# ip ... command
    private void ip(ArrayList<String> commands_list){
        // TODO
    }

    // (config)# no ip ... command
    private void no_ip(ArrayList<String> commands_list){
        // TODO
    }

    /////////////////////////////////////////////////////////
    //                   config-if mode                    //
    /////////////////////////////////////////////////////////

    // (config-if)# ip ...
    private void interface_ip(ArrayList<String> commands_list){
        if (commands_list.size() > 0){
            String single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (interface_ip_commands.contains(single_command)) {
                switch (single_command) {
                    case "address" -> interface_ip_address(commands_list);
                    case "?" -> question_mark(interface_ip_commands, interface_ip_commands_info);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // (config-if)# no ip ...
    private void interface_no_ip(ArrayList<String> commands_list){
        if (commands_list.size() > 0){
            String single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (interface_ip_commands.contains(single_command)) {
                switch (single_command) {
                    case "address" -> interface_no_ip_address(commands_list);
                    case "?" -> question_mark(interface_ip_commands, interface_ip_commands_info);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // (config-if)# ip address <ip address> <mask>
    private void interface_ip_address(ArrayList<String> commands_list){
        switch (commands_list.size()){
            case 0,1 -> incomplete_command();
            case 2 -> {
                // get ip and mask
                String ip_address_string = commands_list.get(0);
                String mask_string = commands_list.get(1);

                // valid ip and mask
                IPv4MessageTypes ip_message = IPv4.is_ip_valid(ip_address_string);
                IPv4MessageTypes mask_message = IPv4.is_mask_valid(mask_string);

                // if valid check interface valid
                if (ip_message == IPv4MessageTypes.is_valid && mask_message == IPv4MessageTypes.is_valid) {
                    IPv4MessageTypes ip_interface_message = device.set_interface_ip(active_interface,
                            IPv4.parse_to_long(ip_address_string), IPv4.parse_mask_to_long(mask_string));
                    switch (ip_interface_message) {
                        case is_valid:
                            break;
                        case is_net_address, is_broadcast_address, mask_is_over_30:
                            device.add_line_to_monitor("Bad mask " + mask_string + " for address "
                            + ip_address_string);
                            break;
                        case overlaps:
                            device.add_line_to_monitor(ip_address_string + " " + mask_string
                            + " overlaps with other interface");
                            break;
                    }
                }else{
                    // if not valid add message to router monitor
                    if (ip_message != IPv4MessageTypes.is_valid){
                        device.add_line_to_monitor("IP address is not valid");
                    }
                    if (mask_message != IPv4MessageTypes.is_valid){
                        device.add_line_to_monitor("Mask is not valid");
                    }
                }
            }
            default -> wrong_command();
        }
    }

    // (config-if)# no ip address
    private void interface_no_ip_address(ArrayList<String> commands_list){
        if (commands_list.size() == 0){
            device.delete_interface_ip(active_interface);
        }else{
            wrong_command();
        }
    }

    // (config-if)# no ...
    private void interface_no(ArrayList<String> commands_list){
        if (commands_list.size() > 0){
            String single_command = commands_list.get(0);
            commands_list.remove(single_command);
            if (config_if_no_commands.contains(single_command)){
                switch (single_command) {
                    case "ip" -> interface_no_ip(commands_list);
                    case "shutdown" -> interface_no_shutdown(commands_list);
                    case "?" -> question_mark(config_no_commands, config_commands_info);
                    default -> execute_config_no_command(single_command, commands_list);
                }
            }else{
                wrong_command();
            }
        }else{
            incomplete_command();
        }
    }

    // (config-if)# shutdown
    private void interface_shutdown(ArrayList<String> commands_list){
        if (commands_list.size() == 0){
            device.down_interface(active_interface);
        }else{
            wrong_command();
        }
    }

    // (config-if)# no shutdown
    private void interface_no_shutdown(ArrayList<String> commands_list){
        if (commands_list.size() == 0){
            device.up_interface(active_interface);
        }else{
            wrong_command();
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

    // execute command in config mode which is possible only in child
    protected abstract void execute_config_no_command(String single_command,
                                                   ArrayList<String> commands_list);

    // execute show command which is possible only in child
    protected abstract void execute_show_command(String single_command,
                                                   ArrayList<String> commands_list);

    // execute show ip command which is possible only in child
    protected abstract void execute_show_ip_command(String single_command,
                                                 ArrayList<String> commands_list);
}
