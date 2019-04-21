public class Brute
{
   
   
    private static void examine(Point[] points){
        int N = points.length;
        for(int i=0;i<N;i++){
            for(int k=i+1;k<N;k++)
            {
                for(int l = k+1; l<N;l++)
                {
                    for(int m =l+ 1; m<N;m++)
                    {
                      Point p = points[i];  
                      Point q = points[k];  
                      Point r = points[l];
                      Point s = points[m];
                      
                      if(p.slopeTo(q) == p.slopeTo(s)&&p.slopeTo(q) == p.slopeTo(r)&&p.slopeTo(q) == p.slopeTo(s))
                      {
                          StdOut.println("p->"+p + " q->"+q + " r->"+r + " s->"+s);
                          p.drawTo(s);
                          q.drawTo(r);
                          r.drawTo(s);
                          
                      }
                     }
                    
                }
            }
        
        }
    }
    
    
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
  
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points[i] = p;
            p.draw();
        }
        
       examine(points);
        
             // display to screen all at once
        StdDraw.show(0);
    }
}

