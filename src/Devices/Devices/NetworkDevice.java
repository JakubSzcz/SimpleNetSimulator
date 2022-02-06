package Devices.Devices;

import Application.Application;
import Application.Trash;
import Devices.CLI.NetworkDeviceCLI;
import Protocols.Frame.Frame;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // normal case constructor
    public NetworkDevice(String name, int int_number){
        this(name,int_number, 200, false);
    }

    public NetworkDevice(String name, int int_number, int clock_period){
        this(name,int_number, clock_period, false);
    }

    // test case constructor, Network device for tests without thread running
    public NetworkDevice(String name, int int_number, Boolean test){
        this(name, int_number, 200, test);
    }

    public NetworkDevice(String name, int int_number, int clock_period, Boolean test){
        this.name = name;
        this.net_card = new NetworkCard(int_number);
        this.monitor = new Monitor();
        this.turned_on = true;
        this.router_speed = clock_period;
        this.applications = new ArrayList<>();
        this.taken_identifiers = new ArrayList<>();
        this.cli = null;
        turn_on();
        if (!test){
            start();
        }
    }

    // turns on the device
    public void turn_on(){
        applications.add(new Trash());
        turned_on = true;
    }

    // turns off the device
    public void turn_of(){
        kill_all_applications();
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

    // actions taken after receiving a frame
    abstract void handle_frame(Frame frame);

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


    // check if card buffer is empty - only for tests
    public boolean is_buffer_empty(){
        return net_card.is_buffer_empty();
    }

    // return monitor to string
    public String get_monitor(){
        return monitor.to_string();
    }

    // clear monitor
    public void clear_monitor(){
        monitor.clear();
    }

    // name getter
    public String get_name() {
        return name;
    }

    // int number getter
    public int get_int_number(){
        return net_card.get_int_number();
    }

    // monitor add line
    public void add_line_to_monitor(String line){
        monitor.add_line(line);
    }

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

    // execute commands
    public void execute_command(String command){
        cli.execute_command(command);
    }

    // get possible commands
    public HashMap<String, ArrayList<String>> get_all_commands(){
        return cli.get_all_commands();
    }
}
