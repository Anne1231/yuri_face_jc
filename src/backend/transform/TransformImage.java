package backend.transform;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

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

    /*
    * PasteImage関数
    * 引数
    * Mat base
    * 貼り付けさきの画像
    * Mat image
    * 貼り付ける画像
    * int tx
    * 貼り付けるX位置
    * int ty
    * 貼り付けるY位置
     */
    public static Mat PasteImage(Mat base, Mat image, int tx, int ty){
        Mat dst = base.clone();
        byte[] color = new byte[3];
        int ix, iy = 0;
        int base_row = base.rows();
        int image_row = image.rows();
        int base_col = base.cols();
        int image_col = image.cols();
        for(int y = ty;y < base_row && iy < image_row; y++, iy++){
            ix = 0;
            for(int x = tx;x < base_col && ix < image_col; x++, ix++){
                image.get(iy, ix, color);
                base.put(y, x, color);
            }
        }
        return dst;
    }

    /*
    * SharpenImage関数
    * 回転画像等の周りを黒いピクセルで埋められた画像において、黒い部分と本来の画像の曖昧な部分を削りとる関数
    * 引数
    * cv::Mat src_image
    * 削りとる画像
    * 返り値
    * 削り取られた画像
    */
    public static Mat SharpenImage(Mat src_image) {

        int start_x, start_y, end_x, end_y;
        start_x = start_y = end_x = end_y = 0;

        byte[] color = new byte[3];

        loop1 : for(int y = 0; y < src_image.rows(); y++){
            for(int x = 0; x < src_image.cols(); x++){
                src_image.get(y, x, color);
                if(color[0] + color[1] + color[2] == 0){
                    start_x = x;
                    start_y = y;
                    break loop1;
                }
            }
        }

        loop2 : for(int y = src_image.rows() - 1; y >= 0; y--){
            for(int x = src_image.cols() - 1; x >= 0; x--){
                src_image.get(y, x, color);
                if(color[0] + color[1] + color[2] == 0){
                    end_x = x;
                    end_y = y;
                    break loop2;
                }
            }
        }

        Scalar black = new Scalar(0, 0, 0);
        Imgproc.line(src_image, new Point(start_x, start_y), new Point(end_x, start_y), black, 5);
        Imgproc.line(src_image, new Point(end_x, start_y), new Point(end_x, end_y), black, 5);
        Imgproc.line(src_image, new Point(end_x, end_y), new Point(start_x, end_y), black, 5);
        Imgproc.line(src_image, new Point(start_x, end_y), new Point(start_x, start_y), black, 5);

        return src_image.clone();
    }

    /*
    * YuriInpainting関数
    * 画像の指定された範囲を修復する関数
    * 今回はナビエストークスのアルゴリズム
    * 引数
    * Mat image
    * 修復する画像
    * Rect rect
    * 修復する範囲
     */
    public static Mat YuriInpainting(Mat image, Rect rect) {
        Mat mask = new Mat(image.size(), CvType.CV_8UC1, Scalar.all(0));
        mask.submat(rect).setTo(Scalar.all(255));
        Mat result = image.clone();
        Photo.inpaint(image, mask, result, 10, Photo.INPAINT_NS);
        return result;
    }

    public static Mat YuriFacePasteImage(Mat dst_image, Mat src_image, int dx, int dy){
        Mat dst = dst_image.clone();
        byte[] color = new byte[3];
        int ix, iy = 0;
        int base_row = dst.rows();
        int image_row = dst.rows();
        int base_col = dst.cols();
        int image_col = dst.cols();
        for(int y = dy;y < base_row && iy < image_row; y++, iy++){
            ix = 0;
            for(int x = dx;x < base_col && ix < image_col; x++, ix++){
                src_image.get(iy, ix, color);
                if(color[0] + color[1] + color[2] != 0)
                    dst.put(y, x, color);
            }
        }
        return dst;
    }

}
