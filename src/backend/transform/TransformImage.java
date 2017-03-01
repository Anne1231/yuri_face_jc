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
    /*
    *
    * ゴミに見えるけど使用方法
        Mat img = Imgcodecs.imread("C:\\Users\\Akihiro\\Desktop\\yuri_face_test1.png");
        //img = TransformImage.RotationTransform(img, 45);
        Mat original = new Mat(4,2, CvType.CV_32F);
        original.put(0, 0, new float[]{ 0.0f, 0.0f, img.cols()*0.2f, img.rows()*0.8f, img.cols(), img.rows(), img.cols()*0.8f, img.rows()*0.2f });
        img = TransformImage.PerspectiveTransform(img, original, img.size());
        Imgcodecs.imwrite("C:\\Users\\Akihiro\\Desktop\\a.png", img);
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
        rot_mat.put(0, 2, rot_mat.get(0, 2)[0] + (rect.width >> 1) - center.x);
        rot_mat.put(1, 2, rot_mat.get(1, 2)[0] + (rect.height >> 1) - center.y);

        Imgproc.warpAffine(image, result, rot_mat, rect.size());

        return result;
    }

    /*
    * CutImage関数
    * 画像の指定した範囲を切り抜く関数
    * 引数
    * cv::Mat image
    * 切り抜き元の画像
    * cv::Rect rect
    * 切り出す領域
    * 返り値
    * 切り出されたcv::Mat
    */
    public static Mat CutImage(Mat image, Rect rect) {
        return new Mat(image, rect).clone();
    }

    public static Mat PasteImage(Mat dst, Mat image, int tx, int ty){
        Mat base = dst.clone();
        byte[] color = new byte[3];
        int ix, iy = 0;
        int base_row = base.rows();
        int image_row = image.rows();
        int base_col = base.cols();
        int image_col = image.cols();
        int begin_x = tx;
        int begin_y = ty;
        for(int y = begin_y;y < base_row && iy < image_row; y++, iy++){
            ix = 0;
            for(int x = begin_x;x < base_col && ix < image_col; x++, ix++){
                image.get(iy, ix, color);
                base.put(y, x, color);
            }
        }
        return base;
    }

}
