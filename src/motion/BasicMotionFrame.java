package motion;

import Layers.Layer;
import UI.Point2i;
import UI.UIValues;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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

    public BasicMotionFrame(ArrayList<Point2i> points){
        xPoints = new double[points.size()];
        yPoints = new double[points.size()];

        for(int i = 0;i < points.size();i++){
            xPoints[i] = points.get(i).getX();
            yPoints[i] = points.get(i).getY();
        }

    }

    //オブジェクト生成時に色指定バージョン
    public BasicMotionFrame(ArrayList<Point2i> points, Color color){
        xPoints = new double[points.size()];
        yPoints = new double[points.size()];

        for(int i = 0;i < points.size();i++){
            xPoints[i] = points.get(i).getX();
            yPoints[i] = points.get(i).getY();
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
