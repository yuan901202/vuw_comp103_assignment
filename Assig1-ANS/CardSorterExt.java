/* Code for COMP103, Assignment 1
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.util.*;

import java.awt.Color;
import java.io.*;

/** Simple program for dealing with cards.
    The program involves a deck of cards, shown face down on the left of the table.
    The user may click on the deck to take the top card and add it to the "hand".
    The cards in the hand are displayed in a row to the right of the deck.
    The user may click on a card in the hand to select it (it is then shown
     sticking up from the hand slightly).
    If there is a selected card, then clicking on any position in the hand will
     move the selected card to that position.
    If there is a selected card, then clicking on one of the discard piles
     (shown above the hand) will move the selected card to that discard pile, if
     the selected card is the next card in the sequence for that pile.
    The top card of each discard pile is shown face up. An empty discard pile
    is shown as a rectangle.
    
    The program uses the Card class, which has a constructor to make a new card,
    and methods to draw the card (front or back) at a specified position on
    the table.
*/


public class CardSorterExt implements UIMouseListener {

    // Constants for the positions of the deck, the hand, and the discard pile
    public static final int DECK_X = 50;
    public static final int DECK_Y = 300;

    public static final int HAND_X = 150;
    public static final int HAND_Y = 300;
    public static final int HAND_SEP = 15;

    public static final int TARGETS_Y = 50;
    public static final int TARGETS_X = 200;
    public static final int TARGETS_SEP = 100;

    // constants for drawing the board
    public static final Color FELT_COLOR = new Color(32, 112, 48);
    public static final int TABLE_WIDTH = 1000;
    public static final int TABLE_HEIGHT = 500;
    public static final int CARD_WIDTH = 75;
    public static final int CARD_HEIGHT = 110;

    // the three collections of cards (the deck, the hand, and the discard)
    // YOUR CODE HERE
    private Stack<Card> deck;    // the list of cards in the deck pile
    private List<Card> hand;      // the list of cards in the hand
    private Stack<Card>[] targets = new Stack[4]; // the four discard piles
    // END OF YOUR CODE

    // the currently selected card. (null if none selected)
    private Card selected; 

    /** Set up the mouse listener, and set up the
        the deck (a list of shuffled cards), the hand, and the four discard piles,
	To set up the deck, it creates lots of new Cards, adds them to the stack,
	then shuffles it.
	(There is a method in Collections for shuffling lists and stacks!) 
	Then draws everything
    */
    public CardSorterExt() {
        UI.setMouseListener(this);
	// YOUR CODE HERE
	deck = new Stack<Card>();
        for (String suit : new String[]{"spades", "diamonds", "clubs", "hearts"}){
	    for (int rank = 1; rank <= 13; rank++) {
		deck.add(new Card(suit, rank));
	    }
	}
	Collections.shuffle(deck);

        hand = new ArrayList<Card>();
	for (int i=0; i<4; i++){
	    targets[i] = new Stack<Card>();
	}

	// END OF YOUR CODE
        redraw();
	UI.println("Click on deck to move card to hand");
	UI.println("Click on hand to select card or move in hand");
	UI.println("Click on a discard pile to discard selected card");
    }

    /** All behaviour is determined by where the mouse is released, and whether
	  there is a currently selected card.
	If the mouse position is on the deck stack, then
 	   take the top card from the deck and add it to the hand.
	If the mouse position is on a position of a card in the hand:
	   it either selects the card, or moves the currently selected card
	   to this place
	If the mouse position is on a discard pile, 
	If the mouse position is on a target pile, then
	   try to move the selected card to the target pile (if legal)
    */
    public void mousePerformed(String action, double x, double y) {
        if (action.equals("released")){
	    // YOUR CODE HERE
	    if (pointAtPlace(x, y, DECK_X, DECK_Y)) {   // is it on the deck pile
		takeFromDeck();
	    }
	    for (int i = hand.size()-1; i>=0; i--) {  // is it on a position in the hand
		if (pointAtPlace(x, y, HAND_X+i*HAND_SEP, HAND_Y)) {
		    pickCardInHand(i);
		    break;
		}
	    }
	    for (int i=0; i<4; i++){
		if (pointAtPlace(x, y, TARGETS_X+i*TARGETS_SEP, TARGETS_Y)){
		    this.moveToTarget(targets[i]);
		    break;
		}
	    }
	    // END OF YOUR CODE
	    redraw();
	}
    }
    

