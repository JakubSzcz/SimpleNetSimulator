package PopUps;

import Devices.Devices.NetworkDevice;
import Devices.Link;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteDeviceController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // devices to delete
    @FXML
    public ComboBox devices_to_delete;

    // warnings
    @FXML
    public Label warning;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    @FXML
    public void initialize(){
        for (NetworkDevice device : Topology.get_topology().get_devices()){
            devices_to_delete.getItems().add(device.get_name());
            devices_to_delete.setValue(device.get_name());
        }
    }

    // delete
    @FXML
    public void delete(){
        if (devices_to_delete.getValue() == null){
            warning.setText("Link is not chosen");
        }else{
            String device_name = (String) devices_to_delete.getValue();
            Topology.get_topology().delete_device(device_name);

            Stage stage = (Stage) devices_to_delete.getScene().getWindow();
            stage.close();
        }
    }

    // cancel
    @FXML
    public void cancel(){
        Stage stage = (Stage) devices_to_delete.getScene().getWindow();
        stage.close();
    }

}
