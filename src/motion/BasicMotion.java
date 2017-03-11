package motion;

import Layers.Layer;
import UI.Dot;
import UI.LayerData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by Akihiro on 2017/03/03.
 */
public class BasicMotion extends Transition {
    private String motion_name;
    private ArrayList<BasicMotionFrame> motion_data;
    private BasicMotionFrame now;
    private BasicMotionFrame before;
    private BasicMotionFrame next;
    private Timeline motion;
    private int mill_sec;
    private LayerData.LayerDataType type;
    private Layer preview_layer;
    private boolean playing;

    public BasicMotion(String name, ArrayList<LayerData> layer_datas, LayerData.LayerDataType type){
        motion_name = name;
        motion_data = new ArrayList<>();
        layer_datas.forEach(layerData -> motion_data.add(new BasicMotionFrame(layerData)));
        now = motion_data.get(0);
        before = now;
        this.type = type;
        playing = false;
    }

    public BasicMotion(String name, ArrayList<LayerData> layer_datas, LayerData.LayerDataType type, Layer preview_layer, int mill_sec){
        motion_name = name;
        motion_data = new ArrayList<>();
        layer_datas.forEach(layerData -> motion_data.add(new BasicMotionFrame(layerData)));
        now = motion_data.get(0);
        before = now;
        next = motion_data.get(1);
        this.type = type;
        this.mill_sec = mill_sec;
        setCycleDuration(Duration.millis(this.mill_sec));
        this.preview_layer = preview_layer;
        playing = false;
    }

    @Override
    protected void interpolate(double frac){
        //before.fillWhite(this.preview_layer);
        preview_layer.getGraphicsContext().setFill(Color.WHITE);
        preview_layer.getGraphicsContext().fillRect(0, 0, preview_layer.getCanvas().getWidth(), preview_layer.getCanvas().getHeight());
        before = now.CreateMid(next, frac);
        before.Draw(this.preview_layer);
        System.out.println("playing");
    }

    public void preview(Layer preview_layer){
        now = motion_data.get(0);

        preview_layer.getGraphicsContext().setFill(Color.WHITE);
        preview_layer.getGraphicsContext().fillRect(0, 0, preview_layer.getCanvas().getWidth(), preview_layer.getCanvas().getHeight());
        preview_layer.getCanvas().toFront();

        motion = new Timeline(new KeyFrame(Duration.millis(mill_sec), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                before.fillWhite(preview_layer);
                now.Draw(preview_layer);
                next();
            }
        }));

        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.FINISH);
        alert.setHeaderText("プレビュー中です");
        alert.setTitle("プレビュー");
        alert.showAndWait();

        motion.stop();
        preview_layer.getGraphicsContext().clearRect(0, 0, preview_layer.getCanvas().getWidth(), preview_layer.getCanvas().getHeight());
        preview_layer.getCanvas().toBack();

    }

    public ArrayList<BasicMotionFrame> getMotion_data() {
        return motion_data;
    }

    public final int getMill_sec() {
        return mill_sec;
    }

    public void setMill_sec(int mill_sec) {
        this.mill_sec = mill_sec;
        setCycleDuration(Duration.millis(this.mill_sec));
    }

    public String getMotionName() {
        return motion_name;
    }

    public void setPreviewLayer(Layer preview_layer) {
        this.preview_layer = preview_layer;
    }

    public final BasicMotionFrame getNow() {
        return now;
    }

    public final BasicMotionFrame getBefore(){
        return before;
    }

    public final void next(){
        before = now;
        int index = motion_data.indexOf(now);
        if(index  != motion_data.size() - 1){
            now = motion_data.get(index + 1);
        }else{
            now = motion_data.get(0);
        }
    }

    public LayerData.LayerDataType getType() {
        return type;
    }

    public String getName() {
        return motion_name;
    }

    public void usingFxPreview(){

        now = motion_data.get(0);
        before = now;
        next = motion_data.get(1);

        preview_layer.getGraphicsContext().setFill(Color.WHITE);
        preview_layer.getGraphicsContext().fillRect(0, 0, preview_layer.getCanvas().getWidth(), preview_layer.getCanvas().getHeight());
        preview_layer.getCanvas().toFront();

        playing = true;

        motion = new Timeline(new KeyFrame(Duration.millis(mill_sec), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                play();

                now = next;
                int index = motion_data.indexOf(next);
                if (index != motion_data.size() - 1) {
                    next = motion_data.get(index + 1);
                    System.out.println("index(true) = " + index);
                } else {
                    System.out.println("index(false) = " + index);
                    next = motion_data.get(0);
                }
                playing = true;
            }
        }));

        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.FINISH);
        alert.setHeaderText("プレビュー中です");
        alert.setTitle("プレビュー");
        alert.showAndWait();

        motion.stop();
        preview_layer.getGraphicsContext().clearRect(0, 0, preview_layer.getCanvas().getWidth(), preview_layer.getCanvas().getHeight());
        preview_layer.getCanvas().toBack();
        playing = false;
    }

}
