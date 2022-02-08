package main;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // main map
    @FXML
    private AnchorPane map;

    //
    private boolean add_device_clicked;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    MainController(){
        add_device_clicked = false;
    }

    // add device to map
    @FXML
    private void add_device(){
        add_device_clicked = true;
    }

    // delete device
    @FXML
    private void delete_device(){
        // TODO
    }

    // add link
    @FXML
    private void add_link(){
        // TODO
    }

    // delete link
    @FXML
    private void delete_link(){
        // TODO
    }

    //
    @FXML
    private void map_click(){
        // TODO
        add_device_clicked = false;
    }
}