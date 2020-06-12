import comp102.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This is the main class of the entire program. It sets up the input side
 * of the user interface, and contains a GeneralTree object. Note that this 
 * GeneralTree object is not null even if there are no actual nodes inside the tree.
 * 
 * The actual display of the tree is handled in the GeneralTree class itself.
 * This class deals with input, and the output of responses to report queries.
 */

public class TaxonomyApp implements UIButtonListener {

    // Fields
    private GeneralTree tree;  // the tree of the current organisational chart

    /** Construct a new TaxonomyApp object
     *  Set up the GUI
     */
    public TaxonomyApp() {

        tree = new GeneralTree();
        
        // General buttons
        UI.addButton("New taxonomy", this);
        UI.addButton("Load taxonomy", this);
        UI.addButton("Save taxonomy", this);

        // Nodes buttons
        UI.addButton("Add node", this);
        UI.addButton("Remove node", this);
        UI.addButton("Move node", this);

        // Reports buttons
        UI.addButton("Report all below", this);
        UI.addButton("Report all above", this);
        UI.addButton("Report all same depth nodes", this);
        UI.addButton("Find closest ancestor", this);

        tree.redraw();
    }

    // GUI methods

    /** Respond to button presses */
    public void buttonPerformed(String button) {
        if (button.equals("New taxonomy")) {
            String rootString = UI.askString("name of taxonomy:");
            tree = new GeneralTree();
            tree.addNode(rootString, null);
            tree.redraw();
        }
        else if (button.equals("Load taxonomy")) {
            String fname = UIFileChooser.open("Filename to read text from");
            if (fname == null) {
                JOptionPane.showMessageDialog(null, "No file name specified");
                return;
            }
            try {
                Scanner scan = new Scanner(new File(fname));
                tree.load(scan);
                scan.close();
            }
            catch(IOException ex) {
                UI.println("File loading failed: " + ex);
            } 
            tree.redraw();
        }
        else if (button.equals("Save taxonomy")) {
            tree.save();
        }
        else if (button.equals("Add node")) {
            String parent = UI.askString("parent to add under:");
            String data = UI.askString("string to add under " + parent + ":");
            tree.addNode(data, parent);
            tree.redraw();
        }
        else if (button.equals("Remove node")) {
            String target = UI.askString("string to remove:");
            tree.removeNode(target);
            tree.redraw();
        }
        else if (button.equals("Move node")) {
            String target = UI.askString("String at root of subtree:");
            String destination = UI.askString("To new parent destination");
            tree.moveNode(target, destination);
            tree.redraw();
        }
        //Reports
        else if (button.equals("Report all below")) {
            UI.println("Subtree:");
            String subtreeRoot = UI.askString("String at root of subtree:");
            tree.listSubtree(subtreeRoot);
        }
        else if (button.equals("Report all above")) {
            String target = UI.askString("target string:");
            tree.listChain(target);
        }
        else if (button.equals("Report all same depth nodes")) {
            int depth = UI.askInt("Give depth:");
            tree.listDepth(depth);
        }
        else if (button.equals("Find closest ancestor")) {
            String target1 = UI.askString("First string:");
            String target2 = UI.askString("Second string:");
            String closestAncestor = tree.findClosestCommonAncestor(target1, target2);
            UI.println("closest ancestor: " + closestAncestor);
        }
    }

    // Main
    public static void main(String[] arguments) {
        new TaxonomyApp();
    }   

}
