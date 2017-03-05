package UI;

import javafx.scene.control.TreeItem;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class ReferenceImagesUI extends LayersTree {
    private TreeItem<String> face_base;
    private CorePartLayerDatas corePartLayerDatas;

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

    }

    public CorePartLayerDatas getCorePartLayerDatas() {
        return corePartLayerDatas;
    }
}
