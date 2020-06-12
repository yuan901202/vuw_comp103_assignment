import comp102.*;
import java.util.*;
import java.io.*;

public class ExerciseNumberList implements UIButtonListener {

    private List<Double> currentValues; // the list of numbers
    
    public ExerciseNumberList() {
        UI.addButton("Add Last", this);
        UI.addButton("Add First", this);
        UI.addButton("Remove Last", this);
        UI.addButton("Remove First", this);
        UI.addButton("Add At Position", this);
        UI.addButton("Remove Number", this);
        UI.addButton("Remove From Position", this);
        UI.addButton("Promote Number", this);

        // Create a list and put values in it.
        currentValues = new ArrayList<Double>();
        for (double value : new double[]{0.58, 1.41, 1.62, 2.72, 3.14}){
            currentValues.add(value);
        }
        // Print the values in the list
        printList();
    }
    
    public void printList() {
        for (int i = 0; i<currentValues.size(); i++){
            UI.printf("%d: %5.2f \n", i, currentValues.get(i));
        }
        UI.println("----");
    }
    
    public void buttonPerformed(String button) {
	if (button.equals("Add Last")) {
            // Add a number to the end of the list
            double toAdd = UI.askDouble("What number would you like to add?");
            currentValues.add(toAdd);
        }
        else if (button.equals("Add First")) {
            // Adds a number at the beginning of the list
            double toAdd = UI.askDouble("What number would you like to add?");
	    // YOUR CODE HERE
            currentValues.add(0, toAdd);
	    // END OF YOUR CODE
        }
        else if (button.equals("Remove First")) {
            // Remove the number at the start of the list
            // YOUR CODE HERE
            if (!currentValues.isEmpty()) {
                currentValues.remove(0);
            }
            // END OF YOUR CODE
        }
        else if (button.equals("Remove Last")) {
            // Remove the number at the end of the list
            // YOUR CODE HERE
            if (!currentValues.isEmpty()) {
                currentValues.remove(currentValues.size() - 1);
            }
            // END OF YOUR CODE
        }
        else if (button.equals("Add At Position")) {
            // Add a number at a position
            double toAdd = UI.askDouble("What number would you like to add?");
            int position = UI.askInt("Which position would you like to add it?");
            // YOUR CODE HERE
            if (position > 0 && position < currentValues.size()) {
                currentValues.add(position, toAdd);
            }
            // END OF YOUR CODE
        }
        else if (button.equals("Remove Number")) {
            // Remove a particular number from the list
            double toRemove = UI.askDouble("Which number would you like to remove?");
            // YOUR CODE HERE
            currentValues.remove(toRemove); // make sure you use the right remove!
            // END OF YOUR CODE
        }
        else if (button.equals("Remove From Position")) {
            // Remove the number at a position from the list
            int position = UI.askInt("Which position would you like to remove?");
            // YOUR CODE HERE
            currentValues.remove(position); // make sure you use the right remove!
            // END OF YOUR CODE
        }
        else if (button.equals("Promote Number")) {
            // Move a particular number one position earlier in the list
            double toPromote = UI.askDouble("Which number would you like to promote?");
            // YOUR CODE HERE
	    int index = currentValues.indexOf(toPromote);
	    if (index==-1){
		UI.println("That number is not in the list");
	    }
	    else if (index==0){
		UI.println("That number is already top of the list");
	    }
	    else{
		currentValues.remove(index); 
		currentValues.add(index-1, toPromote);
	    }
            // END OF YOUR CODE
        }
   
        printList();
    }

    public static void main(String args[]) {
        new ExerciseNumberList();
    }
}
