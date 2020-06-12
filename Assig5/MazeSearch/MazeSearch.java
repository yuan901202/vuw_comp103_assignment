/* Code for COMP103, Assignment 5
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.UI;
import java.awt.*;
import java.util.*;

public class MazeSearch {

    public final int CELL_SIZE;
    public final int DELAY = 50;

    private final Map<Cell, Set<Cell>> neighbours = new HashMap<Cell, Set<Cell>>();
    private final int size;
    private final Cell entrance, exit;

    private boolean finished;

    /**
     * Creates a new maze using MazeGenerator and draws it to the UI class.
     */
    public MazeSearch(int size) {
        this.size = size;

        if (size > 600 / 20) {
            CELL_SIZE = 600 / size;
        } else {
            CELL_SIZE = 20;
        }

        MazeGenerator generator = new MazeGenerator(size);
        generator.generate(neighbours);
        entrance = generator.getEntrance();
        exit = generator.getExit();

        draw();
    }

    /**
     * Draws the maze. This method should be called once after creating a new maze to draw it to
     * the UI class.
     */
    public void draw() {
        UI.clearGraphics(false);

        UI.setColor(Color.BLACK);
        UI.fillRect(0, 0, size * CELL_SIZE, size * CELL_SIZE, false);

        for (Cell from : neighbours.keySet()) {
            for (Cell to : neighbours.get(from)) {
                drawPassage(from, to, Color.WHITE, false);
            }
        }

        drawCell(entrance, Color.GREEN, false);
        drawCell(exit, Color.GREEN, false);

        UI.repaintGraphics();
    }

    /**
     * Draws a specific cell. This method will fill the interior of a cell with the specified color.
     * The redraw parameter is passed to the UI class, and if it is true this method will delay
     * returning for a while so that the user has time to see the change.
     */
    public void drawCell(Cell cell, Color color, boolean redraw) {
        UI.setColor(color);

        int x = cell.x * CELL_SIZE;
        int y = cell.y * CELL_SIZE;
        int w = CELL_SIZE;
        int h = CELL_SIZE;

        UI.fillRect(x + 1, y + 1, w - 2, h - 2, redraw);
        if (redraw) UI.sleep(DELAY);
    }

    /**
     * Draws a passage between two cells, filling both the cells and the space between them with the
     * specified color. The redraw parameter is passed to the UI class, and if it is true then this
     * method will delay returning for a while so that the user has time to see the change.
     */
    public void drawPassage(Cell from, Cell to, Color color, boolean redraw) {
        UI.setColor(color);

        int x1 = Math.min(from.x, to.x);
        int x2 = Math.max(from.x, to.x);
        int y1 = Math.min(from.y, to.y);
        int y2 = Math.max(from.y, to.y);

        int x = x1 * CELL_SIZE;
        int y = y1 * CELL_SIZE;
        int w = (x2 + 1 - x1) * CELL_SIZE;
        int h = (y2 + 1 - y1) * CELL_SIZE;

        UI.fillRect(x + 1, y + 1, w - 2, h - 2, redraw);
        if (redraw) UI.sleep(DELAY);
    }

    /**
     * Run the exploration algorithm.
     */
    public void run() {
        explore(entrance);
    }

    /**
     * Mark the current cell as visited, then recursively explore the cell's neighbouring cells.
     * Before exploring a cell draw a passage between the current cell and the cell you are about to
     * explore in yellow, and after exploring a cell draw the same passage in red.
     * <p/>
     * If you have found the exit cell you should set 'finished' to true, then return without drawing
     * anything.
     */
    private void explore(Cell cell) {
        // YOUR CODE HERE cell.setVisited(true);
        if (cell == exit) {
            finished = true;
            return;
        }
        
        for (Cell next : neighbours.get(cell)) {
            if (!next.isVisited()) {
                drawPassage(cell, next, Color.YELLOW, true);
                explore(next);
                if (finished) {
                    break;
                }
                drawPassage(cell, next, Color.RED, true);
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            int size = UI.askInt("What size maze would you like?");
            MazeSearch ms = new MazeSearch(size);
	    ms.run();
        }
    }
}

