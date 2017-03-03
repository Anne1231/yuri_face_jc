package backend.face;

import UI.LayerData;
import backend.transform.TransformImage;
import backend.utility.Geometry;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Face {
    private final Mat original;
    private FaceBase faceBase;
    private LeftEyebrows leftEyebrows;
    private RightEyebrows rightEyebrows;
    private LeftEye leftEye;
    private RightEye rightEye;
    private Mouth mouth;

    public Face(Mat src_image, ArrayList<LayerData> LayerDatas){
        original = src_image.clone();
        Rect rect;
        for(LayerData data : LayerDatas){
            rect = Geometry.MakeBoundingBox(data.getDotSet(), original.size());
            switch (data.getType()){
                case FaceBase:
                    faceBase = new FaceBase(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case LeftEyebrows:
                    leftEyebrows = new LeftEyebrows(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case RightEyebrows:
                    rightEyebrows = new RightEyebrows(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case LeftEye:
                    leftEye = new LeftEye(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case RightEye:
                    rightEye = new RightEye(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case Mouth:
                    mouth = new Mouth(data, TransformImage.CutImage(original, rect), rect);
                    break;
                case NullNull:
                    break;
                default:
            }
        }
    }

    public FaceBase getFaceBase() {
        return faceBase;
    }

    public LeftEye getLeftEye() {
        return leftEye;
    }

    public LeftEyebrows getLeftEyebrows() {
        return leftEyebrows;
    }

    public Mouth getMouth() {
        return mouth;
    }

    public RightEye getRightEye() {
        return rightEye;
    }

    public RightEyebrows getRightEyebrows() {
        return rightEyebrows;
    }

    public final Mat getOriginal(){
        return original;
    }
}
