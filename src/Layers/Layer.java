package Layers;

import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;

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
        canvas.setOnScroll(event -> scroll_handler(event));
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

    /*
    * アクティブレイヤーの変更を行うメソッド
     */
    public void beForward(){
        canvas.toFront();
    }

    public void scroll_handler(ScrollEvent event){}

    public void clear(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
