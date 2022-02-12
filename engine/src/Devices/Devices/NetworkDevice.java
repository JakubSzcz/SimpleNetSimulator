package Devices.Devices;

import Application.Application;
import Application.Trash;
import Devices.CLI.CLIModes;
import Devices.CLI.NetworkDeviceCLI;
import Protocols.Frame.Frame;
import Protocols.Packets.IPv4;
import Protocols.Packets.IPv4MessageTypes;

import java.io.Serializable;
import java.util.*;

public abstract class NetworkDevice extends Thread implements Serializable {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // name of the device
    protected String name;

    // network card
    protected NetworkCard net_card;

    // monitor
    protected Monitor monitor;

    // if device is turned on - true
    protected boolean turned_on;

    // sleep time in run method
    protected int router_speed;

    // Applications
    protected ArrayList<Application> applications;

    // applications taken identifiers
    protected ArrayList<Integer> taken_identifiers;

    // application max number
    protected int MAX_IDENTIFIER = 2000;

    // CLI
    protected NetworkDeviceCLI cli;

    // position
    private double pos_x;
    private double pos_y;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // normal case constructor
    public NetworkDevice(String name, int int_number){
        this(name,int_number, 200, false, 0, 0);
    }

    public NetworkDevice(String name, int int_number, double pos_x, double pos_y){
        this(name,int_number, 200, false, pos_x, pos_y);
    }

    public NetworkDevice(String name, int int_number, int clock_period){
        this(name,int_number, clock_period, false, 0, 0);
    }

    // test case constructor, Network device for tests without thread running
    public NetworkDevice(String name, int int_number, Boolean test){
        this(name, int_number, 200, test, 0, 0);
    }

    public NetworkDevice(String name, int int_number, int clock_period,
                         Boolean test, double pos_x, double pos_y){
        this.name = name;
        this.net_card = new NetworkCard(int_number);
        this.monitor = new Monitor();
        this.turned_on = true;
        this.router_speed = clock_period;
        this.applications = new ArrayList<>();
        this.taken_identifiers = new ArrayList<>();
        this.cli = null;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        turn_on();
        if (!test){
            start();
        }
    }

    // turns on the device
    public void turn_on(){
        applications.add(new Trash());
        if (!currentThread().isAlive()){
            start();
        }
        turned_on = true;
    }

    // turns off the device
    public void turn_of(){
        kill_all_applications();
        interrupt();
        turned_on = false;
    }

    // sends a frame by given interface
    public void add_out_traffic(Frame frame, int int_number){
        net_card.add_out_traffic(frame, int_number);
    }

