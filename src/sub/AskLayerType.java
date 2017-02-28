package sub;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by Akihiro on 2017/02/28.
 */
public class AskLayerType extends Stage {

    public static String layer_name;

    public AskLayerType(Window window){
        setTitle("レイヤータイプ選択");
        initStyle(StageStyle.UTILITY);
        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(300);
        setHeight(300);

        VBox root = new VBox();
        root.setSpacing(5.0);
        root.setAlignment(Pos.CENTER);

        ToggleGroup radio_button_group = new ToggleGroup();

        Label message_label = new Label("どのレイヤーを選択しますか？");

        RadioButton mouth = new RadioButton("口");
        RadioButton right_eye = new RadioButton("右目");
        RadioButton left_eye = new RadioButton("左目");
        RadioButton right_eyebrows = new RadioButton("右眉毛");
        RadioButton left_eyebrows = new RadioButton("左眉毛");

        mouth.setToggleGroup(radio_button_group);
        right_eye.setToggleGroup(radio_button_group);
        left_eye.setToggleGroup(radio_button_group);
        right_eyebrows.setToggleGroup(radio_button_group);
        left_eyebrows.setToggleGroup(radio_button_group);

        mouth.setOnAction(event -> layer_name = "Mouth");
        right_eye.setOnAction(event -> layer_name = "RightEye");
        left_eye.setOnAction(event -> layer_name = "LeftEye");
        right_eyebrows.setOnAction(event -> layer_name = "RightEyebrows");
        left_eyebrows.setOnAction(event -> layer_name = "LeftEyebrows");

        Button ok = new Button("レイヤー作成");

        ok.setOnAction(event -> close());

        root.getChildren().addAll(message_label, right_eyebrows, left_eyebrows, right_eye, left_eye, mouth, ok);

        setScene(new Scene(root));
    }
}
