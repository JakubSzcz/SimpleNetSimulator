package GUI.Topology;

import Devices.Devices.NetworkInterface;
import Devices.Devices.Router;
import Devices.Link;
import GUI.Forms.RouterPopUp;
import GUI.Icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Topology {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // routers in topology
    ArrayList<RouterButton> routers;

    // Full Links (name + link + position)
    ArrayList<FullLink> flinks;

    // topology object
    private static final Topology topology = new Topology();

    // max number of interfaces on router
    private final int max_int_number = 8;

    // min number of interfaces
    private final int min_int_number = 1;

    // max number of letters in name
    private final int max_name_characters = 10;

    // min number of letter in name
    private final int min_name_characters = 1;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    private Topology(){
        routers = new ArrayList<>();
        flinks = new ArrayList<>();
    }

    // add router to topology
    public AddRouterMessages add_router(Position position, String name, int int_number, RouterPopUp router_pop_up){
        // check if number of interfaces is not too high
        if (int_number > max_int_number){
            return AddRouterMessages.too_many_interfaces;
        // check if number of interfaces is not too small
        }else if (int_number < min_int_number){
            return AddRouterMessages.too_less_interfaces;
        // check if name length isn't too short
        }else if(name.length() < min_name_characters){
            return AddRouterMessages.too_short_name;
        // check if name length isn't too long
        }else if (name.length() > max_name_characters){
            return AddRouterMessages.too_long_name;
        }

        // check if given name hasn't already been taken
        for(RouterButton router : routers){
            if (router.get_router().get_name().equals(name)){
                return AddRouterMessages.name_is_taken;
            }
        }

        // if is valid
        routers.add(new RouterButton(Icons.icon.router(), position, new Router(name, int_number), router_pop_up));
        return AddRouterMessages.is_valid;
    }

    // delete router and it's links from topology
    public void delete_router(String name){
        // find router to delete
        for(RouterButton router : routers){
            Router router_to_check = router.get_router();
            if (router_to_check.get_name().equals(name)){
                // check if it contains any links
                if(!flinks.isEmpty()) {
                    // iterate trough links in order to find link attached to router
                    boolean is_deleted = true;
                    while(is_deleted){
                        is_deleted = false;
                        for (FullLink flink : flinks) {
                            for (int i = 0; i < router_to_check.get_int_number(); i++) {
                                // check if any of router interfaces is included in searched link
                                // and if so, delete given link
                                if (flink.get_link().get_end1().equals(router_to_check.get_interface(i))) {
                                    flinks.remove(flink);
                                    is_deleted = true;
                                    break;
                                } else if (flink.get_link().get_end2().equals(router_to_check.get_interface(i))) {
                                    flinks.remove(flink);
                                    is_deleted = true;
                                    break;
                                }
                            }
                            if(is_deleted){
                                break;
                            }
                        }
                    }
                }
                // remove given router
                routers.remove(router);
                break;
            }
        }
    }

    // add link to topology
    public AddLinkMessages add_link(String end1, String end2){
        // end = [name: int_number], split to array
        String[] end1_array = end1.split(": ");
        String[] end2_array = end2.split(": ");

        // int numbers
        int end1_int_number = Integer.parseInt(end1_array[1]);
        int end2_int_number = Integer.parseInt(end2_array[1]);

        // router names
        String r1_name = end1_array[0];
        String r2_name = end2_array[0];

        // chosen RouterButtons
        RouterButton r1 = null;
        RouterButton r2 = null;

        for (RouterButton router: routers){
            if (router.get_router().get_name().equals(r1_name)){
                r1 = router;
            }else if (router.get_router().get_name().equals(r2_name)){
                r2 = router;
            }
        }

        // if link between same router
        if (r1_name.equals(r2_name)){
            return AddLinkMessages.same_router_chosen;
        }

        // if link has already been established
        if (r1 != null && r2 != null){
            for (FullLink flink : flinks){
                // find router connected to link
                RouterButton end1_router = null;
                RouterButton end2_router = null;
                // iterate routers on routers list
                for (RouterButton router: routers){
                    // iterate interfaces on router
                    for (int i = 0; i < router.get_router().get_int_number(); i++){
                        // check if interface on link is the interface on router
                        if (flink.get_link().get_end1() == router.get_router().get_interface(i)){
                            end1_router = router;
                        }else if (flink.get_link().get_end2() == router.get_router().get_interface(i)){
                            end2_router = router;
                        }
                    }
                }

                // if routers connected to link are r1 and r2 return error
                if (end1_router == r1 && end2_router == r2){
                    return AddLinkMessages.link_already_established;
                }else  if (end1_router == r2 && end2_router == r1){
                    return AddLinkMessages.link_already_established;
                }
            }
        }else{
            return AddLinkMessages.error;
        }

        // router interfaces, for new link ends
        NetworkInterface end1_interface = r1.get_router().get_interface(end1_int_number);
        NetworkInterface end2_interface = r2.get_router().get_interface(end2_int_number);

        // Positions of router
        Position[] end_positions = new Position[2];
        end_positions[0] = r1.get_position();
        end_positions[1] = r2.get_position();

        // adding flink to flinks
        Link link_to_add = new Link(end1_interface, end2_interface);
        FullLink to_add = new FullLink(link_to_add, end_positions, get_link_name(flinks));
        flinks.add(to_add);
        return AddLinkMessages.is_valid;
    }

    //delete link
    public void delete_link(String name){
        // if there is no flinks -> break
        if(!flinks.isEmpty()) {
            // find flink to delete
            name = name.trim();
            for (int i = 0; i < flinks.size(); i++) {
                if (flinks.get(i).get_name().equals(name)) {
                    // delete link from flinks
                    flinks.remove(flinks.get(i));
                    //log
                    //System.out.println("deletes flink");
                }
            }
        }
    }


    // add router on panel
    public void refresh(JPanel panel){
        JPanel map = new JPanel(new GridLayout(20, 20));
        boolean flag;

        for (int i = 0; i < 400; i++){
            flag = false;
            // add router
            for (RouterButton router: routers){
                // convert position to index
                int index = router.get_position().get_x() + 20 * router.get_position().get_y();

                // if i == index add router to panel
                if (index == i){
                    map.add(router);
                    flag = true;
                    break;
                }
            }
            // add white space if router hasn't been added
            if (!flag){
                JPanel p = new JPanel();
                p.setForeground(Color.WHITE);
                p.setBackground(Color.WHITE);
                p.setVisible(false);
                map.add(p);
            }
        }
        // delete old map from panel
        panel.removeAll();
        map.setBackground(Color.WHITE);
        map.setForeground(Color.WHITE);

        // add new map to panel
        panel.add(map);
        panel.revalidate();
    }

    public void draw_links(JPanel panel){
        Graphics2D graphics2D = (Graphics2D) panel.getGraphics();
        for (FullLink flink : flinks) {
            // half of rectangle on which routers are placed
            float x_half = (float) panel.getWidth() / 40;
            float y_half = (float) panel.getHeight() / 40;

            // link ends coordinates
            float end1_x = (float) flink.get_link_position()[0].get_x() * panel.getWidth() / 20;
            float end1_y = (float) flink.get_link_position()[0].get_y() * panel.getHeight() / 20;
            float end2_x = (float) flink.get_link_position()[1].get_x() * panel.getWidth() / 20;
            float end2_y = (float) flink.get_link_position()[1].get_y() * panel.getHeight() / 20;

            // draw link
            graphics2D.drawLine((int) (end1_x + x_half), (int) (end1_y + y_half),
                    (int) (end2_x + x_half), (int) (end2_y + y_half));

            // link name coordinates
            float x_average = (end1_x + end2_x + 2 * x_half) / 2;
            float y_average = (end1_y + end2_y + 2 * y_half) / 2;

            // draw link name
            graphics2D.drawString("link " + flink.get_name(), (int) x_average, (int) y_average);
        }
    }

    // checks if name of link is already taken
    public String get_link_name(ArrayList<FullLink> flinks){
        if(flinks.isEmpty()){
            return "0";
        }
        int possible_name = 0;
        while (true){
            boolean flag = true;
            for (FullLink flink : flinks) {
                if (flink.get_name().equals(String.valueOf(possible_name))) {
                    possible_name++;
                    flag = false;
                    break;
                }
            }
            if(flag){
                break;
            }
        }
        return String.valueOf(possible_name);
    }

    // getters:

    // topology getter
    public static Topology get_topology(){
        return topology;
    }

    // routers getter
    public ArrayList<RouterButton> get_routers(){
        return routers;
    }

    // routers
    public Router get_router(int int_number){
        return routers.get(int_number).get_router();
    }

    // max router number getter
    public int get_max_int_number(){
        return max_int_number;
    }

    // min router number getter
    public int get_min_int_number() {
        return min_int_number;
    }

    // max name characters getter
    public int get_max_name_characters() {
        return max_name_characters;
    }

    // min name characters getter
    public int get_min_name_characters() {
        return min_name_characters;
    }

    // flinks getter
    public ArrayList<FullLink> get_flinks(){ return flinks;}

    // topology clear
    public void clear(){
        routers.clear();
        flinks.clear();
    }

    public void save(String file_name){
        if (file_name.length() > 3){
            String extension = file_name.substring(file_name.length() - 4);
            if (!extension.equals(".sns")){
                file_name = file_name + ".sns";
            }
        }else{
            file_name = file_name + ".sns";
        }
        try {
            // new output stream object
            ObjectOutputStream output_stream = new ObjectOutputStream(new FileOutputStream(file_name));
            // routers ArrayList
            ArrayList<Router> routers_list = new ArrayList<>();
            // router Positions ArrayList
            ArrayList<Position> positions_list = new ArrayList<>();

            // add routers and positions to list
            for (RouterButton router : routers){
                routers_list.add(router.get_router());
                positions_list.add(router.get_position());
            }

            // add lists to file
            output_stream.writeObject(routers_list);
            output_stream.writeObject(positions_list);
            output_stream.writeObject(flinks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String file_name, RouterPopUp router_pop_up, JPanel panel){
        try {
            ObjectInputStream input_stream = new ObjectInputStream(new FileInputStream(file_name));
            topology.clear();
            // routers ArrayList
            ArrayList<Router> routers_list = (ArrayList<Router>)input_stream.readObject();
            // router Positions ArrayList
            ArrayList<Position> positions_list = (ArrayList<Position>)input_stream.readObject();

            // add routers to topology
            for (int i = 0; i < routers_list.size(); i++){
                topology.add_router(positions_list.get(i), routers_list.get(i).get_name(),
                        routers_list.get(i).get_int_number(),router_pop_up);
            }
            flinks = (ArrayList<FullLink>)input_stream.readObject();
            topology.refresh(panel);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
