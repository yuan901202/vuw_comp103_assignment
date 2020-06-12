import comp102.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This is the main class of the entire program. It sets up the input side
 * of the user interface, and contains a BSTSet object. Note that this 
 * BSTSet object is not null even if there are no actual nodes inside the tree.
 * 
 * Due to the size of the tree, there is not graphical display of the tree.
 * This class deals with input, and the output of responses to report queries.
 */

public class DictionaryApp implements UIButtonListener {

    // Fields
    private Set<String> dict = new BSTSet<String>();

    /** Construct a new DictionaryApp object
     *  Set up the GUI
     */
    public DictionaryApp() {

        UI.addButton("Load dictionary", this);
        UI.addButton("Check text spelling", this);
        UI.addButton("Display dictionary", this);
        UI.addButton("Save dictionary", this);
        UI.addButton("Depth of tree", this);
        UI.addButton("Remove bad words", this);

    }

    // GUI methods

    /** Respond to button presses */
    public void buttonPerformed(String button) {
        if (button.equals("Load dictionary")) {
            String fname = "unsorted-dictionary.txt";
            try {
                dict = new BSTSet<String>();
                Scanner scan = new Scanner(new File(fname));
                long start = System.currentTimeMillis();
                while (scan.hasNext()) {
                    dict.add (scan.next());
                }
                long adding = (System.currentTimeMillis() - start);
                UI.printf("-----------------\n Adding took: %.3f seconds\n", adding/1000.0);
                scan.close();
                UI.println("dictionary loaded");
            }
            catch(IOException ex) {
                UI.println("File loading failed: " + ex);
            } 
        }

        else if (button.equals("Check text spelling")) {
            String fname = "plato.txt";
            try {
                //load the words from the file into an arraylist
                String textWord;
                Scanner scan = new Scanner(new File(fname));
                long start = System.currentTimeMillis();
                while (scan.hasNext()) {
                    textWord = scan.next();
                    if (! dict.contains(textWord)) UI.println(textWord);
                }
                long checking = (System.currentTimeMillis() - start);
                UI.printf("-----------------\n Checking took: %.3f seconds\n", checking/1000.0);
                scan.close();
            }
            catch(IOException ex) {
                UI.println("File loading failed: " + ex);
            } 
        }

        else if (button.equals("Display dictionary")) {
            Iterator<String> itr = dict.iterator();
            long start = System.currentTimeMillis();
            while (itr.hasNext()) {
                UI.println(itr.next());
            }
            long displaying = (System.currentTimeMillis() - start);
            UI.printf("-----------------\n Displaying took: %.3f seconds\n", displaying/1000.0);
        }

        else if (button.equals("Save dictionary")) {
            try {
                PrintStream ps = new PrintStream(new File("dictionary.txt"));
                Iterator<String> itr = dict.iterator();
                while (itr.hasNext()) {
                    ps.println(itr.next());
                }
                ps.close();
                UI.println("dictionary saved into dictionary.txt");
            }
            catch(IOException ex) {
                System.out.println("File Saving failed: " + ex);
            }
        }

        else if (button.equals("Depth of tree")) {
            UI.println("height of tree is: " + ((BSTSet)dict).height());
        }

        else if (button.equals("Remove bad words")) {
            String fname = "bad-words.txt";
            try {
                Scanner scan = new Scanner(new File(fname));
                while (scan.hasNext()) {
                    dict.remove (scan.next());
                }
                scan.close();
                UI.println("bad words removed");
            }
            catch(IOException ex) {
                UI.println("File loading failed: " + ex);
            } 
        }
    }

    // Main
    public static void main(String[] arguments) {
        new DictionaryApp();
    }   

}
