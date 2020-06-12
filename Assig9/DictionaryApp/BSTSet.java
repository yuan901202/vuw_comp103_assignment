/* Code for COMP103, Assignment 9
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.util.*;
import java.io.*;

/** Implementation of a Set type using a Binary Search Tree to store
 *  the items. Uses a comparator to compare items. There is a default
 *  comparator that works if the items are Comparable It does not
 *  allow null elements or duplicates. Attempting to add null should
 *  throw an exception Adding an element which is already present
 *  should simply return false, without changing the set.
 */

public class BSTSet <E> extends AbstractSet <E> {

    // Data fields
    private BSTNode root;
    private int count = 0;
    private Comparator<E> comp;   // default comparator

    /** Private class for the nodes.
     *  Has public fields so methods in BSTSet can access fields directly. 
     */
    private class BSTNode {

    // Data fields

    public E value;
    public BSTNode left = null;
    public BSTNode right = null;

    // Constructor

    public BSTNode(E v) {
        value = v;
    }
    
    }
    // Constructors - can either use a default comparator or provide one

    public BSTSet() {
        comp = new ComparableComparator();      // Declared below
    }

    public BSTSet(Comparator <E> c) {
        comp = c;
    }

    // Methods

    /** Return true iff the set is empty */
    public boolean isEmpty() {
        return count == 0;
    }

    /** Return the number of elements in set */

    public int size() {
        return count;
    }


    /** Return true iff (if and only if) the set contains an item
     * (the item must be non null) 
     */
    public boolean contains(Object item) {
        // YOUR CODE HERE
        if(item == null){
            return false;
        }
        if(root == null){
            return false;
        }
        BSTNode node = root;
        while(node!=null){
            int a = comp.compare((E) item, node.value);
            if(a == 0){
                return true;
            }
            else if(a > 0){
                node = node.right;
            }
            else{
                node = node.left;
            }      
        }
        return false;
    }


    /** Add the specified element to this set, so long as it is not 
     * null or is not already in the set. Return true if the 
     * collection changes, and false if it does not change. Note that
     * there is a count field in the BSTSet that should also be updated.
     */
    public boolean add(E item) {
        if (item == null) 
        throw new IllegalArgumentException("null invalid value for set");
        if (contains(item)) return false;
        // YOUR CODE HERE
        if(root == null){
            root = new BSTNode(item);
            count++;
            return true;
        }
        BSTNode node = root;
        while(true){
            int a = comp.compare((E)item, node.value);
            if(a >= 0){
                if(node.right == null){
                    node.right = new BSTNode(item);
                    count++;
                    return true;
                }
                else{
                    node = node.right;
                }
            }
            else{
                if(node.left == null){
                    node.left = new BSTNode(item);
                    count++;
                    return true;
                }
                else{
                    node = node.left;
                }
            }
        }
    }
      
    /** Remove the element matching a given item. Note that any children
     *  of the removed node should be kept in the tree. Return true if
     *  the collection changes, and false if it did not change. Note that
     * there is a count field in BSTSet that should be updated.
     */
    public boolean remove(Object item) {
        if (item == null) return false;
        // YOUR CODE HERE
        if(root.value.equals(item)){
            root = removeRoot(root);
            count--;
            return true;
        }
        BSTNode node = root;
        while(true){
            int a = comp.compare((E)item, node.value);
            if(a >= 0){
                if(node.right.value.equals(item)){
                    node.right = removeRoot(node.right);
                    count--;
                    return true;
                }
                else if(node.right == null){
                    return false;
                }
                else{
                    node = node.right;
                } 
            }
            else{
                if(node.left.value.equals(item)){
                    node.left = removeRoot(node.left);
                    count--;
                    return true;
                }
                else if(node.left == null){
                    return false;
                }
                else{
                    node = node.left;
                }
            }
        }
    }
    
    private BSTNode removeRoot(BSTNode subtree){
        if (subtree.right == null){
            return subtree.left;
        }
        if (subtree.left == null){
            return subtree.right;
        }
        if (subtree.right.left == null) {
            subtree.right.left = subtree.left;
            return subtree.right;
        }
        BSTNode node = subtree.right;
        while(node.left.left!=null){
            node = node.left;
        }
        BSTNode leftmost = node.left;
        node.left = node.left.right;
        leftmost.right = subtree.right;
        leftmost.left = subtree.left;
        return leftmost;
    }

    /** Return an iterator over the elements in this bag. */
    public Iterator <E> iterator() {
        return new BSTSetIterator(root);
    }

    /** Useful method for debugging */
    public String toString() {
    if (root == null)
        return "<Empty Set>";
    else {
        StringBuilder ans = new StringBuilder("{Set:");
        Iterator<E> itr = iterator();
        if (itr.hasNext())
        ans.append(itr.next());
        while (itr.hasNext()) {
        ans.append(",");
        ans.append(itr.next());
        }
        ans.append("}");
        return ans.toString();
    }
    }

    /** This method returns the height of the tree. 
     * For this assignment we want you to compute the height as the
     * number of nodes in the longest path from the root to a leaf-node 
     * (i.e. a leaf-node at the greatest depth).
     * Returns -1 if the height can't be computed, 0 if there is no
     * root node, and greater than 0 in all other cases.
     */
    public int height() {
        // YOUR CODE HERE        
        //return -1; // to compile this stub
        return height(root);
    }
    
    private int height(BSTNode node){
        if (node == null){
            return 0;
        }
        
        int rightHeight = height(node.right);
        int leftHeight = height(node.left);
        if (leftHeight > rightHeight){
            return leftHeight + 1;
        }
        else{
            return rightHeight + 1; 
        }
    }

    /** Iterator for BST To iterate through the tree in sorted order,
     *  it must do an iterative in-order traversal, using a stack to
     *  keep track of its position To accomplish this, it must process
     *  all the left subtree of a node before it processes the node.
     *  Therefore, every time it pushes a node on the stack, it must
     *  also immediately push all the left descendents of the node on
     *  to the stack, all the way to the leftmost descendent. When it
     *  pops a node from the stack, it processes the node and then
     *  pushes its right child onto the stack (and all that child's
     *  left descendents)
     */
    private class BSTSetIterator implements Iterator<E> {
    
    Stack<BSTNode> stack = new Stack<BSTNode>();

    public BSTSetIterator(BSTNode root) {
        // YOUR CODE HERE
        for(BSTNode node = root; node != null; node = node.left){
            stack.push(node);
        }
    }
    
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public E next() {
        // YOUR CODE HERE
        if(stack.isEmpty()){
            throw new NoSuchElementException();
        }
        BSTNode node = stack.pop();
        for(BSTNode nd = node.right; nd != null; nd = nd.left){
            stack.push(nd);
        }
        return node.value;
        //return null;  // DOESN'T WORK - Just to make this stub compile
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    }
    
    // Comparator for comparable 

    private class ComparableComparator implements Comparator<E> {
    public int compare(E ob1, E ob2) {
        return ((Comparable)ob1).compareTo(ob2);
    }
    }
    
}