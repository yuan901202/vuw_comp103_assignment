import java.util.*;


/** 
 * This GeneralTreeNode represents a simple node in a taxonomy. The node stores references 
 * to children and a parent (although the latter is null if this is the root node).
 * All of the complex operations on the tree structure are dealt with by GeneralTree, however.
 * 
 * For the purposes of one technique for drawing on the screen, there is a location field as well.
 * This is purely for drawing purposes, and is not directly related to the concept of the tree structure.
 * NOTE: There are other techniques for drawing the tree that don't require the location field, so you 
 * may decide not to use the location field/methods at all.
 * 
 * @author Monique and Stuart
 * @version 1.0
 */

public class GeneralTreeNode {

    // A reference to the parent node of this node. That parent node will also have a reference to this node. 
    private GeneralTreeNode parent;

    // Since this is a general tree, we don't have a limit on the number of children a node may have, so we use a set.
    private Set<GeneralTreeNode> children = new HashSet<GeneralTreeNode>();

    // This field is only used for one technique for drawing the tree on the screen.
    private Location location;  //location of the center of the node on the screen

    // The data that is being stored at the node.
    private String data;
    
    // Constructor

    public GeneralTreeNode(String str) {
        data = str;
    }
    
    // Methods

    public String getData() {
        return this.data;
    }
    
    public void setParent(GeneralTreeNode parent) {
        this.parent = parent;
    }

    public GeneralTreeNode getParent() {
        return this.parent;
    }

    public void setChildren(Set<GeneralTreeNode> children) {
        this.children = children;
    }

    public Set<GeneralTreeNode> getChildren() {
        return children;
    }

    // nb. the child will still exist, provided you have another ref to it.
    public void removeChild(GeneralTreeNode child) {
        children.remove(child);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
