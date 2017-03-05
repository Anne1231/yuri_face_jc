package Layers;

import UI.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class SelectAreaLayer extends Layer {
    private int x;
    private int y;
    private Rectangle2D rectangle2D;

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
            if(x < event.getX()) {
                if(y < event.getY()) {
                    rectangle2D = new Rectangle2D(x, y, event.getX() - x, event.getY() - y);
                }else{
                    rectangle2D = new Rectangle2D(x, event.getY(), event.getX() - x, y - event.getY());
                }
            }else if(x > event.getX()){
                if(y < event.getY()) {
                    rectangle2D = new Rectangle2D(event.getX(), y, x - event.getX(), event.getY() - y);
                }else{
                    rectangle2D = new Rectangle2D(event.getX(), event.getY(), x - event.getX(), y - event.getY());
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "グループ化しますか？", ButtonType.NO, ButtonType.YES);
            alert.setHeaderText("グループ作成の確認");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.YES){
                Main.CurrentLayerData.addPolygon(new Polygon(Main.CurrentLayerData.CreateSubPolygon(rectangle2D)));
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.getCustomColors();
            }

        });
    }

    public Polygon CreatePolygon(LayerData layerData){
        ArrayList<Dot> dots = new ArrayList<>();
        layerData.getDotSet().stream().parallel().filter(dot -> rectangle2D.contains(new Point2D(dot.getX(), dot.getY()))).forEach(dot -> {
            dots.add(dot);
            dot.Draw(this, Color.BLUE);
        });
        return new Polygon(dots);
    }
}
