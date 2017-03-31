package UI;

import Layers.Layer;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;

import static UI.UIValues.LAYER_LIST_HEIGHT;
import static UI.UIValues.LAYER_LIST_WIDTH;
import static UI.UIValues.LAYER_WIDTH;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class ReferenceImagesUI extends LayersTree {
    private TreeItem<String> face_base;
    private CorePartLayerDatas corePartLayerDatas;
    private CheckBox preview;

    public ReferenceImagesUI(String tree_name){
        super(tree_name);

        corePartLayerDatas = new CorePartLayerDatas();

        face_base = new TreeItem<>("顔面");
        face_base.getChildren().add(new TreeItem<>("輪郭"));
        face_base.setExpanded(true);
        layer_root.getChildren().add(face_base);
        face_base.expandedProperty().addListener((observable, oldValue, newValue) -> {
            face_base.setExpanded(true);
        });

        right_eye_tree.getChildren().add(new TreeItem<>("黒目"));
        right_eye_tree.getChildren().add(new TreeItem<>("まぶた"));
        right_eye_tree.setExpanded(true);
        right_eye_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            right_eye_tree.setExpanded(true);
        });

        left_eye_tree.getChildren().add(new TreeItem<>("黒目"));
        left_eye_tree.getChildren().add(new TreeItem<>("まぶた"));
        left_eye_tree.setExpanded(true);
        left_eye_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            left_eye_tree.setExpanded(true);
        });

        right_eyebrows_tree.getChildren().add(new TreeItem<>("眉"));
        right_eyebrows_tree.setExpanded(true);
        right_eyebrows_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            right_eyebrows_tree.setExpanded(true);
        });

        left_eyebrows_tree.getChildren().add(new TreeItem<>("眉"));
        left_eyebrows_tree.setExpanded(true);
        left_eyebrows_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            left_eyebrows_tree.setExpanded(true);
        });

        mouth_tree.getChildren().add(new TreeItem<>("口"));
        mouth_tree.setExpanded(true);
        mouth_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            mouth_tree.setExpanded(true);
        });

        layer_root.expandedProperty().addListener((observable, oldValue, newValue) -> {
            layer_root.setExpanded(true);
        });

        preview = new CheckBox("他パーツを裏に表示");

    }

    public void DrawAllData(Layer front, Layer lines){
        corePartLayerDatas.getM_mouth().AllDraw4PR(front, lines);
        corePartLayerDatas.getR_e_mabuta().AllDraw4PR(front, lines);
        corePartLayerDatas.getR_e_kurome().AllDraw4PR(front, lines);
        corePartLayerDatas.getL_e_mabuta().AllDraw4PR(front, lines);
        corePartLayerDatas.getL_e_kurome().AllDraw4PR(front, lines);
        corePartLayerDatas.getR_e_b_mayu().AllDraw4PR(front, lines);
        corePartLayerDatas.getL_e_b_mayu().AllDraw4PR(front, lines);
        corePartLayerDatas.getM_mouth().AllDraw4PR(front, lines);
    }

    public CorePartLayerDatas getCorePartLayerDatas() {
        return corePartLayerDatas;
    }

    public CheckBox getPreviewBox() {
        return preview;
    }

    public void SettingAnchor(){
        AnchorPane.setLeftAnchor(preview, LAYER_LIST_WIDTH + LAYER_WIDTH + 20);
        AnchorPane.setTopAnchor(preview, LAYER_LIST_HEIGHT + 50);
    }
}
