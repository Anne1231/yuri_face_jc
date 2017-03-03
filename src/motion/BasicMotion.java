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
    private String motion_name;
    private ArrayList<BasicMotionFrame> motion_data;
    private BasicMotionFrame now;
    private Timeline motion;
    private int mill_sec;

    public BasicMotion(String name, ArrayList<LayerData> layer_datas){
        motion_name = name;
        motion_data = new ArrayList<>();
        layer_datas.forEach(layerData -> motion_data.add(new BasicMotionFrame(layerData)));
    }

    public void preview(Layer preview_layer){
        now = motion_data.get(0);

        motion = new Timeline(new KeyFrame(Duration.millis(mill_sec), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                now.Draw(preview_layer);
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

    public ArrayList<BasicMotionFrame> getMotion_data() {
        return motion_data;
    }

    public int getMill_sec() {
        return mill_sec;
    }

    public void setMill_sec(int mill_sec) {
        this.mill_sec = mill_sec;
    }

}
