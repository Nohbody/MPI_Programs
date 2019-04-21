import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
   private int size;
   private Node first;
   private Node last;

   public Deque()                     // construct an empty deque
   {
       size = 0;
   }
   public boolean isEmpty()           // is the deque empty?
   {
       return first == null;
   }
   public int size()                  // return the number of items on the deque
   {
       return size;
   }
   public void addFirst(Item item)    // insert the item at the front
   {
       if(item == null)
       {
           throw new java.lang.NullPointerException();
       }
       Node n = new Node();
       n.item = item;
       if(!isEmpty())
       {
           Node oldFirst = first;
           n.next = oldFirst;
           first = n;
       }
       else{
          first = n;
          last = n; 
       }
       size ++;
   }
   public void addLast(Item item)     // insert the item at the end
   {
       Node n = new Node();
       n.item = item;

       if(!isEmpty()){ 
           last.next = n;         
           n.previous = last;
       }
       else{
          first = n;
          last = n;
       }
       size++;
   }
   public Item removeFirst()          // delete and return the item at the front
   {
       if(!isEmpty())
       {
          Node newFirst = first.next;  
           if(newFirst!=null)
           {   
               first =null;
               first = newFirst;
               return first.item;
           }
           else{
               last =null;
           }
           size--;
       }
       else{
           // do nothing or throw exception  
            throw new java.util.NoSuchElementException();
       }
       
       return null;
   }
   public Item removeLast()           // delete and return the item at the end
   {
       if(!isEmpty()){
           size--;
           if(last.previous !=null){
             Node n = last.previous;
             last = null;
             last = n;
             return last.item;
          }
          else{
              first = null;
              last =null;
              return null;
          }
                }
       else{
           throw new java.util.NoSuchElementException();
       }
   }
   public Iterator<Item> iterator()   // return an iterator over items in order from front to end
   {
   
       return new DequeIterator();
   }

   private class DequeIterator implements Iterator <Item>{
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
   }
  
   public static void main(String[] args) {
     Deque d = new Deque();  
     System.out.println("Check if it is empty :"+d.isEmpty());
     d.addFirst(1);
     System.out.println("Check if it is empty :"+d.isEmpty());
     d.removeLast();
     System.out.println("Check if it is empty :"+d.isEmpty());
     d.addFirst(3);
     d.addFirst(5);

     d.addFirst(45);

     d.addLast(10);
     System.out.println("Check if it is empty :"+d.isEmpty());
     Iterator itr =d.iterator();
      while(itr.hasNext()) {
         Object element = itr.next();
         System.out.print(element + " ");
      }
   }
}