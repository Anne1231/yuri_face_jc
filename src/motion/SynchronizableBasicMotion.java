package motion;

import Layers.Layer;
import UI.LayerDataEx;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/03/06.
 */
public class SynchronizableBasicMotion {
    private BasicMotion right_eye;
    private BasicMotion left_eye;
    private BasicMotion right_eyebrows;
    private BasicMotion left_eyebrows;
    private BasicMotion mouth;
    private LayerDataEx face_base;
    private int re_ms;
    private int le_ms;
    private int reb_ms;
    private int leb_ms;
    private int m_ms;

    public SynchronizableBasicMotion(BasicMotion r_e_motion, BasicMotion l_e_motion, BasicMotion r_e_b_motion, BasicMotion l_e_b_motion, BasicMotion m_motion, LayerDataEx face_base){
        right_eye = r_e_motion;
        left_eye = l_e_motion;
        right_eyebrows = r_e_b_motion;
        left_eyebrows = l_e_b_motion;
        mouth = m_motion;

        this.face_base = face_base;
    }

    public void preview(Layer layer){

        re_ms = 0;
        le_ms = 0;
        reb_ms = 0;
        leb_ms = 0;
        m_ms = 0;

        Timeline motion = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(re_ms > right_eye.getMill_sec()){
                    right_eye.getNow().Draw(layer);
                    right_eye.next();
                    re_ms = 0;
                }
                if(le_ms > left_eye.getMill_sec()){
                    left_eye.getNow().Draw(layer);
                    left_eye.next();
                    le_ms = 0;
                }
                if(reb_ms > right_eyebrows.getMill_sec()){
                    right_eyebrows.getNow().Draw(layer);
                    left_eye.next();
                    reb_ms = 0;
                }
                if(leb_ms > left_eyebrows.getMill_sec()){
                    left_eyebrows.getNow().Draw(layer);
                    left_eye.next();
                    leb_ms = 0;
                }
                if(m_ms > mouth.getMill_sec()){
                    mouth.getNow().Draw(layer);
                    left_eye.next();
                    m_ms = 0;
                }
            }
        }));

        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();
    }
}
