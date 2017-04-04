package UI;

import Layers.Layer;
import Layers.SystemLayers;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import sub.EditFPPWindowDepth3;
import sub.EditFPPropertyWindow;

import static UI.Main.CurrentLayerData;
import static UI.UIValues.*;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class ReferenceImagesUI extends LayersTree {

    private TreeItem<String> face_base;
    private CorePartLayerDatas corePartLayerDatas;
    private CheckBox preview;
    private PickedColorUI pickedColorUI;

    public ReferenceImagesUI(String tree_name, SystemLayers systemLayers, Stage stage){
        super(tree_name);

        pickedColorUI = new PickedColorUI(LAYER_LIST_WIDTH + LAYER_WIDTH + 20, LAYER_LIST_HEIGHT + 80);

        ContextMenu popup_menu_4_depth1 = new ContextMenu();
        MenuItem preview_all_menu  = new MenuItem("全体をプレビュー");
        popup_menu_4_depth1.getItems().add(preview_all_menu);

        ContextMenu popup_menu_4_depth2 = new ContextMenu();
        MenuItem property = new MenuItem("プロパティ");
        MenuItem preview_menu  = new MenuItem("プレビュー");

        property.setOnAction(event -> {
            EditFPP(stage);
        });

        preview_menu.setOnAction(event -> {
            PreviewDepth2(systemLayers.getPreview());
        });

        popup_menu_4_depth2.getItems().addAll(preview_menu, property);

        ContextMenu popup_menu_4_depth3 = new ContextMenu();
        MenuItem pp_setting = new MenuItem("プロパティ");

        pp_setting.setOnAction(event -> {
            EditFPP_Depth3(stage);
        });

        popup_menu_4_depth3.getItems().addAll(pp_setting);

        corePartLayerDatas = new CorePartLayerDatas();

        face_base = new TreeItem<>("顔面");
        face_base.getChildren().addAll(new TreeItem<>("輪郭"), new TreeItem<>("髪"));
        face_base.setExpanded(true);
        layer_root.getChildren().add(face_base);
        face_base.expandedProperty().addListener((observable, oldValue, newValue) -> {
            face_base.setExpanded(true);
        });

        right_eye_tree.getChildren().addAll(new TreeItem<>("目の切り口"), new TreeItem<>("黒目"), new TreeItem<>("まつげ"));
        right_eye_tree.setExpanded(true);
        right_eye_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            right_eye_tree.setExpanded(true);
        });

        left_eye_tree.getChildren().addAll(new TreeItem<>("目の切り口"), new TreeItem<>("黒目"), new TreeItem<>("まつげ"));
        left_eye_tree.setExpanded(true);
        left_eye_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            left_eye_tree.setExpanded(true);
        });

        right_eyebrows_tree.getChildren().add(new TreeItem<>("眉"));
        right_eyebrows_tree.setExpanded(true);
        right_eyebrows_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            right_eyebrows_tree.setExpanded(true);
        });

        left_eyebrows_tree.getChildren().add(new TreeItem<>("眉"));
        left_eyebrows_tree.setExpanded(true);
        left_eyebrows_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            left_eyebrows_tree.setExpanded(true);
        });

        mouth_tree.getChildren().addAll(new TreeItem<>("口"), new TreeItem<>("舌"));
        mouth_tree.setExpanded(true);
        mouth_tree.expandedProperty().addListener((observable, oldValue, newValue) -> {
            mouth_tree.setExpanded(true);
        });

        layer_root.expandedProperty().addListener((observable, oldValue, newValue) -> {
            layer_root.setExpanded(true);
        });

        preview = new CheckBox("他パーツを裏に表示");


        treeView.setOnMouseClicked(event -> {

            int depth = 0;

            TreeItem<String> select = treeView.getSelectionModel().selectedItemProperty().get();
            setSelecting_tree(select);

            if(event.getButton() == MouseButton.PRIMARY){
                popup_menu_4_depth2.hide();
                popup_menu_4_depth3.hide();
            }else if(event.getButton() == MouseButton.SECONDARY){
                TreeItem<String> ref = select;
                while(true){
                    if(ref == null){
                        break;
                    }
                    ref = ref.getParent();
                    depth++;
                }
            }

            if(depth == 1) {
                if(event.getButton() == MouseButton.SECONDARY)
                    popup_menu_4_depth1.show(treeView, event.getScreenX(), event.getScreenY());
                setSelecting_tree(select);
            } else if(depth == 2){
                if(event.getButton() == MouseButton.SECONDARY)
                    popup_menu_4_depth2.show(treeView, event.getScreenX(), event.getScreenY());
                setSelecting_tree(select);
            } else if(depth == 3){
                if(event.getButton() == MouseButton.SECONDARY)
                    popup_menu_4_depth3.show(treeView, event.getScreenX(), event.getScreenY());
                setSelecting_tree(select);

                if (select.getValue().equals("輪郭")) {
                    CurrentLayerData = corePartLayerDatas.getLayerData("f_b_rinkaku");
                    Main.SwitchPartLayer(
                            CurrentLayerData,
                            systemLayers.getFront(),
                            systemLayers.getLines()
                    );
                } else if (select.getValue().equals("髪")) {
                    CurrentLayerData = corePartLayerDatas.getLayerData("f_b_kami");
                    Main.SwitchPartLayer(
                            CurrentLayerData,
                            systemLayers.getFront(),
                            systemLayers.getLines()
                    );
                }else if (select.getValue().equals("黒目")) {
                    if (select.getParent().getValue().equals("右目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("r_e_kurome");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    } else if (select.getParent().getValue().equals("左目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("l_e_kurome");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    }
                } else if (select.getValue().equals("まつげ")) {
                    if (select.getParent().getValue().equals("右目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("r_e_matsuge");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    } else if (select.getParent().getValue().equals("左目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("l_e_matsuge");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    }
                } else if (select.getValue().equals("目の切り口")) {
                    if (select.getParent().getValue().equals("右目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("r_e_kirikuchi");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    } else if (select.getParent().getValue().equals("左目")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("l_e_kirikuchi");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    }
                } else if (select.getValue().equals("眉")) {
                    if (select.getParent().getValue().equals("右眉")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("r_e_b_mayu");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    } else if (select.getParent().getValue().equals("左眉")) {
                        CurrentLayerData = corePartLayerDatas.getLayerData("l_e_b_mayu");
                        Main.SwitchPartLayer(
                                CurrentLayerData,
                                systemLayers.getFront(),
                                systemLayers.getLines()
                        );
                    }
                } else if (select.getValue().equals("口")) {
                    CurrentLayerData = corePartLayerDatas.getLayerData("m_mouth");
                    Main.SwitchPartLayer(
                            CurrentLayerData,
                            systemLayers.getFront(),
                            systemLayers.getLines()
                    );
                } else if (select.getValue().equals("舌")) {
                    CurrentLayerData = corePartLayerDatas.getLayerData("m_shita");
                    Main.SwitchPartLayer(
                            CurrentLayerData,
                            systemLayers.getFront(),
                            systemLayers.getLines()
                    );
                }

                systemLayers.getCreateLL().eraseLayer();
            }
        });

        preview.setOnAction(event -> {
            systemLayers.getPreview().getGraphicsContext().clearRect(0, 0, systemLayers.getPreview().getCanvas().getWidth(), systemLayers.getPreview().getCanvas().getHeight());
            if(preview.isSelected()){
                this.DrawAllData(systemLayers.getPreview(), systemLayers.getPreview());
            }
        });

        AnchorPane.setLeftAnchor(treeView, WINDOW_WIDTH - LAYER_LIST_WIDTH);
        AnchorPane.setRightAnchor(treeView, 0.0);
        AnchorPane.setTopAnchor(treeView, UIValues.MENU_HEIGHT);

    }

    public void DrawAllData(Layer front, Layer lines){
        for(LayerDataEx data : corePartLayerDatas.getLayerDataExArray()){
            data.AllDraw4PR(front, lines);
        }
    }

    public CorePartLayerDatas getCorePartLayerDatas() {
        return corePartLayerDatas;
    }

    public CheckBox getPreviewBox() {
        return preview;
    }

    public void SettingAnchor(){
        AnchorPane.setLeftAnchor(preview, LAYER_LIST_WIDTH + LAYER_WIDTH + 20);
        AnchorPane.setTopAnchor(preview, LAYER_LIST_HEIGHT + 50);
    }

    private void EditFPP(Stage stage){
        Window window = stage;
        EditFPPropertyWindow sub = new EditFPPropertyWindow(window, selecting_tree.getValue(), corePartLayerDatas.getLayerData(selecting_tree.getValue()));
        sub.show();
    }

    private void EditFPP_Depth3(Stage stage){
        if(corePartLayerDatas.getLayerData(selecting_tree.getParent().getValue(), selecting_tree.getValue()).isPolygonEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("パーツが定義されていません");
            alert.showAndWait();
            return;
        }

        Window window = stage;
        EditFPPWindowDepth3 sub = new EditFPPWindowDepth3(
                window,
                selecting_tree.getParent().getValue() + "→" + selecting_tree.getValue(),
                corePartLayerDatas.getLayerData(selecting_tree.getParent().getValue(), selecting_tree.getValue()));
        sub.show();
    }

    private void PreviewDepth2(Layer preview_layer){
        for(TreeItem<String> item : selecting_tree.getChildren()){
            Polygon polygon = corePartLayerDatas.getLayerData(selecting_tree.getValue(), item.getValue()).getPolygons().get(0);
            preview_layer.getGraphicsContext().setFill(polygon.getDotColor());
            polygon.DrawDots(preview_layer);
            preview_layer.getGraphicsContext().fillPolygon(
                    polygon.getxPoints(),
                    polygon.getyPoints(),
                    polygon.getxPoints().length
            );
            System.out.println(item.getValue());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.FINISH);
        alert.setHeaderText("プレビュー中です");
        alert.setTitle("プレビュー");
        alert.showAndWait();

        preview_layer.eraseLayer();

    }

    public PickedColorUI getPickedColorUI() {
        return pickedColorUI;
    }
}