    // checks if there are frames in buffer
    public void run(){
        while (true){
            if (!net_card.is_buffer_empty()){
                handle_frame(net_card.get_frame_from_buffer());
            }

            try {
                Thread.sleep(router_speed);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // name getter
    public String get_name() {
        return name;
    }

    // position getters
    public double get_pos_x() {
        return pos_x;
    }

    public double get_pos_y() {
        return pos_y;
    }

    // actions taken after receiving a frame
    abstract void handle_frame(Frame frame);

    // check if card buffer is empty - only for tests
    public boolean is_buffer_empty(){
        return net_card.is_buffer_empty();
    }

    /////////////////////////////////////////////////////////
    //                 interface functions                 //
    /////////////////////////////////////////////////////////

    // returns interface, link needs it
    public NetworkInterface get_interface(int int_number){
        return net_card.get_interface(int_number);
    }

    // turn of interface
    public void down_interface(int int_number){
        net_card.down_interface(int_number);
    }

    // turn up interface
    public void up_interface(int int_number){
        net_card.up_interface(int_number);
    }

    // int number getter
    public int get_int_number(){
        return net_card.get_int_number();
    }

    // set ip address of given interface
    public IPv4MessageTypes set_interface_ip(int int_number, long ip_address, long mask){
        IPv4MessageTypes message = IPv4.is_interface_ip_valid(ip_address, mask);
        long smaller_mask;
        Map<String, Long> int_ip_address;
        if (message == IPv4MessageTypes.is_valid) {
            for (int i = 0; i < get_int_number(); i++){
                if (i != int_number){
                    int_ip_address = net_card.get_ip_address(i);
                    if (mask < int_ip_address.get("mask")){
                        smaller_mask = mask;
                    }else{
                        smaller_mask = int_ip_address.get("mask");
                    }
                    long net_address = int_ip_address.get("address") & smaller_mask;
                    if ((ip_address & smaller_mask) == net_address){
                        return IPv4MessageTypes.overlaps;
                    }
                }
            }
        }
        return message;
    }

    // delete ip address of given interface
    public void delete_interface_ip(int int_number){
        net_card.get_interface(int_number).set_ip_address(-1, -1);
    }

    /////////////////////////////////////////////////////////
    //                 monitor functions                   //
    /////////////////////////////////////////////////////////

    // return monitor to string
    public String get_monitor(){
        return monitor.to_string();
    }

    // clear monitor
    public void clear_monitor(){
        monitor.clear();
    }

    // monitor add line
    public void add_line_to_monitor(String line){
        monitor.add_line(line);
    }

    /////////////////////////////////////////////////////////
    //                 application functions               //
    /////////////////////////////////////////////////////////

    // remove application
    public void remove_application(int identifier){
        for (Application application: applications){
            if (application.identifier == identifier){
                taken_identifiers.remove(Integer.valueOf(identifier));
                applications.remove(application);
                break;
            }
        }
    }

    // generate application identifier
    protected int generate_app_id(){
        Random generator = new Random();
        int identifier = -1;
        OUTER: while (identifier == -1){
            identifier = generator.nextInt(MAX_IDENTIFIER);
            if (identifier == 0){
                identifier = -1;
                continue;
            }
            for (int taken_id: taken_identifiers){
                if (taken_id == identifier){
                    identifier = -1;
                    continue OUTER;
                }
            }
        }
        taken_identifiers.add(identifier);
        return identifier;
    }

    // add to trash
    public void add_to_trash(HashMap<String, Object> record){
        Trash trash = null;
        for (Application application: applications){
            if (application instanceof Trash trash_ap){
                trash = trash_ap;
            }
        }
        if (trash == null){
            trash = new Trash();
            applications.add(trash);
        }
        trash.add_to_buffer(record);
    }

    // trash buffer getter
    public ArrayDeque<HashMap<String, Object>> get_trash_buffer(){
        Trash trash = null;
        for (Application application: applications){
            if (application instanceof Trash trash_ap){
                trash = trash_ap;
            }
        }
        if (trash == null){
            trash = new Trash();
            applications.add(trash);
        }
        return trash.get_buffer();
    }

    // kill all apps
    private void kill_all_applications(){
        for(Application application: applications){
            application.stop();
            taken_identifiers.remove(Integer.valueOf(application.identifier));
        }
        applications.clear();
    }

    /////////////////////////////////////////////////////////
    //                   cli functions                     //
    /////////////////////////////////////////////////////////

    // execute commands
    public void execute_command(String command){
        cli.execute_command(command);
    }

    // execute commands
    public void execute_command(String command, boolean add_to_monitor){
        cli.execute_command(command, add_to_monitor);
    }

    // get possible commands
    public HashMap<String, ArrayList<String>> get_all_commands(){
        return cli.get_all_commands();
    }

    // get possible commands info
    public HashMap<String, HashMap<String, String>> get_all_commands_info(){
        return cli.get_all_commands_info();
    }

    // get cli mode
    public CLIModes get_cli_mode() {
        return cli.get_mode();
    }

    // prompt getter
    public String get_prompt(){
        return cli.get_prompt();
    }

    // input block
    public void block_cmd(){
        cli.block();
    }
    public void unblock_cmd(){
        cli.unblock();
    }
    public boolean is_input_blocked(){
        return cli.is_input_blocked();
    }
}
