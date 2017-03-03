package UI;

import Layers.FrontDotLayer;
import Layers.GridLayer;
import Layers.Layer;
import Layers.LinesLayer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import static UI.Main.*;
import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/02/27.
 */
public class ConfigLayer {

    public static boolean dot_dragged = false;
    public static void ConfigLinesLayer(LinesLayer lines, FrontDotLayer front, GridLayer gridLayer){

        SettingAnchor(lines);

        ContextMenu popup_lines = new ContextMenu();
        MenuItem cat_dot = new MenuItem("選択中のドットと連結");
        MenuItem quit_cat = new MenuItem("選択状態を終了");
        MenuItem remove_dot = new MenuItem("選択中のドットを削除");
        MenuItem move_dot = new MenuItem("選択中のドットをここに移動");

        /*
        * ドット連結
         */
        cat_dot.setOnAction(event -> {
            for(final Dot p : CurrentLayerData.getDotList()){
                if(Math.abs(p.getX() - x) < 5){
                    if(Math.abs(p.getY() - y) < 5){
                        if(!p.isSelected()){
                            lines.getGraphicsContext().setLineWidth(0.5);
                            lines.getGraphicsContext().setStroke(Color.BLACK);
                            lines.getGraphicsContext().strokeLine(selecting_dot.getX(), selecting_dot.getY(), p.getX(), p.getY());
                            selecting_dot.Connect(p);
                            selecting_dot.UnSelect();
                            SwitchFrontLayer(front);
                            break;
                        }
                    }
                }
            }
        });

        cat_dot.setDisable(true);

        /*
        * 選択状態終了
         */
        quit_cat.setOnAction(event -> {
            selecting_dot.UnSelect();
            SwitchFrontLayer(front);
        });

        /*
        * ドット削除
         */
        remove_dot.setOnAction(event -> {

            selecting_dot.Erase(front);
            CurrentLayerData.RemoveDot(selecting_dot);
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            CurrentLayerData.DrawAllLines(lines);
            Main.SwitchFrontLayer(front);

        });

        /*
        * ドットを移動
         */
        move_dot.setOnAction(event -> {

            /*
            * 新しい座標を決定
             */
            Dot update_dot;
            if(gridLayer.isEnableComplete()) {
                update_dot = new Dot(x, y, gridLayer.getInterval());
            }else{
                update_dot = new Dot(x, y);
            }

            //現在のドットをレイヤーから消す（消しゴム）
            selecting_dot.Erase(front);

            //レイヤーデータ上で、現在地のデータを移動先の座標に変更
            CurrentLayerData.MoveDot(selecting_dot, update_dot);

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            update_dot.Draw(front, Color.RED);

        });

        popup_lines.getItems().addAll(cat_dot, move_dot, remove_dot, quit_cat);

        lines.getCanvas().setOnContextMenuRequested(event -> {
            popup_lines.show(lines.getCanvas(), event.getScreenX(), event.getScreenY());
        });

        lines.getCanvas().setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                popup_lines.hide();
            } else if (event.getButton() == MouseButton.SECONDARY){
                x = (int)event.getX();
                y = (int)event.getY();
            }
        });

        lines.getCanvas().setOnMouseMoved(event -> {
            for(Dot p : CurrentLayerData.getDotList()){
                if(p.isSelected())
                    continue;
                if(Math.abs(p.getX() - event.getX()) < 5){
                    if(Math.abs(p.getY() - event.getY()) < 5){
                        cat_dot.setDisable(false);
                        p.Draw(front, Color.RED);
                        break;
                    }else{
                        p.Draw(front, Color.BLACK);
                        cat_dot.setDisable(true);
                    }
                }else{
                    p.Draw(front, Color.BLACK);
                    cat_dot.setDisable(true);
                }
            }
            footer.PutText(String.valueOf((int)event.getX()) + ":" + String.valueOf((int)event.getY()), WINDOW_WIDTH - 80);
        });

        lines.getCanvas().setOnMouseDragged(event -> {
            if(!dot_dragged)
                return;
            /*
            * 新しい座標を決定
             */
            Dot update_dot;
            if(gridLayer.isEnableComplete()) {
                update_dot = new Dot((int)event.getX(), (int)event.getY(), gridLayer.getInterval());
            }else{
                update_dot = new Dot((int)event.getX(), (int)event.getY());
            }

            //現在のドットをレイヤーから消す（消しゴム）
            selecting_dot.Erase(front);

            //レイヤーデータ上で、現在地のデータを移動先の座標に変更
            CurrentLayerData.MoveDot(selecting_dot, update_dot);

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            update_dot.Draw(front, Color.RED);
        });

        lines.getCanvas().setOnMousePressed(event -> {
            if(Math.abs(selecting_dot.getX() - event.getX()) < 5){
                if(Math.abs(selecting_dot.getY() - event.getY()) < 5) {
                    dot_dragged = true;
                }
            }
        });

        lines.getCanvas().setOnMouseReleased(event -> dot_dragged = false);


    }
}
