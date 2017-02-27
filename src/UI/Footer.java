package UI;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class Footer extends Layer {
    public Footer(double width, double height){
        super(width, height);
    }

    public void PutText(String str, int from_left){
        graphicsContext.setFill(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        graphicsContext.fillRect(from_left - 100, 0, UIValues.WINDOW_WIDTH, 20);

        graphicsContext.setFill(new Color(0.2f, 0.2f, 0.2f, 1.0f));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);
        graphicsContext.fillText(str, from_left, 10);
    }
}
