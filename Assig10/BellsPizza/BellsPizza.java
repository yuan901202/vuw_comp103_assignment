/* Code for COMP 103 Assignment 10
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.util.*;
import java.io.*;

/**  note data file needs 45 mins free at the end.

###########

 */
public class BellsPizza {
    // Fields
    private Map<String, Integer> destinations = new HashMap<String, Integer>();
    private double UrgentPrice = 20;
    private double StandardPrice = 10;
    private double HandlingCost = 1.5;
    private double PerMinCost = .25;
    //private double penalty = 2;
    
    private final int ChCyclists = 3; //modify there has three cyclists that can be used
    
    // Constructors
    /** Construct a new BellsPizza object, loads in destinations that the company delivers to (including delivery times), and then runs the simulation tests.
     */
    public BellsPizza() {
        loadDestinations();
        runTests();
    }

    /* Load in destinations and delivery times from destinations.txt
     */
    private void loadDestinations() {
        try {
            Scanner sc = new Scanner(new File("destinations.txt"));
            while (sc.hasNext()) {
                destinations.put(sc.next(), sc.nextInt());
            }
        }
        catch(IOException e) {System.out.println("Destination file failed: "+e);}
        System.out.println(destinations);
    }

    /* 
    This method runs through each of the logs (currently "log...",
    change to "completLog..." for the completion code). Each log file
    is used to simulate handling a sequence of pizza orders over
    time. For each log file, the simulation is first run with an
    ordinary queue (using a standard linked list from the Java
    collections library), and then your HeapArrayQueue. For the
    challenge, you should uncomment the code that also uses the
    HeapLinkedQueue as well.
     */
    private void runTests() {
        try {
            for (int i = 1; i<=20; i++) {
                String fname= "log"+i;
                System.out.println("==================================\nLog File: "+ fname);
                System.out.println("------------------\nQueue: ");
                double profitQueue = run(new LinkedList<Pizza>(), new Scanner(new File(fname)));

                System.out.println("------------------\nPriorityQueue: ");
                double profitPQueue = run(new HeapArrayQueue<Pizza>(), new Scanner(new File(fname)));
                System.out.printf("          Priority Queue advantage = %4.2f\n", profitPQueue-profitQueue);
                /*
                System.out.println("------------------\nChallenge PriorityQueue: ");
                double profitPQueue = run(new HeapLinkedQueue<Pizza>(), new Scanner(new File(fname)));
                System.out.printf("          Priority Queue advantage = %4.2f\n", profitPQueue-profitQueue);
                 */
            }
        } catch (IOException e) {System.out.println("Opening log file failed: "+e);}
    } 

    /*
    Reads a single pizza in from the log file, and then returns the
    data as a Pizza object. If there is no pizza order at this time,
    then we return null;
     */
    private Pizza readPizza(int time, Scanner logFile) {
        String status = logFile.next();
        if (!status.equals("-")) {
            String dest = logFile.next();
            boolean urgent = status.equals("urgent");
            int deadline = time + (urgent?30:120);
            int delivTime = destinations.get(dest);
            return new Pizza(dest, delivTime, deadline, time, urgent);
        } else {
            return null;
        }

    }

    /*
    Return the profit (if positive, or loss if negative) of handling
    the pizza orders contained in the log file, using the queue provided
    by the runTests() method above.
     */

    private double run (Queue<Pizza> queue, Scanner logFile) {
        double total = 0; // total money earned

        int countOnTime = 0;
        int countLateSt = 0;
        int countLateUr = 0;
        int countUndelivered = 0;
        
        Queue<Integer> cyclists = new HeapArrayQueue<Integer>();
        for(int loop = 0; loop < this.ChCyclists; loop++){
            cyclists.offer(0);
        }
        
        //int cyclistAvailableAt = 0;  // When the cyclist will next be available.
        while (logFile.hasNext()) {
            int time = logFile.nextInt();
            Pizza pizza = this.readPizza(time, logFile);
            if (pizza!=null) {
                queue.offer(pizza);
                total += (pizza.urgent()?UrgentPrice:StandardPrice) - HandlingCost;
            }
            
            int cyclistAvailableAt = cyclists.peek(); //check the next cyclist if it available
            
            if (cyclistAvailableAt < time && !queue.isEmpty()) {
                Pizza deliveredPizza = queue.poll();
                if (time + deliveredPizza.deliveryTime() > deliveredPizza.deadline()) {
                    if (deliveredPizza.urgent()) {
                        countLateUr++;
                        //total -= penalty*UrgentPrice;
                        total -= 3*UrgentPrice;
                    }
                    else{
                        countLateUr++;
                        //total -= penalty*StandardPrice;
                        total -= 3*StandardPrice;
                    }   
                } else {
                    countOnTime++;
                }
                int tripTime = 2 * deliveredPizza.deliveryTime();
                total -= PerMinCost * tripTime;
                cyclistAvailableAt = time + tripTime;
            }
        }

        logFile.close();

        while(!queue.isEmpty()) {
            Pizza undeliveredPizza = queue.poll();
            //total -= penalty*(undeliveredPizza.urgent()?UrgentPrice:StandardPrice);
            total -= 3*(undeliveredPizza.urgent()?UrgentPrice:StandardPrice);
            countUndelivered++;
            /*
            System.out.println("Undelivered: "+(pizza.urgent()?"urgent":"standard")+ " for "+ // 
            pizza.destination());
             */
        }

        System.out.printf("on time=%2d, late: %2d (urgent=%2d, std=%2d, und=%2d) \nProfit: $%4.2f\n",
            countOnTime, (countLateUr+countLateSt+countUndelivered),
            countLateUr, countLateSt, countUndelivered, total);
        return total;
    }

    // Main
    public static void main(String[] arguments) {
        BellsPizza bp = new BellsPizza();
    } 

}
