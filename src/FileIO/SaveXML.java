package FileIO;

import Layers.ImageLayer;
import UI.Dot;
import UI.LayerData;
import UI.LayersTree;
import UI.Main;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by Akihiro on 2017/03/02.
 */
public class SaveXML {
    public static void saveToXML(ArrayList<LayerData> data, LayersTree layersTree, Stage stage, ImageLayer imageLayer){
        // Documentインスタンスの生成
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = documentBuilder.newDocument();

        /*
        Element image_path = document.createElement("ImagePath");
        Element image_name_value;
        if(!imageLayer.getImagePath().isEmpty()) {
            System.out.println(imageLayer.getImagePath().replaceAll("/", "a"));
            image_name_value = document.createElement(imageLayer.getImagePath().replaceAll("/", "a"));
        }else{
            image_name_value = document.createElement("null");
            image_path.appendChild(image_name_value);
        }
        image_path.appendChild(image_name_value);

        document.appendChild(image_path);
        */
        // XML文書の作成
        Element catalog = document.createElement("catalog");
        document.appendChild(catalog);

        Element mouth = document.createElement("Mouth");


        for(TreeItem<String> item : layersTree.getMouth_tree().getChildren()){
            LayerData ref = null;
            for(LayerData layer_data : data){
                //select.getParent()な理由
                    /*
                    * select.getValue()で自分の名前、select.getParentで親の絶対パスになるからちょうどよい
                     */
                if(layer_data.getName().equals(Main.MakeLayerdataName(item.getValue(), item.getParent()))){
                    ref = layer_data;
                    break;
                }
            }
            //なんも取れなかったらエラー
            if(ref == null) {
                System.out.println("ERROR");
                break;
            }

            Element layer = document.createElement("Layer");
            layer.setAttribute("inside", ref.getName());
            Element layer_name = document.createElement("name");

            layer_name.appendChild(document.createTextNode(item.getValue()));
            layer.appendChild(layer_name);

            int index = 0;
            for(Dot dot : ref.getDotList()){
                Element dot_element = document.createElement("dot");
                dot_element.setAttribute("index", String.valueOf(index));
                dot_element.appendChild(document.createTextNode(String.valueOf(dot.getX())));
                dot_element.appendChild(document.createTextNode(" "));
                dot_element.appendChild(document.createTextNode(String.valueOf(dot.getY())));
                layer.appendChild(dot_element);
                index++;
            }

            index = 0;
            for(Dot dot : ref.getDotList()){
                for(Dot connect : dot.getConnected_dots()) {
                    Element line = document.createElement("line");
                    line.setAttribute("index", String.valueOf(index));
                    line.appendChild(document.createTextNode(String.valueOf(dot.getX())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(dot.getY())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(connect.getX())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(connect.getY())));
                    layer.appendChild(line);
                    index++;
                }
            }
            mouth.appendChild(layer);
        }

        catalog.appendChild(mouth);

        // XMLファイルの作成
        File file = new File("test.xml");
        write(file, document);
    }

    private static boolean write(File file, Document document) {

        // Transformerインスタンスの生成
        Transformer transformer = null;
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        // Transformerの設定
        transformer.setOutputProperty("indent", "yes"); //改行指定
        transformer.setOutputProperty("encoding", "UTF-8"); // エンコーディング

        // XMLファイルの作成
        try {
            transformer.transform(new DOMSource(document), new StreamResult(
                    file));
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
