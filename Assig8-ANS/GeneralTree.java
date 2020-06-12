import java.awt.Color;
import java.util.*;
import java.io.*;
import javax.swing.*;
import comp102.*;

/**
 * This GeneralTree class encapsulates all of the GeneralTreeNode objects.
 * 
 * @author Monique and Stuart 
 * @version 1.0
 */
public class GeneralTree
{
    private GeneralTreeNode root;
    private int windowWidth = UI.getCanvasWidth();
    private double nodeRad = 15;
    private int levelSep = 60;
    /**
     * The initial GeneralTree contains no nodes, so the root is set to null to reflect this.
     */
    public GeneralTree()
    {
        this.root = null;
    }

    /** 
     *  Find the node in the tree that contains the given string.
     *  Traverses the tree, looking at each node.
     *  If the target string equals the node's string, then return the node.
     *  If no node is found, then return null.
     *  Actually uses a private helper function that you will write yourselves.
     */
    private GeneralTreeNode findNode(String target) {
        return findNode(target, root);
    }

    
    /**
     * CORE
     * 
     * Find the node whose data is equal to the target string. If the target string appears multiple times, then just return the 
     * first one encountered. The addNode method should guarantee that duplicate strings aren't added however.
     * 
     * @param  target  the (assumed unique) string, that is in the node we want.  
     * @return the node that contains the target string (assumed to be unique in the tree), or null if no such node exists. If the string is somehow not
     * unique, then the first node to be encountered with this string will be returned. It cannot be guaranteed that this is the same node each time, if
     * duplicates exist.
     */
    
    private GeneralTreeNode findNode(String target, GeneralTreeNode node) {
        // YOUR CODE HERE
        if (node == null) {
            return null;
        }
        if (node.getData().equals(target)) {
            return node;
        }// this is the node we want
        for (GeneralTreeNode child : node.getChildren()) {
            GeneralTreeNode ans = findNode(target, child);
            if (ans != null)
                return ans;
        }
        // END OF YOUR CODE
        return null;  // it wasn't found;
    }

    // Methods acting on nodes of the tree.
    
    /** 
     *  CORE 
     *  
     *  Add a new node (with the data string stored in it) as a child to the node identified with the parent string. 
     *  The new node should have no children. There is one exception to this however. If the parent string is null, then
     *  the assumption is that we're adding a new root node. In this case, if there is already a valid root node, 
     *  then this old root node should become child of the new node, and the new node becomes the root of the tree.
     *  The new node should only be added into the tree if the data string is unique, and doesn't already appear in the tree.
     *  
     *  Do nothing if the the parent string is not null, but no node contains that parent string. 
     */
    public void addNode(String data, String parent) {
        // YOUR CODE HERE
        if (findNode(data) != null) {
            UI.println("Can't add " + data + " as it already exists in the tree");
            return;
        }
        
        GeneralTreeNode newNode = new GeneralTreeNode(data);
        if (parent == null) {
            GeneralTreeNode tempRoot = root;
            if (tempRoot != null) {
                newNode.getChildren().add(tempRoot);
                tempRoot.setParent(newNode);
            }
            root = newNode;
        } else {
            GeneralTreeNode parentNode = this.findNode(parent);
            if (parentNode == null) return;
            parentNode.getChildren().add(newNode);
            newNode.setParent(parentNode);
        }
        // END OF YOUR CODE
    }

    /** 
     *  CORE
     *  
     *  This method removes the node containing the target string (usual assumptions of uniqueness apply). 
     *  The target node's children must become children of it's parent.
     *  Do nothing if the node is the root node of the entire tree (or if target node doesn't exist). 
     */
    public void removeNode(String target) {
        // YOUR CODE HERE
        // Remove the node and transfer the children set of its parent.
        GeneralTreeNode targetNode = this.findNode(target);
        if ((targetNode == null) || (targetNode == root)) 
            return;
        GeneralTreeNode parent = targetNode.getParent();
        // remove node from its parent's set of children.
        parent.getChildren().remove(targetNode);
        // add children of node to children set of its parent.
        for(GeneralTreeNode child : targetNode.getChildren()) {
            parent.getChildren().add(child);
            child.setParent(parent);
        }
        // END OF YOUR CODE
    }


