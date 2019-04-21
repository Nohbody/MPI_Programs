import java.util.Iterator;
import java.util.Arrays;
public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private  Item items[];
   
    private static int max_size;
    private static int currentIndex = 0; //same thing as current size

    public RandomizedQueue() {
        items = (Item[]) new Object[2];
        currentIndex =0;
        max_size = 2;

    } 

// is the queue empty?
    public boolean isEmpty(){
    
        return currentIndex==0;
    }           

    public int size() {

        return currentIndex;
    }   // return the number of items on the queue
    
    public void enqueue(Item item){// add the item
        if(item==null){
          throw new java.lang.NullPointerException();
        }
        
     //resize the array        
     if(max_size == currentIndex){
          resize(2*max_size);        
     }
    
     items[currentIndex] = item;
     currentIndex++;   
    } 

    private void resize(int newSize){
       
        Item newitems[] = (Item[]) new Object[newSize];
        //Copy elements
        int ksize = (newSize>max_size)?max_size:newSize;
        
        for (int i=0;i<ksize; i++)
        {
            newitems[i] = items[i];
        }
         max_size = newSize;
        items = newitems;
        
    }
    
    private void exchange(Item[] a, int i, int j) {
        if (i == j)
            return;
        Item swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    
    
    public Item dequeue(){ // delete and return a random item
      if(isEmpty()){
          throw new java.util.NoSuchElementException();
      }
        int r = StdRandom.uniform(currentIndex);
        exchange(items, r, currentIndex-1);
        Item result = items[currentIndex-1];
        items[currentIndex-1] = null;
                
        currentIndex--;
        if (currentIndex * 4 < max_size && max_size > 1) 
        { 
            resize(max_size/2);
        }
            
            
        return result;

    }             
    public Item sample(){ // return (but do not delete) a random item
         if (currentIndex == 0)
            throw new java.util.NoSuchElementException("Empty queue");
         
        int r = StdRandom.uniform(currentIndex);
        Item it = items[r]; 
        return it;
        
    }               
    
    

    public Iterator<Item> iterator()   // return an iterator over items in order from front to end
   {
   
       return new RandomizedQueueIterator();
   }

   private class RandomizedQueueIterator implements Iterator <Item>{

        private int[] idx;
        private int count = currentIndex;
        public RandomizedQueueIterator() {
            idx = new int[count];
            for (int i = 0; i < count; i++) {idx[i] = i;}
              StdRandom.shuffle(idx);

        }
    
       public boolean hasNext(){
           return count!=0;
       }
       
       public Item next(){
          if(!hasNext())
           {
              throw new java.util.NoSuchElementException();
           
          }
         
           return items[idx[--count]];
  
       }
       public void remove(){
        throw new java.lang.UnsupportedOperationException();
       }
   }
       



public static void main(String[] args) {
     RandomizedQueue d = new RandomizedQueue();  
     System.out.println("Check if it is empty :"+d.isEmpty());
     d.enqueue(1);
     d.enqueue(3);
     System.out.println(Arrays.toString(d.items));
     
     System.out.println("Check if it is empty. it shouldn't be :"+d.isEmpty());
      Iterator itr =d.iterator();
      while(itr.hasNext()) {
         Object element = itr.next();
        
      }
      
     System.out.println("\n");
     d.dequeue();
     System.out.println(Arrays.toString(d.items));
     
     System.out.println("Size after dequeu is:"+currentIndex); 
     System.out.println("Check if it is empty. it should be :"+d.isEmpty());

     itr =d.iterator();
     
      while(itr.hasNext()) {
          Object element = itr.next();
          
      }
      
     d.enqueue(4);
     d.enqueue(5);
     d.enqueue(45);
     
     System.out.println(Arrays.toString(d.items));
     
     itr =d.iterator();
     while(itr.hasNext()) {
         Object element = itr.next();
     }
     
     System.out.println("\n Size after 5* enqueue and 1 deque shoud be 4 and is:"+currentIndex); 
     d.dequeue();
    
     
     System.out.println("\n Size shoudld be 3 :"+currentIndex);
     itr =d.iterator();
      while(itr.hasNext()) {
         Object element = itr.next();
      }
      System.out.println(Arrays.toString(d.items));
       d.dequeue();
       d.dequeue();
       d.dequeue();
       System.out.println(Arrays.toString(d.items));
   }

}




