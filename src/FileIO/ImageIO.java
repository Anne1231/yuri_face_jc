package FileIO;

import Layers.ImageLayer;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class ImageIO {

    private static FileChooser image_selecter = new FileChooser();

    /*
    * GetImagePath関数
    * 画像を開いて、パスを取得する関数
     */
    public static String GetImagePath(Stage stage) {

        image_selecter.setTitle("File select");
        image_selecter.getExtensionFilters().add(new FileChooser.ExtensionFilter("イメージファイル", "*.jpg", "*.png"));

        File img_file = image_selecter.showOpenDialog(stage);

        if(img_file != null) {
            return img_file.getPath().toString();
        }else{
            return null;
        }
    }

    /*
    * SelectAndOpenImage関数
    * 画像を選択し、開く処理を一度にこなす関数
     */
    public static Image SelectAndOpenImage(Stage stage, ImageLayer imageLayer){
        imageLayer.setImagePath(Paths.get(GetImagePath(stage)).toUri().toString());
        return new Image(imageLayer.getImagePath());
    }

}