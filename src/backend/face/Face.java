package backend.face;

import UI.LayerData;
import org.opencv.core.Mat;

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
        for(LayerData data : LayerDatas){

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
