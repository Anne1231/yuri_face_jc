package Layers;

import javafx.stage.Stage;

import static UI.UIValues.INIT_GRID_INTERVAL;
import static UI.UIValues.LAYER_HEIGHT;
import static UI.UIValues.LAYER_WIDTH;

/**
 * Created by Akihiro on 2017/03/07.
 */
public class SystemLayers {

    private FrontDotLayer front;
    private FrontDotLayer normal_front;
    private LinesLayer lines;
    private GridLayer grid;
    private ImageLayer image_layer;
    private Layer preview;
    private SelectAreaLayer selecting_rect;

    public SystemLayers(Stage stage){
        front = new FrontDotLayer(LAYER_WIDTH, LAYER_HEIGHT);       //ドットを描画するレイヤー
        normal_front = new FrontDotLayer(LAYER_WIDTH, LAYER_HEIGHT);       //ドットを描画するレイヤー2
        lines = new LinesLayer(LAYER_WIDTH, LAYER_HEIGHT);       //線を描画するレイヤー
        grid  = new GridLayer(LAYER_WIDTH, LAYER_HEIGHT, INIT_GRID_INTERVAL);       //グリッドを描画するレイヤー
        image_layer = new ImageLayer(LAYER_WIDTH, LAYER_HEIGHT); //下敷き画像を描画するレイヤー
        preview = new Layer(LAYER_WIDTH, LAYER_HEIGHT);     //プレビューを描画するレイヤー
        selecting_rect = new SelectAreaLayer(stage, LAYER_WIDTH, LAYER_HEIGHT);
    }

    public FrontDotLayer getFront() {
        return front;
    }

    public FrontDotLayer getNormalFront() {
        return normal_front;
    }

    public GridLayer getGrid() {
        return grid;
    }

    public ImageLayer getImageLayer() {
        return image_layer;
    }

    public Layer getPreview() {
        return preview;
    }

    public LinesLayer getLines() {
        return lines;
    }

    public SelectAreaLayer getSelectingRect() {
        return selecting_rect;
    }

}
