package main;

import Topology.AddDeviceMessages;
import PopUps.AddDeviceController;
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

    // if map was clicked
    @FXML
    private void map_click(){
        // set cursor to default
        map.setCursor(Cursor.DEFAULT);

        // if add device is active, show add device pop up
        if (add_device_clicked){
            AddDeviceController.returned_valid = AddDeviceMessages.is_not_valid;
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

            // add router to map if valid
            if (AddDeviceController.returned_valid == AddDeviceMessages.is_valid){
                map.getChildren().addAll(AddDeviceController.returned_button);
            }
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
}