/* Code for COMP 103 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;

public class HeapArrayQueue <E extends Comparable<? super E> > extends AbstractQueue <E> {

    // YOUR CODE HERE
    private E[] data;
    private int count;

    public HeapArrayQueue() {
        this.data = (E[])(new Comparable[7]);
        this.count = 0;
    }

    public E peek() {
        if  (this.isEmpty())	
            return null;

        return this.data[0];
    }

    public E poll() {
        if  (this.isEmpty())	
            return null;

        E ans = this.data[0];
        this.count--;
        if  (this.count > 0) {
            this.data[0] = this.data[this.count];
            this.pushdown(0);
        }
        //System.out.println("next item to removed off queue: " + ans);
        return ans;
    }

    public boolean offer(E value) {
        if  (value == null) 
            return false;

        this.ensureCapacity();
        this.data[this.count++] = value;
        this.pushup(this.count - 1);
        return true;

    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() { 
        return this.size() == 0; 
    }

    public Iterator<E> iterator() { 
        return null; 
    }

    private void ensureCapacity() {
        if (this.count == this.data.length) {
            E[] newData = (E[])new Comparable[this.data.length * 2];
            for (int loop = 0; loop < this.count; loop++) {
                newData[loop] = this.data[loop];
            }
            this.data = newData;
        }
        return;
    }

    private void pushdown(int index) {
        int smallestChild = (2 * index) + 1;
        int otherChild = smallestChild + 1;

        if (smallestChild >= this.count)
            return;

        if ((otherChild < this.count) 
        && (this.data[smallestChild].compareTo(this.data[otherChild]) > 0)) {
            smallestChild = otherChild;
        }

        if (this.data[index].compareTo(this.data[smallestChild]) > 0) {
            this.swap(index, smallestChild);
            this.pushdown(smallestChild);
        }
        return;
    }

    private void pushup(int index) {
        if (index == 0) 
            return;

        int parent = (index - 1) / 2;
        if (this.data[parent].compareTo(this.data[index]) > 0) {
            this.swap(parent, index);
            this.pushup(parent);
        }
        return;
    }

    private void swap(int from, int to) {
        E temp = this.data[from];
        this.data[from] = this.data[to];
        this.data[to] = temp;
        return;
    }

    // END OF YOUR CODE
}
