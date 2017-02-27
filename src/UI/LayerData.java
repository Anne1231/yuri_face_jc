package UI;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class LayerData {

    String name;
    ArrayList<Dot> dots;

    public LayerData(){
        dots = new ArrayList<>();
    }

    public LayerData(String layer_name){
        dots = new ArrayList<>();
        name = layer_name;
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
}
