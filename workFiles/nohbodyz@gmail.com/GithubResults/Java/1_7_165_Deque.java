import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by zora on 2/2/16.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node nodeFirst, nodeLast;
    private int size;
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    public Deque() {
        nodeFirst = null;
        nodeLast = null;
        size = 0;
    }                          // construct an empty deque

    public boolean isEmpty() {
        return (nodeFirst == null && nodeLast == null);
    }                 // is the deque empty?

    public int size() {
        return size;
    }                       // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Cannot add null to stack");
        Node oldFirst = nodeFirst;
        nodeFirst = new Node();
        nodeFirst.item = item;
        nodeFirst.prev = null;
        nodeFirst.next = oldFirst;
        if (oldFirst != null) { oldFirst.prev = nodeFirst; }
        if (nodeLast == null) { nodeLast = nodeFirst; }
        size++;
    }         // add the item to the front

    public void addLast(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Cannot add null to stack");
        Node oldLast = nodeLast;
        nodeLast = new Node();
        nodeLast.item = item;
        nodeLast.prev = oldLast;
        nodeLast.next = null;
        if (oldLast != null) { oldLast.next = nodeLast; }
        if (nodeFirst == null) { nodeFirst = nodeLast; }
        size++;
    }          // add the item to the end

    public Item removeFirst() {
        if (nodeFirst == null) throw new java.util.NoSuchElementException("Stack is empty");
        Item item = nodeFirst.item;
        if (nodeFirst == nodeLast) {
            nodeFirst = null;
            nodeLast = null;
        }
        else {
            nodeFirst = nodeFirst.next;
            nodeFirst.prev = null;
        }
        size--;
        return item;
    }               // remove and return the item from the front

    public Item removeLast() {
        if (nodeLast == null) throw new java.util.NoSuchElementException("Stack is empty");
        Item item = nodeLast.item;
        if (nodeFirst == nodeLast) {
            nodeFirst = null;
            nodeLast = null;
        }
        else {
            nodeLast = nodeLast.prev;
            nodeLast.next = null;
        }
        size--;
        return item;
    }                // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }        // return an iterator over items in order from front to end

    private class DequeIterator implements Iterator<Item> {
        private Node current = nodeFirst;

        public boolean hasNext() { return current != null; }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Method Doesn't exist");
        }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("It's already the end of the stack");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        StdOut.println(deque.size());
        deque.addLast(2);
        StdOut.println(deque.size());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());

    }  // unit testing
}