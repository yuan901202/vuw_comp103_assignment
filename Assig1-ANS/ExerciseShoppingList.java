// YOUR CODE HERE
import comp102.*;
import java.util.*;
import java.io.*;

public class ExerciseShoppingList implements UIButtonListener {

    private List<String> shopping; // the list of numbers

    private String selected = "";
    
    public ExerciseShoppingList() {
        UI.addButton("Add item", this);
        UI.addButton("Select item", this);
        UI.addButton("Remove item", this);
        UI.addButton("Move item up", this);
        UI.addButton("Move item down", this);

        // Create an empty list.
        shopping = new ArrayList<String>();
        printList();
    }
    
    public void printList() {
        UI.println("----To Buy----");
	for (String item : shopping){
	    if (item.equals(selected)) {
		UI.print("*");
	    }
            UI.println(item);
        }
        UI.println("----");
    }
    
    public void buttonPerformed(String button) {
	if (button.equals("Add item")) {
            String item = UI.askString("What is the new item?");
            shopping.add(item);
        }
        else if (button.equals("Select item")) {
	    selected = UI.askString("Which item do you want to select?");
	}
        else if (button.equals("Remove item")) {
            int index = shopping.indexOf(selected);
	    if (index>-1){
                shopping.remove(index);
            }
        }
        else if (button.equals("Move item up")) {
            int index = shopping.indexOf(selected);
	    if (index>0){
                shopping.remove(index);
		shopping.add(index-1, selected);
            }
        }
        else if (button.equals("Move item down")) {
            int index = shopping.indexOf(selected);
	    if (index>-1 && index<shopping.size()-1){
                shopping.remove(index);
		shopping.add(index+1, selected);
            }
        }
        printList();
    }

    public static void main(String args[]) {
        new ExerciseShoppingList();
    }
}
// END OF YOUR CODE
