package UI;

/**
 * Created by Akihiro on 2017/02/26.
 */
public class Point2i {
    protected int x;
    protected int y;

    public Point2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point2i(){
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Point2i p) {
        return ((p.x == this.x) && (p.y == this.y));
    }

    public Point2i clone(){
        return new Point2i(this.x, this.y);
    }

    public double distance(Point2i p){
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }
}
