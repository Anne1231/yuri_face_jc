package UI;

import Layers.FrontDotLayer;
import Layers.Layer;
import Layers.LinesLayer;
import javafx.scene.paint.Color;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Akihiro on 2017/02/26.
 */

public class LayerData {
    public enum LayerDataType {
        NullNull,
        FaceBase,
        LeftEye,
        RightEye,
        LeftEyebrows,
        RightEyebrows,
        Mouth;
        public static String ToString(LayerDataType type){
            switch (type){
                case NullNull:
                    return "null";
                case FaceBase:
                    return "FaceBase";
                case LeftEye:
                    return "LeftEye";
                case RightEye:
                    return "RightEey";
                case LeftEyebrows:
                    return "LeftEyebrows";
                case RightEyebrows:
                    return "RightEyebrows";
                case Mouth:
                    return "Mouth";
                default:
                    return "null";
            }
        }

        public static LayerDataType ToType(String type_name){
            switch (type_name){
                case "FaceBase":
                    return FaceBase;
                case "LeftEye":
                    return LeftEye;
                case "RightEye":
                    return RightEye;
                case "LeftEyebrows":
                    return LeftEyebrows;
                case "RightEyebrows":
                    return RightEyebrows;
                case "Mouth":
                    return Mouth;
                default:
                    return NullNull;
            }
        }
    }

    private LayerDataType type;
    private String name;
    private HashSet<Dot> dot_set;
    private ArrayList<Line> line_list;

    public LayerData(){
        dot_set = new HashSet<>();
        line_list = new ArrayList<>();
        name = new String();
    }

    public LayerData(String layer_name, LayerDataType type){
        dot_set = new HashSet<>();
        line_list = new ArrayList<>();
        name = layer_name;
        this.type = type;
    }

    public LayerData clone(){
        LayerData layerData = new LayerData(this.name, this.type);
        dot_set.forEach(dot -> layerData.dot_set.add(dot.clone()));
        line_list.forEach(line -> layerData.connect(line));
        return layerData;
    }

    public void AddDot(Dot dot){
        dot_set.add(dot);
    }

    public HashSet<Dot> getDotSet() { return dot_set; }

    public String getName() {
        return name;
    }

    public void AllDraw(Layer front, Layer lines){
        line_list.forEach(line -> line.Draw(lines, 0.5, Color.BLACK));
        dot_set.forEach(dot -> dot.Draw(front, Color.BLACK));
    }

    public void DrawAllLines(LinesLayer layer){
        line_list.forEach(line -> line.Draw(layer, 0.5, Color.BLACK));
    }

    public void RemoveDot(Dot select_dot){
        short[] memo = new short[UIValues.MAX__CONNECTION];
        java.util.Arrays.fill(memo, (short)(-1));

        short i, index;
        dot_set.remove(select_dot);

        memo[0] = -1;

        i = 0;
        index = 0;
        for(Line line : line_list){
            if(line.contains(select_dot)){
                memo[index] = i;
                index++;
            }
            i++;
        }
        for(i = 0;memo[i] != -1;i++){
            line_list.remove(memo[i]);
        }
    }

    public Line connect(Dot begin, Dot end){
        Line line = new Line(begin, end);
        line_list.add(line);
        return line;
    }

    public void connect(Line line){
        line_list.add(line);
    }

    public void MoveDot(Dot select_dot, Dot update_dot){

        line_list.forEach(line -> line.exchange(select_dot, update_dot));

        dot_set.remove(select_dot);
        dot_set.add(update_dot);

    }

    public LayerDataType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(LayerDataType type) {
        this.type = type;
    }

    public ArrayList<Line> getLineList() {
        return line_list;
    }


    public void Organize(){
        /*
        ArrayList<Dot> new_dots = new ArrayList<>();

        int size = this.dots.size();
        Dot ref;

        ref = dots.get(1);

        flag : for(int i = 1;i < size;i++){

            new_dots.add(ref);

            sub_loop1 : for(Dot dot : ref.getConnected_dots()) {
                for(Point2i p : new_dots){
                    if (p.getX() == dot.getX() && p.getY() == dot.getY()) {
                        continue sub_loop1;
                    }
                }
                ref = dot;
                continue flag;
            }
            Dot[] connect = organize_sub_find_conect(ref);
            sub_loop2 : for(int n = 0;n < 2;n++){
                for(Point2i p : new_dots){
                    if (p.getX() == connect[n].getX() && p.getY() == connect[n].getY()) {
                        continue sub_loop2;
                    }
                }
                ref = connect[n];
                break;
            }
        }

        for(Dot dot : dots){
            if(!new_dots.contains(dot)) {
                new_dots.add(dot);
                break;
            }
        }

        this.dots = null;
        this.dots = new_dots;
        */
    }

    public Dot[] organize_sub_find_conect(Dot finding){
        /*
        char i = 0;
        Dot[] result = new Dot[2];
        result[0] = result[1] = null;
        for(Dot p : dots){
            for(Dot connect_p : p.getConnected_dots()){
                if(finding.equals(connect_p)){
                    result[i] = p;
                    i++;
                    break;
                }
            }
        }
        return result;
    */
        return null;
    }


}
