public class Percolation{ 
    private int[][] grid;
    private WeightedQuickUnionUF quickUnion;
    private int N;
    private int vtop;
    private int vbottom;
    
public Percolation(int N)              // create N-by-N grid, with all sites blocked
    {
        this.N=N;
        grid = new int[N][N];
        int index =0;
        for (int i=0;i< N;i++)
        {
            for (int j=0;j < N;j++)
            {
                grid[i][j]=0;
                index = flatten(N, N);
            }
        }
        vtop = index +1;
        vbottom = index+2;
        quickUnion =  new WeightedQuickUnionUF(vbottom+1);
    }

    private int flatten(int row,int column)
    {
        
        return (N*(row-1) +(column-1));
    }
    
    private void validate(int i, int j){
        if(i<1||j<1||i>N ||j>N) throw new java.lang.IndexOutOfBoundsException("Wrong Index"+j+""+i);
    }
    
    
    public void open(int row, int column)         // open site (row i, column j) if it is not already
    {
        validate(row,column);
        
        //Mark as open
        if(isOpen(row,column)){
           return;
        }
        
        grid[row-1][column-1] = 1;
        // check if neighbors are open as well. If so connect them.
        //left
        if(column!=1 &&grid[row-1][column-2] == 1)
        {
            quickUnion.union(flatten(row,column),flatten(row,column-1));
        }
        //right
        if(column != N &&grid[row-1][column] == 1)
        {
            quickUnion.union(flatten(row,column),flatten(row,column+1));
        }
        //bottom
        if(row != N &&grid[row][column-1] == 1)
        {
             quickUnion.union(flatten(row,column),flatten(row+1,column));
        }  
       
        //top
        if(row !=1 &&grid[row-2][column-1] == 1)
        {
             quickUnion.union(flatten(row,column),flatten(row-1,column));
             //System.out.println("Found Neighbor on on top");
        }  
        
        if(row==1&&grid[row-1][column-1] == 1){
          System.out.println("");
          quickUnion.union(flatten(row,column),vtop);
        //  System.out.println("top"+row +" "+column);
        }
        
        if(row==N&&grid[row-1][column-1] == 1){
          quickUnion.union(flatten(row,column),vbottom);
          //System.out.println("bottom"+row +" "+column);
        }
        
       /// System.out.println(Arrays.toString(quickUnion.id));
           
}
           
public boolean isOpen(int i, int j)    // is site (row i, column j) open?
      {
            validate(i,j);
            return  grid[i-1][j-1] == 1;
        }

public boolean isFull(int i, int j)    // is site (row i, column j) full?
 {
     validate(i,j);
     return quickUnion.connected(vtop, flatten(i,j));
 }
           
           
           public boolean percolates()            // does the system percolate?
           {
            return quickUnion.connected(vtop,vbottom);
        }
           
          
           
           


    public static void main(String[] args) {
        test3();
    }

    private static void test2() {
        final Percolation p = new Percolation(3);
        System.out.println("Operation 1");
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
        p.open(1, 2);
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
        System.out.println("p.isFull(1, 2) = " + p.isFull(1, 2));

        System.out.println("Operation 2");
        System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
        p.open(2, 2);
        System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
        System.out.println("p.isFull(2, 2) = " + p.isFull(2, 2));

        System.out.println("Operation 3");
        System.out.println("p.isOpen(3, 2) = " + p.isOpen(3, 2));
        p.open(3, 2);
        System.out.println("p.isOpen(3, 2) = " + p.isOpen(3, 2));
        p.isFull(3, 2);


        System.out.println("p.percolates() = " + p.percolates());
    }

    private static void test3() {
        final Percolation p = new Percolation(3);
//        System.out.println("");
//        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
//        p.open(1, 2);
//        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
//        System.out.println("");
        System.out.println("p.isOpen(1,1) = " + p.isOpen(1, 1));
        p.open(1, 1);
        System.out.println("p.isOpen(1,1) = " + p.isOpen(1, 1));
        System.out.println("");
        System.out.println("p.isFull(2, 2) = " + p.isFull(1, 2));
        p.open(1, 2);
        System.out.println("p.isOpen(2, 2) = " + p.isOpen(2, 2));
        System.out.println("p.isFull(2, 2) = " + p.isFull(2, 2));
        p.open(2, 2);
        System.out.println("p.isOpen(2, 2) = " + p.isOpen(2, 2));
        System.out.println("");
         
        System.out.println("p.isFull(2, 3) = " + p.isFull(2, 3));
        System.out.println("p.isOpen(2, 3) = " + p.isOpen(2, 3));
        p.open(2, 3);
        System.out.println("p.isOpen(2, 3) = " + p.isOpen(2, 3));
       
        p.open(3, 2);
        
        System.out.println("p.percolates() = " + p.percolates());

    }



    
    private static void test4() {
        final Percolation p = new Percolation(5);
        System.out.println("p.isOpen(1, 3) = " + p.isOpen(1, 3));
        p.open(1, 3);
        System.out.println("p.isOpen(1,3) = " + p.isOpen(1, 3));
        System.out.println("p.isFull(1, 3) = " + p.isFull(1, 3));
    }
}