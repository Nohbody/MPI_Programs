public class PercolationStats{

    private int currentT;
    private int T;
    private int N;
    
    private double[] opened;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T)
    {
        if(N<=0 ||T<=0)
        {
            throw  new java.lang.IllegalArgumentException();
        }
        this.T = T;
        currentT=0;
        this.N = N;
        opened = new double[T];
        double size = N *N;
       
       
        Percolation p = new Percolation(N);
        for(int i=0;i<T;i++){
             
            // peform tests
            double count=0.0;
            currentT =i;
            while(!p.percolates()){
              int row = StdRandom.uniform(1, N + 1);
              int column = StdRandom.uniform(1, N + 1);
              if (!p.isOpen(row, column)) {
                p.open(row, column);
                count++;
              }
            }
        
        opened[currentT] = count/size; 
        }        
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(opened);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
         return StdStats.stddev(opened);
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo()
    {
        return mean() - ((1.96 * Math.sqrt(stddev())) / Math.sqrt(T));
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi()
    {
        return mean() + ((1.96 * Math.sqrt(stddev())) / Math.sqrt(T));
    }
    
    public static void main(String[] args) {
        if(args.length!=2)
        {
           System.out.printf("You need to provide 2 args");
        }
        else{
          int N = Integer.valueOf(args[0]);
          int T = Integer.valueOf(args[1]);
        
          PercolationStats ps = new PercolationStats(N, T);
          System.out.printf("mean is= %f\n", ps.mean());
          System.out.printf("stddev is                  = %f\n", ps.stddev());
          System.out.printf("95%% confidence interval  = %f, %f\n"
                , ps.confidenceHi(), ps.confidenceLo());
          
        }
    }
    
}
