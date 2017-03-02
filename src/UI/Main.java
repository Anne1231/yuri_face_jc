package UI;

import FileIO.*;
import Layers.*;
import backend.face.Face;
import backend.transform.TransformImage;
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
import javafx.stage.Window;
import org.omg.CORBA.SystemException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import sub.AskLayerType;

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

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        InitWindow(stage);

        /*
        * フッターの設定
         */
        footer = new Footer(WINDOW_WIDTH, 20);
        footer.getGraphicsContext().setFill(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        footer.getGraphicsContext().fillRect(0, 0, UIValues.FOOTER_WIDTH, UIValues.FOOTER_HEIGHT);

        /*
        * yuri faceの初期化
        * 最初のレイヤーの作成とか
         */
        //yuri_face_init(layer_list);

        /*
        * アンカーペインを採用してみた
         */
        AnchorPane root = new AnchorPane();

        /*
        * 重要なグラフィックレイヤーたち
         */
        FrontDotLayer front = new FrontDotLayer(WINDOW_WIDTH, WINDOW_HEIGHT);       //ドットを描画するレイヤー
        LinesLayer lines = new LinesLayer(WINDOW_WIDTH, WINDOW_HEIGHT);       //線を描画するレイヤー
        GridLayer grid  = new GridLayer(WINDOW_WIDTH, WINDOW_HEIGHT, INIT_GRID_INTERVAL);       //グリッドを描画するレイヤー
        ImageLayer image_layer = new ImageLayer(WINDOW_WIDTH, WINDOW_HEIGHT); //下敷き画像を描画するレイヤー
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

        LayersTree layersTree = new LayersTree();
        ConfigLayerList(stage, layersTree, front, lines);

        /*
        * レイヤーの各種設定
        * この中でアンカーペインの設定も行う
         */
        //ConfigFrontLayer(front, lines, grid, layer_list);
        ConfigFrontLayer(front, lines, grid, layersTree);
        ConfigLayer.ConfigLinesLayer(lines, front, grid);
        ConfigImageLayer(image_layer);
        SettingAnchor(preview);

        /*
        * ラベルの設定
         */
        Label layer_label = new Label("レイヤー");
        AnchorPane.setTopAnchor(layer_label, MENU_HEIGHT);
        AnchorPane.setLeftAnchor(layer_label, UIValues.LAYER_LIST_WIDTH / 3);

        Label bairitsu_label = new Label("下敷き画像倍率");
        AnchorPane.setBottomAnchor(bairitsu_label, UIValues.FOOTER_HEIGHT + 30);
        AnchorPane.setLeftAnchor(bairitsu_label, UIValues.LAYER_LIST_WIDTH / 3);

        TextField image_bairitsu = new TextField("100.0%");
        image_bairitsu.setAlignment(Pos.BASELINE_RIGHT);
        image_bairitsu.setPrefWidth(LAYER_LIST_WIDTH - 20);
        AnchorPane.setBottomAnchor(image_bairitsu, UIValues.FOOTER_HEIGHT + 5);
        AnchorPane.setLeftAnchor(image_bairitsu, 0.0);

        image_bairitsu.setOnAction(event -> {
            double result = Double.parseDouble(image_bairitsu.getText().replaceAll("[^.0-9]",""));
            image_bairitsu.setText(result + "%");
            result /= 100.0;
            Image image = image_layer.getImage();
            image_layer.clear();
            image_layer.DrawImageWithResize(image, 0, 0, image.getWidth(), image.getHeight(), result);
        });

        /*
        * メニューバーの設定
         */
        ConfigMenuBar(menubar, stage, front, lines, grid, image_layer, preview, image_bairitsu, layersTree);


        /*
        * ノードを登録
         */
        root.getChildren().addAll(menubar, /*layer_list*/layersTree.getTreeView(), layer_label, bairitsu_label, image_bairitsu, front.getCanvas(), lines.getCanvas(), grid.getCanvas(), image_layer.getCanvas(), preview.getCanvas(), footer.getCanvas());

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
    private static void ConfigFrontLayer(FrontDotLayer front, LinesLayer lines, GridLayer gridLayer, LayersTree layersTree){

        SettingAnchor(front);

        ContextMenu popup = new ContextMenu();
        MenuItem choose = new MenuItem("ドットを選択");
        MenuItem put = new MenuItem("ドットを配置");

        /*
        * ドット配置処理
         */
        put.setOnAction(event -> {
            if(layersTree.getLayers_count() == 0){
                return;
            }
            Dot dot;
            if(gridLayer.isEnableComplete()) {
                dot = new Dot(x, y, gridLayer.getInterval());
            }else{
                dot = new Dot(x, y);
            }
            dot.Draw(front, Color.BLACK);
            CurrentLayerData.AddDot(dot);
        });

        /*
        * ドット選択処理
         */
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
            if(layersTree.getLayers_count() == 0){
                return;
            }
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
    private static void ConfigMenuBar(MenuBar menu, Stage stage, Layer front, Layer lines, GridLayer grid_layer, ImageLayer image_layer, Layer preview, TextField image_b, LayersTree layersTree){
        Menu help = new Menu("ヘルプ");
        MenuItem dev = new MenuItem("DEVELOPERS");
        help.getItems().addAll(dev);
        Menu  display = new Menu("表示");
        CheckMenuItem grid_config = new CheckMenuItem("グリッド");
        CheckMenuItem grid_complete = new CheckMenuItem("グリッドによる補完");
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

            image_layer.getCanvas().toBack();

        });

        grid_complete.setOnAction(event -> {
            grid_layer.ConfigCompletion(grid_complete.isSelected());
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
        DrawGrid(grid_layer, INIT_GRID_INTERVAL);

        display.getItems().addAll(grid_config, grid_complete, preview_menu);
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
                img = ImageIO.SelectAndOpenImage(stage, image_layer);
            }catch (Exception e){
                System.out.println(e);
                return;
            }
            image_b.setText("100.0%");
            image_layer.DrawImageNormal(img, 0, 0);
        });

        open_yfml.setOnAction(event -> {
            //OpenYFML.open_yfml(stage, front, lines, image_layer, null, image_b);
            try {
                LoadXML.loadXML(stage, LayerDatas, layersTree);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        save.setOnAction(event -> {
            Save.save_to_file(LayerDatas, stage, image_layer);
            SaveXML.saveToXML(LayerDatas, layersTree, stage, image_layer);
        });

        dev.setOnAction(event -> {
            Mat original = Imgcodecs.imread(image_layer.getImagePath().substring(8));
            Face face = new Face(original, LayerDatas);
            image_layer.DrawImageNormal(core.Convert.Mat2Image(face.getMouth().openMouth(face, 4, 0)), 0, 0);
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
    private static void DrawGrid(GridLayer grid_layer, int interval){
        int i;
        grid_layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        grid_layer.getCanvas().toFront();
        grid_layer.getGraphicsContext().setStroke(Color.GRAY);
        grid_layer.getGraphicsContext().setLineWidth(0.5);

        grid_layer.setInterval(interval);

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


    private static void ConfigLayerList(Stage stage, LayersTree layersTree, FrontDotLayer front, LinesLayer lines){
        AnchorPane.setTopAnchor(layersTree.getTreeView(), UIValues.LAYER_LIST_SCREEN_Y);
        AnchorPane.setLeftAnchor(layersTree.getTreeView(), 0.0);
        layersTree.getTreeView().setPrefWidth(UIValues.LAYER_LIST_WIDTH);
        layersTree.getTreeView().setPrefHeight(UIValues.LAYER_LIST_HEIGHT);

        ContextMenu popup_ll = new ContextMenu();
        MenuItem create_layer = new MenuItem("新規レイヤー");
        popup_ll.getItems().addAll(create_layer);

        create_layer.setOnAction(event -> CreateLayer(stage, layersTree));

        layersTree.getTreeView().setOnContextMenuRequested(event -> {
            if(layersTree.getSelecting_tree() != null) {
                popup_ll.show(layersTree.getTreeView(), event.getScreenX(), event.getScreenY());
            }
        });

        layersTree.getTreeView().setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                popup_ll.hide();
            }
            TreeItem<String> select = layersTree.getTreeView().getSelectionModel().selectedItemProperty().get();

            int depth = 0;
            TreeItem<String> ref = select;
            while(true){
                if(ref == null){
                    break;
                }
                ref = ref.getParent();
                depth++;
            }

            System.out.println(depth);

            if(depth == 2) {
                layersTree.setSelecting_tree(select);
            }else if(depth == 3){
                for(LayerData layer_data : LayerDatas){
                    //select.getParent()な理由
                    /*
                    * select.getValue()で自分の名前、select.getParentで親の絶対パスになるからちょうどよい
                     */
                    if(layer_data.getName().equals(MakeLayerdataName(select.getValue(), select.getParent()))){
                        CurrentLayerData = layer_data;
                        SwitchLayer(CurrentLayerData, front, lines);
                        break;
                    }
                }
                //新規レイヤーメニューは表示させない
                //裏ではnullで判定してる
                layersTree.setSelecting_tree(null);
            }else{
                //新規レイヤーメニューは表示させない
                //裏ではnullで判定してる
                layersTree.setSelecting_tree(null);
            }
        });

        layersTree.getTreeView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        layersTree.getTreeView().setEditable(true);

    }

    /*
    * レイヤーを新しく作成する関数
     */
    private static void CreateLayer(Stage stage, LayersTree layersTree){
        Window window = stage;
        Stage select_window = new AskLayerType(window);
        select_window.showAndWait();
        if(AskLayerType.success) {  //正常に終了したときのみレイヤー追加処理
            addLayer(AskLayerType.layer_name, layersTree.WhichType(layersTree.getSelecting_tree()), layersTree);
        }
    }

    /*
    * 新しいレイヤーを追加する関数
     */
    public static void addLayer(String layer_name, LayerData.LayerDataType type, ListView<String> listView){
        CurrentLayerData = new LayerData(layer_name, type);
        LayerDatas.add(CurrentLayerData);
        listView.getItems().add(layer_name);
        listView.getSelectionModel().select(layer_name);
    }

    /*
   * 新しいレイヤーを追加する関数
    */
    public static void addLayer(String layer_name, LayerData.LayerDataType type, LayersTree layersTree){

        CurrentLayerData = new LayerData(MakeLayerdataName(layer_name, layersTree.getSelecting_tree()), type);
        LayerDatas.add(CurrentLayerData);

        switch (type){
            case NullNull:
                break;
            case FaceBase:
                break;
            case LeftEye:
                layersTree.getLeft_eye_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.increase_layers_count();
                break;
            case RightEye:
                layersTree.getRight_eye_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.increase_layers_count();
                break;
            case LeftEyebrows:
                layersTree.getLeft_eyebrows_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.increase_layers_count();
                break;
            case RightEyebrows:
                layersTree.getRight_eyebrows_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.increase_layers_count();
                break;
            case Mouth:
                layersTree.getMouth_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.increase_layers_count();
            default:
                break;
        }
    }

    /*
    * yuri faceの初期化
    * 今のところ、初期レイヤーの作成のみ
     */
    private static void yuri_face_init(ListView<String> listView){
        //addLayer("レイヤー1", listView);
    }

    /*
    * レイヤーデータの差し替えを行う関数
    * リストビューをクリックして変更する方
     */
    private static void SwitchLayer(LayerData new_layer_data, FrontDotLayer front, Layer lines){
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

    public static String MakeLayerdataName(String tail, TreeItem<String> item){
        /*
            * 固有の名称を生成
             */
        StringBuilder builder = new StringBuilder();
        TreeItem<String> ref = item;
        builder.append(tail);
        while(ref != null){
            builder.append(ref.getValue());
            ref = ref.getParent();
        }

        return builder.toString();
    }

}
