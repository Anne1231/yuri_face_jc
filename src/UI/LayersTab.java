package UI;

import Layers.Layer;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class LayersTab {

    private ListView<String> mouth_layers;
    private ListView<String> right_eye_layers;
    private ListView<String> left_eye_layers;
    private ListView<String> left_eyebrows_layers;
    private ListView<String> right_eyebrows_layers;

    // タブペイン
    private TabPane layers_tab;
    private Tab right_eye_tab;
    private Tab left_eye_tab;
    private Tab right_eyebrows_tab;
    private Tab left_eyebrows_tab;
    private Tab mouth_tab;

    int layer_count;


    public void LayersTab(){
         mouth_layers = new ListView<>();
         right_eye_layers = new ListView<>();
         left_eye_layers = new ListView<>();
         left_eyebrows_layers = new ListView<>();
         right_eyebrows_layers = new ListView<>();

        /*
         * タブペイン
         */
        layers_tab = new TabPane();
        right_eye_tab = new Tab("右目");
        left_eye_tab = new Tab("左目");
        right_eyebrows_tab = new Tab("右眉");
        left_eyebrows_tab = new Tab("左眉");
        mouth_tab = new Tab("お口");

        right_eye_tab.setContent(right_eye_layers);
        left_eye_tab.setContent(left_eye_layers);
        right_eyebrows_tab.setContent(right_eyebrows_layers);
        left_eyebrows_tab.setContent(left_eyebrows_layers);
        mouth_tab.setContent(mouth_layers);

        layers_tab.getTabs().addAll(right_eyebrows_tab, left_eyebrows_tab, right_eye_tab, left_eye_tab, mouth_tab);

        layer_count = 0;

     }

    public ListView<String> getLeft_eye_layers() {
        return left_eye_layers;
    }

    public ListView<String> getLeft_eyebrows_layers() {
        return left_eyebrows_layers;
    }

    public ListView<String> getMouth_layers() {
        return mouth_layers;
    }

    public ListView<String> getRight_eye_layers() {
        return right_eye_layers;
    }

    public ListView<String> getRight_eyebrows_layers() {
        return right_eyebrows_layers;
    }

    public Tab getLeft_eye_tab() {
        return left_eye_tab;
    }

    public Tab getLeft_eyebrows_tab() {
        return left_eyebrows_tab;
    }

    public Tab getRight_eye_tab() {
        return right_eye_tab;
    }

    public Tab getRight_eyebrows_tab() {
        return right_eyebrows_tab;
    }

    public Tab getMouth_tab() {
        return mouth_tab;
    }

    public TabPane getLayers_tab() {
        return layers_tab;
    }

    public int getLayer_count() {
        return layer_count;
    }

    public void increase_LayerCount() {
        this.layer_count++;
    }

    public void decrease_LayerCount() {
        this.layer_count--;
    }
}
