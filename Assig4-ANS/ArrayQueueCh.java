/* Code for COMP 103, Assignment 4
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import java.util.concurrent.*;

/**
 * ArrayQueueCh - a Queue collection;
 *   THIS VERSION IS THE EXTENDED VERSION FOR THE CHALLENGE
 * The implementation uses an array to store the items.
 * It adds items at the back of the queue and removes them from the front
 * Removing items will leave space at the front of the array.
 *
 * It has two fields to record the front and the back of the queue:
 *  front is the index of the item at the front of the queue
 *  back is the index of the place where the NEXT item will be added
 *    (ie, one beyond the last item in the queue)
 * When front == back, then the queue is empty.
 * For example, if the queue currently has 3 items, A, B, and C,
 *  The fields might be:
 *   data:  | - | - | A | B | C | - | - | - |
 *            0   1   2   3   4   5   6   7
 *   front: 2
 *   back:  5
 * If we then called offer("D"), it should be:
 *   data:  | - | - | A | B | C | D | - | - |
 *            0   1   2   3   4   5   6   7
 *   front: 2
 *   back:  6
 * If we then called poll(), it should return A and be:
 *   data:  | - | - | - | B | C | D | - | - |
 *            0   1   2   3   4   5   6   7
 *   front: 3
 *   back:  6
 * With three more calls to poll(), it should be empty:
 *   data: | - | - | - | - | - | - | - | - |
 *            0   1   2   3   4   5   6   7
 *   front: 6
 *   back:  6
 *
 * When back gets to the end of the array, it will make room for more items.
 * If there is enough room at the beginning of the array (head > 1/2 array length)
 * it will just shift all the items back to the beginning of the array.
 * For example, if we called offer("F") in the following state: 
 *   data:  | - | - | - | - | - | D | E | F |
 *            0   1   2   3   4   5   6   7
 *   front: 5
 *   back:  8
 * It should move all the items down within the array, and then add "G":
 *   data:  | D | E | F | G | - | - | - | - |
 *            0   1   2   3   4   5   6   7
 *   front: 0
 *   back:  4
 * Otherwise, it will create a new array of double the 
 * current size, and copy all the items over to the new array,
 *
 * ArrayQueueCh extends AbstractQueue, so it only needs to implement
 *   size(),
 *   peek(),
 *   poll(),
 *   offer(), and 
 *   iterator().
 */

public class ArrayQueueCh <E> extends AbstractQueue <E> implements BlockingQueue<E> {

    private static int INITIALCAPACITY = 16;

    private E[] data;
    private int front = 0;  //the index of the first item in the queue
    private int back = 0;   //the index where the next new item will go
    // items are stored from front..back-1
    // if front == back, then the queue is empty.

    @SuppressWarnings("unchecked")  // this will stop Java complaining
    public ArrayQueueCh() {
        data = (E[]) new Object[INITIALCAPACITY];
    }

       /** Return the number of elements in collection */
    public synchronized int size () {
        return back - front;     
    }

    /** Return the value at the front of the queue,
     *  or null if the queue is empty, but does not change the queue 
     */
    public synchronized E peek() {
        if (isEmpty())
            return null;
        else
            return data[front];
    }

    
    /** Remove and return the value at the front of the queue,
     *  or null if the queue is empty 
     */
    public synchronized E poll() {
        if (isEmpty())
            return null;
        else {
            E ans = data[front];
            front = (front+1);       
            return ans;
        }
    }

  
    /** Add an item onto the back for the queue, unless the item is null
     *  If the item was added, it returns true,
     *  if the item was null, it returns false 
     */
    public synchronized boolean offer(E value) {
        if (value == null)
            return false;
        else {
            ensureCapacity();
            data[back] = value;
            back = (back+1);
            return true;
        }
    }

    /** Ensure data array has sufficient number of elements
    *  to add a new element 
    */
    @SuppressWarnings("unchecked") // this will stop Java complaining
    private void ensureCapacity () {
        if (back == data.length){  // there is no room to add the next item
            // if we are at least 1/2 empty just shift items down 
            if (front > data.length/2) {
                int j = 0;           
                for (int i = front; i < back; i++){
                    data[j] = data[i];
                    j++;       
                }
                back = back-front;     // reset back and front.
                front = 0;
            }
            else { // need to get a bigger array
                E[] newArray = (E[])(new Object[data.length*2]);
                int j=0;
                for (int i = front; i < back; i++){
                    newArray[j++] = data[i];
                }
                back = back-front;     // reset back and front.
                front = 0;
                data = newArray;
            }
        }
    }

    public synchronized int drainTo(Collection<? super E> c){return 0; }
    public synchronized int drainTo(Collection<? super E> c, int max){ return 0; }

    public synchronized boolean offer(E e, long timeout, TimeUnit unit){ return offer(e);}
    public synchronized E poll(long timeout, TimeUnit unit){ return poll(); }

    public synchronized void put(E e){offer(e); notify();}
    public synchronized E take()throws InterruptedException{
	    while(isEmpty()){wait();}
	    return poll();
	}
    public int remainingCapacity(){return Integer.MAX_VALUE;}


    /** Return an iterator over the elements in the list */
    public Iterator <E> iterator() {
        return new ArrayQueueChIterator<E>(this);
    }

    /** Iterator for ArrayQueueCh.
     *  This implementation is not smart, and may be corrupted if
     *  any changes are made to the ArrayQueueCh that it is iterating down.
     *  Note that because it is an inner class, it has access to the 
     *  ArrayQueueCh's private fields.
     */
    private class ArrayQueueChIterator <E> implements Iterator <E> {
        // needs fields, constructor, hasNext(), next(), and remove()

        private ArrayQueueCh<E> queue; // reference to the list it is iterating down
        private int nextIndex;       // the index of the next value the iterator will return

        private ArrayQueueChIterator (ArrayQueueCh <E> q) {
            queue = q;
            nextIndex= q.front;
        }

        /** Return true if iterator has at least one more element */
        public boolean hasNext () {
            return (nextIndex < back);
        }

        /** Return next element in the set */
        public E next () {
            if (nextIndex == back) 
                throw new NoSuchElementException(); 
            E ans = queue.data[nextIndex];
            nextIndex = (nextIndex+1);
            return ans;
        }

        /** Remove from the set the last element returned by the iterator.
         *  The queue does not permit this operation
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    

}

