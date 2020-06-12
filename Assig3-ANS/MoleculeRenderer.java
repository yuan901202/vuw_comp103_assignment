/* Code for COMP103, Assignment 3
 * Name: 
 * Usercode: 
 * ID: 
 */

import comp102.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** Program to render a molecule on the graphics pane from different possible
 *  perspectives.
 *  A molecule consists of a collection of atoms.
 *  Each atom has a type (eg, Carbon, or Hydrogen, or Oxygen, ..),
 *  and a three dimensional position in the molecule (x, y, z).
 *
 *  Each molecule is described in a file by a list of atoms and their positions.
 *  The molecule is rendered by drawing a colored circle for each atom.
 *  The size and color of each atom is determined by the type of the atom.
 * 
 *  To make sure that the nearest atoms appear in front of the furthest atoms,
 *  the atoms must be rendered in order from the furthest away to the nearest.
 *  Each viewing perspective imposes a different ordering on the atoms.
 *
 *  The description of the size and color for rendering the different types of
 *  atoms is stored in the file "element-table.txt" which should be read and
 *  stored in a Map.  When an atom is rendered, the type should be looked up in
 *  the map to find the size and color to pass to the atom's render() method
 * 
 *  A molecule can be rendered from different perspectives, and the program
 *  provides four buttons to control the perspective of the rendering:
 *   "Front" renders the molecule from the front (perspective = 0 degrees)
 *   "Back" renders the molecule from the back (perspective = 180 degrees)
 *   "Left" renders the molecule from the left (perspective = -90 degrees)
 *   "Right" renders the molecule from the right (perspective = 90 degrees)
 *   "PanLeft" decreases the perspective of the rendering by 5 degrees,
 *   "PanRight" increases the perspective of the rendering by 5 degrees,
 */

public class MoleculeRenderer implements UIButtonListener {

    // Fields
    // Map containing the size and color of each atom type.
    private Map<String,Element> elementTable; 

    // The collection of the atoms in the current molecule
    private List<Atom> molecule;  

    private double currentAngle = 0.0;    //current viewing angle (in degrees)

    private double panStep = 5.0;
  
    // Constructor:
   /** Set up the Graphical User Interface and read the file of element data of
     *	each possible type of atom into a Map: where the type is the key
     *  and an ElementInfo object is the value (containing size and color).
     */
    public MoleculeRenderer() {
	UI.addButton("Read", this);
	UI.addButton("FromFront", this);
	// YOUR CODE HERE
	UI.addButton("FromBack", this);
	UI.addButton("FromLeft", this);
	UI.addButton("FromRight", this);
	UI.addButton("PanLeft", this);
	UI.addButton("PanRight", this);
	// END OF YOUR CODE
	readElementTable();    //  Read the element table first
    }


    /** Respond to button presses.
     *  Most of the presses will set the currentAngle and sort the list of molecules
     *  using the appropriate comparator
     */
    public void buttonPerformed(String button) {
	if (button.equals("Read")) {
	    currentAngle = 0;
	    String filename = UIFileChooser.open();
	    readMoleculeFile(filename);
	    Collections.sort(molecule, new BackToFrontComparator());
	}
	else if (button.equals("FromFront")) {  // set the angle and sort from back to front
	    currentAngle = 0;
	    Collections.sort(molecule, new BackToFrontComparator());
	}
	// YOUR CODE HERE
	else if (button.equals("FromLeft")) { // set the angle and sort from right to left
	    currentAngle = -90;
	    Collections.sort(molecule, new RightToLeftComparator());
	}
	else if (button.equals("FromRight")) { // set the angle and sort from left to right
	    currentAngle = 90;
	    Collections.sort(molecule, new LeftToRightComparator());
	}
	else if (button.equals("FromBack")) { // set the angle and sort from front to back
	    currentAngle = 180;
	    Collections.sort(molecule, new FrontToBackComparator());
	}
	else if (button.equals("PanLeft")) {   // decrement the angle and sort 
	    currentAngle -= panStep;
	    Collections.sort(molecule, new PerspectiveComparator());
	}
	else if (button.equals("PanRight")) {   // increment the angle and sort 
	    currentAngle += panStep;
	    Collections.sort(molecule, new PerspectiveComparator());
	}
	// END OF YOUR CODE

	// render the molecule according to the current ordering
	render();
    }


    /** Reads the molecule data from a file containing one line for each atom in
     *  the molecule.
     *  Each line contains an atom type and the 3D coordinates of the atom.
     *  For each atom, it constructs an Atom object,
     *   and adds it to the List of Atoms in the molecule.
     *  To get the color and the size of each atom, it has to look up the type
     *   of the atom in the Map of elements.
     */
    public void readMoleculeFile(String fname) {
	try {
	    // YOUR CODE HERE
	    if (fname == null) {
		UI.println("Reading molecule file failed: no file specified");
		return;
	    }
	    molecule = new ArrayList<Atom>();
	    UI.println("Reading molecule file....");
	    Scanner scan = new Scanner(new File(fname));
	    int failedAtoms = 0;
	    while (scan.hasNext()) {
		String type = scan.next();
		double x = scan.nextDouble();
		double y = scan.nextDouble();
		double z = scan.nextDouble();
		Element elem = elementTable.get(type);
		if (elem == null) {
		    UI.println("Unknown atom type in molecule file: " + type);
		    failedAtoms++;
		    continue;
		}
		Atom atom = new Atom(x, y, z, elem.color, elem.radius);
		molecule.add(atom);
	    }
	    scan.close();
	    UI.print("Reading molecule file " + fname + " read "
		     + molecule.size() + " atoms");
	    if (failedAtoms > 0)
		UI.print(", failed on "+failedAtoms);
	    UI.println();
	}
	// END OF YOUR CODE

	catch(IOException ex) {
	    UI.println("Reading molecule file " + fname + " failed");
	}
    }
    
