package UI;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class LayersTree {

    protected TreeItem<String> layer_root;
    protected TreeItem<String> mouth_tree;
    protected TreeItem<String> right_eye_tree;
    protected TreeItem<String> left_eye_tree;
    protected TreeItem<String> right_eyebrows_tree;
    protected TreeItem<String> left_eyebrows_tree;
    protected TreeView<String> treeView;
    protected TreeItem<String> selecting_tree;
    protected String selecting;
    protected int layers_count;
    protected char selecting_depth;

    protected boolean layer_selecting;

    public LayersTree(String tree_name){
        layer_root = new TreeItem<>(tree_name);
        layer_root.setExpanded(true);
        mouth_tree = new TreeItem<>("口");
        right_eye_tree = new TreeItem<>("右目");
        left_eye_tree = new TreeItem<>("左目");
        right_eyebrows_tree = new TreeItem<>("右眉");
        left_eyebrows_tree = new TreeItem<>("左眉");
        layer_root.getChildren().addAll(right_eyebrows_tree, left_eyebrows_tree, right_eye_tree, left_eye_tree, mouth_tree);
        treeView = new TreeView<String>(layer_root);

        mouth_tree.setExpanded(true);
        AnchorPane.setTopAnchor(treeView, UIValues.LAYER_LIST_SCREEN_Y);
        AnchorPane.setLeftAnchor(treeView, 0.0);
        treeView.setPrefWidth(UIValues.LAYER_LIST_WIDTH);
        treeView.setPrefHeight(UIValues.LAYER_LIST_HEIGHT);

        selecting = new String();
        layers_count = 0;
        selecting_depth = 0;
    }

    public TreeItem<String> getLayer_root() {
        return layer_root;
    }

    public TreeItem<String> getLeft_eye_tree() {
        return left_eye_tree;
    }

    public TreeItem<String> getLeft_eyebrows_tree() {
        return left_eyebrows_tree;
    }

    public TreeItem<String> getMouth_tree() {
        return mouth_tree;
    }

    public TreeItem<String> getRight_eye_tree() {
        return right_eye_tree;
    }

    public TreeItem<String> getRight_eyebrows_tree() {
        return right_eyebrows_tree;
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }

    public int getLayers_count() {
        return layers_count;
    }

    public void increase_layers_count(){
        layers_count++;
    }

    public void decrease_layers_count(){
        layers_count--;
    }

    public void setSelecting(String path){
        this.selecting = path;
    }

    public String getSelecting() {
        return selecting;
    }

    public TreeItem<String> getSelecting_tree() {
        return selecting_tree;
    }

    public void setSelecting_tree(TreeItem<String> selecting_tree) {
        this.selecting_tree = selecting_tree;
    }

    public LayerData.LayerDataType WhichType(TreeItem<String> item){
        if(right_eyebrows_tree.equals(item)){
            return LayerData.LayerDataType.RightEyebrows;
        }else if(left_eyebrows_tree.equals(item)){
            return LayerData.LayerDataType.LeftEyebrows;
        }else if(right_eye_tree.equals(item)){
            return LayerData.LayerDataType.RightEye;
        }else if(left_eye_tree.equals(item)){
            return LayerData.LayerDataType.RightEye;
        }else if(mouth_tree.equals(item)){
            return LayerData.LayerDataType.Mouth;
        }
        return LayerData.LayerDataType.NullNull;
    }

    public void setLayer_selecting(boolean status){
        layer_selecting = status;
    }

    public boolean isLayer_selecting(){
        return layer_selecting;
    }

    public void setSelectingDepth(char selecting_depth) {
        this.selecting_depth = selecting_depth;
    }

    public char getSelectingDepth() {
        return selecting_depth;
    }
}
