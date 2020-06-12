/* Code for COMP103 Assignment 2
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import comp102.UI;

public class Order {

    /** the items that are wanted for the order */
    //private boolean wantsFish;
    //private boolean wantsChips;
    //private boolean wantsBurger;

    /** the items that have been added and are ready in the order */
    //private boolean hasFish;
    //private boolean hasChips;
    //private boolean hasBurger;
    
    private List<String> itemsWanted = new ArrayList<String>();       
    private List<String> itemsReady = new ArrayList<String>();
    
    public Order() {

        int items = 1 + (int)(Math.random() * 6);
        for (int i = 0; i < items; i++) {
            int choice = (int)(Math.random() * 3);
            if (choice==0) {
                itemsWanted.add("Fish");
            }    
            else if (choice==1) {
                itemsWanted.add("Chips");
            }
            else if (choice==2) {
                itemsWanted.add("Burger");
            }
        }
    }

    /** The order is ready as long as there every item that is
     *  wanted is also ready.
     */
    public boolean isReady() {
        return itemsWanted.isEmpty();
    }


    /** If the item is wanted but not already in the order,
     *  then put it in the order and return true, to say it was successful.
     *  If the item not wanted, or is already in the order,
     *  then return false to say it failed.
     */
    public boolean addItemToOrder(String item){
        if (itemsWanted.contains(item)){
            itemsWanted.remove(item);
            itemsReady.add(item);
            return true;
        }
        return false;
    }

    /** Computes and returns the price of an order.
     *  Core: Uses constants: 2.50 for fish, 1.50 for chips, 5.00 for burger
     *  to add up the prices of each item
     *  Completion: Uses a map of prices to look up prices
     */
    public double getPrice() {
        double price = 0;
        for (String item : itemsReady) {
            if (item.equals("Fish")) {
                price += 2.50;
            }
            if (item.equals("Chips")){ 
                price += 1.50;
            }
            if (item.equals("Burger")){ 
                price += 5.00;
            }
        }
        return price;
    }


    public void draw(int y) {
        int x = 15;
        for (String item : itemsReady){
            UI.drawImage(item+".png", x, y);
            x += 35;
        }
        for (String item : itemsWanted){
            UI.drawImage(item+"-grey.png", x, y);
            x += 35;
        }
    }
}
