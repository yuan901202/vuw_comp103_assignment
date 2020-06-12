/* Code for COMP 103, Assignment 7
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.*;
/**
 * SlideshowViewer is a class that uses the image (names) list, and displays one image at a time on the graphics
 * display. The user can manually move through the list, or they can request that the entire list be shown in order, with 
 * a two second delay between consecutive images.
 * 
 * The class is passed control by the SlideshowApp class, and the showViewerUI(...) method is passed the 
 * first node of the linked list by SlideshowApp.
 * 
 * SlideshowViewer should not modify the linked list structure, and only reads from it.
 * 
 * @author Stuart Marshall 
 * @version 18 September 2012
 */
public class SlideshowViewer implements UIButtonListener {

    // This boolean field identifies whether the viewer is the current active mode in the user interface. 
    // If it is, then it can respond to the relevant button presses.
    private boolean active;
    
    // The currently selected index in the list. This refers to the only image that is shown in the graphics display.
    private int currentImage;
    
    // The first node in a linked list data structure. When the viewer becomes the active mode, SlideshowApp also 
    // passes through a reference to the start of the list that SlideshowCreator was working on. This parameter is stored in this field.
    private ImageNode images;
    
    public SlideshowViewer() {
        this.active = false;
        this.currentImage = 0;
    }
    
    
    /**
     * Changes the graphics display in the UI to now show the viewer. In particular, the first image in the
     * slideshow will be visible on screen. Sets the viewer to active so it can now respond to button presses. 
     * 
     * @param   firstImage     a reference to the first node in the list for viewing.
     */
    public void showViewerUI(ImageNode firstImage) {
        this.currentImage = 0;
        this.images = firstImage;
        this.active = true;
        this.redraw();
    }
    
    public void hideViewerUI() {
        this.active = false;
    }
    
    /**
     * This method is called when a button that this object is listening to is pressed.
     * The first thing it does is check if the viewer is currently active. If not, then it simply returns and ignores the button press.
     * Otherwise, it calls the appropriate helper method.
     * 
     * @param   name    The name of the button pressed.
     */
    public void buttonPerformed(String name) {
        if (!this.active) return;
        
        if (name.equals("VIEWER: next image")) {
            this.next();
        } else if (name.equals("VIEWER: previous image")) {
            this.previous();
        } else if (name.equals("VIEWER: start show")) {
            this.slideshow();
        }
    }

    /**
     * Uses the currentImage integer field as a positional index into the linked list.
     * Once the file name has been retrieved from that index, then it can be displayed in the UI.
     * 
     */
    public void redraw() {
        UI.clearGraphics();
	if (this.images != null) {
	    UI.drawImage(this.images.getName(this.currentImage), 10, 10, 450, 450);
	}
    } 
    
    /**
     * Increments the currently selected index, assuming we're not already at the end of the list.
     * Forces a redraw if the index has changed.
     */
    private void next() {
        int size = this.images.size();
        if (this.currentImage < (size - 1)) {
            this.currentImage++;
            this.redraw();
        }
    }
    
    
    /**
     * Decrements the currently selected index, assuming we're not already at the start of the list.
     * Forces a redraw if the index has changed.
     */
    private void previous() {
        if (this.currentImage > 0) {
            this.currentImage--;
            this.redraw();
        }
    }
    
    /**
     * Starting at the beginning, this method goes through a timed loop of the list's indices, continually 
     * incrementing the currently selected index by using the "next()" method, and then forcing a redraw.
     * 
     */
    private void slideshow() {
        int size = this.images.size();
        this.currentImage = 0;
        for (int loop = 0; loop < size; loop++) {
            UI.sleep(2000);
            this.redraw();
            this.next();
        }
        
    }
    
}
