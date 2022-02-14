package PopUps;

import Devices.Devices.NetworkDevice;
import Devices.Devices.NetworkInterface;
import MapObjects.InterfaceInfo;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class DeviceController extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // current device
    static public String device_name;

    // old monitor
    private String old_monitor = new String();

    // commands history
    private int commands_history_pointer = 0;

    @FXML
    public TextArea cli_text_area;

    @FXML
    public Tab cli_tab;

    @FXML
    public Tab gui_tab;

    @FXML
    public VBox gui_vbox;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    @FXML
    public void initialize(){
        // key enter
        cli_text_area.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                cli_text_are_enter();
            }
        });

        // key filter key, released
        cli_text_area.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            // device
            NetworkDevice device = Topology.get_topology().get_device(device_name);

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
            // device
            NetworkDevice device = Topology.get_topology().get_device(device_name);

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
                if (!isAlive()){
                    start();
                }
                cli_text_area.setScrollTop(Double.MAX_VALUE);
                cli_text_area.getScene().getWindow().setOnCloseRequest(close_event -> interrupt());
            }
        });

        // mouse disable
        cli_text_area.addEventFilter(MouseEvent.ANY, mouse_event -> {
            mouse_event.consume();
        });

        // cli open
        cli_tab.setOnSelectionChanged(event ->{
            // TODO
        });

        // gui vbox add
        for (int i = 0; i < Topology.get_topology().get_device(device_name).get_int_number(); i++){
            gui_vbox.getChildren().add(
                    new InterfaceInfo(device_name, i));
        }
    }

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
        interrupt();
    }

    @Override
    public void run() {
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        while (true){
            // System.out.println("monitor");
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
}
