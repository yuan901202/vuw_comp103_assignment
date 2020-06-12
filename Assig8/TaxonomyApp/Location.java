/* Code for comp103, Assignment 7
 * Name: pondy & marcus
 */

/** Represents a position on the window
 */

public class Location {

    // Fields

    private double x;  
    private double y;  

    // Constructor

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Methods

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Location other) {
        return Math.hypot((x - other.x), (y - other.y));
    }

}
