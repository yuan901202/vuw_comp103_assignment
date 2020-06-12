/* Code for COMP 103, Assignment 3
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.util.*;
import java.io.*;

/** SokobanUndo
 */

public class SokobanUndo implements UIButtonListener, UIKeyListener {

    // Fields
    private Cell[][] cells;   // the array describing the current warehouse.
    private int rows;
    private int cols;

    private Coord agentPos;
    private String agentDirection = "left";

    private final int maxLevels = 4;
    private int level = 0;

    // YOUR CODE HERE
    private Stack<ActionRecord> history = new Stack<ActionRecord>();
    // END OF YOUR CODE

    private Map<Character,Cell> cellMapping;  // character in file to cell type
    private Map<Cell,String> imageMapping;    // cell type to image of cell
    private Map<String,String> agentMapping;    // direction to image of worker
    private Map<String,String> keyMapping;      // key string to direction

    // Constructors
    /** Construct a new Sokoban object
     *  and set up the GUI
     */
    public SokobanUndo() {
	UI.addButton("New Level", this);
	UI.addButton("Restart", this);
	UI.addButton("left", this);
	UI.addButton("up", this);
	UI.addButton("down", this);
	UI.addButton("right", this);
	// YOUR CODE HERE
	UI.addButton("Undo", this);
	// END OF YOUR CODE

	UI.println("Put the boxes away.");
	UI.println("You may use keys (wasd or ijkl) but click on the graphics pane first");
	UI.setKeyListener(this);

	initialiseMappings();
	load();
    }

    /** Respond to button presses */
    public void buttonPerformed(String button) {
	if (button.equals("New Level")) {
	    level = (level+1)%maxLevels;
	    load();
	}
	else if (button.equals("Restart"))
	    load();
	// YOUR CODE HERE
	else if (button.equals("Undo"))
	    undo();
	// END OF YOUR CODE
	else 
	    doAction(button);
    }

    /** Respond to key actions */
    public void keyPerformed(String key) {
	doAction(keyMapping.get(key));
    }

    /** Move the agent in the specified direction, if possible.
     *  If there is box in front of the agent and a space in front of the box,
     *    then push the box.
     *  Otherwise, if there is anything in front of the agent, do nothing.
     */
    public void doAction(String dir) {
	if (dir==null) return;
	// YOUR CODE HERE
	if (dir.equals("undo")) {undo(); return;}
	// END OF YOUR CODE
	agentDirection = dir;
	Coord newP = agentPos.next(dir);  // where the agent will move to
	Coord nextP = newP.next(dir);     // the place two steps over
	if ( cells[newP.row][newP.col].hasBox() && cells[nextP.row][nextP.col].free() ) {
	    push(dir);
	    // YOUR CODE HERE
	    history.push(new ActionRecord("push", dir));
	    // END OF YOUR CODE
	}
	else if ( cells[newP.row][newP.col].free() ) {
	    move(dir);
	    // YOUR CODE HERE
	    history.push(new ActionRecord("move", dir));
	    // END OF YOUR CODE
	}
    }

    /** Move the agent into the new position (guaranteed to be empty) */
    public void move(String dir) {
	drawCell(agentPos);
	agentPos = agentPos.next(dir);
	drawAgent();
	Trace.println("Move " + dir);
	UI.repaintGraphics();
    }

    /** Push: Move the agent, pushing the box one step */
    public void push(String dir) {
	drawCell(agentPos);
	agentPos = agentPos.next(dir);
	drawAgent();
	Coord boxP = agentPos.next(dir);
	cells[agentPos.row][agentPos.col] = cells[agentPos.row][agentPos.col].moveOff();
	cells[boxP.row][boxP.col] = cells[boxP.row][boxP.col].moveOn();
	drawCell(boxP);
	Trace.println("Push " + dir);
	UI.repaintGraphics();
    }
   
    /** Pull: (useful for undoing a push in the opposite direction)
     *	move the agent in direction from dir,
     *	pulling the box into the agent's old position
     */
    public void pull(String dir) {
	String opDir = oppositeDirection(dir);
	Coord boxP = agentPos.next(opDir);
	cells[boxP.row][boxP.col] = cells[boxP.row][boxP.col].moveOff();
	cells[agentPos.row][agentPos.col] = cells[agentPos.row][agentPos.col].moveOn();
	drawCell(boxP);
	drawCell(agentPos);
	agentPos = agentPos.next(dir);
	agentDirection = opDir;
	drawAgent();
	Trace.println("Pull " + dir);
	UI.repaintGraphics();
    }

   
    // YOUR CODE HERE
    /** Undo: take an action off the history stack, and reverse its effect:
     *	if it was a move, then move in the reverse direction.
     *	if it was a push, then do a pull. 
     */
    public void undo() {
	if (history.isEmpty()) {
	    UI.println("No more history to undo");
	    return;
	}
	ActionRecord a = history.pop();
	if (a.isMove()){
	    Trace.println("undo move " + a.dir());
	    move(oppositeDirection(a.dir()));
	}
	else{
	    Trace.println("undo push " + a.dir());
	    pull(oppositeDirection(a.dir()));
	}
    }
    // END OF YOUR CODE

