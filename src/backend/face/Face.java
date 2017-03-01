package backend.face;

import UI.LayerData;
import org.opencv.core.Mat;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Face {
    private FaceBase faceBase;
    private LeftEyebrows leftEyebrows;
    private RightEyebrows rightEyebrows;
    private LeftEye leftEye;
    private RightEye rightEye;
    private Mouth mouth;

    public Face(Mat src_image, LayerData left_eyebrows, LayerData right_eyebrows, LayerData left_eye, LayerData right_eye, LayerData mouth){
        //left_eyebrows = new LeftEyebrows();
    }

}
