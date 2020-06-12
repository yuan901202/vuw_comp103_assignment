/* Code for COMP103, Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.util.*;
import java.io.*;


/** Person   */

public class Person {
    private String name;
    private int dob;

    public Person(String name, int dob) {
	this.name = name;
	this.dob = dob;
    }

    public String getName() {
	return name;
    }

    public int getDOB() {
	return dob;
    }

    public String toString() {
	return String.format("%s (%d)", name, dob);
    }
}

