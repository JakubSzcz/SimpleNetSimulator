package PopUps;

import Devices.Link;
import Topology.Topology;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteLinkController {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // warnings label
    @FXML
    Label warning;

    // link to delete
    @FXML
    ComboBox link_combo_box;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // initialize
    @FXML
    public void initialize(){
        for (Link link : Topology.get_topology().get_links()){
            link_combo_box.getItems().add("link " + link.get_id());
            link_combo_box.setValue("link " + link.get_id());
        }
    }

    // delete
    @FXML
    public void delete(){
        if (link_combo_box.getValue() == null){
            warning.setText("Link is not chosen");
        }else{
            String link_id_string = (String) link_combo_box.getValue();
            int link_id = Integer.parseInt(link_id_string.replace("link ", ""));
            Topology.get_topology().delete_link(link_id);

            Stage stage = (Stage) link_combo_box.getScene().getWindow();
            stage.close();
        }
    }

    // cancel
    @FXML
    public void cancel(){
        Stage stage = (Stage) link_combo_box.getScene().getWindow();
        stage.close();
    }
}
