/**
 * Created by zora on 2/20/16.
 */
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private Point[] orderedPoints;
    private Point[] slopesToOrigin;
    private int numLineSegments;
    private LineSegment[] linePoints;


    private void mergePoint(Point[] array, int lo, int hi){
        Point[] aux = new Point[hi - lo + 1];
        int mid = (lo + hi) / 2;
        int i = lo, j = mid + 1;
        for (int k = 0; k < aux.length; k++) {
            if (i == mid + 1) aux[k] = array[j++];
            else if (j == hi) aux[k] = array[i++];
            else if (array[i].compareTo(array[j]) <= 0) aux[k] = array[i++];
            else aux[k] = array[j++];
        }
        for (int k = lo; k <= hi; k++) array[k] = aux[k];
    }

    private void mergeSlope(Point[] array, Point ref, int lo, int hi){
        Point[] aux = new Point[hi - lo + 1];
        int mid = (lo + hi) / 2;
        int i = lo, j = mid + 1;
        for (int k = 0; k < aux.length; k++) {
            if (i == mid + 1) aux[k] = array[j++];
            else if (j == hi) aux[k] = array[i++];
            else if (ref.slopeOrder().compare(array[i], array[j]) <= 0) aux[k] = array[i++];
            else aux[k] = array[j++];
        }
        for (int k = lo; k <= hi; k++) array[k] = aux[k];
    }

    private void sort(Point[] array, Point ref, int lo, int hi, int dummy){
        if (array.length == 1) return;
        int mid = (lo + hi) / 2;
        sort(array, ref, lo, mid, dummy);
        sort(array, ref, mid + 1, hi, dummy);
        if (dummy == 0) mergePoint(array, lo, hi);
        else mergeSlope(array, ref, lo, hi);

    }


    private void Resize(int capacity) {
        LineSegment[] tempPoints = new LineSegment[capacity * 2];
        for (int i = 0; i < capacity; i++) {
            tempPoints[i] = linePoints[i];
        }
        linePoints = tempPoints;
    }

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException ("Empty Array");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException ("Empty Array");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException ("Repeated Point");
            }
        }

        numLineSegments = 0;
        linePoints = new LineSegment[10];
        orderedPoints = new Point[points.length];
        // slopesToOrigin = new Point[orderedPoints.length];
        // slopesToOrigin.clear();
        Arrays.sort(orderedPoints);
        //sort(orderedPoints, orderedPoints[0], 0, orderedPoints.length - 1, 0);
        for (int i = 0; i < orderedPoints.length - 4; i++) {
            slopesToOrigin = new Point[orderedPoints.length - i - 1];
            for (int j = i + 1; j < orderedPoints.length; j++) {
                slopesToOrigin[j - i - 1] = orderedPoints[j];
            }
            sort(slopesToOrigin, orderedPoints[i], 0, slopesToOrigin.length - 1, 1);
            int k = 1;
            for (int j = 1; j < slopesToOrigin.length; j++) {
                if (slopesToOrigin[j] == slopesToOrigin[j - 1]) k++;
                else {
                    if (k >= 4) {
                        linePoints[numLineSegments++] = new LineSegment(orderedPoints[i], slopesToOrigin[j - 1]);
                        if (numLineSegments == linePoints.length) Resize(linePoints.length);
                    }
                    k = 1;
                }
            }
        }
    }    // finds all line segments containing 4 or more points
    public int numberOfSegments() {
        return numLineSegments;
    }       // the number of line segments
    public LineSegment[] segments() {
        return linePoints;
    }               // the line segments

    public static void main(String[] args) {}
}
