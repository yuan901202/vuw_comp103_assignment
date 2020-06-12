/* Code for COMP 103 Assignment 3  */

/**  The possible cells, along with useful methods.
     Would be better represented by an Enum class.
*/

public class Cell {
    private final String type;

    private Cell(String t){
	type = t;
    }
    // the five constants representing the five different kinds of cell
    public static final Cell empty = new Cell("empty");
    public static final Cell wall = new Cell("wall");
    public static final Cell box = new Cell("box");
    public static final Cell shelf = new Cell("shelf");
    public static final Cell boxOnShelf = new Cell("boxOnShelf"); 

    /** Whether there is a box on this cell */
    public boolean hasBox() {
	return (this==box || this==boxOnShelf);
    }

    /** Whether the cell is free to move onto */
    public boolean free() {
	return (this==empty || this==shelf);
    }

    /** The cell you get if you push a box off this cell */
    public Cell moveOff() {
	if (this==box) return empty;
	if (this==boxOnShelf)  return shelf;
	return this;
    }

    /** The cell you get if you push a box on to this cell */
    public Cell moveOn() {
	if (this==empty) return box;
	if (this==shelf) return boxOnShelf;
	return this;
    }
}
