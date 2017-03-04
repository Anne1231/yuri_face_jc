package backend.utility;

import UI.Dot;
import UI.Point2i;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Geometry {
    public static Rect MakeBoundingBox(HashSet<Dot> dots, Size size){
        int start_x, start_y, end_x, end_y;
        start_x = (int)size.width;
        start_y = (int)size.height;
        end_x = 0;
        end_y = 0;

        for(Dot dot : dots){
            if(dot.getX() < start_x)
                start_x = dot.getX();
            if(dot.getX() > end_x)
                end_x = dot.getX();
            if(dot.getY() < start_y)
                start_y = dot.getY();
            if(dot.getY() > end_y)
                end_y = dot.getY();
        }

        return new Rect(start_x, start_y, end_x - start_x, end_y - start_y);
    }

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
}
