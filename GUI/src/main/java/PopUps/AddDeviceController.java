package PopUps;

import Topology.AddDeviceMessages;
import Topology.Topology;
import Topology.NetworkDevicesTypes;
import DeviceButton.DeviceButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AddDeviceController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // type combo box
    @FXML
    ComboBox type;

    // name of router
    @FXML
    TextField name;

    // number of interfaces
    @FXML
    TextField int_number;

    // warnings
    @FXML
    Label warning;

    // return values
    static public AddDeviceMessages returned_valid;
    static public Button returned_button;

    // mouse position
    static  public double mouse_x;
    static  public double mouse_y;

    // all added buttons
    private static final ArrayList<DeviceButton> devices = new ArrayList<>();

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // initialize
    @FXML
    public void initialize(){
        type.getItems().add(NetworkDevicesTypes.ROUTER);
        // type.getItems().add(NetworkDevicesTypes.SWITCH);
        type.setValue(NetworkDevicesTypes.ROUTER);
    }

    // add device
    @FXML
    public void add(){
        boolean is_valid = true;

        // name
        String name_string = name.getText();

        // int number
        String string_int_number = int_number.getText();

        // type
        NetworkDevicesTypes device_type = (NetworkDevicesTypes) type.getValue();

        // int number in int
        int int_number_int;

        // check if int number is integer
        try{
            // interfaces number
            int_number_int = Integer.parseInt(string_int_number);

            // add device to topology
            AddDeviceMessages message = Topology.get_topology().add_device(name_string, int_number_int, device_type);

            // if any errors occurred
            if (message == AddDeviceMessages.name_is_taken){
                is_valid = false;
                warning.setText("This name has already been taken");
            }else if (message == AddDeviceMessages.too_many_interfaces){
                is_valid = false;
                warning.setText("Number of interfaces must not exceed " + Topology.MAX_INT_NUMBER);
            }else if (message == AddDeviceMessages.too_less_interfaces){
                is_valid = false;
                warning.setText("Number of interfaces must not be smaller than" + Topology.MIN_INT_NUMBER);
            }else if (message == AddDeviceMessages.too_short_name){
                is_valid = false;
                warning.setText("Number of interfaces must not be smaller than " + Topology.MIN_NAME_CHARACTERS);
            }else if (message == AddDeviceMessages.too_long_name){
                is_valid = false;
                warning.setText("Number of interfaces must not exceed " + Topology.MAX_NAME_CHARACTERS);
            }
        }catch (NumberFormatException e){
            // if int number isn't integer
            is_valid = false;
            warning.setText("Number of interfaces must be integer");
        }
        // if valid
        if (is_valid){
            DeviceButton button = new DeviceButton(name_string, mouse_x, mouse_y);
            devices.add(button);
            returned_button = button;
            returned_valid = AddDeviceMessages.is_valid;
            close();
        }
    }

    @FXML
    public void close(){
        Stage stage = (Stage) type.getScene().getWindow();
        stage.close();
    }

}
