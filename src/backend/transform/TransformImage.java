package backend.transform;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class TransformImage {

    /*
    * PerspectiveTransform関数
    * 画像を透視射影変換して返す関数
    * 引数
    * Mat image
    * 透視射影変換を行う画像
    * Mat src_matrix
    * どんなふうに透視射影変換を行うか
    * Size dst_size
    * 生成される画像のサイズ
    * 返り値
    * 第一引数で受け取ったMatの透視射影変換結果の画像
    */
    public static Mat PerspectiveTransform(Mat image, Mat src_matrix, Size dst_size) {
        Mat result, dst = new Mat(image.rows(), image.cols(), image.type());

        Mat original = new Mat(4,2, CvType.CV_32F);
        original.put(0, 0, new float[]{ 0.0f, 0.0f, 0.0f, image.rows(), image.cols(), image.rows(), image.cols(), 0.0f });

        //変換行列作成
        result = Imgproc.getPerspectiveTransform(original, src_matrix);
        Imgproc.warpPerspective(image, dst, result, dst_size);

        return dst;

    }

    /*
    * RotationTransform関数
    * 画像を回転変形する関数
    * 引数
    * Mat image
    * 回転変形させる画像
    * double angle
    * 回転させる角度（度数法で指定）
    * 返り値
    * 第一引数で受け取ったMatの回転変形結果の画像
    */
    public static Mat RotationTransform(Mat image, double angle) {
        //結果用
        Mat result = new Mat(image.rows(), image.cols(), image.type());

        //画像の中心座標を取得
        Point center = new Point(image.cols() / 2.0, image.rows() / 2.0);
        Mat rot_mat = Imgproc.getRotationMatrix2D(center, angle, 1.0);

        //矩形を決定する
        Rect rect = new RotatedRect(center, image.size(), angle).boundingRect();

        //変換行列を調整
        rot_mat.put(0, 2, rect.width / 2.0 - center.x);
        rot_mat.put(1, 2, rect.height / 2.0 - center.y);

        Imgproc.warpAffine(image, result, rot_mat, rect.size());

        return result;
    }
}
