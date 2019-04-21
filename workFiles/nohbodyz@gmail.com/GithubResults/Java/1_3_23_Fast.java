/*
 Think of p as the origin.
 For each other point q, determine the slope it makes with p.
  
*/
import java.util.Comparator;

public class Fast
{
    private static void swap(int el1, int el2, Object[] points)
    {
       Object a = points[el1];
       Object b = points[el2];
        points[el1] = b;
        points[el2] = a;      
        
    }
    
    private static void partition(Object[] points, Comparator comparator, int start, int end){
        int size = end -start;
        if(size<2)return;
        //choosing a pivot
      
        Object p = points[start];
        int L=start;
        int U = end;
       
        while(L<U)
        {
           while(comparator.compare(points[L], p)>0){
                L++;
           }
        
            while(comparator.compare(points[L], p)<0){

                    U--;
       

           }        
           //swap
           swap(L,U,points);
           partition(points,comparator, start, L-1);
           partition(points,comparator, L+1, end);
            
        }
    }
    
    private static boolean iscollinear(Point a, Point b, Point c){
        double slope1 = a.slopeTo(b);
        double slope2 = a.slopeTo(c);
        double slope3 = b.slopeTo(c);
        if(slope1==slope2&&slope1==slope3){
            return true;
        }
        return false;
    }
    
    
    private static void examine(Point[] points){
      //get point 0
        Point p0 = points[0];
        java.util.Arrays.sort(points, p0.SLOPE_ORDER);
        //get sorted arrau
        for(int a=0;a<points.length;a++){
           for(int b=a+1; b<points.length;b++)
           {
               for(int c=b+1; c<points.length;c++)
               {
                   Point a1 = points[a];
                   Point b1 = points[b];
                   Point c1 = points[c];
                   
                   if(iscollinear(a1,b1,c1)){
                        StdOut.println("a->"+a1 + " b->"+b1 + " c->"+c1);
                          a1.drawTo(b1);
                          b1.drawTo(c1);
                          a1.drawTo(c1);
                   }
               }
           
           }
        
        }
        
       //Sort the points according to the slopes they makes with p. 
        //Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.   
    }
    
     public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        
      
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Integer k[] =new Integer[N];
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            k[i] = x;
            Point p = new Point(x, y);
            points[i] = p;
            p.draw();
        }
        
       examine(points);
    
        StdDraw.show();
       
    }
}