package UI;

import Layers.Layer;
import javafx.scene.paint.Color;

/**
 * Created by Akihiro on 2017/03/03.
 */
public class Line {
    private Point2i begin;
    private Point2i end;

    public Line(Point2i begin, Point2i end){
        this.begin = begin.clone();
        this.end = end.clone();
    }

    public void Draw(Layer layer, double width, Color color){
        layer.getGraphicsContext().setLineWidth(width);
        layer.getGraphicsContext().setStroke(color);
        layer.getGraphicsContext().strokeLine(begin.getX(), begin.getY(), end.getX(), end.getY());
    }

    public boolean contains(Point2i p){
        if(begin.equals(p) || end.equals(p))
            return true;
        else
            return false;
    }

    public void exchange(Point2i target, Point2i new_p){
        if(begin.equals(target)) {
            begin.setX(new_p.getX());
            begin.setY(new_p.getY());
        }else if(end.equals(target)){
            end.setX(new_p.getX());
            end.setY(new_p.getY());
        }
    }

    public Line clone(){
        return new Line(this.begin.clone(), this.end.clone());
    }

    public Point2i getBegin() {
        return begin;
    }

    public Point2i getEnd() {
        return end;
    }

    public boolean equals(Line line) {
        return (line.getBegin().equals(this.getBegin()) && line.getBegin().equals(this.getEnd()))
                ||
                (line.getBegin().equals(this.getEnd()) && line.getBegin().equals(this.getBegin()));
    }
}
