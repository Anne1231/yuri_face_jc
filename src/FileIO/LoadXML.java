package FileIO;

import Layers.Layer;
import UI.Dot;
import UI.LayerData;
import UI.LayersTree;
import UI.Main;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Akihiro on 2017/03/02.
 */
public class LoadXML {

    public static void loadXML(String file_name, ArrayList<LayerData> data, LayersTree layersTree) throws SAXException, IOException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse("test.xml");

        Element root = document.getDocumentElement();

        //ルート要素のノード名を取得する
        System.out.println("ノード名：" +root.getNodeName());

        //ルート要素の属性を取得する
        System.out.println("ルート要素の属性：" + root.getAttribute("name"));

        //ルート要素の子ノードを取得する
        NodeList rootChildren = root.getChildNodes();
;
        System.out.println("------------------");

        for(int i = 0;i < rootChildren.getLength();i++) {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (element.getNodeName().equals("Mouth")) {
                    NodeList personChildren = node.getChildNodes();
                    for (int j = 0;j < personChildren.getLength();j++) {
                        LayerData layerData = new LayerData();
                        Node MouthNode = personChildren.item(j);
                        if (MouthNode.getNodeType() == Node.ELEMENT_NODE) {

                            if (MouthNode.getNodeName().equals("Layer")) {
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
                                    Main.addLayer(mouth_info.getTextContent(), layersTree.WhichType(layersTree.getSelecting_tree()), layersTree);
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
                                    Dot dot = new Dot(Integer.valueOf(tokenizer.nextToken()), Integer.valueOf(tokenizer.nextToken()));
                                    for(Dot finding : layerData.getDotList()){
                                        if(finding.equals(dot)){{
                                            dot.getConnected_dots().add(finding);
                                        }}
                                    }
                                }
                            }

                        }
                        data.add(layerData);
                    }
                    System.out.println("------------------");
                }
            }

        }

    }
}
