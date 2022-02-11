package main;

import MapObjects.DeviceButton;
import Devices.Devices.NetworkDevice;
import Devices.Devices.Router;
import Devices.Link;
import MapObjects.LinkLine;
import Topology.NetworkDevicesTypes;
import PopUps.AddDeviceController;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // main map
    @FXML
    private AnchorPane map;

    // if add device was clicked
    private boolean add_device_clicked;

    /////////////////////////////////////////////////////////
    //                      settings                       //
    /////////////////////////////////////////////////////////

    // links id
    public static boolean show_links_id = true;

    // link interfaces info
    public static boolean show_interfaces_info = true;


    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // init
    public void initialize(){
        add_device_clicked = false;
    }

    /////////////////////////////////////////////////////////
    //                 button functions                    //
    /////////////////////////////////////////////////////////

    // add device to map
    @FXML
    private void add_device(){
        add_device_clicked = true;
        map.setCursor(Cursor.CROSSHAIR);
        map.setOnMouseClicked(event ->{
            if(event.getButton() == MouseButton.PRIMARY){
                AddDeviceController.mouse_x = event.getX();
                AddDeviceController.mouse_y = event.getY();
                map_click();
            }else if (event.getButton() == MouseButton.SECONDARY){
                map_right_click();
            }
        });
    }

    // delete device
    @FXML
    private void delete_device(){
        FXMLLoader fxmlLoader = new FXMLLoader(AddDeviceController.class.getResource("delete-device-pop-up.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Delete device");
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        refresh();
    }

    // add link
    @FXML
    private void add_link(){
        FXMLLoader fxmlLoader = new FXMLLoader(AddDeviceController.class.getResource("add-link-pop-up.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Add link");
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        refresh();
    }

    // delete link
    @FXML
    private void delete_link(){
        FXMLLoader fxmlLoader = new FXMLLoader(AddDeviceController.class.getResource("delete-link-pop-up.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Delete link");
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        refresh();
    }

    // if map was clicked
    @FXML
    private void map_click(){
        // set cursor to default
        map.setCursor(Cursor.DEFAULT);

        // if add device is active, show add device pop up
        if (add_device_clicked){
            FXMLLoader fxmlLoader = new FXMLLoader(AddDeviceController.class.getResource("add-device-pop-up.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("Add device");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refresh();
        }

        // disable add device
        add_device_clicked = false;
    }

    // if map was right-clicked
    @FXML
    private void map_right_click(){
        map.setCursor(Cursor.DEFAULT);
        add_device_clicked = false;
    }

    // refresh map
    private void refresh(){
        map.getChildren().clear();
        draw_devices();
        draw_links();
    }

    private void draw_devices(){
        NetworkDevicesTypes device_type = NetworkDevicesTypes.ROUTER;
        for (NetworkDevice device: Topology.get_topology().get_devices()){
            if (device instanceof Router){
                device_type = NetworkDevicesTypes.ROUTER;
            }
            DeviceButton button = new DeviceButton(device.get_name(),
                    device.get_pos_x(), device.get_pos_y(), device_type);
            map.getChildren().add(button);
        }
    }

    private void draw_links(){
        for (Link link: Topology.get_topology().get_links()){
            LinkLine link_line = new LinkLine(link.get_start_x(), link.get_start_y(),
                    link.get_end_x(), link.get_end_y(), link.get_id(),
                    "int " + link.get_end1().get_number(),
                    "int " + link.get_end2().get_number());
            map.getChildren().add(link_line);
            if (show_links_id){
                map.getChildren().add(link_line.get_id_label());
            }
            if (show_interfaces_info){
                map.getChildren().add(link_line.get_end1_label());
                map.getChildren().add(link_line.get_end2_label());
            }
        }
    }
}