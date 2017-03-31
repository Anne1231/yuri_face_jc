package UI;

import Layers.FrontDotLayer;
import Layers.LinesLayer;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.Optional;

import static UI.Main.*;

/**
 * Created by takai on 17/03/31.
 */
public class NormalLayersTree extends LayersTree {

    public NormalLayersTree(String tree_name, Stage stage, FrontDotLayer normal_front, FrontDotLayer front, LinesLayer lines, ReferenceImagesUI referenceImagesUI){
        super(tree_name);

        ContextMenu popup_ll = new ContextMenu();
        MenuItem create_layer = new MenuItem("新規レイヤー");
        MenuItem clone_item = new MenuItem("複製");
        popup_ll.getItems().addAll(create_layer, clone_item);

        ContextMenu copy_menu = new ContextMenu();
        MenuItem copy_item = new MenuItem("コピー");
        copy_menu.getItems().addAll(copy_item);

        setLayer_selecting(false);

        create_layer.setOnAction(event -> CreateLayer(stage, referenceImagesUI));

        treeView.setOnContextMenuRequested(event -> {
            if(selecting_tree != null) {
                popup_ll.show(treeView, event.getScreenX(), event.getScreenY());
            }else if(isLayer_selecting()){
                copy_menu.show(treeView, event.getScreenX(), event.getScreenY());
            }
        });

        copy_item.setOnAction(event -> {
            PinnedData = CurrentLayerData;
        });

        clone_item.setOnAction(event -> {

            if(PinnedData == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("コピーされたレイヤーが存在しません");
                alert.showAndWait();
                return;
            }

            TextInputDialog clone_layer = new TextInputDialog("レイヤー");
            clone_layer.setTitle("レイヤー複製");
            clone_layer.setHeaderText("レイヤーの複製");
            clone_layer.setContentText("レイヤー名 :");
            Optional<String> result = clone_layer.showAndWait();

            if(result.isPresent()) {
                if (result.get().isEmpty())
                    return;
                for (TreeItem<String> item : selecting_tree.getChildren()) {
                    if (item.getValue().equals(result.get())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("同名のレイヤーが存在します");
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
            }else if(depth == 3){
                setLayer_selecting(true);

                CurrentLayerData = SearchAndGetLayer(select.getValue(), WhichType(select.getParent()));
                SwitchUsersLayer(CurrentLayerData, normal_front, lines);

                //パーツ側のフロントレイヤーを全削除
                front.eraseLayer();

                //新規レイヤーメニューは表示させない
                //裏ではnullで判定してる
                setSelecting_tree(null);
            }else{
                //新規レイヤーメニューは表示させない
                //裏ではnullで判定してる
                setSelecting_tree(null);
            }
        });

        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.setEditable(true);

    }

    /*
   * レイヤーを新しく作成する関数
    */
    private void CreateLayer(Stage stage, ReferenceImagesUI referenceImagesUI){
        TextInputDialog create_layer = new TextInputDialog("レイヤー");
        create_layer.setTitle("新規レイヤー");
        create_layer.setHeaderText("新規レイヤーの作成");
        create_layer.setContentText("レイヤー名 :");
        Optional<String> result = create_layer.showAndWait();

        if(result.isPresent()){
            if(result.get().isEmpty())
                return;
            for(TreeItem<String> item : selecting_tree.getChildren()){
                if(item.getValue().equals(result.get())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("同名のレイヤーが存在します");
                    alert.showAndWait();
                    return;
                }
            }
            addLayer(result.get(), WhichType(selecting_tree), this, referenceImagesUI);
        }
    }

}
