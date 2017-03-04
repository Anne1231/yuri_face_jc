package FileIO;

import Layers.ImageLayer;
import UI.Dot;
import UI.LayerData;
import UI.LayersTree;
import UI.Main;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static FileIO.OpenYFML.GetFilePath;

/**
 * Created by Akihiro on 2017/03/02.
 */
public class LoadXML {

    public static void loadXML(Stage stage, ArrayList<LayerData> data, LayersTree layersTree, ImageLayer imageLayer, TextField image_b) throws SAXException, IOException, ParserConfigurationException {

        boolean flag = false;

        String path = GetFilePath(stage);
        if(path.isEmpty())
            return;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(path);

        Element root = document.getDocumentElement();

        //ルート要素のノード名を取得する
        System.out.println("ノード名：" +root.getNodeName());

        //ルート要素の属性を取得する
        System.out.println("ルート要素の属性：" + root.getAttribute("name"));

        //ルート要素の子ノードを取得する
        NodeList rootChildren = root.getChildNodes();
;
        System.out.println("------------------");

        LayerData layerData;
        String front_end_layer_name = null;
        for(int i = 0;i < rootChildren.getLength();i++) {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (element.getNodeName().equals("Mouth")) {
                    NodeList children = node.getChildNodes();
                    for (int j = 0;j < children.getLength();j++) {
                        layerData = new LayerData();
                        Node MouthNode = children.item(j);
                        if (MouthNode.getNodeType() == Node.ELEMENT_NODE) {
                            if (MouthNode.getNodeName().equals("Layer")) {
                                flag = true;
                                System.out.println("レイヤー内部表現：" + ((Element)MouthNode).getAttribute("inside"));
                                /*
                                * 内部表現をセット
                                 */
                                layerData.setName(((Element)MouthNode).getAttribute("inside"));
                            }
                            for(int k = 0;k < MouthNode.getChildNodes().getLength();k++){
                                Node mouth_info = MouthNode.getChildNodes().item(k);
                                if (mouth_info.getNodeName().equals("name")) {
                                    /*
                                    * このレイヤーデータをLayersTreeに追加
                                    * 最初にLayersTreeに関する情報が来る場所なので、ここでLayersTreeに追加
                                     */
                                    //Main.addLayer(mouth_info.getTextContent(), LayerData.LayerDataType.Mouth, layersTree);
                                    front_end_layer_name = mouth_info.getTextContent();
                                    System.out.println("レイヤー名:" + mouth_info.getTextContent());
                                }else if(mouth_info.getNodeName().equals("dot")){
                                    System.out.println("インデックス" + ((Element)mouth_info).getAttribute("index"));
                                    System.out.println("ドット" + mouth_info.getTextContent());
                                    StringTokenizer tokenizer = new StringTokenizer(mouth_info.getTextContent());
                                    /*
                                    * ドットを追加（インデックスはいらんだろう）
                                     */
                                    layerData.AddDot(new Dot(Integer.valueOf(tokenizer.nextToken()), Integer.valueOf(tokenizer.nextToken())));
                                }else if(mouth_info.getNodeName().equals("line")){
                                    System.out.println("インデックス" + ((Element)mouth_info).getAttribute("index"));
                                    System.out.println("ライン" + mouth_info.getTextContent());
                                    /*
                                    * ラインを追加
                                     */
                                    StringTokenizer tokenizer = new StringTokenizer(mouth_info.getTextContent());
                                    layerData.connect(new Dot(Integer.valueOf(tokenizer.nextToken()), Integer.valueOf(tokenizer.nextToken())),
                                            new Dot(Integer.valueOf(tokenizer.nextToken()), Integer.valueOf(tokenizer.nextToken())));

                                }
                            }

                        }
                        if(flag) {
                            layerData.setType(LayerData.LayerDataType.Mouth);
                            data.add(layerData);
                            layersTree.getMouth_tree().getChildren().add(new TreeItem<>(front_end_layer_name));
                            layersTree.getMouth_tree().setExpanded(true);
                            layersTree.increase_layers_count();
                            flag = false;
                        }else{
                            layerData = null;
                        }
                    }
                    System.out.println("------------------");
                }else if (element.getNodeName().equals("ImagePath")){
                    NodeList image_path = node.getChildNodes();
                    for(int j = 0;j < image_path.getLength();j++) {
                        Node image_path_node = image_path.item(j);
                        if (image_path_node.getTextContent().isEmpty()) {
                            if(!image_path_node.getTextContent().equals("null")) {
                                Image img;
                                imageLayer.setImagePath(image_path_node.getTextContent());
                                img = new Image(image_path_node.getTextContent());
                                image_b.setText("100.0%");
                                imageLayer.DrawImageNormal(img, 0, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}
