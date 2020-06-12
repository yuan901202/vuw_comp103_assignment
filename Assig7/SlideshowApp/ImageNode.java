/* Code for COMP 103, Assignment 7
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

/**
 * ImageNode is a simple class representing a singly-linked linked-list data structure, without a header 
 * or wrapper class. Rather than storing the entire image in the node, only the file name is stored.  
 * 
 * @author Stuart Marshall 
 * @version 18 September 2012
 */
public class ImageNode {

    // a string that contains the full name on the filesystem of an image file.
    private String imageName;
    
    // a reference to the next ImageNode in the linked list.
    private ImageNode next;
    
    /**
     * Constructor for objects of class ImageNode
     */
    public ImageNode(String imageNameStr, ImageNode nextNode)
    {
        this.imageName = imageNameStr;
        this.next = nextNode;
    }

    public String getName() {
        return this.imageName;
    }
    
    public ImageNode getNext() {
        return this.next;
    }

    public void setNext(ImageNode newNext) {
        this.next = newNext;
    }
    
    /**
     * This public remove method can be called from outside this class, and then switches between using the 
     * iterative or recursive private implementations of the method depending on the value of a boolean field in the 
     * SlideshowApp class.
     * 
     * The node at index 'pos' should be removed from the list.
     * 
     * @param pos an index in the list. 
     */
    public void remove(int pos) {
        if (SlideshowApp.isRecursive) {
            this.removeRec(pos);
        } else {
            this.removeIter(pos);
        }
    }

    /**
     * This public add method can be called from outside this class, and then switches between using the 
     * iterative or recursive private implementations of the method depending on the value of a boolean field in the 
     * SlideshowApp class.
     * 
     * The string is then added to a newly created node that will be placed at index 'pos'. Whatever was already 
     * at that index becomes the next node after the newly created node.
     * 
     * @param imageFileStr the string representing the full filename of the image added to the list.
     * @param pos an index in the list. 
     */ 
    public void add(String imageFileStr, int pos) {
        if (SlideshowApp.isRecursive) {
            this.addRec(imageFileStr, pos);
        } else {
            this.addIter(imageFileStr, pos);
        }
    }

    /**
     * This public getName method can be called from outside this class, and then switches between using the 
     * iterative or recursive private implementations of the method depending on the value of a boolean field in the 
     * SlideshowApp class.
     * 
     * The value of the string at index 'pos' is returned. Will return a null reference if pos is invalid.
     * 
     * @param pos an index in the list. 
     */ 
   
    public String getName(int pos) {
        if (SlideshowApp.isRecursive) {
            return this.getNameRec(pos);
        } else {
            return this.getNameIter(pos);
        }
    }
    
    /**
     * This public size method can be called from outside this class, and then switches between using the 
     * iterative or recursive private implementations of the method depending on the value of a boolean field in the 
     * SlideshowApp class.
     * 
     * 
     * @return the number of nodes in the list starting at this node.
     */
    public int size() {
        if (SlideshowApp.isRecursive) {
            return this.sizeRec();
        } else {
            return this.sizeIter();
        }
    }

    
    /**
     * For the completion part of the assignment.
     * 
     * Using recursion, create a new node with the string parameter stored in it, at the index position 
     * specified in the integer parameter. The old node at the index position should now follow the newly inserted
     * node.
     *
     */
    private void addRec(String imageFileStr, int pos) {
        // YOUR CODE HERE
        if(pos == 1 || this.next == null){
            this.next = new ImageNode(imageFileStr, this.next); 
        }
        else{
            this.next.addRec(imageFileStr, pos - 1);
        }
    }
    
    /**
     * For the core part of the assignment.
     * 
     * Using an iterative approach, create a new node with the string parameter stored in it, at the index position 
     * specified in the integer parameter. The old node at the index position should now follow the newly inserted
     * node.
     *
     */
    private void addIter(String imageFileStr, int pos) {
        // YOUR CODE HERE
        ImageNode newNode = this;
        int currentPos = 1;
        while(currentPos < pos && newNode.next!=null){
            newNode = newNode.next;
            currentPos++;
        }
        newNode.next = new ImageNode(imageFileStr, newNode.next);
    }
    
    /**
     * For the completion part of the assignment.
     * 
     * Using recursion, remove the node at the index position specified in the integer parameter.
     *
     */
    private void removeRec(int pos) {
        // YOUR CODE HERE
        if(this.next == null){
            return;
        }
        
        if(pos == 1){
            this.next = this.next.next;
        } 
        else{
            this.next.removeRec(pos - 1);
        }
    }
    
    /**
     * For the core part of the assignment.
     * 
     * Using an iterative approach, remove the node at the index position specified in the integer parameter.
     *
     */
    private void removeIter(int pos) {
        // YOUR CODE HERE
        ImageNode newNode = this;
        int currentPos = 1;
        while(currentPos < pos && newNode.next != null){
            newNode = newNode.next;
            currentPos++;
        }
        if(newNode.next != null){
            newNode.next = newNode.next.next;
        }
    }
    
    
    /**
     * For the completion part of the assignment.
     * 
     * Using recursion, retrieve the string stored in the node at index 'pos'. 
     *
     * @param pos an index into the list, with 0 being the index of the first node in the whole list.
     * @return the string at position 'pos', or null if there is no such string or node.
     */
    private String getNameRec(int pos) {
        // YOUR CODE HERE
        if(pos == 0){
            return this.imageName;
        } 
        else if(this.next == null){
            return null;
        } 
        else{
            return this.next.getNameRec(pos - 1);
        }
    }

    /**
     * For the core part of the assignment.
     * 
     * Using an iterative approach, retrieve the string stored in the node at index 'pos'. 
     *
     * @param pos an index into the list, with 0 being the index of the first node in the whole list.
     * @return the string at position 'pos', or null if there is no such string or node.
     */
    private String getNameIter(int pos) {
        // YOUR CODE HERE
        ImageNode newNode = this;
        int currentPos = 0;
        while(currentPos < pos && newNode != null){
            newNode = newNode.next;
            currentPos++;
        }
        if(newNode == null){
            return null;
        }
        return newNode.imageName;
    }

    /**
     * For the completion part of the assignment.
     * 
     * Using recursion, calculate the size of the list starting from this node.
     *
     */
    private int sizeRec() {
        // YOUR CODE HERE
        if(this.next == null){
            return 1;
        } 
        else{
            return this.next.size() + 1;
        }
    }
    
    /**
     * For the core part of the assignment.
     * 
     * Using an iterative approach, calculate the size of the list starting from this node.
     *
     */
    private int sizeIter() {
        // YOUR CODE HERE
        int size = 0;
        ImageNode newNode = this;
        while(newNode != null){
            size++;
            newNode = newNode.next;
        }
        return size;
    }
}