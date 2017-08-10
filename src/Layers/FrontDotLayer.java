package Layers;

import UI.Dot;
import UI.Main;
import UI.UIValues;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.Optional;

import static UI.Main.CurrentLayerData;
import static UI.Main.x;
import static UI.Main.y;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class FrontDotLayer extends Layer {
    private boolean selecting;
    private Dot last;

    public FrontDotLayer(double width, double height){
        super(width, height);
        selecting = false;
        last = null;
    }

    public boolean isSelecting() {
        return this.selecting;
    }

    public void Selecting(boolean status){
        this.selecting = status;
    }

    public void setLast(Dot dot){
        last = dot;
    }

    public Dot getLast() {
        return last;
    }

    public boolean isLastEmpty(){
        return last == null;
    }

    @Override
    public void scroll_handler(ScrollEvent event)
    {
        GridLayer grid_layer = Main.systemLayers.getGrid();
        grid_layer.editInterval(event.getDeltaY() > 0 ? UIValues.EXPANSION_PER_ONE_SCROLL : -UIValues.EXPANSION_PER_ONE_SCROLL);
        grid_layer.redrawGrid();
    }

    /*
    * ドット配置を行うメソッド
     */
    public void putDot(GridLayer gridLayer){

        //nullチェック
        if(CurrentLayerData == null)
            return;

        for(final Dot p : CurrentLayerData.getDotSet()){
            if(Math.abs(p.getX() - x) < 5){
                if(Math.abs(p.getY() - y) < 5){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ドットを配置しますか？", ButtonType.NO, ButtonType.YES);
                    alert.setHeaderText("付近にドットがあります");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(!result.isPresent() || result.get() == ButtonType.NO){
                        return;
                    }
                    break;
                }
            }
        }

        //グリッド補正の判定
        Dot dot = gridLayer.isEnableComplete() ? new Dot(x, y, gridLayer.getInterval()) : new Dot(x, y);

        dot.Draw(this, Color.BLACK);
        CurrentLayerData.AddDot(dot);

        setLast(dot);
    }
}
