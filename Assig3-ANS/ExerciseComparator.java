/* Code for COMP102 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/** ExerciseComparator   */

public class ExerciseComparator implements UIButtonListener {

    private List<Card> hand;

    /**
     *   Set up the hand and the buttons
     */
    public ExerciseComparator() {
	UI.addButton("New Hand", this);
	UI.addButton("Default Sort", this);
	UI.addButton("Special Sort", this);
	newHand();
        Collections.shuffle(hand);

        draw();
    }


    public void buttonPerformed(String button){
	if (button.equals("New Hand")) {
	    newHand();
	    Collections.shuffle(hand);
	}
	else if (button.equals("Default Sort")) {
	    Collections.sort(hand);
	}
	if (button.equals("Special Sort")) {
	    Collections.sort(hand, new SpecialCardComparator());
	}
	draw();
    }

    /** The comparator class for the special sort:
	Ace comes first, then decreasing rank from king down to 2.
	If the ranks are the same, then sorted by suit:
	  Hearts, then Diamonds, then Spades, then Clubs.
	Returns -1 if c1 comes first;  +1 if c2 comes first,
	and 0 if they are the same.
    */
    private class SpecialCardComparator implements Comparator<Card>{

	public int compare(Card c1, Card c2){
	    // YOUR CODE HERE
	    int r1 =  c1.getRank();
	    if (r1==1) r1 = 14;
	    int r2 =  c2.getRank();
	    if (r2==1) r2 = 14;

	    if (r1 > r2) { return -1; }
	    else if (r2 > r1) { return 1; }
	    else {
		int s1 = Card.suitNames.indexOf(c1.getSuit());
		int s2 = Card.suitNames.indexOf(c2.getSuit());
		return s1-s2;
	    }
	    // END OF YOUR CODE
	}
    }


    /**
       Creates a new hand 
     */
    public void newHand(){	
	hand = new ArrayList<Card>();
	for (String suit :  Card.suitNames) {
	    for (int rank = 1; rank <= 13; rank++) {
		Card card = new Card(suit, rank);
		hand.add(new Card(suit, rank));
	    }
	}
    }

    public static final Color FELT_COLOR = new Color(32, 112, 48);
    public static final int PADDING = 15;

    public static final int HAND_X = 20;
    public static final int HAND_Y = 12;
    /**
     Draws the table and the hand
     */
    public void draw() {
        // draw background
        UI.clearGraphics();
        UI.setColor(FELT_COLOR);
        UI.fillRect(0, 0, UI.getCanvasWidth(), UI.getCanvasHeight());

        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).draw(HAND_X + i * PADDING, HAND_Y);
        }
	UI.repaintGraphics();
    }



    public static void main(String[] arguments){
       ExerciseComparator obj = new ExerciseComparator();
    }	


}
