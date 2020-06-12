/* Code for COMP 103, Assignment 4
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;

/**
 * ArraySet - a Set collection;
 *
 * The implementation uses an array and a count to store the items.
 *  The items in the set should be stored in positions
 *  0, 1,... (count-1) of the array
 * The size of the array when the set is first created should be 10. 
 * It does not keep the items in any particular order, and may change the
 *  order of the remaining items when removing items.  Eg, it can always add
 *  a new item at the end, and it can move the last item into the place of an
 *  item being deleted - there is no need to shift all  the items up or down
 *  to keep them in order.
 * It does not allow null items or duplicates.
 *  Attempting to add null should throw an exception
 *  Adding an item which is already present should simply return false, without
 *  changing the set.
 * It should always compare elements using equals()  (not using ==)
 * When full, it will create a new array of double the current size, and
 *  copy all the items over to the new array
 */

public class ArraySet <E> extends AbstractSet <E> {

    // Data fields
    // YOUR CODE HERE
    private static int INITIALCAPACITY = 10;
    private E[] data;
    private int count = 0;
    // END OF YOUR CODE

    // Constructors

    @SuppressWarnings("unchecked")  // this will stop Java complaining
    public ArraySet() {
        // YOUR CODE HERE
        data = (E[]) new Object[INITIALCAPACITY];
        // END OF YOUR CODE
    }

    // Methods

    /** Returns number of elements in collection as integer 
     */
    public int size () {
        // YOUR CODE HERE
        return count;
        // END OF YOUR CODE
    }

    /** Add the specified element to this set (if it is not a duplicate of an element
     *  already in the set).
     *  Will not add the null value (throws an IllegalArgumentException in this case)
     *  Return true if the collection changes, and false if it did not change.
     */
    public boolean add(E item) {
        // YOUR CODE HERE
        if (item == null)
            throw new IllegalArgumentException("null invalid value for set");
        if (contains(item)) {return false; }
        ensureCapacity ();
        data[count] = item;
        count++;
        return true;
        // END OF YOUR CODE
    }

    /** Return true if this set contains the specified element. */
    public boolean contains(Object item) {
        // YOUR CODE HERE
        if (item == null) return false;
        return findIndex(item) >= 0;
        // END OF YOUR CODE
    }

    /** Remove an element matching a given item
     *  Return true if the item was present and then removed.
     *  Make no change to the set and return false if the item is not present.
     */
    public boolean remove (Object item) {
        // YOUR CODE HERE
        if (item == null) return false;
        int index = findIndex(item);
        if (index < 0)
            return false;
        else {
            count--;
            data[index] = data[count];
            return true;
        }
        // END OF YOUR CODE
    }

    /** Return an iterator over the elements in this set. */
    public Iterator <E> iterator() {
        // YOUR CODE HERE
        return new ArraySetIterator<E>(this);
        // END OF YOUR CODE
    }

    /** Ensure data array has sufficient number of elements
    *  to add a new element 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining
    private void ensureCapacity () {
        // YOUR CODE HERE
        if (count < data.length) return;
        E[] newArray = (E[]) (new Object[data.length*2]);
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
        // END OF YOUR CODE
    }

    // You may find it convenient to define the following method and use it in
    // the methods above, but you don't need to do it this way.

    /** Find the index of an element in the dataarray, or -1 if not present
     *  Assumes that the item is not null 
     */
    private int findIndex(Object item) {
        // YOUR CODE HERE
        // (if you want to define this method)
        for (int i=0; i<count; i++)
            if (item.equals(data[i]))
                return i;
        return -1;
        // END OF YOUR CODE
    }

    private class ArraySetIterator <E> implements Iterator <E> {

        // needs fields, constructor, hasNext(), next(), and remove()

        // YOUR CODE HERE
        private ArraySet<E> set;
        private int nextIndex = 0;
        private boolean canRemove = false;

        private ArraySetIterator(ArraySet<E> s) {
            set = s;
        }
	// END OF YOUR CODE

        /** Return true if iterator has at least one more element */
        public boolean hasNext() {
            // YOUR CODE HERE
            return (nextIndex < set.count);
            // END OF YOUR CODE
        }

        /** Return next element in the set */
        public E next() {
            // YOUR CODE HERE
            if (nextIndex >= set.count)
                throw new NoSuchElementException();
            canRemove = true;
            return set.data[nextIndex++];
            // END OF YOUR CODE
        }

        /** Remove from the set the last element returned by the iterator.
         *  Can only be called once per call to next.
         */
        public void remove() {
            // YOUR CODE HERE
            if (! canRemove)
                throw new IllegalStateException();
            nextIndex--;
            set.count--;
            set.data[nextIndex] = set.data[set.count];
            set.data[set.count] = null;
            canRemove = false;
            // END OF YOUR CODE
        }
    }
}

