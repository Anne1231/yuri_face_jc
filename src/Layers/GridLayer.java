package Layers;

import UI.UIValues;
import javafx.scene.paint.Color;

import static UI.UIValues.*;
import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/02/27.
 */
public class GridLayer extends Layer {
    private int interval;
    private boolean enableComplete;

    public GridLayer(double width, double height, int interval){
        super(width, height);
        this.interval = interval;
        enableComplete = false;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void ConfigCompletion(boolean status){
        enableComplete = status;
    }

    public boolean isEnableComplete() {
        return enableComplete;
    }

    /*
    * グリッドを描画するメソッド
     */
    public void redrawGrid(int interval){

        this.interval = interval;
        redrawGrid();

    }

    public void redrawGrid(){
        int i;
        this.graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.graphicsContext.setStroke(Color.GRAY);
        this.graphicsContext.setLineWidth(0.5);

        for(i = 0;i < WINDOW_WIDTH;i += interval){
            this.graphicsContext.strokeLine(i, 0, i, WINDOW_HEIGHT);
        }

        for(i = 0;i < WINDOW_HEIGHT;i += interval){
            this.graphicsContext.strokeLine(0, i, WINDOW_WIDTH, i);
        }


        drawFrame();
    }

    /*
    * グリッドを消すメソッド
     */
    public void eraseGrid(){
        eraseLayer();
        drawFrame();
    }

    /*
    * グリッドシステムレイヤーの枠を描画する非公開メソッド
     */
    private void drawFrame(){
        this.graphicsContext.setLineWidth(2);
        this.graphicsContext.setFill(Color.BLACK);
        this.graphicsContext.strokeRect(0, 0, LAYER_WIDTH, LAYER_HEIGHT);
    }

    void editInterval(int value)
    {
        if(value > 0)
        {
            if(interval <= UIValues.MAXIMUM_GRID_INTERVAL)
            {
                interval += value;
            }
        }else if(value < 0)
        {
            if(interval >= UIValues.MINIMUM_GRID_INTERVAL)
            {
                interval += value;
            }
        }
    }
}
