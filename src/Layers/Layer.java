package Layers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/02/25.
 */
public class Layer {
    protected Canvas canvas;
    protected GraphicsContext graphicsContext;

    public Layer(double width, double height){
        canvas = new Canvas(width, height);
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    /*
    * 指定したグラフィックレイヤーをすべて消す関数
     */
    public void eraseLayer(){
        this.graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
