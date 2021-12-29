package GUI.Topology;

import Devices.Link;

public class FullLink {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////
    private final Link link;
    private final Position[] link_positions;
    private final String name;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    FullLink(Link link, Position[] link_positions, String name){
        this.link = link;
        this.link_positions = link_positions;
        this.name = name;
    }


    // getters:
    // get link
    public Link get_link() {
        return link;
    }
    // get link position
    public Position[] get_link_position() {
        return link_positions;
    }
    // get links name
    public String get_name() {
        return name;
    }
}
