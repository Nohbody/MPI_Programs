import java.awt.*;

public class SeamCarver {
   private Picture picture;
   private boolean dfsmatrix[][];
   private EdgeWeightedDigraph digraph;
   
   
   
   public SeamCarver(Picture _picture){
       picture = _picture;
       dfsmatrix = new boolean[this.height()][this.width()];
      //create digraph
       createDigraph();
   }
   
   private int flattenPoint(int x, int y){
       int V = x *this.width() + y;
       return V;
   }
   
   private void createDigraph(){
       
       double matrix[][] =new double [this.height()][this.width()];
      
       for(int i=0; i<this.height();i++)
       {      
           double row[] = new double[this.width()];
           for(int j=0;j<this.width();j++)
           {
        
             row[j] = energy(j,i); //energy of column x and row y
            }
           matrix[i] = row; 
       }
       
       digraph = new EdgeWeightedDigraph(this.width() * this.height());
          for(int i=0; i<this.height();i++)
       {    for(int j=0;j<this.width();j++)
           {
              //get a point
              int V = flattenPoint(i,j);
              //get adjacent points
               DirectedEdge de =null;
              if(j-1>=0&&i+1<=this.height())
              { 
                  int V1 = flattenPoint(i+1,j-1);
                  de = new DirectedEdge(V,V1,energy(i+1,j-1));                  
              }
              if(i+1<=this.height())
              { 
                  int V1 = flattenPoint(i+1,j);
                  de = new DirectedEdge(V,V1,energy(i+1,j));                                    
              }
               if(j+1<=this.width()&&i+1<=this.height())
              { 
                  int V1 = flattenPoint(i+1,j-1);
                  de = new DirectedEdge(V,V1,energy(i+1,i+1));                  
              }
               if(de!=null){
                   digraph.addEdge(de);
               }
           }     
        }
          
          
   }
   
   
   public Picture picture()                       // current picture
   {
       return picture;
   }
   public int width(){
   
       return picture.width();
   }                         // width  of current picture
   public int height()                        // height of current picture
   {
      return picture.height();
   }
   public double energy(int x, int y)            // energy of pixel at column x and row y in current picture
   {
       //throw exception      
       if(x<0||y<0||x > height()||y>height())
       {
            
       }
            
       // Check for edge           
       if(x==0||y==0||y == height()||x== width())
       {
           return 195075;
       }   
       else{
           Color colorX1 = picture.get(x-1,y);
           Color colorX2 = picture.get(x+1,y);
           
           Color colorY1 = picture.get(x,y-1);
           Color colorY2 = picture.get(x,y+1);
           
           int rx = colorX2.getRed()-colorX1.getRed(); 
           int gx = colorX2.getGreen()-colorX1.getGreen();    
           int bx = colorX2.getBlue()-colorX1.getBlue();
 
           int ry = colorY2.getRed()-colorY1.getRed(); 
           int gy = colorY2.getGreen()-colorY1.getGreen();    
           int by = colorY2.getBlue()-colorY1.getBlue();
           
           double dx = rx* rx + gx*gx + bx*bx;
           double dy = ry* ry + gy*gy + by*by;
           
           double sum = dx + dy;
           return sum;
       }
   }
   /*This is similar to the classic shortest path problem in an edge-weighted digraph except for the following:
    *The weights are on the vertices instead of the edges.
We want to find the shortest path from any of the W pixels in the top row to any of the W pixels in 
the bottom row. The digraph is acyclic, where there is a downward edge from pixel (x, y) 
to pixels (x ? 1, y + 1), (x, y + 1), and (x + 1, y + 1), 
assuming that the coordinates are in the prescribed range. 
*/    
   public   int[] findHorizontalSeam()            // sequence of indices for horizontal seam in current picture
   {
      //create a matrix 
      //create an array of adjacent list it could be array as well
      double matrix[][] = new double[this.height()][this.width()];
      double row[] = new double[this.width()];
       for(int i=0; i<this.height();i++)
       {    for(int j=0;j<this.width();j++)
           {
        
             row[j] = energy(j,i); //energy of column x and row y
           }
           matrix[i] = row; 
       }
       //matrix is just a raw matrix. now we need to calculate a minimum sequence of steps
       
       
       return new int[0];
   }
   public   int[] findVerticalSeam()              // sequence of indices for vertical   seam in current picture
   {
       int row[] = new int[this.width()];
       return row;
   }
   public    void removeHorizontalSeam(int[] a)   // remove horizontal seam from current picture
   {
   
   }
   public    void removeVerticalSeam(int[] a)     // remove vertical   seam from current picture
   {
   
   }    
     

// private double
   private void Dfs(int matrix[][]){
       
//       Stack stack = new Stack();
//       int min = (int) Double.POSITIVE_INFINITY;;
//       
//       for(int i =0; i< matrix[0].length; i++)
//       {
//          //calculate dfs
//           stack = new Stack();
//           Point p = new Point(0,i);
//           Stack s = dfs(dfsmatrix, 0, i, stack);
//           
//       }
   }
   
   private Stack dfs(boolean matrix[][],int x, int y,Stack stack){
       matrix[x][y] = true;
       
       
       return stack;
   }
   
    private class Point{
       public int x;
       public int y;
       public Point(int _x, int _y){
           this.x = _x;
           this.y = _y;
       }
   }
    
    public static void main(String[] args) {
     
    }  
}