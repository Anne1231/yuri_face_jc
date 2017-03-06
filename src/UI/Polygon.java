package UI;

import Layers.Layer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class Polygon {
    private double[] xPoints;
    private double[] yPoints;
    private String polygon_name;
    private Color dot_color;

    public Polygon(ArrayList<Dot> points, String polygon_name, Color color){
        xPoints = new double[points.size()];
        yPoints = new double[points.size()];

        for(int i = 0;i < points.size();i++){
            xPoints[i] = points.get(i).getX();
            yPoints[i] = points.get(i).getY();
        }

        this.polygon_name = polygon_name;
        this.dot_color = color;

    }

    public Polygon(int size, String polygon_name, Color color){
        xPoints = new double[size];
        yPoints = new double[size];
        this.polygon_name = polygon_name;
        this.dot_color = color;
    }

    public double getX(int index){
        return xPoints[index];
    }

    public double getY(int index){
        return yPoints[index];
    }

    public String getPolygonName() {
        return polygon_name;
    }

    public Color getDotColor() {
        return dot_color;
    }

    public int size(){
        return xPoints.length;
    }

    public Polygon clone(){
        Polygon clone = new Polygon(this.xPoints.length, this.polygon_name, this.dot_color);
        for(int i = 0;i < this.xPoints.length;i++){
            clone.xPoints[i] = this.xPoints[i];
            clone.yPoints[i] = this.yPoints[i];
        }
        return clone;
    }

    public void MoveDot(Point2i old, Point2i update){
        for(int i = 0;i < xPoints.length;i++){
            if(old.getX() == xPoints[i]){
                if(old.getY() == yPoints[i]){
                    xPoints[i] = update.getX();
                    yPoints[i] = update.getY();
                }
            }
        }
    }

    public Dot isOverlaps(Point2i point){
        for(int i = 0;i < xPoints.length;i++){
            if(Math.abs(point.getX() - xPoints[i]) <= 5){
                if(Math.abs(point.getY() - yPoints[i]) <= 5){
                    return new Dot((int)xPoints[i], (int)yPoints[i]);
                }
            }
        }
        return null;
    }

    public void DrawDots(Layer layer){
        for(int i = 0;i < xPoints.length;i++){
            layer.getGraphicsContext().setFill(dot_color);
            layer.getGraphicsContext().setStroke(dot_color);
            layer.getGraphicsContext().setLineWidth(UIValues.DOT_CIRCLE_WIDTH);
            layer.getGraphicsContext().fillOval(xPoints[i], yPoints[i], 4, 4);
            layer.getGraphicsContext().strokeOval(xPoints[i] - 3, yPoints[i] - 3, 10, 10);
        }
    }

    public double[] getxPoints() {
        return xPoints;
    }

    public double[] getyPoints() {
        return yPoints;
    }
}