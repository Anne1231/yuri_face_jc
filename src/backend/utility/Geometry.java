package backend.utility;

import UI.Dot;
import UI.Point2i;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Geometry {

    public static boolean isWithin(ArrayList<Point2i> ring, Point2i point){
        Point2i start, end;
        double total = 0;
        for(int i = 0;i < ring.size() - 1;i++){
            start = ring.get(i);
            end = ring.get(i + 1);
            total += ComputeAngleRad(start, point, end);
        }
        total += ComputeAngleRad(ring.get(0), point, ring.get(ring.size() - 1));
        if(Math.toDegrees(total) == 360)
            return true;
        else
            return false;
    }

    public static double ComputeAngleRad(Point2i start, Point2i mid, Point2i end){
        return Math.acos((
                Math.abs((start.getX() - mid.getX()) * (end.getX() - mid.getX()))
                +
                Math.abs((start.getY() - mid.getY()) * (end.getY() - mid.getY()))
        )
                /
                (start.distance(mid) * end.distance(mid))
        );
    }

    public static int EventDotToGridDot(double val, int interval){
        return (int)(val / interval);
    }
}
