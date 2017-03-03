package sub;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class CreateMotionWindow extends Stage {

    public CreateMotionWindow(Window window){

        setTitle("モーション作成");
        initStyle(StageStyle.UTILITY);
        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(600);
        setHeight(700);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10.0);

        Label layer_for_motion = new Label("モーション用レイヤー");

        ListView<String> including_layers = new ListView<>(FXCollections.observableArrayList("モーションに使うレイヤー名を入力してください"));
        including_layers.setMaxWidth(500);
        including_layers.setPrefHeight(450);

        including_layers.setEditable(true);

        including_layers.setCellFactory(TextFieldListCell.forListView());

        including_layers.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                including_layers.getItems().set(t.getIndex(), t.getNewValue());
                System.out.println("commit");
            }

        });

        including_layers.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                System.out.println("cancel");
            }
        });

        Button add = new Button("レイヤーを追加");
        add.setMaxWidth(100);

        add.setOnAction(event -> {
            including_layers.getItems().add("");
            including_layers.getSelectionModel().select(including_layers.getItems().size()-1);
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        HBox motion_name_box = new HBox();
        motion_name_box.setAlignment(Pos.BOTTOM_CENTER);

        Label motion_name_label = new Label("モーション名 : ");

        TextField motion_name = new TextField();
        motion_name.setText("モーション名を入力してください");
        motion_name.setMaxWidth(300.0);

        motion_name_box.getChildren().addAll(motion_name_label, motion_name);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        root.getChildren().addAll(layer_for_motion, including_layers, add, motion_name_box);

        setScene(new Scene(root));

    }
}
