package sub;

import UI.LayerData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public static LayerData.LayerDataType type;
    public static boolean success;

    public AskLayerType(Window window){
        success = false;

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

        Label message_label = new Label("レイヤーのタイプを選択");

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

        mouth.setOnAction(event -> type = LayerData.LayerDataType.Mouth);
        right_eye.setOnAction(event -> type = LayerData.LayerDataType.RightEye);
        left_eye.setOnAction(event -> type = LayerData.LayerDataType.LeftEye);
        right_eyebrows.setOnAction(event -> type = LayerData.LayerDataType.RightEyebrows);
        left_eyebrows.setOnAction(event -> type = LayerData.LayerDataType.LeftEyebrows);

        TextField layer_name_filed = new TextField("レイヤー");
        layer_name_filed.setMaxWidth(220.0
        );

        Button ok = new Button("レイヤー作成");

        ok.setOnAction(event -> {
            if(!layer_name_filed.getText().equals("")){
                layer_name = layer_name_filed.getText();
                success = true;
                close();
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("エラー");
                error.setTitle("エラー");
                error.setContentText("レイヤー名が不正です");
                error.showAndWait();
            }
        });

        root.getChildren().addAll(message_label, right_eyebrows, left_eyebrows, right_eye, left_eye, mouth, layer_name_filed, ok);

        setScene(new Scene(root));
    }
}
