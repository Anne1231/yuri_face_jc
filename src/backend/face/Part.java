package backend.face;

import UI.Dot;
import UI.LayerData;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Part {
    protected LayerData layerData;
    protected Mat image;
    protected Rect in_face;

    public Part(LayerData data, Mat image, Rect rect){
        this.layerData = data;
        this.image = image.clone();
        this.in_face = rect.clone();
    }

    public String getName(){
        return layerData.getName();
    }

    public Mat getImage() {
        return image;
    }

    public Rect getIn_file() {
        return in_face;
    }

}
