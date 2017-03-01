package backend.face;

import UI.Dot;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Part {
    private String name;
    private ArrayList<Dot> dots;
    private Mat image;
    private Rect in_file;

    public Part(String object_name, Mat image, Rect rect){
        this.name = object_name;
        dots = new ArrayList<>();
        this.image = image.clone();
        this.in_file = rect.clone();
    }

    public String getName(){
        return name;
    }

    ArrayList<Dot> getDots(){
        return this.dots;
    }

    public Mat getImage() {
        return image;
    }

    public Rect getIn_file() {
        return in_file;
    }
    
}
