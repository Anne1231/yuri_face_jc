package backend.face;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class RightEyebrows extends Part {
    public RightEyebrows(String object_name, Mat image, Rect rect){
        super(object_name, image, rect);
    }
}
