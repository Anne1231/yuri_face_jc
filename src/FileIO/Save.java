package FileIO;

import UI.Dot;
import UI.LayerData;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class Save {

    private static FileChooser saver = new FileChooser();

    public static void save_to_file(ArrayList<LayerData> data, Stage stage){
        String file_name = GetSavePath(stage);
        if(file_name == null)
            return;

        try {
            File file = new File(file_name);
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println("yfml");
            printWriter.println("{");
            for (LayerData layerData : data) {
                printWriter.println("LayerName " + layerData.getName());
                printWriter.println("{");
                printWriter.println("dots");
                for(Dot dot : layerData.getDotList()){
                    printWriter.println(dot.getX() + " " + dot.getY());
                }
                printWriter.println("lines");
                for(Dot dot : layerData.getDotList()){
                    for(Dot connected : dot.getConnected_dots()){
                        printWriter.println(dot.getX() +  " " + dot.getY() + " " + connected.getX() + " " + connected.getY());
                    }
                }

                printWriter.println("}");
            }
            printWriter.println("}");
            printWriter.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static String GetSavePath(Stage stage) {

        saver.setTitle("保存");
        saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("Yuri Face Markup Language", "*.yfml"));
        File importFile = saver.showSaveDialog(stage);
        if(importFile != null) {
            String path = importFile.getPath().toString();
            return path;
        }
        return null;
    }
}
