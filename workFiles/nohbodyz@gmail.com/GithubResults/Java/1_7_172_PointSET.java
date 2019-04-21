import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 3/5/16.
 */
public class PointSET {
    private SET<Point2D> pointSet;

    public PointSET() {
        pointSet = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return (pointSet.size() == 0);
    }                     // is the set empty?

    public int size() {
        return pointSet.size();
    }                        // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        if (!contains(p)) {
            pointSet.add(p);
        }
    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        return pointSet.contains(p);
    }           // does the set contain point p?

    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException("Value is Null");
        SET<Point2D> rectPointSet = new SET<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                rectPointSet.add(point);
            }
        }
        return rectPointSet;
    }            // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        if (pointSet.isEmpty()) return null;
        double min = pointSet.min().distanceTo(p);
        Point2D minPoint = pointSet.min();
        for (Point2D point : pointSet) {
            if (point.distanceTo(p) < min) {
                min = point.distanceTo(p);
                minPoint = point;
            }
        }
        return minPoint;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        RectHV rect = new RectHV(0, 0, 0.6, 0.6);
        PointSET pointSet = new PointSET();
        Point2D[] points = new Point2D[5];
        points[0] = new Point2D(0.1, 0.3);
        points[1] = new Point2D(0.3, 0.3);
        points[2] = new Point2D(0.5, 0.5);
        points[3] = new Point2D(0.7, 0.8);
        points[4] = new Point2D(0.8, 0.8);
        Point2D testPoint = new Point2D(0.2, 0.5);
        for (int i = 0; i < 5; i++) {
            pointSet.insert(points[i]);
        }
        StdOut.println(pointSet.range(rect));
        StdOut.println(pointSet.nearest(points[3]));
        StdOut.println(pointSet.nearest(testPoint));
        // pointSet.draw();
    }                 // unit testing of the methods (optional)
}
