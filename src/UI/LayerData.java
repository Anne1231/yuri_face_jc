package UI;

import Layers.FrontDotLayer;
import Layers.Layer;
import Layers.LinesLayer;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

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
    private ArrayList<Polygon> polygons;

    public LayerData(){
        dot_set = new HashSet<>();
        line_list = new ArrayList<>();
        name = new String();
        polygons = new ArrayList<>();
    }

    public LayerData(String layer_name, LayerDataType type){
        dot_set = new HashSet<>();
        line_list = new ArrayList<>();
        name = layer_name;
        this.type = type;
        polygons = new ArrayList<>();
    }

    public LayerData clone(){
        LayerData layerData = new LayerData(this.name, this.type);
        dot_set.forEach(dot -> layerData.dot_set.add(dot.clone()));
        line_list.forEach(line -> layerData.connect(line.clone()));
        return layerData;
    }

    public void AddDot(Dot dot){
        dot_set.add(dot);
    }

    public HashSet<Dot> getDotSet() { return dot_set; }

    public String getName() {
        return name;
    }

    public void AllDraw4PR(Layer front, Layer lines){
        line_list.forEach(line -> line.Draw(lines, 0.5, Color.BLACK));
        dot_set.forEach(dot -> dot.Draw(front, Color.BLACK));
    }

    public void AllDraw4N(Layer front, Layer lines){
        line_list.forEach(line -> line.Draw(lines, 0.5, Color.BLACK));
        dot_set.forEach(dot -> dot.Draw(front, Color.BLACK));

        polygons.forEach(polygon -> {
            for(int i = 0;i < polygon.size();i++){
                front.getGraphicsContext().clearRect(polygon.getX(i) - 3, polygon.getY(i) - 3, 11, 11);
                {
                    front.getGraphicsContext().setFill(polygon.getDotColor());
                    front.getGraphicsContext().setStroke(polygon.getDotColor());
                    front.getGraphicsContext().setLineWidth(UIValues.DOT_CIRCLE_WIDTH);
                    front.getGraphicsContext().fillOval(polygon.getX(i), polygon.getY(i), 4, 4);
                    front.getGraphicsContext().strokeOval(polygon.getX(i) - 3, polygon.getY(i) - 3, 10, 10);
                }
            }
        });

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


    public ArrayList<Dot> CreatePolygon(){
        ArrayList<Line> memo = new ArrayList<>();
        this.line_list.forEach(line -> memo.add(line));
        ArrayList<Dot> polygon = new ArrayList<>();
        Line line = memo.get(0);
        Point2i next;
        int index;


        for(int i = 0;i < line_list.size();i++){

            polygon.add(new Dot(line.getBegin().getX(), line.getBegin().getY()));
            next = line.getEnd();

            index = memo.indexOf(line);
            if(index != -1)
                memo.remove(index);

            for(Line loop_line : memo){
                if(loop_line.contains(next)){
                    line = loop_line;
                }
            }
        }

        return polygon;
    }

    public ArrayList<Dot> CreateSubPolygon(Rectangle2D rectangle2D){
        ArrayList<Line> memo = new ArrayList<>();
        this.line_list
                .stream()
                .parallel()
                .filter(line ->
                        rectangle2D.contains(new Point2D(line.getBegin().getX(), line.getBegin().getY()))
                        || rectangle2D.contains(new Point2D(line.getEnd().getX(), line.getEnd().getY()))
                )
                .forEach(line -> memo.add(line));

        ArrayList<Dot> polygon = new ArrayList<>();
        Line line = memo.get(0);
        Point2i next;
        int index;


        for(int i = 0;i < line_list.size();i++){

            polygon.add(new Dot(line.getBegin().getX(), line.getBegin().getY()));
            next = line.getEnd();

            index = memo.indexOf(line);
            if(index != -1)
                memo.remove(index);

            for(Line loop_line : memo){
                if(loop_line.contains(next)){
                    line = loop_line;
                }
            }
        }

        return polygon;
    }

    public void addPolygon(Polygon polygon){
        polygons.add(polygon);
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }
}
