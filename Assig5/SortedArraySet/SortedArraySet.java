/* Code for COMP 103, Assignment 5
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.util.*;

/**
 * SortedArraySet - a Set collection;
 *
 * The implementation uses an array and a count to store the items.
 *  The items in the set should be stored in positions
 *  0, 1,... (count-1) of the array
 * The size of the array when the set is first created should be 10. 
 * It keeps the items in order according to the comparator.
 *  Ie, when it adds a new item, it must put it in the right place
 *  When it searches for an item, it should use binary search.
 *  Note, the comparator assumes that the items are Comparable.
 * It does not allow null items or duplicates.
 *  Attempting to add null should throw an exception
 *  Adding an item which is already present should simply return false, without
 *  changing the set.
 * It should always compare elements using equals()  (not using ==)
 * When full, it will create a new array of double the current size, and
 *  copy all the items over to the new array
 */

public class SortedArraySet <E> extends AbstractSet <E> {

    // Data fields
    private static int INITIALCAPACITY = 10;
    private E[] data;
    private int count = 0;

    private Comparator<E> comp;    // use comp to compare items. 

    // --- Constructors --------------------------------------
    /** Constructor to make a new empty set */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet() {
        comp = new ComparableComparator();
        data = (E[]) new Object[INITIALCAPACITY];
    }

    /** Constructor to make a new empty set, with a given comparator */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Comparator<E> comparator) {
        comp = comparator;
        data = (E[]) new Object[INITIALCAPACITY];
    }

    /** Constructor that takes a whole collection and sorts it all at once */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Collection<E> col) {
        comp = new ComparableComparator();
        // YOUR CODE HERE
        data = (E[]) new Object[col.size()];
        for (E item: col){
            data[count++] = item;
        }
        Arrays.sort(data, comp);
    }

    /** Constructor that takes a whole collection and sorts it all at once */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Collection<E> col, Comparator<E> comparator) {
        comp = comparator;
        // YOUR CODE HERE
        data = (E[]) new Object[col.size()];
        for (E item: col){
            data[count++] = item;
        }
        Arrays.sort(data, comp);
    }

    // --- Methods --------------------------------------

    /** Returns number of elements in collection as integer 
     */
    public int size () {
        return count;
    }

    /** Add the specified element to this set (if it is not a duplicate of an element
     *  already in the set).
     *  Will not add the null value (throws an IllegalArgumentException in this case)
     *  Return true if the collection changes, and false if it did not change.
     */
    public boolean add(E item) {
        // YOUR CODE HERE
        if (item == null){
            throw new IllegalArgumentException("null value");
        }
 
        int i = findIndex(item);
        if (i != count && item.equals(data[i])){
            return false;
        }
        
        ensureCapacity();
        for (int j = count; j > i; j--){
            data[j] = data[j - 1];
        }
        data[i] = item;
        count++;
        return true;
    }

    /** Return true if this set contains the specified element. */
    @SuppressWarnings("unchecked")  // stops Java complaining about the call to compare 
    public boolean contains(Object item) {
        E itm = (E) item;
        // YOUR CODE HERE
        if (itm == null){
            return false;
        }
        
        int i = findIndex(itm);
        if (i != count && itm.equals(data[i])){
            return true;
        } 
        return false;
    }

    /** Remove an element matching a given item
    *  Return true if the item was present and then removed.
    *  Make no change to the set and return false if the item is not present.
    */
    @SuppressWarnings("unchecked")  // stops Java complaining about the call to compare 
    public boolean remove (Object item) {
        E itm = (E) item;
        // YOUR CODE HERE
        if (itm == null){
            return false;
        }
      
        int i = findIndex(itm);
        if (i != count && itm.equals(data[i])){
            count--;
            for (int j = i; j < count; j++){
                data[j] = data[j + 1];
            }
            data[count] = null;
            return true;
        }    
        
        return false;
    }

    // It is much more convenient to define the following method 
    // and use it in the methods above.

    /** Find the index of where an element is in the dataarray,
     *  (or where it ought to be, if it's not there).
     *  Assumes that the item is not null.
     *  Uses binary search and requires that the items are kept in order.
     *  Should use  compareTo to compare values */
    private int findIndex(E item){
        // YOUR CODE HERE
        Comparable<E> value = (Comparable<E>) item;
        int low = 0;
        int high = count; 

        while(low < high){
            int mid = (low + high) / 2;

            if(value.compareTo(data[mid]) > 0){
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    /** Ensure data array has sufficient number of elements
    *  to add a new element 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    private void ensureCapacity () {
        if (count < data.length) return;
        E[] newArray = (E[]) (new Object[data.length*2]);
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
    }

    // --- Iterator and Comparator --------------------------------------

    /** Return an iterator over the elements in this set. */
    public Iterator <E> iterator() {
        return new SortedArraySetIterator(this);
    }

    private class SortedArraySetIterator implements Iterator <E> {
        // needs fields, constructor, hasNext(), next(), and remove()
        private SortedArraySet<E> set;
        private int nextIndex = 0;
        private boolean canRemove = false;

        private SortedArraySetIterator(SortedArraySet<E> s) {
            set = s;
        }

        /** Return true if iterator has at least one more element */
        public boolean hasNext() {
            return (nextIndex < set.count);
        }

        /** Return next element in the set */
        public E next() {
            if (nextIndex >= set.count)
                throw new NoSuchElementException();
            canRemove = true;
            return set.data[nextIndex++];
        }

        /** Remove from the set the last element returned by the iterator.
         *  Can only be called once per call to next.
         */
        public void remove() {
            if (! canRemove)
                throw new IllegalStateException();
            set.remove(set.data[nextIndex-1]);
            canRemove = false;
        }
    }

    /** This is a comparator that assumes that E's are Comparable:
    it casts them to Comparable<E>, and then calls their compare method.
    It will fail if E's are not Comparable - in this case, the set should
    have been constructed with an appropriate comparator.
     */
    private class ComparableComparator implements Comparator<E>{
        @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
        public int compare(E item, E other){
            Comparable<E> itm = (Comparable<E>) item;
            return itm.compareTo(other);
        }
    }
}

