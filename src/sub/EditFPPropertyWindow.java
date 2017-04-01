package sub;

import UI.LayerDataEx;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by takai on 17/03/31.
 */
public class EditFPPropertyWindow extends Stage {

    public EditFPPropertyWindow(Window window, String tree_name, LayerDataEx layerDataEx){
        setTitle(tree_name + "のプロパティ編集");
        initStyle(StageStyle.UTILITY);
        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(500);
        setHeight(800);
        AnchorPane root = new AnchorPane();


        HBox color_select_box = new HBox();
        Label color_label = new Label("塗りつぶす色を選択 : ");
        ColorPicker colorPicker = new ColorPicker();
        color_select_box.getChildren().addAll(color_label, colorPicker);
        color_select_box.setAlignment(Pos.CENTER);
        SetAnchorPane(color_select_box, 100.0, 50.0);


        HBox buttons_box = new HBox();
        Button done = new Button("保存");
        Button cancel = new Button("キャンセル");
        buttons_box.getChildren().addAll(done, cancel);
        buttons_box.setSpacing(20.0);
        SetAnchorPane(buttons_box, 200, 700);

        root.getChildren().addAll(color_select_box, buttons_box);

        Scene scene = new Scene(root);
        setScene(scene);
    }

    private void SetAnchorPane(Node node, double x, double y){
        AnchorPane.setLeftAnchor(node, x);
        AnchorPane.setTopAnchor(node, y);
    }
}
