import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 2/20/16.
 */


public class BruteCollinearPoints {
    //private final Point[] points;
    private int numLineSegments = 0;
    private LineSegment[] linePoints = new LineSegment[10];

    private boolean NotEqual(Point a, Point b, Point c) {
        return (a.slopeOrder().compare(b, c) != +0.00 || a.slopeOrder().compare(b, c) != -0.00); //********//
    }

    private boolean Less(Point a, Point b) {
        return (a.compareTo(b) < 0);
    }

    private void Resize(int capacity) {
        LineSegment[] tempPoints = new LineSegment[capacity * 2];
        for (int i = 0; i < capacity; i++) {
            tempPoints[i] = linePoints[i];
        }
        linePoints = tempPoints;
    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException ("Empty Array");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException ("Empty Array");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException ("Repeated Point");
            }
        }

        Point[] endPoints = new Point[2];
        for (int p = 0; p < points.length - 3; p++) {
            endPoints[0] = points[p];
            for (int q = p + 1; q < points.length - 2; q++) {
                endPoints[1] = points[q];
                for (int r = q + 1; r < points.length - 1; r++) {
                    if (NotEqual(points[p], points[q], points[r])) continue;
                    //if (Less(points[r], endPoint1)) endPoint1 = points[r];
                    endPoints[0] = Less(points[r], endPoints[0])? points[r]: endPoints[0];
                    endPoints[1] = Less(endPoints[1], points[r])? points[r]: endPoints[1];
                    for (int s = r + 1; s < points.length; s++) {
                        if (NotEqual(points[p], points[q], points[s])) continue;
                        endPoints[0] = Less(points[s], endPoints[0])? points[s]: endPoints[0];
                        endPoints[1] = Less(endPoints[1], points[s])? points[s]: endPoints[1];
                        linePoints[numLineSegments++] = new LineSegment(endPoints[0], endPoints[1]);
                        if (numLineSegments == linePoints.length) Resize(linePoints.length);
                    }
                }
            }
        }
    }   // finds all line segments containing 4 points

    public int numberOfSegments() {
        return numLineSegments;
    }        // the number of line segments
    public LineSegment[] segments() {
        return linePoints;
    }               // the line segments

    public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

    }
}
