package FileIO;

import Layers.ImageLayer;
import UI.Dot;
import Layers.Layer;
import UI.LayerData;
import UI.Main;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class OpenYFML {
    private static FileChooser chooser = new FileChooser();

    public static void open_yfml(Stage stage, ArrayList<LayerData> data, Layer front, Layer lines, ImageLayer imageLayer, ListView<String> listview, TextField image_b){

        listview.getItems().remove(0);
        data.remove(0);
        String path = GetFilePath(stage);
        try {
            int section_count = 0;
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String str = bufferedReader.readLine();
            if(!str.equals("yfml")) {
                System.out.println(str);
                System.exit(-1);
            }

            /*
            * 下敷き画像があれば読む
             */
            str = bufferedReader.readLine();
            if(!str.equals("null")){
                Image img = new Image(str);
                image_b.setText("100.0%");
                imageLayer.DrawImageNormal(img, 0, 0);
            }


            while(str != null){
                switch (str){
                    case "{":
                        section_count++;
                        break;
                    case "}":
                        section_count--;
                        break;
                    default:
                    {
                        StringTokenizer tokenizer = new StringTokenizer(str);
                        while(tokenizer.hasMoreTokens()) {
                            String term = tokenizer.nextToken();
                            if(term.equals("LayerName")){
                                term = tokenizer.nextToken();
                                Main.addLayer(term, listview);
                            }else if(term.equals("dots")){
                                str = bufferedReader.readLine();
                                while(!str.equals("lines")){
                                    StringTokenizer dots_tokens = new StringTokenizer(str);
                                    Main.CurrentLayerData.AddDot(new Dot(Integer.valueOf(dots_tokens.nextToken()), Integer.valueOf(dots_tokens.nextToken())));
                                    str = bufferedReader.readLine();
                                }

                                str = bufferedReader.readLine();
                                while(!str.equals("}")){
                                    StringTokenizer lines_tokens = new StringTokenizer(str);
                                    Dot begin_dot = new Dot(Integer.valueOf(lines_tokens.nextToken()), Integer.valueOf(lines_tokens.nextToken()));
                                    for(Dot dot : Main.CurrentLayerData.getDotList()){
                                        if(!dot.equals(begin_dot))
                                            continue;

                                        Dot end_dot = new Dot(Integer.valueOf(lines_tokens.nextToken()), Integer.valueOf(lines_tokens.nextToken()));
                                        for(Dot finding_connected : Main.CurrentLayerData.getDotList()){
                                            if(finding_connected.equals(end_dot)){
                                                dot.Connect(finding_connected);
                                                break;
                                            }
                                        }
                                        break;

                                    }
                                    str = bufferedReader.readLine();
                                }

                                Main.CurrentLayerData.AllDraw(front, lines);
                                section_count--;
                            }
                        }
                    }

                }
                str = bufferedReader.readLine();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static String GetFilePath(Stage stage) {

        chooser.setTitle("File select");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Yuri Face Markup Language", "*.yfml"));
        File file = chooser.showOpenDialog(stage);

        if(file != null) {
            return file.getPath().toString();
        }else{
            return null;
        }
    }
}
