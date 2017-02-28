package Layers;

import UI.Dot;

/**
 * Created by Akihiro on 2017/03/01.
 */
public class FrontDotLayer extends Layer {
    private boolean selecting;
    
    public FrontDotLayer(double width, double height){
        super(width, height);
        selecting = false;
    }

    public boolean isSelecting() {
        return this.selecting;
    }

    public void Selecting(boolean status){
        this.selecting = status;
    }

}
