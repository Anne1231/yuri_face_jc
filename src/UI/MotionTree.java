package UI;

import Layers.Layer;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import motion.BasicMotion;
import sub.CreateMotionWindow;

import java.util.Optional;

import static UI.Main.*;

/**
 * Created by takai on 17/03/31.
 */
public class MotionTree extends LayersTree {

    public MotionTree(String tree_name, Stage stage, LayersTree layersTree, Layer preview_layer){
        super(tree_name);

        ContextMenu popup_ll = new ContextMenu();
        MenuItem create_layer = new MenuItem("新規モーション");
        MenuItem clone_item = new MenuItem("複製");
        popup_ll.getItems().addAll(create_layer, clone_item);

        ContextMenu copy_menu = new ContextMenu();
        MenuItem copy_item = new MenuItem("コピー");
        MenuItem preview = new MenuItem("プレビュー");
        copy_menu.getItems().addAll(copy_item, preview);

        layer_selecting = false;

        create_layer.setOnAction(event -> CreateMotion(stage, layersTree));

        this.treeView.setOnContextMenuRequested(event -> {
            if(this.selecting_depth == 2) {
                popup_ll.show(this.treeView, event.getScreenX(), event.getScreenY());
            }else if(getSelectingDepth() == 3){
                copy_menu.show(this.treeView, event.getScreenX(), event.getScreenY());
            }
        });

        copy_item.setOnAction(event -> {
            PinnedData = CurrentLayerData;
        });

        preview.setOnAction(event -> {
            BasicMotion basicMotion = SearchAndGetMotion(selecting, WhichType(selecting_tree));
            basicMotion.setMill_sec(1000);
            basicMotion.setPreviewLayer(preview_layer);
            basicMotion.usingFxPreview();
        });

        clone_item.setOnAction(event -> {
            TextInputDialog clone_layer = new TextInputDialog("モーション");
            clone_layer.setTitle("モーション複製");
            clone_layer.setHeaderText("モーションの複製");
            clone_layer.setContentText("モーション名 :");
            Optional<String> result = clone_layer.showAndWait();

            if(result.isPresent()) {
                if (result.get().isEmpty())
                    return;
                for (TreeItem<String> item : selecting_tree.getChildren()) {
                    if (item.getValue().equals(result.get())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("同名のモーションが存在します");
                        alert.showAndWait();
                        return;
                    }
                }
                addCloneLayer(result.get(), PinnedData, this);
            }
        });

        treeView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                popup_ll.hide();
                copy_menu.hide();
            }
            TreeItem<String> select = treeView.getSelectionModel().selectedItemProperty().get();

            int depth = 0;
            TreeItem<String> ref = select;
            while(true){
                if(ref == null){
                    break;
                }
                ref = ref.getParent();
                depth++;
            }

            if(depth == 2) {
                setSelecting_tree(select);
                setSelectingDepth((char)2);
            }else if(depth == 3){
                setLayer_selecting(true);
                //新規レイヤーメニューは表示させない
                //選択中のdepthで判定
                setSelecting(select.getValue());
                setSelectingDepth((char)3);
            }else{
                //新規レイヤーメニューは表示させない
                //選択中のdepthで判定
                setSelectingDepth((char)1);
            }
        });

        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.setEditable(true);

    }

    /*
    * モーションを新しく作成するメソッド
     */
    private void CreateMotion(Stage stage, LayersTree layersTree){
        Window window = stage;
        CreateMotionWindow createMotionWindow = new CreateMotionWindow(window, layersTree, this);

        /*
        * ウィンドウクラス中にモーションを追加しているのでこちら側からは何もしない
         */
        createMotionWindow.show();

    }

}
