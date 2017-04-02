package Layers;

import UI.UIValues;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.IntBuffer;

/**
 * Created by takai on 17/04/01.
 */
public class SpuitLayer extends Layer {

    private Color picked_color;

    public SpuitLayer(SystemLayers systemLayers, double width, double height){
        super(width, height);

        this.canvas.setOnMouseClicked(event -> {
            Image img = systemLayers.getImageLayer().getImage();

            int x = (int)(event.getX() * systemLayers.getImageLayer().getBairitsu());
            int y = (int)(event.getY() * systemLayers.getImageLayer().getBairitsu());

            //ピクセル配列取得
            WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
            int[] pixels = new int[(int)width * (int)height];
            img.getPixelReader().getPixels(0, 0, (int)width, (int)height, format, pixels, 0, (int)width);

            //ピクセルを取得
            int index = (y * (int)width) + x;
            int pixel = pixels[index];

            int r = ((pixel >> 16) & 0xFF);

            int g = ((pixel >> 8) & 0xFF);

            int b = (pixel & 0xFF);

            this.picked_color = Color.color((double)r / 255.0, (double)g / 255.0, (double)b / 255.0);

            this.graphicsContext.clearRect(0, 0, UIValues.LAYER_WIDTH, UIValues.LAYER_HEIGHT);
            this.canvas.toBack();
        });
    }

    private Color getPickedColor(){
        return picked_color;
    }

}
