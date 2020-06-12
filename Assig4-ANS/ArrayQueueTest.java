/* Code for COMP103, Assignment 4
 * Name:
 * Usercode:
 * ID:
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
    private Queue<String> queue;

    /** initialise queue to be an empty queue before each test is run */
    @Before
    public void initialiseEmptyQueue() {
        queue = new ArrayQueue<String>();
    }

    /** method to initialise the queue with the n values "v1", "v2", ... */
    public void fillQueue(int n){
        for (int i = 1; i <= n; i++) {
            queue.offer("v" + i);
        }
    }

    //-------------------------------------------------------------------
    @Test
    public void testIsEmptyOnEmptyQueue() {
        assertTrue("A new array queue should be empty.", queue.isEmpty());
    }

    @Test
    public void testEmptyQueueHasSizeZero(){
        assertEquals("An empty queue should have size zero.", 0, queue.size());
    }

    @Test
    public void testPollPeekEmptyQueue(){
        assertNull("Peek of an empty queue should be null.", queue.peek());
        assertNull("Poll of an empty queue should be null.", queue.poll());
    }

    @Test
    public void testOfferToEmptyQueue(){
        assertTrue("Offer should add item to queue.", queue.offer("v1"));
        assertFalse("Queue should not be empty after offer.", queue.isEmpty());
        assertEquals("Size should be 1 after 1 offer.", 1, queue.size());
        assertEquals("Peek should return the value offered.", "v1", queue.peek());
    }

    @Test
    public void  testRepeatedOffer(){
        for (int i=1; i<30; i++){
            String value = "v" + i;
            assertTrue("Offer should add item to queue.", queue.offer(value));
            assertFalse("Queue should not be empty after offer.", queue.isEmpty());
            assertEquals("Size should be " + i + " after " + i + " offers.", i, queue.size());
        }
    }

    @Test
    public void testOfferNull(){
        assertFalse("Offering null should return false.", queue.offer(null));
    }

    @Test
    public void testPeekPoll(){
        fillQueue(30);
        for (int i=1; i<=30; i++){
            String value = "v"+i;
            assertEquals("Peek should return correct item.", value, queue.peek());
            assertEquals("Poll should return correct item.", value, queue.poll());
        }
    }

    /** Test that queue works when we alternately offer and poll many times */
    @Test
    public void testSizeAfterOfferingAndPolling(){
        queue.offer("v0");
        for (int i=1; i<100; i++) {
            String previousValue = "v"+(i-1);
            String value = "v"+i;
            assertEquals("Size should be 1 before offer.", 1, queue.size());
            assertTrue("Offer should succeed.", queue.offer(value));
            assertEquals("Size should be 2 after offer.", 2, queue.size());
            assertFalse("Queue should not be empty.", queue.isEmpty());
            assertEquals("Head should be previous offer.", previousValue, queue.poll());
            assertEquals("Size should be 1 after poll.", 1,  queue.size());
            assertEquals("Head should be latest value.", value, queue.peek());
        }
    }

    /** Test that queue still works when we force it to grow a lot */
    @Test
    public void testSizeWhenGrowing(){
        for (int i=1; i<100; i++) {
            String value = "v"+i;
            assertTrue("Offer should succeed.",queue.offer(value));
            assertEquals("Size should be "+i+" after offer.", i, queue.size());
            assertEquals("Head should be v1.", "v1", queue.peek());
        }
        for (int i=1; i<100; i++) {
            String value = "v"+i;
            assertEquals(i+"'th head should be "+value+".", value, queue.peek());
            assertEquals(i+"'th head should be "+value+".", value, queue.poll());
        }
        assertTrue("Queue should be empty after polling all items.", queue.isEmpty());
    }

    /** Test iterator */
    @Test
    public void testIterator(){
        fillQueue(100);
        int i = 1;
        for (String item : queue){
            String value = "v"+i;
            i++;
            assertEquals(i+"'th value should be "+value+".", value, item);
        }
    }
    // END OF YOUR CODE

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("ArrayQueueTest");
    }

}
