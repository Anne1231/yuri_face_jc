package UI;

import Layers.SystemLayers;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainView {

    private SystemLayers system_layers;
    private ScrollBar h_scroll_bar;
    private ScrollBar v_scroll_bar;

    public MainView(Stage stage){
        system_layers = new SystemLayers(stage, null);
        h_scroll_bar = new ScrollBar();
        v_scroll_bar = new ScrollBar();

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
}
