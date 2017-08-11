package UI;

import Layers.SystemLayers;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

        v_scroll_bar.valueProperty().addListener(((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
           System.out.println(old_val + ":" + new_val);
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
        int grid_interval = system_layers.getGrid().getInterval();
        int end_x = grid_interval + x, end_y = grid_interval + y;

        HashSet<Dot> data_set = Main.CurrentLayerData.getDotSet();
        data_set.stream().filter(dot -> dot.x >= x && dot.x <= end_x && dot.y >= y && dot.y <= end_y).forEach(system_layers.getFront()::putOneDot);
    }

    public Point2i getMainViewBegin() {
        return main_view_begin;
    }
}
