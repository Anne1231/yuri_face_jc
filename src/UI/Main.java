package UI;

import FileIO.*;
import Layers.*;
import backend.utility.KeyTable;
import javafx.scene.layout.VBox;
import motion.BasicMotion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import motion.SynchronizableBasicMotion;
import org.opencv.core.*;
import sub.CreateMotionWindow;

import java.util.ArrayList;
import java.util.Optional;

import static UI.UIValues.*;

/**
 * Created by Akihiro on 2017/02/25.
 */
public class Main extends Application {

    public static int x, y;
    public static Dot selecting_dot;
    public static LayerData CurrentLayerData = null;
    public static LayerData PinnedData;
    public static ArrayList<BasicMotion> basicMotions = new ArrayList<>();
    public static ArrayList<LayerData> LayerDatas = new ArrayList<>();
    public static KeyTable keyTable = new KeyTable();

    @Override
    public void start(Stage stage){

        Init(stage);

        //ルート
        AnchorPane root = new AnchorPane();

        SystemLayers systemLayers = new SystemLayers(stage);

        //メニューバー
        MenuBar menubar = new MenuBar();

        //参照画像のツリー
        ReferenceImagesUI referenceImagesUI = new ReferenceImagesUI("パーツ", systemLayers, stage);

        NormalLayersTree layersTree = new NormalLayersTree("レイヤー", stage, systemLayers.getCreateLL(), systemLayers.getFront(), systemLayers.getLines(), referenceImagesUI);
        MotionTree motionTree = new MotionTree("モーション", stage, layersTree, systemLayers.getPreview());

        /*
        * アルファ
        */
        VBox box = new VBox();
        box.getChildren().addAll(layersTree.getTreeView());
        Tab layer_tab = new Tab("レイヤー");
        Tab motion_tab = new Tab("モーション");
        TabPane tabs = new TabPane();
        layer_tab.setContent(box);
        tabs.getTabs().addAll(layer_tab, motion_tab);
        motion_tab.setContent(motionTree.getTreeView());
        AnchorPane.setTopAnchor(tabs, UIValues.LAYER_LIST_SCREEN_Y);
        AnchorPane.setLeftAnchor(tabs, 0.0);
        tabs.setPrefWidth(UIValues.LAYER_LIST_WIDTH);
        tabs.setPrefHeight(UIValues.LAYER_LIST_HEIGHT);

        /*
        * レイヤーの各種設定
        * この中でアンカーペインの設定も行う
         */
        systemLayers.ConfigLayers(layersTree);

        /*
        *下敷き画像関係
         */
        BackGroundImageUI backGroundImageUI = new BackGroundImageUI(systemLayers);
        AnchorPane.setBottomAnchor(backGroundImageUI.getRoot(), UIValues.FOOTER_HEIGHT + 5);
        AnchorPane.setLeftAnchor(backGroundImageUI.getRoot(), 0.0);

        /*
        * メニューバーの設定
         */
        ConfigMenuBar(menubar, stage, systemLayers.getGrid(), systemLayers.getImageLayer(), systemLayers.getPreview(), backGroundImageUI.getImage_bairitsu_field(), layersTree, motionTree, backGroundImageUI, systemLayers.getSelectingRect());

        /*
        * ノードを登録
         */
        root.getChildren().addAll(
                menubar,
                tabs,
                backGroundImageUI.getRoot(),
                systemLayers.getFront().getCanvas(),
                systemLayers.getCreateLL().getCanvas(),
                systemLayers.getLines().getCanvas(),
                systemLayers.getGrid().getCanvas(),
                systemLayers.getImageLayer().getCanvas(),
                systemLayers.getPreview().getCanvas(),
                systemLayers.getSelectingRect().getCanvas(),
                systemLayers.getFooter().getCanvas(),
                referenceImagesUI.getTreeView(),
                referenceImagesUI.getPreviewBox()
        );

        //referenceImageUIのアンカーペイン上の位置を設定
        referenceImagesUI.SettingAnchor();

        //システムレイヤーの順番を調整
        systemLayers.InitSort();

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            keyTable.press(event.getCode());
        });
        scene.setOnKeyReleased(event -> {
            keyTable.release(event.getCode());
        });

        stage.setScene(scene);
        stage.show();

    }

    /*
    * メニューバーの初期設定
     */
    private static void ConfigMenuBar(MenuBar menu, Stage stage, GridLayer grid_layer, ImageLayer image_layer, Layer preview, TextField image_b, LayersTree layersTree, LayersTree motion_tree, BackGroundImageUI backGroundImageUI, SelectAreaLayer selectAreaLayer){
        Menu help = new Menu("ヘルプ");
        MenuItem dev = new MenuItem("DEVELOPERS");
        help.getItems().addAll(dev);
        Menu  display = new Menu("表示");
        CheckMenuItem grid_config = new CheckMenuItem("グリッド");
        CheckMenuItem grid_complete = new CheckMenuItem("グリッドによる補完");
        CheckMenuItem preview_menu = new CheckMenuItem("プレビュー");

        Menu selecting_area = new Menu("範囲選択");
        MenuItem make_group = new MenuItem("グループを作る");

        make_group.setOnAction(event -> {
            selectAreaLayer.getCanvas().toFront();

        });

        selecting_area.getItems().addAll(make_group);

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
                grid_layer.eraseGrid();
                grid_config.setSelected(false);
                return;
            }

            grid_layer.drawGrid(interval);
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
        grid_layer.drawGrid(INIT_GRID_INTERVAL);

        display.getItems().addAll(grid_config, grid_complete, preview_menu);
        Menu file = new Menu("ファイル");
        MenuItem open = new MenuItem("下敷き画像を開く");
        MenuItem open_yfml = new MenuItem("XMLファイルを開く");
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
            System.out.println( Integer.valueOf(backGroundImageUI.getImage_x().getText()));
            image_layer.DrawImageNormal(img, Integer.valueOf(backGroundImageUI.getImage_x().getText()), Integer.valueOf(backGroundImageUI.getImage_y().getText()));
            backGroundImageUI.getDisplay().setSelected(true);
        });

        open_yfml.setOnAction(event -> {
            //OpenYFML.open_yfml(stage, front, lines, image_layer, null, image_b);
            try {
                LoadXML.loadXML(stage, LayerDatas, layersTree, image_layer, image_b);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        save.setOnAction(event -> {
            //Save.save_to_file(LayerDatas, stage, image_layer);
            SaveXML.saveToXML(LayerDatas, layersTree, stage, image_layer);
        });

        dev.setOnAction(event -> {
            BasicMotion re, rb, le, lb, m;

            m = SearchAndGetMotion("m", LayerData.LayerDataType.Mouth);
            m.setMill_sec(100);
            re = SearchAndGetMotion("re", LayerData.LayerDataType.RightEye);
            re.setMill_sec(100);
            le = SearchAndGetMotion("le", LayerData.LayerDataType.LeftEye);
            le.setMill_sec(100);
            lb = SearchAndGetMotion("lb", LayerData.LayerDataType.LeftEyebrows);
            lb.setMill_sec(100);
            rb = SearchAndGetMotion("rb", LayerData.LayerDataType.RightEyebrows);

            SynchronizableBasicMotion  synchronizableBasicMotion = new SynchronizableBasicMotion(re, le, rb, lb, m, null);
            synchronizableBasicMotion.preview(preview);
        });

        file.getItems().addAll(open_yfml, open, save, quit);
        menu.getMenus().addAll(file, display, selecting_area, help);

        menu.setPrefWidth(WINDOW_WIDTH);
    }

    /*
    * ウィンドウの初期設定
     */
    private static void Init(Stage stage){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        stage.setTitle("Yuri Face");
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
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
    public static void addLayer(String layer_name, LayerData.LayerDataType type, LayersTree layersTree, ReferenceImagesUI referenceImagesUI){

        CurrentLayerData = new LayerData(layer_name, type);
        LayerDatas.add(CurrentLayerData);

        switch (type){
            case NullNull:
                break;
            case FaceBase:
                break;
            case LeftEye:
                referenceImagesUI.getCorePartLayerDatas().getL_e_kurome().getPolygons().forEach(polygon -> {
                        CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getL_e_kurome().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                referenceImagesUI.getCorePartLayerDatas().getL_e_matsuge().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getL_e_matsuge().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                layersTree.getLeft_eye_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.getLeft_eye_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case RightEye:
                referenceImagesUI.getCorePartLayerDatas().getR_e_kurome().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getR_e_kurome().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                referenceImagesUI.getCorePartLayerDatas().getR_e_matsuge().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getR_e_matsuge().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                layersTree.getRight_eye_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.getRight_eye_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case LeftEyebrows:
                referenceImagesUI.getCorePartLayerDatas().getL_e_b_mayu().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getL_e_b_mayu().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                layersTree.getLeft_eyebrows_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.getLeft_eyebrows_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case RightEyebrows:
                referenceImagesUI.getCorePartLayerDatas().getR_e_b_mayu().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getR_e_b_mayu().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                layersTree.getRight_eyebrows_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.getRight_eyebrows_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case Mouth:
                referenceImagesUI.getCorePartLayerDatas().getM_mouth().getPolygons().forEach(polygon -> {
                    CurrentLayerData.addPolygon(polygon.clone());
                });
                referenceImagesUI.getCorePartLayerDatas().getM_mouth().getPolygons().stream().parallel().forEach(polygon -> {
                    for(int i = 0;i < polygon.size() - 1;i++) {
                        CurrentLayerData.connect(new Dot((int)polygon.getX(i), (int)polygon.getY(i)), new Dot((int)polygon.getX(i+1), (int)polygon.getY(i+1)));
                    }
                    CurrentLayerData.connect(new Dot((int)polygon.getX(0), (int)polygon.getY(0)), new Dot((int)polygon.getX(polygon.size() - 1), (int)polygon.getY(polygon.size() - 1)));
                });
                layersTree.getMouth_tree().getChildren().add(new TreeItem<>(layer_name));
                layersTree.getMouth_tree().setExpanded(true);
                layersTree.increase_layers_count();
            default:
                break;
        }
    }

    /*
    * レイヤーデータの差し替えを行う関数
    * リストビューをクリックして変更する方
     */
    public static void SwitchUsersLayer(LayerData new_layer_data, FrontDotLayer front, Layer lines){
        front.eraseLayer();
        lines.eraseLayer();
        new_layer_data.AllDraw4N(front, lines);
        lines.getCanvas().toFront();
        front.getCanvas().toFront();
    }

    public static void SwitchPartLayer(LayerData new_layer_data, FrontDotLayer front, Layer lines){
        front.eraseLayer();
        lines.eraseLayer();
        new_layer_data.AllDraw4PR(front, lines);
        lines.getCanvas().toFront();
        front.getCanvas().toFront();
    }

    /*
    * プレビューを表示するための関数
     */
    private static void Preview(Layer layer){
        layer.getGraphicsContext().clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        for(LayerData layer_data : LayerDatas){
            //layer_data.AllDraw(layer, layer);
        }

        layer.getCanvas().toFront();
    }

    /*
    * データのみをコピーして新しくレイヤーを作成する関数
     */
    public static void addCloneLayer(String clone_name, LayerData original, LayersTree layersTree){
        CurrentLayerData = original.clone();
        CurrentLayerData.setType(layersTree.WhichType(layersTree.getSelecting_tree()));
        CurrentLayerData.setName(clone_name);
        LayerDatas.add(CurrentLayerData);
        switch (layersTree.WhichType(layersTree.getSelecting_tree())){
            case NullNull:
                break;
            case FaceBase:
                break;
            case LeftEye:
                layersTree.getLeft_eye_tree().getChildren().add(new TreeItem<>(clone_name));
                layersTree.getLeft_eye_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case RightEye:
                layersTree.getRight_eye_tree().getChildren().add(new TreeItem<>(clone_name));
                layersTree.getRight_eye_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case LeftEyebrows:
                layersTree.getLeft_eyebrows_tree().getChildren().add(new TreeItem<>(clone_name));
                layersTree.getLeft_eyebrows_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case RightEyebrows:
                layersTree.getRight_eyebrows_tree().getChildren().add(new TreeItem<>(clone_name));
                layersTree.getRight_eyebrows_tree().setExpanded(true);
                layersTree.increase_layers_count();
                break;
            case Mouth:
                layersTree.getMouth_tree().getChildren().add(new TreeItem<>(clone_name));
                layersTree.getMouth_tree().setExpanded(true);
                layersTree.increase_layers_count();
            default:
                break;
        }
    }

    public static LayerData SearchAndGetLayer(String layer_name, LayerData.LayerDataType type){
        for(LayerData layerData : LayerDatas){
            if(layerData.getType() == type){
                if(layerData.getName().equals(layer_name)){
                    return layerData;
                }
            }
        }
        return null;
    }

    public static BasicMotion SearchAndGetMotion(String motion_name, LayerData.LayerDataType type){
        for(BasicMotion motion : basicMotions){
            if(motion.getType() == type){
                if(motion.getName().equals(motion_name)){
                    return motion;
                }
            }
        }
        return null;
    }

}
