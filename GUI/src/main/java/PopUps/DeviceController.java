package PopUps;

import Devices.Devices.NetworkDevice;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DeviceController extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // current device
    static public String device_name;

    // old monitor
    private String old_monitor = new String();

    @FXML
    public TextArea cli_text_area;

    @FXML
    public Tab cli_tab;

    @FXML
    public Tab gui_tab;

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

        // key filter
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
                NetworkDevice device = Topology.get_topology().get_device(device_name);
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
                cli_text_area.setText(Topology.get_topology().get_device(device_name).get_prompt());
                cli_text_area.positionCaret(cli_text_area.getText().length());
                start();
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
    }

    @FXML
    public void cli_text_are_enter(){
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        String monitor_old = device.get_monitor();
        String cmd = cli_text_area.getText().replace(monitor_old, "");
        cmd = cmd.replace(device.get_prompt(), "");
        cmd = cmd.replace("\n", "");
        device.execute_command(cmd, true);
        if (!device.is_input_blocked()){
            cli_text_area.setText(device.get_monitor() + device.get_prompt());
            cli_text_area.positionCaret(cli_text_area.getText().length());
        }
        cli_text_area.setScrollTop(Double.MAX_VALUE);
    }

    @FXML
    public void cli_on_close(){
        interrupt();
    }

    @Override
    public void run() {
        NetworkDevice device = Topology.get_topology().get_device(device_name);
        while (true){
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