    /** Load a grid of cells (and agent position) from a file */
    public void load() {
	File f = new File("warehouse" + level + ".txt");
	if (f.exists()) {
	    List<String> lines = new ArrayList<String>();
	    try {
		Scanner sc = new Scanner(f);
		while (sc.hasNext())
		    lines.add(sc.nextLine());
		sc.close();
	    }
	    catch(IOException e) {
		Trace.println("File error " + e);
	    }

	    rows = lines.size();
	    cols = lines.get(0).length();

	    cells = new Cell[rows][cols];

	    for(int row = 0; row < rows; row++) {
		String line = lines.get(row);
		for(int col = 0; col < cols; col++) {
		    if (col>=line.length())
			cells[row][col] = Cell.empty;
		    else {
			char ch = line.charAt(col);
			if ( cellMapping.containsKey(ch) )
			    cells[row][col] = cellMapping.get(ch);
			else {
			    cells[row][col] = Cell.empty;
			    UI.printf("Invalid char: (%d, %d) = %c \n",
				      row, col, ch);
			}
			if (ch=='A')
			    agentPos = new Coord(row,col);
		    }
		}
	    }
	    draw();
	    // YOUR CODE HERE
	    history.clear();
	    // END OF YOUR CODE
	}
    }

    // Drawing 

    private static final int leftMargin = 40;
    private static final int topMargin = 40;
    private static final int cellSize = 25;

    /** Draw the grid of cells on the screen, and the agent */
    public void draw() {
	UI.clearGraphics();
	// draw cells
	for(int row = 0; row<rows; row++)
	    for(int col = 0; col<cols; col++)
		drawCell(row, col);
	drawAgent();
	UI.repaintGraphics();
    }

    private void drawAgent() {
	UI.drawImage(agentMapping.get(agentDirection),
		     leftMargin+(cellSize* agentPos.col),
		     topMargin+(cellSize* agentPos.row),
		     cellSize, cellSize, false);
    }

    private void drawCell(Coord pos) {
	drawCell(pos.row, pos.col);
    }

    private void drawCell(int row, int col) {
	String imageName = imageMapping.get(cells[row][col]);
	if (imageName != null)
	    UI.drawImage(imageName,
			 leftMargin+(cellSize* col),
			 topMargin+(cellSize* row),
			 cellSize, cellSize, false);
    }

    /** Return true iff the warehouse is solved - 
     *	all the shelves have boxes on them 
     */
    public boolean isSolved() {
	for(int row = 0; row<rows; row++) {
	    for(int col = 0; col<cols; col++)
		if(cells[row][col] == Cell.shelf)
		    return  false;
	}
	return true;
    }

    /** Returns the direction that is opposite of the parameter */
    public String oppositeDirection(String dir) {
	if ( dir.equalsIgnoreCase("right")) return "left";
	if ( dir.equalsIgnoreCase("left"))  return "right";
	if ( dir.equalsIgnoreCase("up"))    return "down";
	if ( dir.equalsIgnoreCase("down"))  return "up";
	return dir;
    }

    private void initialiseMappings() {
	// character in files to cell type
	cellMapping = new HashMap<Character,Cell>();
	cellMapping.put('.', Cell.empty);
	cellMapping.put('A', Cell.empty);  // initial position of agent must be an empty cell
	cellMapping.put('#', Cell.wall);
	cellMapping.put('S', Cell.shelf);
	cellMapping.put('B', Cell.box);

	// cell type to image of cell
	imageMapping = new HashMap<Cell, String>();
	imageMapping.put(Cell.empty, "empty.gif");
	imageMapping.put(Cell.wall, "wall.gif");
	imageMapping.put(Cell.box, "box.gif");
	imageMapping.put(Cell.shelf, "shelf.gif");
	imageMapping.put(Cell.boxOnShelf, "boxOnShelf.gif");

	//direction to image of worker
	agentMapping = new HashMap<String, String>();
	agentMapping.put("up", "agent-up.gif");
	agentMapping.put("down", "agent-down.gif");
	agentMapping.put("left", "agent-left.gif");
	agentMapping.put("right", "agent-right.gif");

	// key string to direction 
	keyMapping = new HashMap<String,String>();
	keyMapping.put("i", "up");     keyMapping.put("I", "up");   
	keyMapping.put("k", "down");   keyMapping.put("K", "down"); 
	keyMapping.put("j", "left");   keyMapping.put("J", "left"); 
	keyMapping.put("l", "right");  keyMapping.put("L", "right");
	keyMapping.put("u", "undo");   keyMapping.put("U", "undo");

	keyMapping.put("w", "up");     keyMapping.put("W", "up");   
	keyMapping.put("s", "down");   keyMapping.put("S", "down"); 
	keyMapping.put("a", "left");   keyMapping.put("A", "left"); 
	keyMapping.put("d", "right");  keyMapping.put("D", "right");
    }

    public static void main(String[] args) {
	new SokobanUndo();
    }
}
