/* Code for COMP103, Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.awt.Color;

/** Uses recursion to
     - draw a path of white stepping stones across a pond.
     - draw trees around the pond
 */
public class Exercise implements UIButtonListener, UIMouseListener {

    // Fields
    private double lastX;
    private double lastY;

    public Exercise() {
	UI.addButton("Clear", this);
	UI.addButton("Stepping Stones", this);
	UI.setMouseListener(this);
	drawPond();
    	UI.printMessage("Click mouse to draw trees");
    }


    /** Draw a path of stepping stones, starting at (x,y)
     *	with the first stone of the given width, and each successive
     *	stone 80% of the previous stone.
     *	The stones look approximately right if the height of the oval
     *	is 1/4 the width.
     */
    public void steppingStones(double x, double y, double width) {
	// YOUR CODE HERE
    	if (width >= 15) {
    	    UI.fillOval(x, y, width, width/4);
    	    this.steppingStones(x+width/2, y-width/3, width * 0.9);
    	}
    	// END OF YOUR CODE
    }


    /** Draw a tree with the base at (xBot, yBot).
     *  The top of the first branch should be at xTop, yTop.
     *  Then draw three smaller trees on the top of this branch
     *   with tops above, to the left, and to the right of this branch.
     */
    public void drawTree(double xBot, double yBot, double xTop, double yTop) {
	// YOUR CODE HERE
	UI.drawLine(xBot, yBot, xTop, yTop);
    	if (yBot-yTop >= 10) {
	    double height = (yBot-yTop) ;
	    double midY = yTop - height*0.7;
	    double sideY = yTop - height* 0.6;
	    drawTree(xTop, yTop, xTop, midY);
	    drawTree(xTop, yTop, xTop-height*0.3, sideY);
	    drawTree(xTop, yTop, xTop+height*0.3, sideY);
    	}
    	// END OF YOUR CODE
    }

    

    public void drawPond(){
	UI.clearGraphics();
	UI.setColor(Color.blue);
	UI.fillOval(50, 250, 400, 150);
    }

    /** Respond to button presses */
    public void buttonPerformed(String button) {
	if ( button.equals("Clear")) {
	    drawPond();

	}
	else if (button.equals("Stepping Stones")) {
    	    UI.setColor(Color.white);
	    steppingStones(100, 350, 40);
	}
    }
    
    /** Respond to mouse events */
    public void mousePerformed(String action, double x, double y) {
	if (action.equals("released")) {
    	    UI.setColor(Color.green);
	    drawTree(x, y, x, y-50);
	}
    }





    // Main
    public static void main(String[] arguments){
	new Exercise();
    }	


}
