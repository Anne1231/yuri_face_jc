package UI;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class BackGroundImageUI {
    private VBox root;
    private CheckBox display;
    private Label bairitsu_label;
    private Label image_place;
    private Label x_label;
    private Label y_label;
    private TextField image_bairitsu;
    private TextField image_x;
    private TextField image_y;

    public BackGroundImageUI(){
        root = new VBox();

        display = new CheckBox("下敷き画像を表示");
        display.setDisable(false);

        bairitsu_label = new Label("下敷き画像倍率");

        image_place = new Label("下敷き画像表示位置");

        x_label = new Label("X位置");
        y_label = new Label("Y位置");

        image_bairitsu = new TextField("100.0%");

        image_x = new TextField("0");
        image_y = new TextField("0");

        VBox x_box = new VBox();
        x_box.getChildren().addAll(x_label, image_x);

        VBox y_box = new VBox();
        y_box.getChildren().addAll(y_label, image_y);

        HBox place_ask = new HBox();
        place_ask.getChildren().addAll(x_box, y_box);

        VBox place_box = new VBox();
        place_box.getChildren().addAll(image_place, place_ask);

        VBox image_bairitsu_box = new VBox();
        image_bairitsu_box.getChildren().addAll(bairitsu_label, image_bairitsu);

        root.getChildren().addAll(display, place_box, image_bairitsu_box);

    }

    public VBox getRoot() {
        return root;
    }

    public CheckBox getDisplay() {
        return display;
    }

    public Label getBairitsu_label() {
        return bairitsu_label;
    }

    public Label getImage_place() {
        return image_place;
    }

    public Label getX_label() {
        return x_label;
    }

    public Label getY_label() {
        return y_label;
    }

    public void setBairitsu_label(Label bairitsu_label) {
        this.bairitsu_label = bairitsu_label;
    }

    public TextField getImage_bairitsu() {
        return image_bairitsu;
    }

    public TextField getImage_x() {
        return image_x;
    }

    public TextField getImage_y() {
        return image_y;
    }

}
