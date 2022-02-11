package PopUps;

import Topology.Topology;
import Topology.AddLinkMessages;
import Devices.Devices.NetworkDevice;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AddLinkController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // combo boxes
    @FXML
    ComboBox end1_combo_box;
    @FXML
    ComboBox end2_combo_box;

    // warnings label
    @FXML
    Label warning;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // initialize
    @FXML
    public void initialize(){
        for (NetworkDevice device: Topology.get_topology().get_devices()){
            ArrayList<Integer> free_interfaces = Topology.get_topology().get_free_int_number(device);
            for (Integer free_interface: free_interfaces){
                end1_combo_box.getItems().add(device.get_name() + ": " + free_interface);
                end1_combo_box.setValue(device.get_name() + ": " + free_interface);
                end2_combo_box.getItems().add(device.get_name() + ": " + free_interface);
                end2_combo_box.setValue(device.get_name() + ": " + free_interface);
            }
        }
    }

    // add
    @FXML
    public void add(){
        // get values
        String end1 = (String) end1_combo_box.getValue();
        String end2 = (String) end2_combo_box.getValue();

        // if ends are empty
        if (end1 == null || end2 == null){
            warning.setText("End are not chosen");
        }else{
            String[] end1_array = end1.split(": ");
            String[] end2_array = end2.split(": ");
            int int_number1 = Integer.parseInt(end1_array[1]);
            int int_number2 = Integer.parseInt(end2_array[1]);
            AddLinkMessages message = Topology.get_topology().
                    add_link(end1_array[0], int_number1, end2_array[0], int_number2);
            if (message == AddLinkMessages.is_valid){
                Stage stage = (Stage) end1_combo_box.getScene().getWindow();
                stage.close();
            }else{
                // TODO
                warning.setText("Error");
            }
        }
    }

    // add
    @FXML
    public void cancel(){
        Stage stage = (Stage) end1_combo_box.getScene().getWindow();
        stage.close();
    }
}
