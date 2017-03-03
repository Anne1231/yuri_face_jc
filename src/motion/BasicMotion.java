package motion;

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
public class BasicMotion {
    private ArrayList<LayerData> motion_data;
    private LayerData now;
    private Timeline motion;
    private int mill_sec;

    public BasicMotion(){
        motion_data = new ArrayList<>();
    }

    public void preview(Layer preview_layer){
        now = motion_data.get(0);
        motion = new Timeline(new KeyFrame(Duration.millis(mill_sec), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                preview_layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                int size = now.getDotSet().size();
                ArrayList<Dot> dots = now.CreatePolygon();
                double[] xPoints = new double[size];
                double[] yPoints = new double[size];
                int i = 0;
                for(Dot dot : dots){
                    xPoints[i] = dot.getX();
                    yPoints[i] = dot.getY();
                    i++;
                }
                preview_layer.getGraphicsContext().fillPolygon(xPoints, yPoints, size);
                if(motion_data.indexOf(now) == motion_data.size() - 1){
                    motion.stop();
                }else {
                    now = motion_data.get(motion_data.indexOf(now) + 1);
                }
            }
        }));

        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();
    }

    public ArrayList<LayerData> getMotion_data() {
        return motion_data;
    }

    public int getMill_sec() {
        return mill_sec;
    }

    public void setMill_sec(int mill_sec) {
        this.mill_sec = mill_sec;
    }

}
