package UI;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class Polygon {
    private double[] xPoints;
    private double[] yPoints;

    public Polygon(ArrayList<Point2i> points){
        xPoints = new double[points.size()];
        yPoints = new double[points.size()];

        for(int i = 0;i < points.size();i++){
            xPoints[i] = points.get(i).getX();
            yPoints[i] = points.get(i).getY();
        }

    }

}