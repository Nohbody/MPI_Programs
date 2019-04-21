import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by zora on 2/3/16.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node curr = null;
    private int N;

    private class Node<Item> {
        private Item item;
        private Node prev;
        private Node next;
    }

    public RandomizedQueue() { N = 0; }                // construct an empty randomized queue

    public boolean isEmpty() {
        return (N == 0);
    }                // is the queue empty?

    public int size() { return N; }                       // return the number of items on the queue

    private void randomWalk(int steps) {
        //StdOut.printf("steps %d\n", steps);
        //StdOut.printf("old: %d\n", (Item)curr.item);
        if (StdRandom.uniform(2) == 1) {
            for (int i = 0; i < steps; i++) { curr = curr.next; }
        }
        else {
            for (int i = 0; i < steps; i++) { curr = curr.prev; }
        }
        //StdOut.printf("new: %d\n", (Item)curr.item);
    }

    public void enqueue(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Cannot add null to queue");
        if (N > 0) {
            randomWalk(StdRandom.uniform(10));
            Node oldCurr = curr;
            curr = new Node(); ////******************////
            curr.item = item;
            curr.prev = oldCurr;
            curr.next = oldCurr.next;
            oldCurr.next.prev = curr;
            oldCurr.next = curr;
        }
        else {
            curr = new Node();
            curr.item = item;
            curr.prev = curr;
            curr.next = curr;

        }
        N++;
    }          // add the item

    public Item dequeue() {
        if (N == 0) throw new java.util.NoSuchElementException("Queue is empty");
        randomWalk(StdRandom.uniform(10));
        Item item = (Item) curr.item;
        curr.next.prev = curr.prev;
        curr.prev.next = curr.next;
        curr = curr.next;
        if (N == 1) { curr = null; }
        N--;
        return item;
    }                   // remove and return a random item

    public Item sample() {
        if (N == 0) throw new java.util.NoSuchElementException("Queue is empty");
        randomWalk(StdRandom.uniform(10));
        return (Item) curr.item;
    }                    // return (but do not remove) a random item

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }                   // return an independent iterator over items in random order

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private RandomizedQueueIterator() {
            randomWalk(StdRandom.uniform(10));
        }

        public boolean hasNext() { return N == 0; }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Method Doesn't exist");
        }
        public Item next() {
            if (N == 0) throw new java.util.NoSuchElementException("No Next Element in Queue");
            Item item = (Item) curr.item;
            curr = curr.next;
            return item;
        }
    }

    private void testRing() {
        for (int i = 0; i < N; i++) {
            StdOut.printf("Current Item is: %d\n", (Item) curr.item);
            curr = curr.next;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 50; i++) {
            queue.enqueue(490);
            queue.enqueue(491);
            queue.enqueue(492);
            queue.dequeue();
            queue.dequeue();
            queue.dequeue();
            //StdOut.println(queue.size());
            //StdOut.println(queue.isEmpty());     //==> true
            queue.enqueue(122);
            queue.enqueue(123);
            queue.dequeue();
            queue.dequeue();
            queue.enqueue(6);
            StdOut.println(queue.dequeue());
        }
        /*
        queue.enqueue(37);
        //StdOut.println(queue.dequeue());
        StdOut.println(queue.isEmpty());
        StdOut.println(queue.isEmpty());
        queue.enqueue(71);
        //queue.testRing();
        queue.enqueue(28);
        queue.enqueue(2);
        //queue.testRing();
        queue.enqueue(52);
        queue.enqueue(9);
        //queue.testRing();
        queue.enqueue(894);
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.size());
        queue.dequeue();
        StdOut.println(queue.isEmpty());
        */
    }   // unit testing

}
