/* Code for COMP103 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import comp102.UI;
import java.util.*;
import java.awt.Color;

/** Represents
    When an order is constructed, it will have a collection of items
     *  added to the the itemsWanted list.
     *  Each time an item is then added to the order, it is moved from the
     *  itemsWanted list to the itemsReady list
     *  The order is ready when there are no items left in the itemsWanted list,
     */ 
public class Order {


    /** The items that are still wanted by the customer, 
     *  and the items that are ready for delivery to the customer.
     */ 
    // YOUR CODE HERE
    private List<String> itemsWanted = new ArrayList<String>();       
    private List<String> itemsReady = new ArrayList<String>();       
    // END OF YOUR CODE
    
    /**
     * When an order is constructed, it has a random number
     * (between 1 and 6) of items on the itemsWanted list.
     */
    public Order() {
	// YOUR CODE HERE
	int numItems = 1+ (int)(Math.random()* 6);
	for (int i=0; i<numItems; i++){
	    int choice = (int)(Math.random() * 3);
	    if (choice==0) itemsWanted.add("Fish");
	    else if (choice==1) itemsWanted.add("Chips");
	    else if (choice==2) itemsWanted.add("Burger");
	}
	// END OF YOUR CODE
    }

    /** The order is ready as long as there are no itemsWanted
     */
    public boolean isReady() {
	// YOUR CODE HERE
	return itemsWanted.isEmpty();
	// END OF YOUR CODE
    }

    /** Moves the specified item from the itemsWanted list (if the item is present)
     *  to the itemsReady list, and returns true, to say it was successful.
     *  If there is no such item in itemsWanted, then it returns false to say it failed.
     */
    public boolean addItemToOrder(String item) {
	// YOUR CODE HERE
	if (itemsWanted.contains(item)){
	    itemsWanted.remove(item);
	    itemsReady.add(item);
	    return true;
	}
	return false;
	// END OF YOUR CODE
    }

    /** Computes and returns the price of an order.
     *	Core: Uses constants: 2.50 for fish, 1.50 for chips, 5.00 for burger
     *  to add up the price of each item on the itemsReady list
     *  Completion: Uses a map of prices in FastFood.Prices to look up prices
     */
    public double getPrice() {
	// YOUR CODE HERE
	double price = 0;
	for (String item : itemsReady) {
	    if (item.equals("Fish")) price += 2.50;
	    if (item.equals("Chips")) price += 1.50;
	    if (item.equals("Burger")) price += 5.00;
	    //	    price += FastFood.Prices.get(item);
	}
	return price;
	// END OF YOUR CODE
    }

    /** Draws the order, showing the ready items in black followed by
     *	the wanted ones in grey
     *  The argument is the vertical position where the order should be drawn.
     */
    public void draw(int y) {
	// YOUR CODE HERE
	int x = 10;
	for (String item : itemsReady){
	    UI.drawImage(item+".png", x, y);
	    x += 50;
	}
	for (String item : itemsWanted){
	    UI.drawImage(item+"-grey.png", x, y);
	    x += 50;
	}
	// END OF YOUR CODE
    }

}
