package sub;

import UI.LayerData;
import UI.LayersTree;
import UI.Main;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import motion.BasicMotion;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class CreateMotionWindow extends Stage {

    private BasicMotion motion;

    public CreateMotionWindow(Window window, LayersTree layersTree, LayersTree motion_tree){

        setTitle("モーション作成");
        initStyle(StageStyle.UTILITY);
        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(600);
        setHeight(750);

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
        motion_name.setMaxWidth(330.0);

        motion_name_box.getChildren().addAll(motion_name_label, motion_name);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        HBox create_button_box = new HBox();
        Button create_button = new Button("モーション作成");
        create_button.setPrefWidth(130);
        create_button.setPrefHeight(130);
        create_button_box.getChildren().addAll(create_button);
        create_button_box.setAlignment(Pos.BOTTOM_RIGHT);
        create_button_box.setSpacing(20.0);
        create_button_box.setPadding(new Insets(10, 10, 10, 10));

        create_button.setOnAction(event -> {
            ArrayList<LayerData> layerDatas = new ArrayList<>();
            including_layers.getItems().forEach(str -> {
                Main.LayerDatas.stream().filter(layerData -> layerData.getType() == motion_tree.WhichType(motion_tree.getSelecting_tree()))
                        .forEach(layerData -> {
                            if(layerData.getName().equals(str)){
                                layerDatas.add(layerData);
                            }
                        });
            });
            BasicMotion basicMotion = new BasicMotion(motion_name.getText(), layerDatas, motion_tree.WhichType(motion_tree.getSelecting_tree()));
            Main.basicMotions.add(basicMotion);
            motion_tree.getSelecting_tree().getChildren().add(new TreeItem<>(motion_name.getText()));
            close();
        });

        root.getChildren().addAll(layer_for_motion, including_layers, add, motion_name_box, create_button_box);

        setScene(new Scene(root));

    }
}