    // Helper methods
    /** Move a card from the deck (if there is one) to the hand */
    public void takeFromDeck(){
	// YOUR CODE HERE
	if (deck.isEmpty()){ return; }
	Card nextCard = deck.pop();
	if (nextCard != null) {
	    hand.add(nextCard);
	}
	// END OF YOUR CODE
    }

    /** Work out if there is a selected card and whether it would be legal
	to move the selected card to the target:
	 If the target stack is empty and the selected card has rank 1 OR
	    the selected card has the same suit and one higher rank than the top
	       card on the target stack)
	 if so
	   Move the selected card from the hand to the target pile
    */
    public void moveToTarget(Stack<Card> target){
	// YOUR CODE HERE
	if (selected == null){ return; }
	if ( target.isEmpty() ){
	    if (selected.getRank() != 1){
		return;
	    }
	}
	else {
	    Card topCard = target.peek();
	    if (selected.getRank() != 1+topCard.getRank() ||
		!selected.getSuit().equals(topCard.getSuit())){
		return;
	    }
	}
	hand.remove(selected);
	target.add(selected);
	selected = null;
	// END OF YOUR CODE
    }

    /** Act on a card at the position index in the hand:
        if there is no card currently selected, then select the card.
        if there is a card selected, then move the selected card to this place.
    */
    public void pickCardInHand(int index){
	// YOUR CODE HERE
	if (selected != null){   // move the selected card to here
	    hand.remove(selected);
	    hand.add(index, selected);
	    selected = null;
	}
	else {
	    selected = hand.get(index);
	}
	// END OF YOUR CODE
    }


    /** would the point (x,y) be on top of a card placed at (cardX, cardY) */
    public boolean pointAtPlace(double x, double y, double cardX, double cardY){
	return (x>=cardX && y >=cardY  &&
		x <= cardX+CARD_WIDTH &&
		y <= cardY+CARD_HEIGHT);
    }


    /** Redraw the table with the deck, the hand, and the discard pile.
     *  The deck shows the back of the top card, and the discard pile shows
     *  the front of the top card.
     *  The hand shows each card, with the selected card sticking out a little
     */
    public void redraw() {
        // draw background
        UI.clearGraphics(false);
        UI.setColor(FELT_COLOR);
        UI.fillRect(0, 0, TABLE_WIDTH, TABLE_HEIGHT);
	// YOUR CODE HERE
        // draw hand
        int x = HAND_X;
        for (Card card : hand) {
            if (card == selected) {
                card.draw(x, HAND_Y - HAND_SEP);
            }
	    else {
		card.draw(x, HAND_Y);
	    }
            x += HAND_SEP;
        }

        // draw deck and discard piles
	if (!deck.isEmpty())
	    deck.peek().drawBack(DECK_X, DECK_Y);

	for (int i=0; i<4; i++){
	    this.drawPile(targets[i], TARGETS_X + i*TARGETS_SEP, TARGETS_Y);
        }
	UI.repaintGraphics();
    }
    

    public void drawPile(Stack<Card> pile, int x, int y){
	if (pile.isEmpty()){
	    UI.setColor(Color.lightGray);
	    UI.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT, false);
	}
	else {
	    pile.peek().draw(x, y);
	}
	// END OF YOUR CODE
    }
    

    public static void main(String[] args) {
	new CardSorterExt();
    }
}
