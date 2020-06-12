/* Code for COMP103, Assignment 1
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
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
    If there is a selected card, then clicking on the table top will move the
     selected card to the discard pile (shown above the hand).
    The top card of the discard pile is shown face up.
    
    The program uses the Card class, which has a constructor to make a new card,
    and methods to draw the card (front or back) at a specified position on
    the table.
*/


public class CardSorter implements UIMouseListener {

    // Constants for the positions of the deck, the hand, and the discard pile
    public static final int DECK_X = 50;
    public static final int DECK_Y = 300;

    public static final int HAND_X = 150;
    public static final int HAND_Y = 300;
    public static final int HAND_SEP = 15;

    public static final int DISCARD_X = 200;
    public static final int DISCARD_Y = 50;

    // constants for drawing the board
    public static final Color FELT_COLOR = new Color(32, 112, 48);
    public static final int TABLE_WIDTH = 1000;
    public static final int TABLE_HEIGHT = 500;
    public static final int CARD_WIDTH = 75;
    public static final int CARD_HEIGHT = 110;
    
    public static final int SEP = 30;
    

    // the three collections of cards (the deck, the hand, and the discard)
    // YOUR CODE HERE
    private Stack<Card> deck;
    private List<Card> hand;
    private Stack<Card> discard_1;
    private Stack<Card> discard_2;
    private Stack<Card> discard_3;
    private Stack<Card> discard_4;
    // the currently selected card. (null if none selected)
    private Card selected; 

    /** Set up the mouse listener, and set up the
        the deck (a list of shuffled cards), the hand, and the discard pile,
    To set up the deck, it creates lots of new Cards, adds them to the stack,
    then shuffles it.
    (There is a method in Collections for shuffling lists and stacks!) 
    Then draws everything
    */
    public CardSorter() {
        UI.setMouseListener(this);
    // YOUR CODE HERE
        this.deck = new Stack<Card>();
        this.hand = new ArrayList<Card>();
        this.discard_1 = new Stack<Card>();
        this.discard_2 = new Stack<Card>();
        this.discard_3 = new Stack<Card>();
        this.discard_4 = new Stack<Card>();
        for(String suit: new String[]{"spades", "hearts", "diamonds", "clubs"}){
            for(int i = 1; i <= 13; i++){
                deck.add(new Card(suit, i));
            }
        }
        Collections.shuffle(deck);
        redraw();
        UI.println("Click on deck to move card to hand");
        UI.println("Click on hand to select card or move in hand");
        UI.println("Click on table to discard selected card");
    }

    /** All behaviour is determined by where the mouse is released, and whether
      there is a currently selected card.
    If the mouse position is on the deck stack, then
       take the top card from the deck and add it to the hand.
    If the mouse position is on a position of a card in the hand:
       it either selects the card, or moves the currently selected card
       to this place
    Otherwise
       if there is a currently selected card, then
         take the selected card out of the hand and put it on the discard pile
    */
    public void mousePerformed(String action, double x, double y) {
        if (action.equals("released")){
        // YOUR CODE HERE
           
            for(int i = hand.size() - 1; i >= 0; i--){
                if(pointAtPlace(x, y, HAND_X + i * HAND_SEP, HAND_Y)){
                    pickCardInHand(i);                    
                    break;
                }
            }
            
            if(pointAtPlace(x, y, DISCARD_X, DISCARD_Y)){
                moveToDiscard(discard_1);
            }
            
            if(pointAtPlace(x, y, DISCARD_X + CARD_WIDTH + SEP, DISCARD_Y)){
                moveToDiscard(discard_2);
            }
            
            if(pointAtPlace(x, y, DISCARD_X + 2*(CARD_WIDTH + SEP), DISCARD_Y)){
                moveToDiscard(discard_3);
            }
            
            if(pointAtPlace(x, y, DISCARD_X + 3*(CARD_WIDTH + SEP), DISCARD_Y)){
                moveToDiscard(discard_4);
            }
            
            if(pointAtPlace(x, y, DECK_X, DECK_Y)){
                takeFromDeck();
            }
            
            redraw();
        }
    
    }
    

    // Helper methods
    /** Move a card from the deck (if there is one) to the hand */
    public void takeFromDeck(){
    // YOUR CODE HERE
        hand.add(deck.pop());
    }

    /**   Move the selected card (if there is one) from the hand to the discard pile
    */
    public void moveToDiscard(Stack<Card> z){
    // YOUR CODE HERE
        if(selected != null){
            if(z.empty()){
                if(selected.getRank() == 1){
                    z.add(selected);
                    hand.remove(selected);
                    selected = null;
                }else{
                    return;
                }
            }else{
                if(z.get(z.size()-1).getSuit().equals(selected.getSuit())){
                    if(z.get(z.size()-1).getRank() == (selected.getRank())){
                        z.add(selected);
                        hand.remove(selected);
                        selected = null;
                    }
                }
            }
        }
    }

    /** Act on a card at the position index in the hand:
        if there is no card currently selected, then select the card.
        if there is a card selected, then move the selected card to this place.
    */
    public void pickCardInHand(int index){
    // YOUR CODE HERE
        if(selected != null){
            hand.remove(selected);
            hand.add(selected);
            selected = null;
        }
        else{
            selected = hand.get(index);
        }
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
        UI.setColor(Color.gray);
        UI.fillRect(DISCARD_X, DISCARD_Y,CARD_WIDTH, CARD_HEIGHT);
        UI.fillRect(DISCARD_X+(CARD_WIDTH + SEP), DISCARD_Y,CARD_WIDTH, CARD_HEIGHT);
        UI.fillRect(DISCARD_X+2*(CARD_WIDTH + SEP), DISCARD_Y,CARD_WIDTH, CARD_HEIGHT);
        UI.fillRect(DISCARD_X+3*(CARD_WIDTH + SEP), DISCARD_Y,CARD_WIDTH, CARD_HEIGHT);
        
    // YOUR CODE HERE
        for(int i = 0; i < deck.size(); i++){
            deck.get(i).drawBack(DECK_X, DECK_Y);
        }
        
        for(int i = 0; i < hand.size(); i++){
            if (hand.get(i) != selected){
                hand.get(i).draw(HAND_X + i * HAND_SEP, HAND_Y);
            } 
            else{
                selected.draw(HAND_X + i * HAND_SEP, HAND_Y - HAND_SEP);
            }
        }
        
       
        
        discard_1.get(discard_1.size() - 1).draw(DISCARD_X, DISCARD_Y);
        
        discard_2.get(discard_2.size() - 1).draw(DISCARD_X+(CARD_WIDTH + SEP), DISCARD_Y);
        
        discard_3.get(discard_3.size() - 1).draw(DISCARD_X+2*(CARD_WIDTH + SEP), DISCARD_Y);
        
        discard_4.get(discard_4.size() - 1).draw(DISCARD_X+3*(CARD_WIDTH + SEP), DISCARD_Y);
               
        UI.repaintGraphics();
    }
    

    public static void main(String[] args) {
        CardSorter sorter = new CardSorter();
    }
}
