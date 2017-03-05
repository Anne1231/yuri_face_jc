package sub;

import UI.Dot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class CreatePolygonWindow extends Stage {

    private String ans_name;
    private Color selected_color;

    public CreatePolygonWindow(Window window){
        setTitle("グループ化");
        initStyle(StageStyle.UTILITY);
        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(400);
        setHeight(400);

        VBox root = new VBox();

        HBox polygon_name_box = new HBox();
        Label pn_label = new Label("グループ名 :");
        TextField polygon_name_tf = new TextField("グループ名");
        polygon_name_box.getChildren().addAll(pn_label, polygon_name_tf);
        polygon_name_box.setAlignment(Pos.CENTER);

        HBox color_select_box = new HBox();
        Label color_label = new Label("ドットの色を選択 : ");
        ColorPicker colorPicker = new ColorPicker();
        color_select_box.getChildren().addAll(color_label, colorPicker);
        color_select_box.setAlignment(Pos.CENTER);

        Button create_button = new Button("グループを作成");
        create_button.setOnAction(event -> {
            this.ans_name = polygon_name_tf.getText();
            this.selected_color = colorPicker.getValue();
            close();
        });

        root.getChildren().addAll(polygon_name_box, color_select_box, create_button);
        root.setSpacing(30);
        root.setPadding(new Insets(30, 30, 30, 30));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);

        setScene(scene);

    }

    public String getAnsName() {
        return ans_name;
    }

    public Color getSelectedColor() {
        return selected_color;
    }
}
