public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item items[];
    private int size;
    public RandomizedQueue() {
        size = 0;
        items = new Item[0];
    }          // construct an empty randomized queue
    public boolean isEmpty(){
    
    }           // is the queue empty?
    public int size() {
        return size;
    }   // return the number of items on the queue
    public void enqueue(Item item){
        
    } // add the item
    public Item dequeue(){
    
    }              // delete and return a random item
    public Item sample(){}               // return (but do not delete) a random item

    public Iterator<Item> iterator()   // return an iterator over items in order from front to end
   {
   
       return new DequeIterator();
   }

   private class RandomizedQueue implements Iterator <Item>{
       private Node current = first;
       public boolean hasNext(){
           return current != null;
       }
       
       public Item next(){
           Item item = current.item;
           current = current.next;
           return item;
       }
       public void remove(){
        throw new java.lang.UnsupportedOperationException();
       }
   }
       
   private class Node{
       public Node next;
       public Node previous;
       public Item item;       
   }   // return an independent iterator over items in random order
}
