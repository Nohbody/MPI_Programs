/**
 * Created by zora on 2/20/16.
 */
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] orderedPoints;
    private Point[] slopesToOrigin;
    private int numLineSegments;
    private LineSegment[] linePoints;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException("Empty Array");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException("Empty Array");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException("Repeated Point");
            }
        }

        numLineSegments = 0;
        linePoints = new LineSegment[10];
        orderedPoints = points.clone();
        // slopesToOrigin = new Point[orderedPoints.length];
        // slopesToOrigin.clear();
        Arrays.sort(orderedPoints);
        for (int i = 0; i < orderedPoints.length - 3; i++) {
            slopesToOrigin = new Point[orderedPoints.length - i - 1]; //*********//
            for (int j = i + 1; j < orderedPoints.length; j++) {
                slopesToOrigin[j - i - 1] = orderedPoints[j];
            }
            Arrays.sort(slopesToOrigin, orderedPoints[i].slopeOrder());
            //checkSorted(orderedPoints[i], slopesToOrigin);
            int k = 1;
            for (int j = 1; j < slopesToOrigin.length; j++) {
                if (orderedPoints[i].slopeOrder().compare(slopesToOrigin[j - 1], slopesToOrigin[j]) == 0) k++;
                else {
                    if (k >= 3) addLine(i, j, k);
                    k = 1;
                }
            }
            if (k >= 3) addLine(i, slopesToOrigin.length, k);
        }
    }    // finds all line segments containing 4 or more points
    public int numberOfSegments() {
        return numLineSegments;
    }       // the number of line segments
    public LineSegment[] segments() {
        return Arrays.copyOfRange(linePoints, 0, numberOfSegments());
    }               // the line segments

    private void addLine(int i, int j, int k) {
        //checkLine(orderedPoints[i], Arrays.copyOfRange(slopesToOrigin, j - k, j - 1));
        Point endPoint = less(orderedPoints[i], slopesToOrigin[j - k]) ? orderedPoints[i] : slopesToOrigin[j - k];
        linePoints[numLineSegments++] = new LineSegment(endPoint, slopesToOrigin[j - 1]);
        if (numLineSegments == linePoints.length) resize(linePoints.length);
    }
    private boolean less(Point a, Point b) {
        return (a.compareTo(b) < 0);
    }

    private void resize(int capacity) {
        LineSegment[] tempPoints = new LineSegment[capacity * 2];
        for (int i = 0; i < capacity; i++) {
            tempPoints[i] = linePoints[i];
        }
        linePoints = tempPoints;
    }

    private void checkSorted(Point a, Point[] array) {
        StdOut.printf(a+":\n");
        for (int i = 0; i < array.length; i++) {
            StdOut.println(array[i]);
            StdOut.println(a.slopeTo(array[i]));
        }

    }

    private void checkLine(Point a, Point[] array) {
        StdOut.println(numLineSegments);
        StdOut.println(a);
        for (int i = 0; i < array.length; i++) StdOut.println(array[i]);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = Integer.parseInt(args[(i + 1) * 2 - 1]);
            int y = Integer.parseInt(args[(i + 1) * 2]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.numberOfSegments());
    }
}