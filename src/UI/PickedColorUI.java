package UI;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by takai on 17/04/02.
 */
public class PickedColorUI extends HBox {

    private final Label picked = new Label("選択中の色");
    private Label web_color_label;
    private Rectangle rect;

    public PickedColorUI(double x, double y){
        super();

        picked.setTextFill(Color.BLACK);

        web_color_label = new Label(Color.WHITE.toString());
        web_color_label.setTextFill(Color.BLACK);

        rect = new Rectangle(x, y, UIValues.PICKED_COLOR_RECT_WIDTH, UIValues.PICKED_COLOR_RECT_WIDTH);
        rect.setFill(Color.WHITE);

        VBox box = new VBox();
        box.getChildren().addAll(picked, rect);

        getChildren().addAll(box, web_color_label);
        setSpacing(5.0);

        AnchorPane.setLeftAnchor(this, x);
        AnchorPane.setTopAnchor(this, y);

    }

    public void updateColor(Color color){
        web_color_label.setText(color.toString().substring(0, color.toString().length() - 2));
        rect.setFill(color);
    }

}
