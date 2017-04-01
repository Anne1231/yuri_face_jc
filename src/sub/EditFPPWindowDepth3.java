package sub;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by takai on 17/04/01.
 */
public class EditFPPWindowDepth3 extends Stage {
    public EditFPPWindowDepth3(Window window, String tree_name){
        setTitle(tree_name + "のプロパティ編集");
        setWidth(500);
        setHeight(800);
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);
        setScene(scene);
    }
}
