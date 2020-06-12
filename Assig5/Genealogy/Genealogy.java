/* Code for COMP103, Assignment 5
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.*;
import java.util.*;
import java.io.*;

/** Genealogy:
 * Prints out information from a genealogical database
 */

public class Genealogy implements UIButtonListener,UITextFieldListener {

    // all the people:  key is a name,  value is a Person object
    private final Map<String, Person> people = new HashMap<String, Person>();

    // person-mother relationships: key is a Person, value is the person's mother
    private final Map<Person, Person> mothers = new HashMap<Person, Person>();

    // person-father relationships: key is a Person, value is the person's father
    private final Map<Person, Person> fathers = new HashMap<Person, Person>();

    // parent-child relationships:  key is a Person, value is a Set of children.
    private final Map<Person, Set<Person>> children = new HashMap<Person, Set<Person>>();

    private Person currentPerson; 

    public Genealogy() {
        loadData();
        populateChildrenMap();

        UI.addButton("Print All", this);
        UI.addTextField("Name", this);
        UI.addButton("Print Ancestors", this);
        UI.addButton("Print Descendants", this);
        UI.addButton("Find Male Lineage", this);
        UI.addButton("Find Female Lineage", this);
    }

    /** Load the information from the file "database.txt".
    Each line of the file has information about one person:
    name, year of birth, mother's name, father's name
    (note: a '-' instead of a name means  the mother or father are unknown)
    For each line,
    - construct a new Person with the information, and add to the names map.
    - if there is a find the   
    and add the person to the people map, 
     */
    public void loadData() {
        Scanner sc;
        try{sc = new Scanner(new File("database.txt"));}
        catch(IOException e){throw new RuntimeException("database.txt not found: " + e);}

        // read the file to construct the people, mothers, and fathers maps.
        while (sc.hasNext()) {
            // YOUR CODE HERE
            String name = sc.next();
            int birth = sc.nextInt();
            Person person = new Person(name, birth);
            people.put(name, person);

            String motherName = sc.next();
            Person mother = people.get(motherName);
            
            if (mother != null) {
                mothers.put(person, mother);
            }

            String fatherName = sc.next();
            Person father = people.get(fatherName);
            
            if (father != null) {
                fathers.put(person, father);
            }
        }
        sc.close();
    }

    /** [Completion] Find all the children of all the people in the database, and put them in the children map
    Iterate through all the people:
    if the person has a mother, then add the person to the mother's children 
    if the person has a father, then add the person to the father's children */
    private void populateChildrenMap() {
        // YOUR CODE HERE
        for (Person person : people.values()) {
            children.put(person, new HashSet<Person>());
        }
        
        for (Person person : people.values()) {
            Person mother = mothers.get(person);
            if (mother!=null) {
                children.get(mother).add(person);
            }
            Person father = fathers.get(person);
            if (father!= null) {
                children.get(father).add(person);
            }
        }
    }

    /** Prints out all the people in the database */
    public void printAll(){
        for (Person person : people.values()) {
            String mothersName = "";
            if (mothers.containsKey(person))
                mothersName = "m: " + mothers.get(person);
            String fathersName = "";
            if (fathers.containsKey(person))
                fathersName = "f: " + fathers.get(person);

            String allChildren = "";
            if (children.containsKey(person)&&!children.get(person).isEmpty())
                allChildren = "ch: "+children.get(person);

            UI.printf("%s (%d) %s %s %s\n",
                person.getName(), person.getDOB(),
                mothersName, fathersName, allChildren);
            UI.println("---------------");
        }
    }

    /** Print out all the ancestors of a person.
    To print an ancestors tree, you must print the
    person (if it is not null), and then
    print the ancestor trees of the mother and then the father
    (with a larger indent than the person had).
    Needs to be recursive.
     */
    private void printAncestorTree(Person person, String indent) {
        // YOUR CODE HERE
        if (person == null) {
            return;     
        }
        UI.println(indent + person);
        printAncestorTree(mothers.get(person), indent + "  ");
        printAncestorTree(fathers.get(person), indent + "  ");
    }

    /** Constructs a list of the female lineage of a person (the mother,
     *  the mother's mother, the mother's mother's mother, etc).
     *  It is given an empty list, and is should add these people to the list.
     *  Do it recursively!
     */
    private void findFemaleLineage(Person person, List<Person> ancestors) {
        // YOUR CODE HERE
        Person mother = mothers.get(person);
        if (mother != null) {
            ancestors.add(mother);
            findFemaleLineage(mother, ancestors);
        }
    }

    /** Constructs a list of the male lineage of a person (the father,
     *  the father's father, the father's father's father, etc).
     *  It is given an empty list, and is should add these people to the list.
     *  Do it recursively!
     */
    private void findMaleLineage(Person person, List<Person> ancestors) {
        // YOUR CODE HERE
        Person father = fathers.get(person);
        if (father != null) {
            ancestors.add(father);
            findMaleLineage(father, ancestors);
        }
    }

    /** [Completion] Print out all the descendants of a person.
    Requires that the populateChildrenMap method has been completed
    which adds all their children to each person in the database.
    To print a descendant tree, you must print the person, and then
    print the descendent tree of each child of the person (indented
    a bit more).
    Needs to be recursive.
     */
    private void printDescendantTree(Person person, String indent) {
        // YOUR CODE HERE
        if (person == null) {
            return;
        }
        UI.println(indent + person);
        for (Person child : children.get(person)) {
            printDescendantTree(child, indent + "  ");
        }
    }

    // The user interface methods
    public void textFieldPerformed(String f, String name){
        String capName = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        currentPerson = people.get(capName);
        UI.printMessage("  ");
    }

    public void buttonPerformed(String button) {
        List<Person> lineage = new ArrayList<Person>();
        if (button.equals("Print All")) {
            printAll();
        }
        else  {
            if (currentPerson == null) {
                UI.printMessage("Not a valid person");
                return;
            }
            if (button.equals("Print Ancestors")) {
                UI.println("Ancestors of "+currentPerson.getName()+": ");
                printAncestorTree(currentPerson, "");
            }
            else if (button.equals("Print Descendants")) {
                UI.println("Descendants of "+currentPerson.getName()+": ");
                printDescendantTree(currentPerson, "");
            }
            else if (button.equals("Find Female Lineage")) {
                UI.println("Female lineage of "+currentPerson.getName()+": ");
                findFemaleLineage(currentPerson, lineage);
                UI.println(lineage);
            }
            else if (button.equals("Find Male Lineage")) {
                UI.println("Male lineage of "+currentPerson.getName()+": ");
                findMaleLineage(currentPerson, lineage);
                UI.println(lineage);
            }
            UI.println("------------------");
        }

    }

    public static void main(String[] args) throws IOException {
        new Genealogy();
    }
}