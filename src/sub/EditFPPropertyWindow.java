package sub;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by takai on 17/03/31.
 */
public class EditFPPropertyWindow extends Stage {

    public EditFPPropertyWindow(Window window, String tree_name){
        setTitle(tree_name + "のプロパティ編集");
        setWidth(500);
        setHeight(800);
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);
        setScene(scene);
    }
}
