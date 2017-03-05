package UI;

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
}