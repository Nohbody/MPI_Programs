/**
 * Created by zora on 2/20/16.
 */
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point{
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that) {
        if (y < that.y) {
            return -1;
        }
        else if (y > that.y) {
            return 1;
        }
        else {
            if (x < that.x) {
                return -1;
            }
            else if (x > that.x) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }    // compare two points by y-coordinates, breaking ties by x-coordinates

    public double slopeTo(Point that) {
        double a = 1.0;
        if (x == that.x) {
            if (y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            else {
                return Double.POSITIVE_INFINITY;
            }
        }
        else return ((that.y - y) * a / (that.x - x));
    }      // the slope between this point and that point

    public Comparator<Point> slopeOrder() {
        Comparator<Point> BY_SLOPE = new CompareSlope();
        return BY_SLOPE;
    }             // compare two points by slopes they make with this point

    private class CompareSlope implements Comparator<Point> {
        public int compare(Point a, Point b) {
            if (Point.this.slopeTo(a) < Point.this.slopeTo(b)) {
                return -1;
            }
            else if (Point.this.slopeTo(a) < Point.this.slopeTo(b)) {
                return 1;
            }
            else return 0;
        }
    }

    public static void main(String[] args) {

    }

}
