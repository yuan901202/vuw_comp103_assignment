/* Code for COMP 103, Assignment 7
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.*;
/**
 * This class represents a tool for creating a list of image file (names) that can later be passed to the SlideshowViewer
 * for proper viewing. This class handles the addition to, removal from, and manipulation of the list, as well as providing a 
 * thumbnail view of the entire list in the graphics display.
 * 
 * @author Stuart Marshall 
 * @version 18 September 2012
 */
public class SlideshowCreator implements UIButtonListener
{

    // boolean field to identify whether this is currently the active mode in the program.
    private boolean active;
    
    // reference to the first node in the linked
    private ImageNode images;
    
    // an integer representing the index of the currently selection in the list. Valid indices start at 0, and go through to 
    // the (size of the list - 1)
    private int selectedIndex;
    
    // fields that can be used to calculate the width of a standard thumbnail, and how they should be laid out in the graphics display.
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_GAP = 10;
    private static final int GRAPHICS_WIDTH = 600;
    
    /**
     * 
     */
    public SlideshowCreator() {
        this.images = null;
        this.selectedIndex = 0;
        this.active = false;
    }
    
    public void showCreatorUI() {
        this.active = true;
        this.redraw();
    }
    
    public void hideCreatorUI() {
        this.active = false;
    }
    
    /**
     * This method calls the relevant helper method for whichever button was pressed.
     * The method will first check that it is the currently active mode in the program, 
     * other it will ignore the call to buttonPerformed(...)
     * 
     * @param name the name of the button pressed.
     */
    public void buttonPerformed(String name) {
        if (!active) return;
        
        if (name.equals("CREATOR: go to start")) {
            this.moveSelectionToStart();
        } else if (name.equals("CREATOR: move left")) {
            this.moveSelectionLeft();
        } else if (name.equals("CREATOR: add image")) {
            this.addImage();
        } else if (name.equals("CREATOR: remove image")) {
            this.removeImage();
        } else if (name.equals("CREATOR: move right")) {
            this.moveSelectionRight();
        } else if (name.equals("CREATOR: go to end")) {
            this.moveSelectionToEnd();
        } else if (name.equals("CREATOR: clear list")) {
            this.clearList();
        } else if (name.equals("CREATOR: reverse list")) {
            this.reverseList();
        }
    }

    /**
     * @return returns the first node in the linked list data structure.
     */
    public ImageNode getList() {
        return this.images;    
    }    
    
    /**
     * For the completion part of the assignment.
     * 
     * Reverses the order of this singly-linked list, so that the last node is now the first node, and 
     * and the second-to-last node is the second node, and so on. Note that the image currently selected
     * should remain the image currently selected even after the list is reversed. This means that 
     * the selectedIndex field may need to change value.
     */
    private void reverseList() {
        // YOUR CODE HERE
        this.images = this.reverse(this.images, null);
        this.selectedIndex = this.images.size() - 1 - this.selectedIndex;
        this.redraw();
    }
    
    //The fllowing code get extra help from tutors("ImageNode reverse", "ImageNode reverseIter")
    private ImageNode reverse(ImageNode current, ImageNode last) {
        ImageNode newNode = null;
        ImageNode next = current.getNext();
        if(next != null){
            newNode = this.reverse(next, current);
        }
        else{
            newNode = current;
        }
        current.setNext(last);
        return newNode;
    }
    
    private ImageNode reverseIter(ImageNode front) {
        ImageNode newNode = front;
        ImageNode newList = null;
        while(newNode != null){
            ImageNode temp = newNode;
            newNode = newNode.getNext();
            temp.setNext(newList);
            newList = temp;
        }
        return newList;
    }
    
    /**
     * For the core part of the assignment.
     * 
     * Make the list empty.
     */
    private void clearList() {
        // YOUR CODE HERE
        this.images = null;
        this.redraw();
    }
    
