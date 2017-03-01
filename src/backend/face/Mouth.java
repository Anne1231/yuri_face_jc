package backend.face;

import UI.LayerData;
import backend.transform.TransformImage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class Mouth extends Part {
    public Mouth(LayerData data, Mat image, Rect rect){
        super(data, image, rect);
    }

    public Mat openMouth(Face face, int level, float manual) {
        Mat result = this.image.clone();
        float k;

        if ((k = openMouth_sub(level)) == -1.0f) {
            k = manual;
        }

        Point center = new Point((in_face.x + (in_face.width >> 1)), (in_face.y + (in_face.height >> 1)));

        /*
        result = TransformImage.RotationTransform(result, 45);

        Mat matrix = new Mat(4,2, CvType.CV_32F);
        matrix.put(
                0,
                0,
                new float[]{
                        0.0f, 0.0f,
                        image.cols() * k, image.rows() * (1.0f - k),
                        image.cols(), image.rows(),
                        image.cols() * (1.0f - k), image.rows() * k
                });

        result = TransformImage.SharpenImage(
                TransformImage.RotationTransform(
                        TransformImage.PerspectiveTransform(
                                result, matrix, new Size(result.cols(), result.rows())), -45)
	    );

        //変形後の幅（回転等で黒くなっている部分を除く）
        return TransformImage.YuriFacePasteImage(
                TransformImage.YuriInpainting(face.getOriginal().clone(), this.in_face),
                result.clone(),
                (int)(center.x - (result.cols() >> 1)),
                (int)(center.y - (result.rows() >> 1)));
*/
        Mat img = result.clone();
        Imgproc.resize(result, img, new Size(0, 0), 0.5 + openMouth_sub(level), 0.5 + openMouth_sub(level), Imgproc.INTER_CUBIC);
        return TransformImage.YuriFacePasteImage(
                TransformImage.YuriInpainting(face.getOriginal().clone(), this.in_face),
                img,
                (int)(center.x - (img.cols() >> 1)),
                (int)(center.y - (img.rows() >> 1)));
    }

    private static float openMouth_sub(int level) {
        switch (level) {
            case 0:
                return 0.0f;
            case 1:
                return 0.01f;
            case 2:
                return 0.03f;
            case 3:
                return 0.05f;
            case 4:
                return 0.1f;
            case 5:
                return 0.15f;
            case 6:
                return 0.2f;
            case 7:
                return 0.25f;
            case 8:
                return 0.3f;
            case 9:
                return 0.35f;
            case 10:
                return 0.4f;
            default:
                break;
        }
        return -1.0f;
    }
}
