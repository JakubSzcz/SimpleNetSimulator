package PopUps;

import Enums.AddDeviceMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // initialize
    @FXML
    public void initialize(){
        type.getItems().add("Router");
    }

    @FXML
    public void add(){
        // add router
        boolean is_valid = true;

        // name
        String name_string = name.getText();

        // int number
        String string_int_number = int_number.getText();

        // int number in int
        int int_number_int;

        // check if int number is integer
        try{
            // interfaces number
            int_number_int = Integer.parseInt(string_int_number);

            // add router to topology
            AddDeviceMessages message = AddDeviceMessages.is_valid;

            // if any errors occurred
            if (message == message.name_is_taken){
                is_valid = false;
                warning.setText(message.toString());
            }else if (message == message.too_many_interfaces){
                is_valid = false;
                warning.setText(message.toString());
            }else if (message == message.too_less_interfaces){
                is_valid = false;
                warning.setText(message.toString());
            }else if (message == message.too_short_name){
                is_valid = false;
                warning.setText(message.toString());
            }else if (message == message.too_long_name){
                is_valid = false;
                warning.setText(message.toString());
            }
        }catch (NumberFormatException e){
            // if int number isn't integer
            is_valid = false;
            warning.setText("Number of interfaces must be integer");
        }
        if (is_valid){
            Button button = new Button();
            button.setText(name_string);
            button.setLayoutX(mouse_x);
            button.setLayoutY(mouse_y);
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
