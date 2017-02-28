package Layers;

import Layers.Layer;
import javafx.scene.image.Image;

import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/02/27.
 */
public class ImageLayer extends Layer {
    private Image image;
    private String image_path;
    private double bairitsu;

    public ImageLayer(double width, double height){
        super(width, height);
        bairitsu = 1.0;
    }

    public void SetImage(Image image){
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public double getBairitsu(){
        return this.bairitsu;
    }

    public void DrawImageNormal(final Image image, double x, double y){
        this.image = image;
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphicsContext.drawImage(image, 0, 0);
    }

    public void DrawImageWithResize(final Image image, double x, double y, double width, double height, double bairitsu){
        this.image = image;
        this.bairitsu = bairitsu;
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphicsContext.drawImage(image, 0, 0, width * bairitsu, height * bairitsu);
    }

    public void setImagePath(String path){
        this.image_path = path;
    }

    public String getImagePath(){
        return image_path;
    }
    public void clear(){
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
