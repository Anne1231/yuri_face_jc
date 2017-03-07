package FileIO;

import Layers.ImageLayer;
import Layers.Layer;
import UI.*;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
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

import static FileIO.Save.GetSavePath;


/**
 * Created by Akihiro on 2017/03/02.
 */
public class SaveXML {
    public static void saveToXML(ArrayList<LayerData> data, LayersTree layersTree, Stage stage, ImageLayer imageLayer){

        String file_name = GetSaveXMLPath(stage);
        if(file_name == null)
            return;

        // Documentインスタンスの生成
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = documentBuilder.newDocument();


        Element image_path = document.createElement("ImagePath");

        String str = imageLayer.getImagePath();
        if(str == null){
            image_path.appendChild(document.createTextNode("null"));
        }else {
            image_path.appendChild(document.createTextNode(str));
        }
        // XML文書の作成
        Element catalog = document.createElement("catalog");
        document.appendChild(catalog);

        Element mouth = document.createElement("Mouth");


        for(TreeItem<String> item : layersTree.getMouth_tree().getChildren()){
            LayerData ref = Main.SearchAndGetLayer(item.getValue(), LayerData.LayerDataType.Mouth);

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
            for(Dot dot : ref.getDotSet()){
                Element dot_element = document.createElement("dot");
                dot_element.setAttribute("index", String.valueOf(index));
                dot_element.appendChild(document.createTextNode(String.valueOf(dot.getX())));
                dot_element.appendChild(document.createTextNode(" "));
                dot_element.appendChild(document.createTextNode(String.valueOf(dot.getY())));
                layer.appendChild(dot_element);
                index++;
            }

            index = 0;
            for(Line c_line : ref.getLineList()){
                    Element line = document.createElement("line");
                    line.setAttribute("index", String.valueOf(index));
                    line.appendChild(document.createTextNode(String.valueOf(c_line.getBegin().getX())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(c_line.getBegin().getY())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(c_line.getEnd().getX())));
                    line.appendChild(document.createTextNode(" "));
                    line.appendChild(document.createTextNode(String.valueOf(c_line.getEnd().getY())));
                    layer.appendChild(line);
                    index++;
            }
            mouth.appendChild(layer);
        }

        catalog.appendChild(image_path);
        catalog.appendChild(mouth);

        // XMLファイルの作成
        File file = new File(file_name);
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

    public static String GetSaveXMLPath(Stage stage) {

        FileChooser saver = new FileChooser();

        saver.setTitle("保存");
        saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML文書", "*.xml"));
        File importFile = saver.showSaveDialog(stage);
        if(importFile != null) {
            String path = importFile.getPath().toString();
            return path;
        }
        return null;
    }
}
