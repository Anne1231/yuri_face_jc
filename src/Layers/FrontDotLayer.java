package Layers;

import UI.Dot;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class FrontDotLayer extends Layer {
    private boolean selecting;
    private Dot last;

    public FrontDotLayer(double width, double height){
        super(width, height);
        selecting = false;
        last = null;
    }

    public boolean isSelecting() {
        return this.selecting;
    }

    public void Selecting(boolean status){
        this.selecting = status;
    }

    public void setLast(Dot dot){
        last = dot;
    }

    public Dot getLast() {
        return last;
    }

    public boolean isLastEmpty(){
        return last == null;
    }
}
