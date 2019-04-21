//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by zora on 2/20/16.
 */


public class BruteCollinearPoints {
    //private final Point[] points;
    private int numLineSegments = 0;
    private LineSegment[] linePoints = new LineSegment[10];

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException("Empty Array");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException("Empty Item");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException("Repeated Point");
            }
        }

        Point[] endPoints = new Point[2];
        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                endPoints[0] = less(points[p], points[q]) ? points[p] : points[q];
                endPoints[1] = less(points[q], points[p]) ? points[p] : points[q];
                for (int r = q + 1; r < points.length - 1; r++) {
                    if (notEqual(points[p], points[q], points[r])) continue;
                    //if (Less(points[r], endPoint1)) endPoint1 = points[r];
                    endPoints[0] = less(points[r], endPoints[0]) ? points[r] : endPoints[0];
                    endPoints[1] = less(endPoints[1], points[r]) ? points[r] : endPoints[1];
                    for (int s = r + 1; s < points.length; s++) {
                        if (notEqual(points[p], points[q], points[s])) continue;
                        endPoints[0] = less(points[s], endPoints[0]) ? points[s] : endPoints[0];
                        endPoints[1] = less(endPoints[1], points[s]) ? points[s] : endPoints[1];
                        linePoints[numLineSegments++] = new LineSegment(endPoints[0], endPoints[1]);
                        // checkLine(points[p], points[q], points[r], points[s]);
                        if (numLineSegments == linePoints.length) resize(linePoints.length);
                    }
                }
            }
        }
    }   // finds all line segments containing 4 points

    public int numberOfSegments() {
        return numLineSegments;
    }        // the number of line segments
    public LineSegment[] segments() {
        return Arrays.copyOfRange(linePoints, 0, numberOfSegments());
    }               // the line segments

    private boolean notEqual(Point a, Point b, Point c) {
        //return (a.slopeOrder().compare(b, c) != +0.00 || a.slopeOrder().compare(b, c) != -0.00); //********//
        return (a.slopeOrder().compare(b, c) != 0);
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

    private void checkLine(Point p, Point q, Point r, Point s) {
        StdOut.println(numLineSegments);
        StdOut.println(p);
        StdOut.println(q);
        StdOut.println(r);
        StdOut.println(s);
    }

    public static void main(String[] args) {
        // read the N points from a file
        /*
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        */
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
