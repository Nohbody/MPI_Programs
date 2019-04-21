public class Subset {
    public static void main(String[] args){
         RandomizedQueue q = new RandomizedQueue();
         int k = Integer.valueOf(args[0]);
         while(!StdIn.isEmpty()){
             String s = StdIn.readString();
             q.enqueue(s);
             StdOut.println("  " + s);
         }

         for(int i=0;i<k;i++){
            Object  w = q.dequeue();
            StdOut.println("  " + w);
         }
         
    }
}