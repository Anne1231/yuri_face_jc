package Layers;

import UI.Main;
import UI.UIValues;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class SelectAreaLayer extends Layer {
    private int x;
    private int y;

    public SelectAreaLayer(double width, double height){
        super(width, height);
        x = y = 0;

        graphicsContext.setLineDashes(6);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(1);

        canvas.setOnMousePressed(event -> {
            x = (int)event.getX();
            y = (int)event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            graphicsContext.clearRect(0, 0, width, height);
            if(x < event.getX()) {
                if(y < event.getY()) {
                    graphicsContext.strokeRect(x, y, event.getX() - x, event.getY() - y);
                }else{
                    graphicsContext.strokeRect(x, event.getY(), event.getX() - x, y - event.getY());
                }
            }else if(x > event.getX()){
                if(y < event.getY()) {
                    graphicsContext.strokeRect(event.getX(), y, x - event.getX(), event.getY() - y);
                }else{
                    graphicsContext.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
                }
            }
        });

        canvas.setOnMouseReleased(event -> {
            graphicsContext.clearRect(0, 0, width, height);
        });
    }
}
