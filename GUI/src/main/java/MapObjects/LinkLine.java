package MapObjects;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class LinkLine extends Line {
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // position correction
    public static double x_correction = 15;
    public static double y_correction = 10;

    // link label
    private final Label id_label = new Label();

    // interfaces info
    private final Label end1_label = new Label();
    private final Label end2_label = new Label();


    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    public LinkLine(double start_x, double start_y, double end_x, double end_y){
        this(start_x, start_y, end_x, end_y, 0, null, null);
    }
    public LinkLine(double start_x, double start_y, double end_x, double end_y, int link_id,
                    String end1_label, String end2_label){
        // Line constructor
        super(start_x + x_correction, start_y + y_correction,
                end_x + x_correction, end_y + y_correction);

        // link id label
        double id_x = Math.abs(start_x + end_x + 2 * x_correction) / 2;
        double id_y = Math.abs(start_y + end_y + 2 * y_correction) / 2;
        id_label.setText("link " + link_id);
        id_label.setLayoutX(id_x);
        id_label.setLayoutY(id_y);

        // end labels
        if (start_x < end_x){
            this.end1_label.setLayoutX(start_x + 3 * x_correction);
            this.end2_label.setLayoutX(end_x - 2 * x_correction);
        }else{
            this.end1_label.setLayoutX(start_x - 3 * x_correction);
            this.end2_label.setLayoutX(end_x + 2 * x_correction);
        }
        this.end1_label.setLayoutY(start_y + y_correction);
        this.end2_label.setLayoutY(end_y);
        this.end1_label.setText(end1_label);
        this.end2_label.setText(end2_label);
    }

    // id label getter
    public Label get_id_label() {
        return id_label;
    }

    // interface labels getters

    public Label get_end1_label() {
        return end1_label;
    }

    public Label get_end2_label() {
        return end2_label;
    }
}
