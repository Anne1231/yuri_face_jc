package UI;

import Layers.SystemLayers;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;

public class MainView {

    private SystemLayers system_layers;
    private ScrollBar h_scroll_bar;
    private ScrollBar v_scroll_bar;
    private Point2i main_view_begin;

    public MainView(Stage stage){
        system_layers = new SystemLayers(stage, null);
        h_scroll_bar = new ScrollBar();
        v_scroll_bar = new ScrollBar();
        main_view_begin = new Point2i(0, 0);

        h_scroll_bar.setPrefSize(UIValues.LAYER_WIDTH, 6);
        AnchorPane.setLeftAnchor(h_scroll_bar, UIValues.LAYER_LIST_WIDTH + UIValues.LIST_TO_CANVAS_WIDTH);
        AnchorPane.setBottomAnchor(h_scroll_bar, UIValues.FOOTER_HEIGHT);

        v_scroll_bar.setPrefSize(UIValues.LAYER_HEIGHT, UIValues.LAYER_HEIGHT);
        v_scroll_bar.setMaxWidth(6.0);
        AnchorPane.setLeftAnchor(v_scroll_bar, UIValues.LAYER_LIST_WIDTH + UIValues.LIST_TO_CANVAS_WIDTH + UIValues.LAYER_WIDTH);
        AnchorPane.setTopAnchor(v_scroll_bar, 32.0);
        v_scroll_bar.setOrientation(Orientation.VERTICAL);

        h_scroll_bar.valueProperty().addListener(((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            main_view_begin.x = new_val.intValue();
            lookAt(main_view_begin.x, main_view_begin.y);
        }));
        v_scroll_bar.valueProperty().addListener(((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            main_view_begin.y = new_val.intValue();
            lookAt(main_view_begin.x, main_view_begin.y);
        }));

    }

    public SystemLayers getSystemLayers(){
        return system_layers;
    }

    public void config_place(NormalLayersTree layersTree){
        system_layers.ConfigLayers(layersTree);
    }

    public void registerToRoot(AnchorPane root){
        root.getChildren().addAll(
                system_layers.getFront().getCanvas(),
                system_layers.getCreateLL().getCanvas(),
                system_layers.getLines().getCanvas(),
                system_layers.getGrid().getCanvas(),
                system_layers.getImageLayer().getCanvas(),
                system_layers.getPreview().getCanvas(),
                system_layers.getSelectingRect().getCanvas(),
                system_layers.getFooter().getCanvas(),
                system_layers.getSpuit().getCanvas(),
                h_scroll_bar,
                v_scroll_bar
        );
    }

    public void lookAt(int x, int y){

        /*
        * クリア
         */
        system_layers.getFront().clear();
        system_layers.getLines().clear();

        int grid_interval = system_layers.getGrid().getInterval();
        int end_x = grid_interval + x, end_y = grid_interval + y;

        /*
        * 描画
         */
        HashSet<Dot> data_set = Main.CurrentLayerData.getDotSet();
        data_set.stream().filter(dot -> dot.x >= x && dot.x <= end_x && dot.y >= y && dot.y <= end_y).forEach(system_layers.getFront()::putOneDot);
        ArrayList<Line> lines = Main.CurrentLayerData.getLineList();
        lines.stream().filter(line -> {
            Point2i dot1 = line.getBegin(), dot2 = line.getEnd();
            return (dot1.x >= x && dot1.x <= end_x && dot1.y >= y && dot1.y <= end_y) || (dot2.x >= x && dot2.x <= end_x && dot2.y >= y && dot2.y <= end_y);
        }).forEach(line -> {
            line.Draw(system_layers.getLines(), 0.5, Color.BLACK);
        });
    }

    public void lookAt(){
        lookAt(main_view_begin.x, main_view_begin.y);
    }

    public Point2i getMainViewBegin() {
        return main_view_begin;
    }

    public void scroll(int delta_x, int delta_y){
        /*
        * main_view_begin.y += check_y(-delta_y);
        * この処理は、マウスホイールの+-がこのプログラムの+-と逆であるから減算を使っている
         */
        main_view_begin.x += check_x(delta_x);
        main_view_begin.y += check_y(-delta_y);
        update_scrollbar();
    }

    private void update_scrollbar(){
        h_scroll_bar.setValue(main_view_begin.x);
        v_scroll_bar.setValue(main_view_begin.y);
    }

    private int check_x(int delta_x){
        if(delta_x < 0){
            if(main_view_begin.x <= 0){
                return 0;
            }else if(main_view_begin.x <= (-delta_x)){
                return -main_view_begin.x;
            }else{
                return delta_x;
            }
        }else if(delta_x > 0){
            if(main_view_begin.x >= h_scroll_bar.getMax()){
                return 0;
            }else if((int)h_scroll_bar.getMax() - main_view_begin.x <= delta_x){
                return (int)h_scroll_bar.getMax() - main_view_begin.x;
            }else{
                return delta_x;
            }
        }

        return delta_x;
    }

    private int check_y(int delta_y){
        if(delta_y < 0){
            if(main_view_begin.y <= 0){
                return 0;
            }else if(main_view_begin.y <= (-delta_y)){
                return -main_view_begin.y;
            }else{
                return delta_y;
            }
        }else if(delta_y > 0){
            if(main_view_begin.y >= h_scroll_bar.getMax()){
                return 0;
            }else if((int)h_scroll_bar.getMax() - main_view_begin.y <= delta_y){
                return (int)h_scroll_bar.getMax() - main_view_begin.y;
            }else{
                return delta_y;
            }

        }

        return delta_y;
    }
}
