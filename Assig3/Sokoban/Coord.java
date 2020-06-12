/* Code for COMP 103 Assignment 3
 */

/** A pair of row and col representing a coordinate in the warehouse.
 *  Also has a method to return the next Coord in a given direction.
 */

public class Coord {

    public final int row;  // because they are final (can't be changed), it is
    public final int col;  // safe to make these fields public.

    Coord (int row, int col) {
	this.row = row;
	this.col = col;
    }

    /** Return the next coord in the specified direction */
    public Coord next(String dir) {
	if (dir.equals("up"))    return new Coord(row-1, col);
	if (dir.equals("down"))  return new Coord(row+1, col);
	if (dir.equals("left"))  return new Coord(row, col-1);
	if (dir.equals("right")) return new Coord(row, col+1);
	return this;
    }

    public String toString() {
	return String.format("(%d,%d)", row, col);
    }
}
