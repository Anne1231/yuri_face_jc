package UI;

import Layers.SystemLayers;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class BackGroundImageUI {
    private StackPane root;
    private CheckBox display;
    private Label bairitsu_label;
    private Label image_place;
    private Label x_label;
    private Label y_label;
    private TextField image_bairitsu;
    private TextField image_x;
    private TextField image_y;

    public BackGroundImageUI(SystemLayers systemLayers){
        root = new StackPane();

        VBox base = new VBox();

        display = new CheckBox("下敷き画像を表示");
        display.setDisable(false);
        display.setOnAction(event -> {
            if (display.isSelected()) {
                systemLayers.getImageLayer().Redraw(
                        Integer.valueOf(image_x.getText()),
                        Integer.valueOf(image_y.getText())
                );
            } else {
                systemLayers.getImageLayer().eraseLayer();
            }
        });

        bairitsu_label = new Label("下敷き画像倍率");

        image_place = new Label("下敷き画像表示位置");

        x_label = new Label("X位置");
        y_label = new Label("Y位置");

        /*
        * 倍率指定のテキストフィールドの設定
         */
        image_bairitsu = new TextField("100.0%");
        image_bairitsu.setMaxWidth(UIValues.LAYER_LIST_WIDTH);
        image_bairitsu.setOnAction(event -> {
            double result = Double.parseDouble(image_bairitsu.getText().replaceAll("[^.0-9]",""));
            image_bairitsu.setText(result + "%");
            result /= 100.0;
            Image image = systemLayers.getImageLayer().getImage();
            systemLayers.getImageLayer().clear();
            systemLayers.getImageLayer().DrawImageWithResize(image,
                    Integer.valueOf(this.image_x.getText()),
                    Integer.valueOf(this.image_y.getText()),
                    image.getWidth(), image.getHeight(), result);
        });


        /*
        * 下敷き画像移動処理を関数インターフェースとして定義しておく
         */
        Runnable image_xy_field_action = () -> {
            systemLayers.getImageLayer().MoveImage(
                Integer.valueOf(image_x.getText()),
                Integer.valueOf(image_y.getText())
            );
        };
        image_x = new TextField("0");
        image_y = new TextField("0");
        image_x.setMaxWidth(UIValues.LAYER_LIST_WIDTH / 2);
        image_y.setMaxWidth(UIValues.LAYER_LIST_WIDTH / 2);

        image_x.setOnAction(event -> image_xy_field_action.run() );
        image_y.setOnAction(event -> image_xy_field_action.run() );

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

        Rectangle rect = new Rectangle(0, 1000, UIValues.LAYER_LIST_WIDTH, 200);
        rect.setFill(Color.WHITE);

        base.getChildren().addAll(rect, display, place_box, image_bairitsu_box);
        base.setSpacing(10);

        root.getChildren().addAll(rect, base);

    }

    public StackPane getRoot() {
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

    public TextField getImage_bairitsu_field() {
        return image_bairitsu;
    }

    public TextField getImage_x() {
        return image_x;
    }

    public TextField getImage_y() {
        return image_y;
    }



}