    /** 
     *  COMPLETION
     * 
     *  Move the target node to be a child of the destination node, along with
     *  the subtree under node.
     *  Note that this is a problem if the "to" node is in the subtree
     *  treeed at node (including if the "to" node is the same as node)
     *  Testing for this situation is an extension. 
     */
    public void moveNode(String target, String destination) {
        // YOUR CODE HERE
        GeneralTreeNode node = this.findNode(target);
        GeneralTreeNode to = this.findNode(destination);
        if ((node == null) || (node == root) || (to == null))
            return;
        if (inSubtree(to, node))  // can't move node into its own subtree!
            return;
        GeneralTreeNode parent = node.getParent();
        // Remove pressedNode from its current parent's set of children.
        parent.getChildren().remove(node);
        // Add pressedNode to the children of node
        to.getChildren().add(node);
        // pressedNode's parent is now node
        node.setParent(to);
        // END OF YOUR CODE
    }

    
    /** 
     *  COMPLETION
     * 
     *  This method returns true or false depending on whether the node in the first parameter is actually in the subtree whose root 
     *  is the node in the second parameter. This method is used by moveNode(...), to ensure that we aren't trying to move a node 
     *  (and hence the subtree rooted at that node) in a way that makes it become a child of one of it's existing descendants. 
     *  
     *  @return true if the node is in the subtree, and false otherwise
     *  
     */
    private boolean inSubtree(GeneralTreeNode node, GeneralTreeNode subtree) {
        // YOUR CODE HERE
        if (node == subtree) return true;
        for(GeneralTreeNode child : subtree.getChildren()) {
            if (inSubtree(node, child))
                return true;
        }
        // END OF YOUR CODE
        return false;
    }

    // Methods for reports, and saving/loading
    /** 
     *  CORE
     *  
     *  Print the strings of all the nodes under the given target node 
     *  (including the target node itself).
     *  
     *  @param target the assumed unique string that identifies the desired node. As elsewhere, if the 
     *  condition that strings are unique in the tree is broken, then it is acceptable to print out the strings 
     *  in the first matching subtree found.
     *  
     */
    public void listSubtree(String target) {
        // YOUR CODE HERE
        GeneralTreeNode targetNode = this.findNode(target);
        if (targetNode != null) {
            listSubtree(targetNode);
        }
    }

    private void listSubtree(GeneralTreeNode node) {
        UI.println("   " + node.getData());
        // for each child:
        for (GeneralTreeNode child : node.getChildren()) 
            listSubtree(child);
        // END OF YOUR CODE
    }
    
    /** 
     *  COMPLETION
     * 
     *  List the names of all the nodes in the chain/path from this node to the root of the entire tree. You may report them in 
     *  either top-down or bottom-up order.
     */
    public void listChain(String target) {
        // YOUR CODE HERE
        GeneralTreeNode targetNode = this.findNode(target);
        if (targetNode == null) return;
        GeneralTreeNode tempNode = targetNode;
        UI.println("Chain: ");
        UI.println("   " + targetNode.getData());
        while (tempNode != root) {
            UI.println("   " + tempNode.getParent().getData());
            tempNode = tempNode.getParent();
        }
        // END OF YOUR CODE
    }

    /** 
     *  COMPLETION
     *  
     *  Print all the string data of all the nodes at the given depth. Should print out an error message if the depth requested is < 0.
     *  Print nothing if the depth >= 0, but if there are no nodes at that depth.
     * 
     *  @param depth the depth of the tree whoses nodes are to be listed. The root is at depth 0.
     */
    public void listDepth(int depth) {
        // YOUR CODE HERE
        if (depth < 0) {
            UI.println("Invalid depth - cannot be negative");
            return;
        }
        listDepth(depth, 0, root);
    }

    private void listDepth(int target, int current, GeneralTreeNode node) {
        if (node == null) return;
        if (current == target) {
            UI.println("   " + node.getData());
            return;
        }
        // for each child:
        current++;
        for (GeneralTreeNode child : node.getChildren()) 
            listDepth(target, current, child);
        // END OF YOUR CODE
    }

    /** 
     * CHALLENGE
     * 
     * Given two nodes (identified here by the assumed unique strings stored in those nodes), return the string at a third node that 
     * is the closest common ancestor of both of the first two nodes. The closest common ancestor is the node that is the root of the 
     * smallest subtree that contains both the first two nodes. The closest common ancestor could even be one of the first two nodes identified by the parameters. Note that this can only 
     * return null if one of the targets doesn't exist, as the tree's root node is the last resort as a common ancestor to all nodes in the tree.
     * 
     * @param target1   the (assumed unique) string in the first node.
     * @param target2   the (assumed unique) string in the second node. 
     * @return the string data at the closest common ancestor node, or null if one or both of the parameter's target nodes don't exist.
     */
    public String findClosestCommonAncestor(String target1, String target2) {
        // YOUR CODE HERE
        GeneralTreeNode n1 = this.findNode(target1);
        GeneralTreeNode n2 = this.findNode(target2);
        if (n1 == null || n2 == null) {
            return null;
        }
        GeneralTreeNode tempNode = n1;
        while (tempNode != root && !inSubtree(n2, tempNode)) {
            tempNode = tempNode.getParent();
        }
        return tempNode.getData();
        // END OF YOUR CODE
    }

    /**
     *  Save the whole tree in a file in a format that it can be loaded back in
     *  and reconstructed 
     */
    public void save() {
        String fname = UIFileChooser.save("Filename to save text into");
        if (fname == null) {
            JOptionPane.showMessageDialog(null, "No file name specified");
            return;
        }
        try {
            PrintStream ps = new PrintStream(new File(fname));

            // then write to it with a recursive method.
            saveHelper(root, ps);
            ps.close();
        }
        catch(IOException ex) {
            UI.println("File Saving failed: " + ex);
        }
    }

