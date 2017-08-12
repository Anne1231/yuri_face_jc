package Layers;

import UI.*;
import backend.utility.Geometry;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static UI.Main.*;
import static UI.UIValues.*;
import static UI.UIValues.WINDOW_HEIGHT;
import static UI.UIValues.WINDOW_WIDTH;

/**
 * Created by Akihiro on 2017/03/07.
 */
public class SystemLayers {

    public static boolean dot_dragged = false;

    private FrontDotLayer front;
    private FrontDotLayer create_ll;
    private LinesLayer lines;
    private GridLayer grid;
    private ImageLayer image_layer;
    private Layer preview;
    private SelectAreaLayer selecting_rect;
    private Footer footer;
    private SpuitLayer spuit;

    /*
    * コンストラクタ
     */
    public SystemLayers(Stage stage, PickedColorUI pickedColorUI){
        front = new FrontDotLayer(LAYER_WIDTH, LAYER_HEIGHT);       //ドットを描画するレイヤー
        create_ll = new FrontDotLayer(LAYER_WIDTH, LAYER_HEIGHT);       //ドットを打てないアニメーション用レイヤー
        lines = new LinesLayer(LAYER_WIDTH, LAYER_HEIGHT);       //線を描画するレイヤー
        grid  = new GridLayer(LAYER_WIDTH, LAYER_HEIGHT, INIT_GRID_INTERVAL);       //グリッドを描画するレイヤー
        image_layer = new ImageLayer(LAYER_WIDTH, LAYER_HEIGHT); //下敷き画像を描画するレイヤー
        preview = new Layer(LAYER_WIDTH, LAYER_HEIGHT);     //プレビューを描画するレイヤー
        selecting_rect = new SelectAreaLayer(stage, LAYER_WIDTH, LAYER_HEIGHT);
        spuit = new SpuitLayer(this, LAYER_WIDTH, LAYER_HEIGHT, pickedColorUI);

        /*
        * フッターの設定
         */
        footer = new Footer(WINDOW_WIDTH, 20);
        footer.getGraphicsContext().setFill(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        footer.getGraphicsContext().fillRect(0, 0, UIValues.FOOTER_WIDTH, UIValues.FOOTER_HEIGHT);
        /*
        * グリッドのレイヤーとフッターだけはここでアンカーペインの設定を行う
         */
        AnchorPane.setBottomAnchor(footer.getCanvas(), 0.0);
        AnchorPane.setLeftAnchor(footer.getCanvas(), 0.0);


        SettingAnchor(front, create_ll, lines, grid, image_layer, preview, selecting_rect, spuit);
    }

    public FrontDotLayer getFront() {
        return front;
    }

    public FrontDotLayer getCreateLL() {
        return create_ll;
    }

    public GridLayer getGrid() {
        return grid;
    }

    public ImageLayer getImageLayer() {
        return image_layer;
    }

    public Layer getPreview() {
        return preview;
    }

    public LinesLayer getLines() {
        return lines;
    }

    public SelectAreaLayer getSelectingRect() {
        return selecting_rect;
    }

    public Footer getFooter(){
        return footer;
    }

    public SpuitLayer getSpuit() {
        return spuit;
    }

    public void ConfigLayers(LayersTree layersTree){
        ConfigFrontLayer(front, lines, grid, layersTree);       //グループ作成用システムレイヤーの初期設定
        ConfigCreateLLayer(create_ll, lines, grid, layersTree); //アニメーション作成用システムレイヤーの初期設定
        ConfigLinesLayer(lines, front, grid);
    }

    /*
    * ドットを描画するレイヤーの初期設定
     */
    private void ConfigFrontLayer(FrontDotLayer front, LinesLayer lines, GridLayer gridLayer, LayersTree layersTree){

        SettingAnchor(front);

        ContextMenu popup = new ContextMenu();
        MenuItem choose = new MenuItem("ドットを選択");
        MenuItem put = new MenuItem("ドットを配置");

        /*
        * ドット配置処理
         */
        put.setOnAction(event -> {
            front.putDot(gridLayer);
        });

        /*
        * ドット選択処理
         */
        choose.setOnAction(event -> {
            for(final Dot p : CurrentLayerData.getDotSet()){
                if(abs_double((double)p.getX(), (double)x / (double)gridLayer.getInterval()) <= 0.5){
                    if(abs_double((double)p.getY(), (double)y / (double)gridLayer.getInterval()) <= 0.5){
                        p.Select();
                        selecting_dot = p;
                        selecting_dot.Select();
                        selecting_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                        lines.beForward();
                        break;
                    }
                }
            }
        });
        popup.getItems().addAll(put, choose);

        front.getCanvas().setOnContextMenuRequested(event -> {
            if(CurrentLayerData == null){
                return;
            }
            popup.show(front.getCanvas(), event.getScreenX(), event.getScreenY());
        });

        front.getCanvas().setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                popup.hide();
            }
            x = (int)event.getX();
            y = (int)event.getY();
            if(keyTable.isPressed(KeyCode.D)){
                front.putDot(gridLayer);
            }else if(keyTable.isPressed(KeyCode.C)){
                if(!front.isLastEmpty()) {
                    Dot dot = front.getLast();
                    front.putDot(gridLayer);
                    CurrentLayerData.connect(dot, front.getLast()).Draw(lines, 0.5, Color.BLACK);
                }
            }
        });

        front.getCanvas().setOnMouseMoved(event -> {
            if(CurrentLayerData == null){
                return;
            }
            for(final Dot p : CurrentLayerData.getDotSet()){
                if(p.isSelected()) {
                    continue;
                }
                if(abs_double((double)p.getX(), (event.getX() / (double)gridLayer.getInterval())) <= 0.5){
                    if(abs_double((double)p.getY(), (event.getY() / (double)gridLayer.getInterval())) <= 0.5){
                        choose.setDisable(false);
                        selecting_dot = p;
                        p.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                        break;
                    }else{
                        choose.setDisable(true);
                        p.drawOffset(front, Color.BLACK, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                    }
                }else{
                    choose.setDisable(true);

                    /*
                    * ↓この処理が重い
                     */
                    p.drawOffset(front, Color.BLACK, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                }
            }

            footer.PutText(String.valueOf((int)event.getX()) + ":" + String.valueOf((int)event.getY()), WINDOW_WIDTH - 80);
        });

        front.getCanvas().setOnMouseDragged(event -> {
            if(!ConfigLayer.dot_dragged) {
                return;
            }
            /*
            * 新しい座標を決定
             */
            Dot update_dot = new Dot(Geometry.EventDotToGridDot(event.getX(), gridLayer.getInterval()), Geometry.EventDotToGridDot(event.getY(), gridLayer.getInterval()));

            //現在のドットをレイヤーから消す（消しゴム）
            selecting_dot.Erase(front, gridLayer.getInterval());

            //レイヤーデータ上で、現在地のデータを移動先の座標に変更
            CurrentLayerData.MoveDot(selecting_dot, update_dot);

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            selecting_dot = update_dot;
            front.setLast(update_dot);
            selecting_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
        });

        front.getCanvas().setOnMousePressed(event -> {
            if(selecting_dot == null) {
                return;
            }
            if(abs_double((double)selecting_dot.getX(), (event.getX() / (double)gridLayer.getInterval())) <= 0.5){
                if(abs_double((double)selecting_dot.getY(), (event.getY() / (double)gridLayer.getInterval())) <= 0.5) {
                    ConfigLayer.dot_dragged = true;
                }
            }
        });

        front.getCanvas().setOnMouseReleased(event -> {
            ConfigLayer.dot_dragged = false;
        });


        choose.setDisable(true);
    }

    private double abs_double(double val1, double val2){
        return Math.abs(val1 - val2);
    }

    private void ConfigCreateLLayer(FrontDotLayer front, LinesLayer lines, GridLayer gridLayer, LayersTree layersTree){
        SettingAnchor(front);

        ContextMenu popup = new ContextMenu();
        MenuItem choose = new MenuItem("ドットを選択");

        /*
        * ドット選択処理
         */
        choose.setOnAction(event -> {
            for(final Dot p : CurrentLayerData.getDotSet()){
                if(abs_double((double)p.getX(), (double)x / (double)gridLayer.getInterval()) <= 0.5){
                    if(abs_double((double)p.getY(), (double)y / (double)gridLayer.getInterval()) <= 0.5){
                        p.Select();
                        selecting_dot = p;
                        selecting_dot.Select();
                        selecting_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                        lines.beForward();
                        break;
                    }
                }
            }
        });
        popup.getItems().addAll(choose);

        front.getCanvas().setOnContextMenuRequested(event -> {
            if(CurrentLayerData == null){
                return;
            }
            popup.show(front.getCanvas(), event.getScreenX(), event.getScreenY());
        });

        front.getCanvas().setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY)
                popup.hide();
            x = (int)event.getX();
            y = (int)event.getY();
        });

        front.getCanvas().setOnMouseMoved(event -> {
            if(CurrentLayerData == null){
                return;
            }

            CurrentLayerData.getPolygons().forEach(polygon -> {
                polygon.DrawDots(front);
            });

            Dot dot;
            for(Polygon polygon : CurrentLayerData.getPolygons()){
                if((dot = polygon.isOverlaps(new Point2i((int)event.getX(), (int)event.getY()))) != null){
                    selecting_dot = dot;
                    break;
                }
            }

            footer.PutText(String.valueOf((int)event.getX()) + ":" + String.valueOf((int)event.getY()), WINDOW_WIDTH - 80);
        });

        front.getCanvas().setOnMouseDragged(event -> {
            if(!ConfigLayer.dot_dragged) {
                return;
            }

            /*
            * 新しい座標を決定
             */
            Dot update_dot = new Dot((int)event.getX(), (int)event.getY(), gridLayer.getInterval());

            //現在のドットをレイヤーから消す（消しゴム）
            selecting_dot.Erase(front, gridLayer.getInterval());

            for(Polygon polygon : CurrentLayerData.getPolygons()){
                polygon.MoveDot(selecting_dot, update_dot);
            }
            CurrentLayerData.getLineList().forEach(line -> line.exchange(selecting_dot, update_dot));

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            selecting_dot = update_dot;
            selecting_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
        });

        front.getCanvas().setOnMousePressed(event -> {

            for(Polygon polygon : CurrentLayerData.getPolygons()){
                if(polygon.isOverlaps(new Point2i((int)event.getX(), (int)event.getY()).EventDotToGridDot(gridLayer.getInterval())) != null){
                    ConfigLayer.dot_dragged = true;
                    break;
                }
            }
        });

        front.getCanvas().setOnMouseReleased(event -> ConfigLayer.dot_dragged = false);


        choose.setDisable(true);
    }

    private void ConfigLinesLayer(LinesLayer lines, FrontDotLayer front, GridLayer gridLayer){

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
            for(final Dot p : CurrentLayerData.getDotSet()){
                if(abs_double((double)p.getX(), (double)x / (double)gridLayer.getInterval()) <= 0.5){
                    if(abs_double((double)p.getY(), (double)y / (double)gridLayer.getInterval()) <= 0.5){
                        if(!p.isSelected()){
                            CurrentLayerData.connect(selecting_dot, p).Draw(lines, 0.5, Color.BLACK);
                            selecting_dot.UnSelect();
                            front.beForward();
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
            front.beForward();
        });

        /*
        * ドット削除
         */
        remove_dot.setOnAction(event -> {

            selecting_dot.Erase(front, gridLayer.getInterval());
            CurrentLayerData.RemoveDot(selecting_dot);
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            CurrentLayerData.DrawAllLines(lines);
            front.beForward();

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
            selecting_dot.Erase(front, gridLayer.getInterval());

            //レイヤーデータ上で、現在地のデータを移動先の座標に変更
            CurrentLayerData.MoveDot(selecting_dot, update_dot);

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            update_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());

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
            for(Dot p : CurrentLayerData.getDotSet()){
                if(p.isSelected())
                    continue;
                if(abs_double((double)p.getX(), (event.getX() / (double)gridLayer.getInterval())) <= 0.5){
                    if(abs_double((double)p.getY(), (event.getY() / (double)gridLayer.getInterval())) <= 0.5){
                        cat_dot.setDisable(false);
                        p.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                        break;
                    }else{
                        p.drawOffset(front, Color.BLACK, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
                        cat_dot.setDisable(true);
                    }
                }else{
                    p.drawOffset(front, Color.BLACK, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
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
            selecting_dot.Erase(front, gridLayer.getInterval());

            //レイヤーデータ上で、現在地のデータを移動先の座標に変更
            CurrentLayerData.MoveDot(selecting_dot, update_dot);

            //線も移動するので一回削除
            lines.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            //さっき変更されたレイヤーデータを元に線を再描画
            CurrentLayerData.DrawAllLines(lines);

            //消されていたドットを更新した座標に再描画
            selecting_dot = update_dot;
            selecting_dot.Select();
            update_dot.drawOffset(front, Color.RED, gridLayer.getInterval(), Main.main_view.getMainViewBegin());
        });

        lines.getCanvas().setOnMousePressed(event -> {
            if(selecting_dot == null) {
                return;
            }
            if(abs_double((double)selecting_dot.getX(), (event.getX() / (double)gridLayer.getInterval())) <= 0.5){
                if(abs_double((double)selecting_dot.getY(), (event.getY() / (double)gridLayer.getInterval())) <= 0.5) {
                    ConfigLayer.dot_dragged = true;
                }
            }
        });

        lines.getCanvas().setOnMouseReleased(event -> dot_dragged = false);


    }

    /*
    * レイヤーの順番を設定するメソッド
    */
    public final void InitSort(){

        this.lines.getCanvas().toFront();
        this.create_ll.getCanvas().toFront();
        this.front.getCanvas().toFront();
        this.grid.getCanvas().toBack();
        this.image_layer.getCanvas().toBack();
        this.footer.getCanvas().toFront();
        this.preview.getCanvas().toBack();
        this.spuit.getCanvas().toBack();
    }

    /*
    * グラフィックレイヤーにおけるアンカーペインの設定を一般化した関数
     */
    private final void SettingAnchor(Layer ... args){
        for(Layer layer : args) {
            AnchorPane.setTopAnchor(layer.getCanvas(), UIValues.MENU_HEIGHT);
            AnchorPane.setLeftAnchor(layer.getCanvas(), UIValues.LAYER_LIST_WIDTH + UIValues.LIST_TO_CANVAS_WIDTH);
        }
    }

}
