package PopUps;

import Devices.Devices.NetworkDevice;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class DeviceController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // current device
    static public String device_name;

    // old monitor
    private String old_monitor = "";

    // commands history
    private int commands_history_pointer = 0;

    /////////////////////////////////////////////////////////
    //              CLI variables and objects              //
    /////////////////////////////////////////////////////////

    @FXML
    public TextArea cli_text_area;

    @FXML
    public Tab cli_tab;

    // cli thread
    Thread cli_thread;

    /////////////////////////////////////////////////////////
    //              GUI variables and objects              //
    /////////////////////////////////////////////////////////

    @FXML
    public Tab gui_tab;

    @FXML
    public VBox gui_vbox;

    // interface h box
    @FXML
    HBox int0;
    @FXML
    HBox int1;
    @FXML
    HBox int2;
    @FXML
    HBox int3;
    @FXML
    HBox int4;
    @FXML
    HBox int5;
    @FXML
    HBox int6;
    @FXML
    HBox int7;

    HBox[] ints;

    // state buttons
    @FXML
    Button int0_state;
    @FXML
    Button int1_state;
    @FXML
    Button int2_state;
    @FXML
    Button int3_state;
    @FXML
    Button int4_state;
    @FXML
    Button int5_state;
    @FXML
    Button int6_state;
    @FXML
    Button int7_state;

    Button[] int_states;


    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    @FXML
    public void initialize(){
        // init

        // int states
        int_states = new Button[]{int0_state, int1_state, int2_state, int3_state, int4_state,
        int5_state, int6_state, int7_state};

        for (int i = 0; i < int_states.length; i++){
            int finalI = i;
            int_states[i].setOnAction(event -> change_int_state(finalI));
        }

        ints = new HBox[]{int0, int1, int2, int3, int4, int5, int6, int7};

        // device
        NetworkDevice device = Topology.get_topology().get_device(device_name);

        // key ENTER
        cli_text_area.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                cli_text_are_enter();
            }
        });

        // key filter key, released
        cli_text_area.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            // down and up arrows assign to history
            KeyCode code = event.getCode();
            boolean is_arrow_key = code == KeyCode.UP || code == KeyCode.DOWN;
            ArrayList<String> commands_history = device.get_commands_history();
            if (is_arrow_key){
                if (!device.is_input_blocked()){
                    if (code == KeyCode.UP){
                        if (commands_history.size() + commands_history_pointer - 1 >= 0){
                            commands_history_pointer--;
                            cli_text_area.setText(device.get_monitor() + device.get_prompt()
                                    + commands_history.get(commands_history.size() + commands_history_pointer));
                            cli_text_area.positionCaret(cli_text_area.getText().length());
                        }
                    }else{
                        if (commands_history.size() + commands_history_pointer + 1 < commands_history.size()){
                            commands_history_pointer++;
                            cli_text_area.setText(device.get_monitor() + device.get_prompt()
                                    + commands_history.get(commands_history.size() + commands_history_pointer));
                            cli_text_area.positionCaret(cli_text_area.getText().length());
                        }
                    }
                }
            }
        });

        // key filter, key any
        cli_text_area.addEventFilter(KeyEvent.ANY, event -> {
            // disable down and up arrows
            KeyCode code = event.getCode();
            boolean is_arrow_key = code == KeyCode.UP || code == KeyCode.DOWN;
            if (is_arrow_key) {
                event.consume();
            }

            // prompt protection
            code = event.getCode();
            boolean is_left_arrow_or_backspace_key = code == KeyCode.LEFT || code == KeyCode.BACK_SPACE;
            if (is_left_arrow_or_backspace_key) {
                String monitor_old = device.get_monitor();
                String after_caret = cli_text_area.getText().substring(cli_text_area.getCaretPosition());
                String cmd = cli_text_area.getText().replace(after_caret, "");
                cmd = cmd.replace(monitor_old, "");
                if (cmd.equals(device.get_prompt())){
                    event.consume();
                }
            }
            // start cli
            if (cli_text_area.getText().equals("")){
                event.consume();
                cli_text_area.setText(Topology.get_topology().get_device(device_name).get_monitor()
                        + Topology.get_topology().get_device(device_name).get_prompt());
                cli_text_area.positionCaret(cli_text_area.getText().length());
                start_cli_thread();
                cli_text_area.setScrollTop(Double.MAX_VALUE);
                cli_text_area.getScene().getWindow().setOnCloseRequest(close_event -> stop_cli_thread());
            }
        });

        // mouse
        cli_text_area.addEventFilter(MouseEvent.ANY, mouse_event -> {
            boolean is_click = mouse_event.getEventType() == MouseEvent.MOUSE_CLICKED;
            if (is_click){
                if(!cli_text_area.isFocused()){
                    cli_text_area.requestFocus();
                    cli_text_area.positionCaret(cli_text_area.getText().length());
                }else{
                    mouse_event.consume();
                }
            }else{
                mouse_event.consume();
            }
        });

        // tab changes
        cli_tab.setOnSelectionChanged(event ->{
            if (cli_thread != null){
                if (cli_thread.isAlive()){
                    stop_cli_thread();
                }
            }
            cli_text_area.clear();
            gui_refresh();
        });
    }

    // start cli thread
    private void start_cli_thread(){
        if (cli_thread != null){
            if (!cli_thread.isAlive()){
                Runnable r = this::cli_run;
                cli_thread = new Thread(r);
                cli_thread.start();
            }
        }else{
            Runnable r = this::cli_run;
            cli_thread = new Thread(r);
            cli_thread.start();
        }
    }

    // stop cli thread
    private void stop_cli_thread(){
        if (cli_thread != null){
            if (cli_thread.isAlive()){
                cli_thread.interrupt();
            }
        }
    }

    // cli thread run method
    private void cli_run() {
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        while (true){
            System.out.println("monitor");
            // update monitor
            if(!device.get_monitor().equals(old_monitor)){
                old_monitor = device.get_monitor();
                if (device.is_input_blocked()){
                    cli_text_area.setText(device.get_monitor());
                    cli_text_area.setEditable(false);
                    // cli_text_area.setStyle("-fx-display-caret: false;");
                }else{
                    cli_text_area.setText(device.get_monitor() + device.get_prompt());
                    cli_text_area.positionCaret(cli_text_area.getText().length());
                    cli_text_area.setEditable(true);
                }
                cli_text_area.setScrollTop(Double.MAX_VALUE);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    /////////////////////////////////////////////////////////
    //                   CLI functions                     //
    /////////////////////////////////////////////////////////

    @FXML
    public void cli_text_are_enter(){
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        String monitor_old = device.get_monitor();
        String cmd = cli_text_area.getText().replace(monitor_old, "");
        cmd = cmd.replace(device.get_prompt(), "");
        cmd = cmd.replace("\n", "");
        device.execute_command(cmd, true);
        old_monitor = device.get_monitor();
        if (!device.is_input_blocked()){
            cli_text_area.setText(device.get_monitor() + device.get_prompt());
            cli_text_area.positionCaret(cli_text_area.getText().length());
        }else{
            cli_text_area.setEditable(false);
        }
        cli_text_area.setScrollTop(Double.MAX_VALUE);
        commands_history_pointer = 0;
    }

    @FXML
    public void cli_on_close(){
        stop_cli_thread();
    }

    /////////////////////////////////////////////////////////
    //                   GUI functions                     //
    /////////////////////////////////////////////////////////

    // refresh gui appearance
    private void gui_refresh(){
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        // device interfaces
        for (int i = 0; i < device.get_int_number(); i++){
            if (device.get_interface(i).is_up()){
                int_states[i].setStyle("-fx-background-color: green");
            }else{
                int_states[i].setStyle("-fx-background-color: red");
            }
        }

        // rest
        for (int i = device.get_int_number(); i < Topology.MAX_INT_NUMBER; i++){
            ints[i].setVisible(false);
        }
    }

    // button state click
    private void change_int_state(int int_number){
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        if (device.get_interface(int_number).is_up()){
            device.down_interface(int_number);
        }else{
            device.up_interface(int_number);
        }
        gui_refresh();
    }
}