    private void saveHelper(GeneralTreeNode node, PrintStream ps) {
        if (node == null) {
            return; // This shouldn't happen anyway.
        }
        // Print out the data string for this node, and how many child nodes there are
        ps.println(node.getData() + " " + node.getChildren().size());

        for (GeneralTreeNode child : node.getChildren()) {
            saveHelper(child, ps);
        }
    }

    /**
     *  Construct a new tree loaded from a file.
     *  
     *  @param scan The scanner connected to the input stream of the file to be loaded in from. 
     */
    public void load(Scanner scan) {
        if (scan.hasNext()) {
            root = loadHelper(scan.next(), scan);
        }
        return;
    }

    private GeneralTreeNode loadHelper(String data, Scanner scan) {
        GeneralTreeNode node = new GeneralTreeNode(data);
        int numChildren = scan.nextInt();
        String junk = scan.nextLine();
        for (int i=0; i<numChildren; i++) {
            GeneralTreeNode child = loadHelper(scan.next(), scan);
            child.setParent(node); // set the child's parent to node.
            node.getChildren().add(child); // tell node to add a child.
        }
        return node;
    }


    // Drawing the tree
    /** Redraw the chart.
     *  First step is to calculate all the locations of the nodes in the tree.
     *  Then traverse the tree to draw all the nodes and lines between parents and children.
     */
    public void redraw() {
        UI.clearGraphics();
        if (root != null) {
            calculateLocations();
            redrawAllNodes(root);
        }
    }
    
    /** 
     *  CORE
     * 
     *  The earlier redraw() method handles computation of where the nodes should go. Now, we actually need to draw each node at the location
     *  stored within it, and then draw lines between parents and children.
     *  This should be a a recursive method to draw all nodes in a subtree. The provided code just draws the tree node; you need to make it draw all the nodes 
     */
    private void redrawAllNodes(GeneralTreeNode node) {
        // YOUR CODE HERE
        GeneralTreeNode parent = node.getParent();
        if (parent != null) {
            UI.drawLine(node.getLocation().getX(), node.getLocation().getY(), parent.getLocation().getX(), parent.getLocation().getY());
        }
        for (GeneralTreeNode child : node.getChildren()) {  
            redrawAllNodes(child);
        }
        // END OF YOUR CODE
        redrawNode(node);
    }
    
    // Helper methods for calculating the layout of the tree.
    /** Calculate locations for each node in the tree.
     *  It does not do a nice job - it just lays out all the nodes on
     *   each level evenly across the width of the window.
     *  It also assumes that the depth of the tree is at most 100 
     */
    private void calculateLocations() {
        int[] widths = new int[100];
        computeWidths(root, 0, widths);

        int[] separations = new int[100];  // separations between nodes at each level
        for (int d=0; d<100; d++) {
            if (widths[d] == 0) break;
            separations[d] = windowWidth / widths[d];
        }

        int[] nextPos = new int[100];  // loc of next node at each level
        for (int d=0; d<100; d++) {
            if (widths[d] == 0) break;
            nextPos[d] = separations[d] / 2;
        }

        setLocations(root, 0, nextPos, separations);
    }

    /** Compute the number of nodes at each level of the tree,
     *  by accumulating the count in the widths array 
     */
    private void computeWidths(GeneralTreeNode node, int depth, int[] widths) {
        widths[depth]++;
        for (GeneralTreeNode child : node.getChildren()) {
            computeWidths(child, depth+1, widths);
        }
    }

    /** Set the location of each nodes at each level of the tree,
     *  using the depth and positions
     */
    private void setLocations(GeneralTreeNode node, int depth, int[] nextPos, int[] separations) {
        node.setLocation(new Location(nextPos[depth], levelSep*depth+levelSep/2));
        nextPos[depth] += separations[depth];
        for (GeneralTreeNode child : node.getChildren())
            setLocations(child, depth+1, nextPos, separations);
    }
    
    /**
     * Draws a node at the location stored in that node. Drawing the node consists of drawing a circle, and writing the data string
     * out "in" that circle. Note the circle doesn't really know the length of the string, so this could look ugly.
     * 
     * @param node  the node to draw on the screen. This node should already have had it's location set earlier on.
     */
    private void redrawNode(GeneralTreeNode node) {
        double x = node.getLocation().getX();
        double y = node.getLocation().getY();
        UI.setColor(Color.white);
        UI.fillOval(x-nodeRad, y-nodeRad, nodeRad*2-1, nodeRad*2-1);
        UI.setColor(Color.black);
        UI.drawOval(x-nodeRad, y-nodeRad, nodeRad*2-1, nodeRad*2-1);

        UI.drawString(node.getData(), x-8, y+5);
    }


    
}