    /** (Completion) Reads a file containing radius and color information about each type of
     *	atom and stores the info in a Map, using the atom type as a key
     */
    private void readElementTable() {
	UI.println("Reading element table file ...");
	try {
	    // YOUR CODE HERE
	    elementTable = new HashMap<String,Element>();
	    Scanner scan = new Scanner(new File("element-table.txt"));
	    while (scan.hasNext()) {
		Element elem = new Element(scan);
		elementTable.put(elem.name, elem);
	    }
	    UI.println("Reading element table file: " + elementTable.size() 
		       + " atom types found");
	    scan.close();
	    // END OF YOUR CODE
	}
	catch (IOException ex) {
	    UI.println("Reading element table file FAILED");
	}
    }
    
    /** Render the molecule, according the the current ordering of Atoms in the
     *  List.
     *  The Atom's render() method needs the current perspective angle 
     */
    public void render() {
	UI.clearGraphics();
	for(Atom atom : molecule) {
	    atom.render(currentAngle);
	}
	UI.repaintGraphics();
    }

    // Private comparator classes
    // You will need a comparator class for each different direction
    // used in the buttonPerformed method.
    //
    // Each comparator class should be a Comparator of Atoms, and will define
    // a compare method that compares two atoms.
    // Each comparator should have a compare method.
    // Most of the comparators do not need an explicit constructor and have no fields.
    // However, the comparator for the pan methods may need a field and a constructor

    /** Comparator that will order atoms from back to front */
    private class BackToFrontComparator implements Comparator<Atom> {
	/**
	 * Uses the z coordinates of the two atoms
	 * larger z means towards the back,
	 * smaller z means towards the front
	 * Returns
	 *  negative if atom1 is more to the back than atom2, (
	 *  0 if they are in the same plane,
	 *  positive if atom1 is more to the front than atom2.
	 */
    // YOUR CODE HERE
	public int compare(Atom atom1, Atom atom2) {
	    if ( atom1.getZ() > atom2.getZ() ) return -1;
	    if ( atom1.getZ() < atom2.getZ() ) return 1;
	    return 0;
	}
    }


    /** Comparator that will order atoms from front to back */
    private class FrontToBackComparator implements Comparator<Atom> {

	/**
	 * Uses the z coordinates of the two atoms
	 * larger z means towards the back,
	 * smaller z means towards the front
	 * Returns
	 *  negative if atom1 is more to the front than atom2, (
	 *  0 if they are in the same plane,
	 *  a positive number if atom1 is more to the back than atom2.
	 */
	public int compare(Atom atom1, Atom atom2) {
	    if ( atom1.getZ() < atom2.getZ() ) return -1;
	    if ( atom1.getZ() > atom2.getZ() ) return 1;
	    return 0;
	}
    }

    /** Comparator that will order atoms from left to right */
    private class LeftToRightComparator implements Comparator<Atom> {
	/** 
	 *  uses the x coordinates of the two atom
	 *  larger x means more towards the right,
	 *  smaller x means more towards the left.
	 *  Returns:
	 *   negative if atom1 is left of atom2, 
	 *   positive if atom 1 is right of atom2.
	 *   0 if they are vertically aligned, 
	 */
	public int compare(Atom atom1, Atom atom2) {
	    if ( atom1.getX() < atom2.getX() ) return -1;
	    if ( atom1.getX() > atom2.getX() ) return 1;
	    return 0;
	}
    }

    /** Comparator that will order atoms from right to left */
    private class RightToLeftComparator implements Comparator<Atom> {

	/**
	 * Uses the x coordinates of the two atoms
	 * larger x means more towards the right,
	 * smaller x means more towards the left.
	 * Returns:
	 *  negative if atom1 is more to the right than atom2,
	 *  0 if they are aligned,
	 *  positive if atom 1 is more to the left than atom2.
	 */
	public int compare(Atom atom1, Atom atom2) {
	    if ( atom1.getX() > atom2.getX() ) return -1;
	    if ( atom1.getX() < atom2.getX() ) return 1;
	    return 0;
	}
    }

    /** Comparator that will order atoms from further to nearer when viewed from
     *  a given perspective.
     *  Needs:
     *  - a constructor that stores the perspective angle,
     *  - a compare method.
     */
    private class PerspectiveComparator implements Comparator<Atom> {
	/* You can give this constructor a field to hold the currentAngle,
	   but it can also access the field of the enclosing class directly.

	  private double currentAngle;
  	  public PerspectiveComparator(double angle) {
	      currentAngle = angle;
	  }
	*/
	/** Returns
	 *  a negative number if atom1 is further than atom2 from this angle
	 *  0 if they are at the same distance,
	 *  a positive number if atom1 is nearer than atom2 from this angle
	 */
	public int compare(Atom atom1, Atom atom2) {
	    return atom1.further(atom2, currentAngle);
	}
    // END OF YOUR CODE
    }

    public static void main(String args[]) {
	new MoleculeRenderer();
    }
}
