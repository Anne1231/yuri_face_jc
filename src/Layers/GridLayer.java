package Layers;

/**
 * Created by Akihiro on 2017/02/27.
 */
public class GridLayer extends Layer {
    private int interval;
    private boolean enableComplete;

    public GridLayer(double width, double height, int interval){
        super(width, height);
        this.interval = interval;
        enableComplete = false;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void ConfigCompletion(boolean status){
        enableComplete = status;
    }

    public boolean isEnableComplete() {
        return enableComplete;
    }
}
