package animation;

import Layers.Layer;
import UI.Dot;
import UI.LayerData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;

import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class Preview {
    private ArrayList<LayerData> datalist;
    private LayerData.LayerDataType type;
    private LayerData now;
    private Timeline animation;

    int mill_sec;
    public Preview(ArrayList<LayerData> data, LayerData.LayerDataType type, int mill_sec){
        datalist = new ArrayList<>();
        /*
        *指定されたタイプのみ抽出してデータリストに追加
         */
        data.stream().filter(layerData -> layerData.getType() == type)
                .forEach(layerData -> datalist.add(layerData));
        this.type = type;
        this.mill_sec = mill_sec;
    }

    public void show(Layer preview){
        now = datalist.get(0);
        animation = new Timeline(new KeyFrame(Duration.millis(mill_sec), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                now.Organize();
                preview.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                int size = now.getDotSet().size();
                double[] xPoints = new double[size];
                double[] yPoints = new double[size];
                int i = 0;
                for(Dot dot : now.getDotSet()){
                    xPoints[i] = dot.getX();
                    yPoints[i] = dot.getY();
                    i++;
                }
                preview.getGraphicsContext().fillPolygon(xPoints, yPoints, size);
                if(datalist.indexOf(now) == datalist.size() - 1){
                    now = datalist.get(0);
                }else {
                    now = datalist.get(datalist.indexOf(now) + 1);
                }
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    public void stop(){
        animation.stop();
    }

}
