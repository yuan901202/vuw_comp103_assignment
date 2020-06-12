/* Code for COMP103, Assignment 9
 * Name:
 * Usercode:
 * ID:
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
     *	Has public fields so methods in BSTSet can access fields directly. 
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
	comp = new ComparableComparator();  	// Declared below
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
	if (item == null) return false;
	if (root == null) return false;
	BSTNode node = root;
	while (node!=null) {
	    int c = comp.compare((E) item, node.value);
	    if (c == 0)
		return true;
	    else if (c < 0)
		node = node.left;
	    else 
		node = node.right;
	}
	return false;
	// END OF YOUR CODE
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
	if (root == null) {
	    root = new BSTNode(item);
	    count++;
	    return true;
	}
	BSTNode node = root;
	while (true) {
	      int c = comp.compare((E)item, node.value);
	      if (c<0) {
		if (node.left == null) {
		    node.left = new BSTNode(item);
		    count++;
		    return true;
		}
		else
		    node = node.left;
	      }
	    else {
		if (node.right == null) {
		    node.right = new BSTNode(item);
		    count++;
		    return true;
		}
		else
		    node = node.right;
	    }
	}
	// END OF YOUR CODE
    }
      

    /** Remove the element matching a given item. Note that any children
     *  of the removed node should be kept in the tree. Return true if
     *  the collection changes, and false if it did not change. Note that
     * there is a count field in BSTSet that should be updated.
     */
    public boolean remove(Object item) {
	if (item == null) return false;
	// YOUR CODE HERE
	if (root == null) return false;
	// item at root of BST
	if (root.value.equals(item)) {
	    root = removeRoot(root);   // return the new subtree without the item
	    count--;
	    return true;
	}
	// Find PARENT of node to remove
	BSTNode node = root;
	while (true) {
	    int c = comp.compare((E)item, node.value);
	    if (c < 0) {
		// look in left subtree
		if (node.left == null)
		    return false;
		else if (node.left.value.equals(item)) {
		    // remove the root of the left subtree
		    node.left = removeRoot(node.left);
		    count--;
		    return true;
		}
		else  // work down the left subtree
		    node = node.left;
	    }
	    else {
		// look in right subtree
		if (node.right == null)
		    return false;
		else if (node.right.value.equals(item)) {
		    // remove the root of the left subtree
		    node.right = removeRoot(node.right);
		    count--;
		    return true;
		}
		else  // work down the left subtree
		    node = node.right;
	    }
	}
    }


    /** Remove the root of the subtree and return
     *  the new root of the remaining subtree.
     *  Has several cases:
     *  - root has no children  => return null
     *  - root has only one child => return the child
     *  - root has two children, replace root by rightmost node in left subtree.
     */
    private BSTNode removeRoot(BSTNode subtree) {
	// case 1, and half of case 2
	if (subtree.right == null) return subtree.left;
	// other half of case 2
	if (subtree.left == null) return subtree.right;
	// easy version of case 3 (subtree.right is the leftmost)
	if (subtree.right.left == null) {
	    subtree.right.left = subtree.left;
	    return subtree.right;
	}
	// hard version of case 3
	// find leftmost node in right subtree
	BSTNode nd = subtree.right;
	while (nd.left.left!=null) nd = nd.left;
	// nd is now the parent of the leftmost node
	BSTNode leftmost = nd.left;
	// slice out leftmost node
	nd.left = nd.left.right;
	// move leftmost node to top
	leftmost.right = subtree.right;
	leftmost.left = subtree.left;
	return leftmost;
	// or subtree.value = leftmost.value; return subtree;
	// END OF YOUR CODE
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
	// Recursive implementation
	return height (root);
    }
    private int height(BSTNode node) {
	if (node == null) return 0;
	int leftHeight = height(node.left);
	int rightHeight = height(node.right);
	if (leftHeight > rightHeight) return leftHeight + 1;
	else return rightHeight + 1;
	// END OF YOUR CODE
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
	    for (BSTNode nd = root; nd!=null;nd=nd.left)
		stack.push(nd);
	    // END OF YOUR CODE
	}
    
	public boolean hasNext() {
	    return !stack.isEmpty();
	}

	public E next() {
	    // YOUR CODE HERE
	    if (stack.isEmpty()) throw new NoSuchElementException();
	    BSTNode node = stack.pop();
	    for (BSTNode nd = node.right; nd!=null; nd=nd.left)
		stack.push(nd);
	    return node.value;
	    // END OF YOUR CODE
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
