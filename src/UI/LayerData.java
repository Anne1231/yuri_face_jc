package UI;

import Layers.FrontDotLayer;
import Layers.Layer;
import Layers.LinesLayer;
import javafx.scene.paint.Color;
import org.opencv.core.Point;

import java.util.ArrayList;

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
    private ArrayList<Dot> dots;

    public LayerData(){
        dots = new ArrayList<>();
        name = new String();
    }

    public LayerData(String layer_name, LayerDataType type){
        dots = new ArrayList<>();
        name = layer_name;
        this.type = type;
    }

    public void AddDot(Dot dot){
        dots.add(dot);
    }

    public ArrayList<Dot> getDotList(){
        return dots;
    }

    public String getName() {
        return name;
    }

    public void AllDraw(Layer front, Layer lines){
        for(Dot dot : dots){
            dot.Draw(front, Color.BLACK);
            for(Dot connected : dot.getConnected_dots()){
                lines.getGraphicsContext().setLineWidth(0.5);
                lines.getGraphicsContext().setStroke(Color.BLACK);
                lines.getGraphicsContext().strokeLine(dot.getX(), dot.getY(), connected.getX(), connected.getY());
            }
        }
    }

    public void DrawAllLines(LinesLayer layer){
        for(Dot dot : dots){
            for(Dot connected : dot.getConnected_dots()){
                layer.getGraphicsContext().setLineWidth(0.5);
                layer.getGraphicsContext().setStroke(Color.BLACK);
                layer.getGraphicsContext().strokeLine(dot.getX(), dot.getY(), connected.getX(), connected.getY());
            }
        }
    }

    public void RemoveDot(Dot select_dot){
        Dot will_remove = new Dot(select_dot.getX(), select_dot.getY());
        short[] memo = new short[UIValues.MAX__CONNECTION];
        java.util.Arrays.fill(memo, (short)(-1));

        short i = 0, index;

        for(Dot dot : this.dots){
            if(dot.equals(will_remove)){
                memo[0] = i;
                break;
            }
            i++;
        }

        this.dots.remove(memo[0]);
        memo[0] = -1;

        for(Dot dot : this.dots){
            i = 0;
            index = 0;
            for(Dot connected : dot.getConnected_dots()){
                if(connected.equals(will_remove)){
                    memo[index] = i;
                    index++;
                }
                i++;
            }
            for(i = 0;memo[i] != -1;i++){
                dot.getConnected_dots().remove(memo[i]);
            }
            java.util.Arrays.fill(memo, (short)(-1));
        }
    }

    public void MoveDot(Dot select_dot, Dot update_dot){

        for(Dot dot : this.dots){
            for(Dot connected : dot.getConnected_dots()){
                if(connected.equals(select_dot)){
                    connected.setX(update_dot.getX());
                    connected.setY(update_dot.getY());
                }
            }
        }

        for(Dot dot : this.dots){
            if(dot.equals(select_dot)){
                dot.setX(update_dot.getX());
                dot.setY(update_dot.getY());
                break;
            }
        }
    }

    public LayerDataType getType() {
        return type;
    }

    public ArrayList<Dot> getDots() {
        return dots;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(LayerDataType type) {
        this.type = type;
    }

    public void Organize(){
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
    }

    public Dot[] organize_sub_find_conect(Dot finding){
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
    }

}
