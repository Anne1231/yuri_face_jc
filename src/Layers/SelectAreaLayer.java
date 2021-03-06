package Layers;

import UI.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import sub.CreatePolygonWindow;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Akihiro on 2017/03/05.
 */
public class SelectAreaLayer extends Layer {
    private int x;
    private int y;
    private Rectangle2D rectangle2D;

    public SelectAreaLayer(Stage stage, double width, double height){
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "グループ化しますか？", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("グループ作成の確認");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.YES){
                Window window = stage;
                CreatePolygonWindow createPolygonWindow = new CreatePolygonWindow(window);
                createPolygonWindow.showAndWait();
                Main.CurrentLayerData.addPolygon(new Polygon(Main.CurrentLayerData.CreateSubPolygon2(rectangle2D), createPolygonWindow.getAnsName(), createPolygonWindow.getSelectedColor()));
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.getCustomColors();
                this.graphicsContext.clearRect(0, 0, UIValues.LAYER_WIDTH, UIValues.LAYER_HEIGHT);
                this.canvas.toBack();
            }
            this.graphicsContext.clearRect(0, 0, UIValues.LAYER_WIDTH, UIValues.LAYER_HEIGHT);
        });

        canvas.setCursor(Cursor.CROSSHAIR);

    }

}
