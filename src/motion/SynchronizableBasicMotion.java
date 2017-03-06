package motion;

import Layers.Layer;
import UI.LayerDataEx;
import UI.UIValues;
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
        boolean[] targeted = new boolean[5];
        java.util.Arrays.fill(targeted, false);

        Timeline motion = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layer.getGraphicsContext().clearRect(0, 0, UIValues.WINDOW_WIDTH, UIValues.WINDOW_HEIGHT);
                if(re_ms > right_eye.getMill_sec()){
                    right_eye.getNow().Draw(layer);
                    targeted[0] = true;
                }
                if(le_ms > left_eye.getMill_sec()){
                    left_eye.getNow().Draw(layer);
                    targeted[1] = true;
                }
                if(reb_ms > right_eyebrows.getMill_sec()){
                    right_eyebrows.getNow().Draw(layer);
                    targeted[2] = true;
                }
                if(leb_ms > left_eyebrows.getMill_sec()){
                    left_eyebrows.getNow().Draw(layer);
                    targeted[3] = true;
                }
                if(m_ms > mouth.getMill_sec()){
                    mouth.getNow().Draw(layer);
                    targeted[4] = true;
                }

                if(targeted[0]){
                    right_eye.next();
                    re_ms = 0;
                    targeted[0] = false;
                }else{
                    re_ms += 30;
                }
                if(targeted[1]){
                    left_eye.next();
                    le_ms = 0;
                    targeted[1] = false;
                }else{
                    le_ms += 30;
                }
                if(targeted[2]){
                    right_eyebrows.next();
                    reb_ms = 0;
                    targeted[2] = false;
                }else{
                    reb_ms += 30;
                }
                if(targeted[3]){
                    left_eyebrows.next();
                    leb_ms = 0;
                    targeted[3] = false;
                }else{
                    leb_ms += 30;
                }
                if(targeted[4]){
                    mouth.next();
                    m_ms = 0;
                    targeted[4] = false;
                }else{
                    m_ms += 30;
                }
            }
        }));

        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();
    }
}
