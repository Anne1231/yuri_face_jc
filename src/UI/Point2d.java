package UI;


/**
 * Created by Akihiro on 2017/02/25.
 */
public class Point2d {
    protected double x;
    protected double y;

    public Point2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point2d(){
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(Point2d p) {
        return ((p.x == this.x) && (p.y == this.y));
    }

    public Point2d clone(){
        return new Point2d(this.x, this.y);
    }
}
