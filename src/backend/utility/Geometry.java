package backend.utility;

import UI.Dot;
import org.opencv.core.Rect;
import org.opencv.core.Size;

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
}
