/* Code for COMP 103 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import java.io.*;

/** Represents informataion about a pizza that has to be delivered.
 */

public class Pizza implements Comparable<Pizza> {

    // Fields
    String destination;
    int deliveryTime;    // time it will take to deliver the pizza
    int deadline;        // The time by which the pizza must be delivered 
    int orderedTime;
    boolean urgent;

    // Constructors

    /** Construct a new Pizza object
     */

    public Pizza(String d, int dt, int dl, int ot, boolean u) {
        destination = d;
        deliveryTime = dt;
        orderedTime = ot;
        deadline = dl;
        urgent = u;
    }

    public String destination() {return destination;}
    public int deadline() {return deadline;}
    public int deliveryTime() {return deliveryTime;}
    public int orderedTime() {return orderedTime;}
    public boolean urgent() {return urgent;}

    /** This pizza is ordered before the other pizza if
    its deadline is earlier.
     */

    public int compareTo(Pizza other) {
        return (deadline - other.deadline);
    }

    public String toString () {
        return "received:" + orderedTime + " to "+destination +" by "+deadline+(urgent?"(urgent) ":" ")+ deliveryTime+ "mins";
    }

}
