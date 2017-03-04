package UI;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;


/**
 * Created by Akihiro on 2017/03/04.
 */
public class LayerDataEx extends LayerData {
    private Image image;
    private String image_path;
    private double bairitsu;
    private Point2D image_point;

    public LayerDataEx(String layer_name, LayerDataType type){
        super(layer_name, type);
        image_point = new Point2D(0, 0);
    }

    public Point2D getImagePoint() {
        return image_point;
    }

    public double getBairitsu() {
        return bairitsu;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImagPpoint(Point2D image_point) {
        this.image_point = image_point;
    }

    public Image getImage() {
        return image;
    }

    public String getImagePath() {
        return image_path;
    }
}
