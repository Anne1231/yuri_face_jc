package UI;

import javafx.scene.image.Image;

import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/02/27.
 */
public class ImageLayer extends Layer {
    Image image;

    public ImageLayer(double width, double height){
        super(width, height);
    }

    public void SetImage(Image image){
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void DrawImageNormal(final Image image, double x, double y){
        this.image = image;
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphicsContext.drawImage(image, 0, 0);
    }

    public void DrawImageWithResize(final Image image, double x, double y, double width, double height){
        this.image = image;
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphicsContext.drawImage(image, 0, 0, width, height);
    }

    public void clear(){
        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
