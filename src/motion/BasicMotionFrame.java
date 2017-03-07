package motion;

import Layers.Layer;
import UI.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class BasicMotionFrame {
    private ArrayList<Polygon> polygons;
    private Color color;

    /*
    * コンストラクタたち
     */

    public BasicMotionFrame(LayerData layerData){
        polygons = new ArrayList<>();
        layerData.getPolygons().stream().parallel().forEach(polygon -> {
            polygons.add(polygon.clone());
        });
    }

    public BasicMotionFrame(){
        polygons = new ArrayList<>();
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
        polygons.forEach(polygon -> {
            layer.getGraphicsContext().setFill(polygon.getDotColor());
            layer.getGraphicsContext().fillPolygon(polygon.getxPoints(), polygon.getyPoints(), polygon.size());
        });
    }

    public void fillWhite(Layer layer){
        layer.getGraphicsContext().setFill(Color.WHITE);
        polygons.forEach(polygon -> {
            layer.getGraphicsContext().fillPolygon(polygon.getxPoints(), polygon.getyPoints(), polygon.size());
        });
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BasicMotionFrame CreateMid(BasicMotionFrame target, double rate){
        BasicMotionFrame frame = new BasicMotionFrame();
        for(int i = 0;i < polygons.size();i++){
            Polygon polygon = new Polygon(polygons.get(i).size(), "", polygons.get(i).getDotColor());
            for(int j = 0;j < polygon.size();j++){
                polygon.setX(j, this.polygons.get(i).getX(j) * (1 - rate) + target.polygons.get(i).getX(j) * rate);
                polygon.setY(j, this.polygons.get(i).getY(j) * (1 - rate) + target.polygons.get(i).getY(j) * rate);
            }
            frame.polygons.add(polygon);
        }
        return frame;
    }

}
