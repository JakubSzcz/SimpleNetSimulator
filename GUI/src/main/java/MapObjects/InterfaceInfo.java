package MapObjects;

import Devices.Devices.NetworkDevice;
import Protocols.Packets.IPv4;
import Topology.Topology;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class InterfaceInfo extends HBox {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // interface
    String device_name;
    int int_number;

    // int number
    Label int_number_label;

    // state
    Button state;

    // address
    Label ip_address;
    Label slash;
    Label mask;

    // link
    Label link_label;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////
    public InterfaceInfo(String device_name, int int_number){
        // init
        this.device_name = device_name;
        this.int_number = int_number;
        int_number_label = new Label();
        ip_address = new Label();
        slash = new Label();
        mask = new Label();
        link_label = new Label();
        state = new Button();

        // style
        setId("body");
        int_number_label.setId("int_number");
        state.setId("state");
        ip_address.setId("ip_address");
        slash.setId("slash");
        mask.setId("mask");
        link_label.setId("link_label");

        // int number
        int_number_label.setText("int " + int_number);
        int_number_label.prefWidthProperty().bind(this.widthProperty().multiply(0.1));

        // state
        state.prefWidthProperty().bind(this.widthProperty().multiply(0.05));

        // ip address and mask
        ip_address.prefWidthProperty().bind(this.widthProperty().multiply(0.2));
        mask.prefWidthProperty().bind(this.widthProperty().multiply(0.1));
        slash.prefWidthProperty().bind(this.widthProperty().multiply(0.05));
        slash.setText("/");

        // link label
        link_label.prefWidthProperty().bind(this.widthProperty().multiply(0.2));

        getStylesheets().add("file:GUI/src/main/resources/css/interface-info.css");
        refresh();
    }

    public void refresh(){
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        // state
        if (device.get_interface(int_number).is_up()){
            state.setStyle("-fx-background-color: green");
        }else{
            state.setStyle("-fx-background-color: red");
        }

        // ip address and mask
        Map<String, Long> address_map = device.get_interface(int_number).get_ip_address();
        if (address_map.get("address") == -1){
            ip_address.setText("-");
            mask.setText("-");
        }else{
            ip_address.setText(IPv4.parse_to_string(address_map.get("address")));
            mask.setText(IPv4.parse_mask_to_string_short(address_map.get("mask")));
        }

        // link label
        int link_id = (int)Topology.get_topology().get_device_from_topology_map(device_name).get(int_number);
        if (link_id == -1){
            link_label.setText("unconnected");
        }else{
            link_label.setText("link " + link_id);
        }

        // add to vbox
        getChildren().clear();
        getChildren().addAll(int_number_label, state, ip_address, slash, mask, link_label);
    }
}
