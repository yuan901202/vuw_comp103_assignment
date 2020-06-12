/* Code for COMP103, Assignment 4
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.util.Iterator;
import java.util.Queue;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/** ArrayQueueTest
 *  A JUnit class for testing an ArrayQueue
 */

public class ArrayQueueTest {

    // YOUR CODE HERE
    public void testIsEmptyOnEmptyQueue(){
        Queue<String> queue = new ArrayQueue<String>();
        assertTrue("A new array queue should be empty.", queue.isEmpty());
    }
    
    /**
     * Create a queue with n elements ("m0", "m1", "m2", "m3"...)
     */
    private Queue<String> createAQueue(int n){
        Queue<String> queue = new ArrayQueue<String>();
        for(int i = 0; i < n; i++){
            String value = "m" + i;
            queue.add(value);
        }
        return queue;
    }
    
    public void testEmptyQueueHasSizeZero(){
        Queue<String> queue = new ArrayQueue<String>();
        assertEquals("An empty queue should have size zero.", 0, queue.size());
    }

    public void testPollPeekEmptyQueue(){
        Queue<String> queue = new ArrayQueue<String>();
        assertNull("Peek of an empty queue should be null.", queue.peek());
        assertNull("Poll of an empty queue should be null.", queue.poll());
    }
    
    public void testOfferToEmptyQueue(){
        Queue<String> queue = new ArrayQueue<String>();
        assertTrue("Offer should add item to queue.", queue.offer("m1"));
        assertFalse("Queue should not be empty after offer.", queue.isEmpty());
        assertEquals("Size should be 1 after 1 offer.", 1, queue.size());
        assertEquals("Peek should return the value offered.", queue.peek(), "m1");
    }

    public void  testRepeatedOffer(){
        Queue<String> queue = new ArrayQueue<String>();
        for (int i = 1; i < 30; i++){
            String value = "m" + i;
            assertTrue("Offer should add item to queue.", queue.offer(value));
            assertFalse("Queue should not be empty after offer.", queue.isEmpty());
            assertEquals("Size should be " + i + " after " + i + " offers.", i, queue.size());
        }
    }
    
    public void testOfferNull(){
        Queue<String> queue = new ArrayQueue<String>();
        assertFalse("Offering null should return false.", queue.offer(null));
    }

    public void testPeekPoll(){
        Queue<String> queue = createAQueue(30);
        for (int i = 0; i < 30; i++){
            String value = "m" + i;
            assertEquals("Peek should return correct item.", value, queue.peek());
            assertEquals("Poll should return correct item.", value, queue.poll());
        }
    }
    
    public void testSizeAfterOfferingAndPolling(){
        Queue<String> queue = new ArrayQueue<String>();
        queue.offer("m0");
        for (int i = 1; i < 100; i++) {
            String previousValue = "m" + (i-1);
            String value = "m" + i;
            assertEquals("Size should be 1 before offer.", queue.size(), 1);
            assertTrue("Offer should succeed.", queue.offer(value));
            assertEquals("Size should be 2 after offer.", queue.size(), 2);
            assertFalse("Queue should not be empty.", queue.isEmpty());
            assertEquals("Head should be previous offer.", queue.poll(), previousValue);
            assertEquals("Size should be 1 after poll.", queue.size(), 1);
            assertEquals("Head should be latest value.", queue.peek(), value);
        }
    }
    
    public void testSizeWhenGrowing(){
        Queue<String> queue = new ArrayQueue<String>();
        for (int i = 1; i < 100; i++) {
            String value = "m" + i;
            assertTrue("Offer should succeed.",queue.offer(value));
            assertEquals("Size should be " + i + " after offer.", queue.size(), i);
            assertEquals("Head should be m1.", queue.peek(), "m1");
        }
        for (int i = 1; i < 100; i++) {
            String value = "m" + i;
            assertEquals(i + " 'th head should be " + value+".", queue.peek(), value);
            assertEquals(i + " ' th head should be " + value+".", queue.poll(), value);
        }
        assertTrue("Queue should be empty after polling all items.", queue.isEmpty());
    }
    
    public void testIterator(){
        Queue<String> queue = createAQueue(100);
        int i = 0;
        for (String item : queue){
            String value = "m" + i;
            i++;
            assertEquals(i + " 'th value should be " + value + ".", item, value);
        }
    }
    
    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("ArrayQueueTest");
    }

}