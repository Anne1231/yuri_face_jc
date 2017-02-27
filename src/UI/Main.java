package UI;

import FileIO.ImageIO;
import FileIO.OpenYFML;
import FileIO.Save;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

import static UI.UIValues.*;

/**
 * Created by Akihiro on 2017/02/25.
 */
public class Main extends Application {

    public static int x, y;
    public static Dot selecting_dot;

    public static LayerData CurrentLayerData;
    public static ArrayList<LayerData> LayerDatas = new ArrayList<>();
    public static Footer footer;

    @Override
    public void start(Stage stage){

        InitWindow(stage);

        /*
        * フッターの設定
         */
        footer = new Footer(WINDOW_WIDTH, 20);
        footer.getGraphicsContext().setFill(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        footer.getGraphicsContext().fillRect(0, 0, UIValues.FOOTER_WIDTH, UIValues.FOOTER_HEIGHT);

        /*
        * リストビューの初期化
         */
        ListView<String> layer_list = new ListView<>();
        ConfigLayerList(layer_list);

        /*
        * yuri faceの初期化
        * 最初のレイヤーの作成とか
         */
        yuri_face_init(layer_list);

        /*
        * アンカーペインを採用してみた
         */
        AnchorPane root = new AnchorPane();

        /*
        * 重要なグラフィックレイヤーたち
         */
        Layer front = new Layer(WINDOW_WIDTH, WINDOW_HEIGHT);       //ドットを描画するレイヤー
        Layer lines = new Layer(WINDOW_WIDTH, WINDOW_HEIGHT);       //線を描画するレイヤー
        Layer grid  = new Layer(WINDOW_WIDTH, WINDOW_HEIGHT);       //グリッドを描画するレイヤー
        Layer image_layer = new Layer(WINDOW_WIDTH, WINDOW_HEIGHT); //下敷き画像を描画するレイヤー
        Layer preview = new Layer(WINDOW_WIDTH, WINDOW_HEIGHT);     //プレビューを描画するレイヤー

        /*
        * グリッドのレイヤーとフッターだけはここでアンカーペインの設定を行う
         */
        SettingAnchor(grid);
        AnchorPane.setBottomAnchor(footer.getCanvas(), 0.0);
        AnchorPane.setLeftAnchor(footer.getCanvas(), 0.0);

        /*
        * メニューバー
         */
        MenuBar menubar = new MenuBar();

        /*
        * メニューバーの設定
         */
        ConfigMenuBar(menubar, stage, front, lines, grid, image_layer, preview, layer_list);

        /*
        * レイヤーの各種設定
        * この中でアンカーペインの設定も行う
         */
        ConfigFrontLayer(front, lines);
        ConfigLayer.ConfigLinesLayer(lines, front);
        ConfigImageLayer(image_layer);
        SettingAnchor(preview);

        /*
        * リストビューのイベントハンドラの定義
         */
        layer_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                for(LayerData layer_data : LayerDatas){
                    if(layer_data.getName().equals(newValue)){
                        CurrentLayerData = layer_data;
                        SwitchLayer(CurrentLayerData, front, lines);
                        break;
                    }
                }
            }
        });

        /*
        * ラベルの設定
         */
        Label layer_label = new Label("レイヤー");
        AnchorPane.setTopAnchor(layer_label, MENU_HEIGHT);
        AnchorPane.setLeftAnchor(layer_label, UIValues.LAYER_LIST_WIDTH / 3);

        Label bairitsu_label = new Label("下敷き画像倍率");
        AnchorPane.setBottomAnchor(bairitsu_label, UIValues.FOOTER_HEIGHT + 30);
        AnchorPane.setLeftAnchor(bairitsu_label, UIValues.LAYER_LIST_WIDTH / 3);

        TextField image_bairitsu = new TextField("100");
        image_bairitsu.setAlignment(Pos.BASELINE_RIGHT);
        image_bairitsu.setPrefWidth(LAYER_LIST_WIDTH - 20);
        AnchorPane.setBottomAnchor(image_bairitsu, UIValues.FOOTER_HEIGHT + 5);
        AnchorPane.setLeftAnchor(image_bairitsu, 0.0);


        /*
        * ノードを登録
         */
        root.getChildren().addAll(menubar, layer_list, layer_label, bairitsu_label, image_bairitsu, front.getCanvas(), lines.getCanvas(), grid.getCanvas(), image_layer.getCanvas(), preview.getCanvas(), footer.getCanvas());

        /*
        * レイヤーの順番をここで描画
         */
        front.getCanvas().toFront();
        grid.getCanvas().toBack();
        image_layer.getCanvas().toBack();
        footer.getCanvas().toFront();
        preview.getCanvas().toBack();

        /*
        * 表示
         */
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /*
    * ドットを描画するレイヤーの初期設定
     */
    private static void ConfigFrontLayer(Layer front, Layer lines){

        SettingAnchor(front);

        ContextMenu popup = new ContextMenu();
        MenuItem choose = new MenuItem("ドットを選択");
        MenuItem put = new MenuItem("ドットを配置");
        put.setOnAction(event -> {
            Dot dot = new Dot(x, y);
            dot.Draw(front, Color.BLACK);
            CurrentLayerData.AddDot(dot);
        });
        choose.setOnAction(event -> {
            for(final Dot p : CurrentLayerData.getDotList()){
                if(Math.abs(p.getX() - x) < 5){
                    if(Math.abs(p.getY() - y) < 5){
                        p.Select();
                        selecting_dot = p;
                        selecting_dot.Select();
                        selecting_dot.Draw(front, Color.RED);
                        SwitchFrontLayer(lines);
                        break;
                    }
                }
            }
        });
        popup.getItems().addAll(put, choose);

        front.getCanvas().setOnMouseClicked(event -> {
            x = (int)event.getX();
            y = (int)event.getY();
        });

        front.getCanvas().setOnContextMenuRequested(event -> {
            popup.show(front.getCanvas(), event.getScreenX(), event.getScreenY());
        });

        front.getCanvas().setOnMouseClicked(event -> {
            popup.hide();
            x = (int)event.getX();
            y = (int)event.getY();
        });

        front.getCanvas().setOnMouseMoved(event -> {
            for(final Dot p : CurrentLayerData.getDotList()){
                if(p.isSelected())
                    continue;
                if(Math.abs(p.getX() - event.getX()) < 5){
                    if(Math.abs(p.getY() - event.getY()) < 5){
                        choose.setDisable(false);
                        p.Draw(front, Color.RED);
                        break;
                    }else{
                        choose.setDisable(true);
                        p.Draw(front, Color.BLACK);
                    }
                }else{
                    choose.setDisable(true);
                    p.Draw(front, Color.BLACK);
                }
            }
            footer.PutText(String.valueOf((int)event.getX()) + ":" + String.valueOf((int)event.getY()), WINDOW_WIDTH - 80);
        });

        choose.setDisable(true);
    }

    /*
    * 線を描画するレイヤーの初期設定
     */


    /*
    * メニューバーの初期設定
     */
    private static void ConfigMenuBar(MenuBar menu, Stage stage, Layer front, Layer lines, Layer grid_layer, Layer image_layer, Layer preview, ListView<String> listView){
        Menu help = new Menu("ヘルプ");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);
        Menu  display = new Menu("表示");
        CheckMenuItem grid_config = new CheckMenuItem("グリッド");
        CheckMenuItem preview_menu = new CheckMenuItem("プレビュー");

        grid_config.setOnAction(event -> {
            int interval;
            TextInputDialog get_interval = new TextInputDialog("30");
            get_interval.setTitle("グリッドの間隔設定");
            get_interval.setHeaderText("グリッドの間隔を偶数で指定してください\n0を指定するとグリッドは非表示になります");
            get_interval.setContentText("グリッドの間隔 :");

            for(;;) {
                Optional<String> result = get_interval.showAndWait();
                if (result.isPresent()) {
                    if ((interval = Integer.valueOf(get_interval.getEditor().getText())) % 2 == 0) {
                        break;
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("偶数を入力してください");
                        alert.showAndWait();
                    }
                }else{
                    return;
                }
            }

            if(interval == 0){
                EraseGrid(grid_layer);
                grid_config.setSelected(false);
                return;
            }

            DrawGrid(grid_layer, interval);
            grid_config.setSelected(true);

        });

        preview_menu.setOnAction(event -> {
            Preview(preview);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("プレビュー");
            alert.setHeaderText("");
            alert.setContentText("プレビュー中です。終了するにはOKボタンを押してください。");
            alert.showAndWait();
            preview.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            preview.getCanvas().toBack();
            preview_menu.setSelected(false);
        });

        grid_config.setSelected(true);
        DrawGrid(grid_layer, 30);

        display.getItems().addAll(grid_config, preview_menu);
        Menu file = new Menu("ファイル");
        MenuItem open = new MenuItem("下敷き画像を開く");
        MenuItem open_yfml = new MenuItem("YuriFaceドキュメントを開く");
        MenuItem save = new MenuItem("保存");
        MenuItem quit = new MenuItem("終了");
        quit.setOnAction(event ->
                System.exit(0)
        );

        open.setOnAction(event -> {
            Image img;
            try {
                img = ImageIO.SelectAndOpenImage(stage);
            }catch (Exception e){
                System.out.println(e);
                return;
            }
            image_layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            image_layer.getGraphicsContext().drawImage(img, 0, 0);
        });

        open_yfml.setOnAction(event -> {
            OpenYFML.open_yfml(stage, LayerDatas, front, lines, listView);
        });

        save.setOnAction(event -> {
            Save.save_to_file(LayerDatas, stage);
        });

        file.getItems().addAll(open_yfml, open, save, quit);
        menu.getMenus().addAll(file, display, help);

        menu.setPrefWidth(WINDOW_WIDTH);
    }

    /*
    * ウィンドウの初期設定
     */
    private static void InitWindow(Stage stage){
        stage.setTitle("Yuri Face");
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
    }

    /*
    * アクティブレイヤーの変更を行う関数
     */
    public static void SwitchFrontLayer(Layer new_layer){
        new_layer.getCanvas().toFront();
    }

    /*
    * グリッドを描画する関数
     */
    private static void DrawGrid(Layer grid_layer, int interval){
        int i;
        grid_layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        grid_layer.getCanvas().toFront();
        grid_layer.getGraphicsContext().setStroke(Color.GRAY);
        grid_layer.getGraphicsContext().setLineWidth(0.5);
        for(i = 0;i < WINDOW_WIDTH;i += interval){
            grid_layer.getGraphicsContext().strokeLine(i, 0, i, WINDOW_HEIGHT);
        }

        for(i = 0;i < WINDOW_HEIGHT;i += interval){
            grid_layer.getGraphicsContext().strokeLine(0, i, WINDOW_WIDTH, i);
        }
        grid_layer.getCanvas().toBack();
    }

    /*
    * グリッドを消す関数
     */
    private static void EraseGrid(Layer grid_layer){
        grid_layer.getCanvas().toFront();
        grid_layer.getGraphicsContext().setFill(Color.WHITESMOKE);

        grid_layer.getGraphicsContext().fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        grid_layer.getCanvas().toBack();
    }

    /*
    * 下敷き画像を描画するレイヤーの初期設定
     */
    private static void ConfigImageLayer(Layer layer){
        SettingAnchor(layer);
    }

    /*
    * グラフィックレイヤーにおけるアンカーペインの設定を一般化した関数
     */
    public static void SettingAnchor(Layer layer){
        AnchorPane.setTopAnchor(layer.getCanvas(), UIValues.MENU_HEIGHT);
        AnchorPane.setLeftAnchor(layer.getCanvas(), UIValues.LAYER_LIST_WIDTH + UIValues.LIST_TO_CANVAS_WIDTH);
    }

    /*
    * レイヤーのリストビューの初期設定
     */
    private static void ConfigLayerList(ListView<String> listView){
        AnchorPane.setTopAnchor(listView, UIValues.LAYER_LIST_SCREEN_Y);
        AnchorPane.setLeftAnchor(listView, 0.0);
        listView.setPrefWidth(UIValues.LAYER_LIST_WIDTH);
        listView.setPrefHeight(UIValues.LAYER_LIST_HEIGHT);

        ContextMenu popup_ll = new ContextMenu();
        MenuItem create_layer = new MenuItem("新規レイヤー");
        popup_ll.getItems().addAll(create_layer);

        create_layer.setOnAction(event -> CreateLayer(listView));

        listView.setOnContextMenuRequested(event -> {
            popup_ll.show(listView, event.getScreenX(), event.getScreenY());
        });

        listView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                popup_ll.hide();
            }
        });

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<String> layer_names = FXCollections.observableArrayList();

        listView.setItems(layer_names);
        listView.setEditable(true);

    }

    /*
    * レイヤーを新しく作成する関数
     */
    private static void CreateLayer(ListView<String> listView){
        TextInputDialog create_layer = new TextInputDialog("レイヤー");
        create_layer.setTitle("新規レイヤー");
        create_layer.setHeaderText("新規レイヤーの作成");
        create_layer.setContentText("レイヤー名 :");
        Optional<String> result = create_layer.showAndWait();

        if(result.isPresent()){
            addLayer(create_layer.getEditor().getText(), listView);
        }

    }

    /*
    * 新しいレイヤーを追加する関数
     */
    public static void addLayer(String layer_name, ListView<String> listView){
        CurrentLayerData = new LayerData(layer_name);
        LayerDatas.add(CurrentLayerData);
        listView.getItems().add(layer_name);
        listView.getSelectionModel().select(layer_name);
    }

    /*
    * yuri faceの初期化
    * 今のところ、初期レイヤーの作成のみ
     */
    private static void yuri_face_init(ListView<String> listView){
        addLayer("レイヤー1", listView);
    }

    /*
    * レイヤーデータの差し替えを行う関数
    * リストビューをクリックして変更する方
     */
    private static void SwitchLayer(LayerData new_layer_data, Layer front, Layer lines){
        AllEraseLayer(front);
        AllEraseLayer(lines);
        new_layer_data.AllDraw(front, lines);
        lines.getCanvas().toFront();
        front.getCanvas().toFront();
    }

    /*
    * 指定したグラフィックレイヤーをすべて消す関数
     */
    private static void AllEraseLayer(Layer layer){
        layer.getCanvas().toFront();
        layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        layer.getCanvas().toBack();
    }

    /*
    * プレビューを表示するための関数
     */
    private static void Preview(Layer layer){
        layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        for(LayerData layer_data : LayerDatas){
            layer_data.AllDraw(layer, layer);
        }

        layer.getCanvas().toFront();
    }

}
