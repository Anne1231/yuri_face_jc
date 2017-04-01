package Layers;

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

            int x = (int)event.getX();
            int y = (int)event.getY();

            //ピクセル配列取得
            WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
            int[] pixels = new int[(int)width * (int)height];
            img.getPixelReader().getPixels(0, 0, (int)width, (int)height, format, pixels, 0, (int)width);

            //ピクセルを取得
            int index = (y * (int)width) + x;
            int pixel = pixels[index];

            int a = (pixel >> 24) & 0xFF;

            int r = ((pixel >> 16) & 0xFF);

            int g = ((pixel >> 8) & 0xFF);

            int b = (pixel & 0xFF);

            this.picked_color = Color.color(r, g, b);
        });
    }

    private Color getPickedColor(){
        return picked_color;
    }

}
