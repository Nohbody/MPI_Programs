public class RecursionTest {
   
    private void sort(int low, int high, int a[])
    {
        if(high<=low){ 
            System.out.println("Should Return");
            return;
        }
        int middle = low +(high-low)/2;
          System.out.println("Middle: "+middle +" high: "+high + " low "+low);
        sort(low,middle,a);
        sort(middle+1,high,a);    
        
        
    }
    
    public static void main(String[] args){
        RecursionTest r = new RecursionTest();
        int a[] = new int[1];
        a[0] = 15;
//        a[0] = 5;
//        a[0] = 7;
//        a[0] = 4;
        r.sort(0,a.length, a);
    }
}
      