package motion;

import Layers.Layer;
import UI.Dot;
import UI.LayerData;
import UI.Point2i;
import UI.UIValues;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class BasicMotionFrame {
    private double[] xPoints;
    private double[] yPoints;
    Color color;

    /*
    * コンストラクタたち
     */

    public BasicMotionFrame(LayerData layerData){
        int size = layerData.getDotSet().size();
        ArrayList<Dot> dots = layerData.CreatePolygon();
        double[] xPoints = new double[size];
        double[] yPoints = new double[size];
        int i = 0;
        for(Dot dot : dots){
            xPoints[i] = dot.getX();
            yPoints[i] = dot.getY();
            i++;
        }
    }

    //オブジェクト生成時に色指定バージョン
    public BasicMotionFrame(LayerData layerData, Color color){
        int size = layerData.getDotSet().size();
        ArrayList<Dot> dots = layerData.CreatePolygon();
        double[] xPoints = new double[size];
        double[] yPoints = new double[size];
        int i = 0;
        for(Dot dot : dots){
            xPoints[i] = dot.getX();
            yPoints[i] = dot.getY();
            i++;
        }

        this.color = color;
    }

    /*
    * 渡されたレイヤーに自らを描画する関数
     */
    public void Draw(Layer layer){
        layer.getGraphicsContext().clearRect(0, 0, UIValues.WINDOW_WIDTH, UIValues.WINDOW_HEIGHT);
        layer.getGraphicsContext().setFill(this.color);
        layer.getGraphicsContext().fillPolygon(xPoints, yPoints, yPoints.length);
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
