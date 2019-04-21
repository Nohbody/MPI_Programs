/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
   
    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

   
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>(){
        public int compare(Point point, Point point2){
                       
            if(point2.slopeTo(point) < point.slopeTo(point2)) return -1;
            if(point.y == point2.y && point.x<point2.x) return -1;
            if(point.x==point2.x && point.y == point2.y) return 0;   
            return 0;
        };
    };
    
    
    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

// slope between this point and that point
 public double slopeTo(Point that) {
  if (this.x == that.x && this.y == that.y) { // Degenerate
   return Double.NEGATIVE_INFINITY;
  }

  if (this.x == that.x) { // Vertical
   return Double.POSITIVE_INFINITY;
  }

  if (this.y == that.y) { // Horizontal
   return new Double(0);
  }

  return ((that.y - this.y) * 1D) / (that.x - this.x);
 }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
      
        //y0 < y1 or if y0 = y1 and x0 < x1.
        if(this.y < that.y) return -1;
        if(this.y == that.y && this.x<that.x) return -1;
        if(this.x==that.x && this.y == that.y) return 0;
        return 1;
        
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p = new Point(14000, 10000);
        Point p1 = new Point(18000, 10000);
        Point p2 = new Point(19000, 10000);
        Point p3 = new Point(21000, 10000);
        p.draw();
        p.drawTo(p1);
        p1.draw();
        p1.drawTo(p2);
        p2.draw();
        p2.drawTo(p3);
        p3.draw();
        
        Point p5 = new Point(-10,-10);
        p5.draw();
        p5.drawTo(p1);
        //slope from p to p1 should be the same as p and p2 or p1 to p2
        assert(p.slopeTo(p1) == p.slopeTo(p2)):"Not Equal"; 
        
        
        
    }
}