    /**
     * For the core part of the assignment.
     * 
     * ask for an image file to be selected from the filesystem, and then add the name of the image file to the list at the currently selected index. 
     * The numerical value of the currently selected index should not change. 
     * 
     * HINT: You should treat inserting at the very start of the list as a special case. 
     * HINT: The iterative version of the add method that you will write in the ImageNode is private, so you'll need to call the 
     * wrapper add method that I have created, that then switches between your iterative and (later) recursive versions.
     * 
     */
    private void addImage() {
        // YOUR CODE HERE
        String imageFile = UIFileChooser.open("Please select an image: ");
        if((this.selectedIndex == 0) || (this.images == null)){
            this.images = new ImageNode(imageFile, this.images);
        } 
        else{
            this.images.add(imageFile, this.selectedIndex);
        }
        this.redraw();
    }
    
    /**
     * For the core part of the assignment.
     * 
     * remove the image file (name) at the currently selected index. 
     * The numerical value of the currently selected index should still point to a valid place in the list after this method, or 
     * 0 in the case where the list ends up empty after deletion of the node. 
     * 
     * HINT: You should treat removing at the very start of the list (or from an empty list) as a special case. 
     * HINT: The iterative version of the remove method that you will write in the ImageNode is private, so you'll need to call the 
     * wrapper add method that I have created, that then switches between your iterative and (later) recursive versions.
     * 
     */
    private void removeImage() {
        // YOUR CODE HERE
        if(this.images == null){
            return;
        } 
        else if(this.selectedIndex == 0){
            this.images = this.images.getNext();
        } 
        else{
            this.images.remove(this.selectedIndex);
            if(this.selectedIndex >= this.images.size()){
                this.selectedIndex--;
            }
        }
        this.redraw();
    }
    
    /**
     * increment the current selected index, assuming that it is not already at the end of the list, and redraw the graphics display., 
     */
    private void moveSelectionRight() {
        // YOUR CODE HERE
        if(images != null){
            if((this.selectedIndex + 1) < this.images.size()){
                this.selectedIndex++;
            }
        } 
        this.redraw();
    }
    
    /**
     * change the current selected index to the last node, and redraw the graphics display.
     */
    private void moveSelectionToEnd() {
        // YOUR CODE HERE
        this.selectedIndex = this.images.size() - 1;
        this.redraw();
    }
    
    /**
     * change the current selected index to be the first node, and redraw the graphics display.
     */
    private void moveSelectionToStart() {
        // YOUR CODE HERE
        this.selectedIndex = 0;
        this.redraw();
    }
    
    /**
     * decrement the current selected index, assuming it is not already at the start of the list, and redraw the graphics display.
     */
    private void moveSelectionLeft() {
        // YOUR CODE HERE
        if(this.selectedIndex > 0){
            this.selectedIndex--;
        }
        this.redraw();
    }

    /**
     * draws the list of image thumbnails on the graphics window. The size of each thumbnail should reduce with the size of the 
     * list, so that all of the thumbnails can fit on the graphics window. There are some static final fields in the SlideshowCreator 
     * class that you may find useful in this regard. 
     * 
     * Note that the currently selected index and the immediate neighbours should be highlighted in some way. The demo does this by 
     * making the thumbnail image at the currently selected index twice the size of an ordinary thumbnail, with the immediate 
     * neighbour on either side one and a half times the size of an ordinary thumbnail.
     */
    private void redraw() { 
        UI.clearGraphics();
        // YOUR CODE HERE
        int x = 10;
        int y = 10;
        if(this.images == null){
            return;
        }
        
        int currentIndex = 0;
        int size = this.images.size();
        
        ImageNode currentImage = this.images;
        int width = Math.min(THUMBNAIL_WIDTH, ((GRAPHICS_WIDTH - (THUMBNAIL_WIDTH * 2)) / size));
        
        while(currentImage != null){
            int currentnodewidth = width;
            if(currentIndex == this.selectedIndex){
                currentnodewidth = THUMBNAIL_WIDTH * 2;
            } 
            else if(Math.abs(currentIndex - this.selectedIndex) == 1){
                currentnodewidth = (int)(THUMBNAIL_WIDTH * 1.5);
            } 
            UI.drawImage(currentImage.getName(), x, y, currentnodewidth, currentnodewidth);
            
            x += (currentnodewidth + THUMBNAIL_GAP);
            currentImage = currentImage.getNext();
            currentIndex++;
        }
   }    
}