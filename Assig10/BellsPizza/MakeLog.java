/* Code for COMP 103 Assignment 
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import java.io.*;

/** <description of class MakeLog>
 */

public class MakeLog {

    // Fields

    private static final double ProbAccept = 0.2;
    private static final double ProbUrgent = 0.3;

    // Constructors
    /** Construct a new MakeLog object
     */

    // Main
    public static void main(String[] arguments){
        Random rand = new Random();
        String[] places = {"kelburn", "karori", "city", "miramar", "brooklyn", 
                "newtown", "mtvictoria", "wilton ", "ngaio", "highbury"};
        for (int i = 1; i <= 20; i++){
            try {
                PrintStream ps = new PrintStream(new File(arguments[0]+i));
                for (int time = 1; time <= 480; time++){
                    ps.print(time);
                    if (rand.nextDouble()>ProbAccept)
                        ps.println(" -");
                    else
                        ps.println(" "+ (rand.nextDouble()<ProbUrgent?"urgent ":"standard ")+
                            places[rand.nextInt(places.length)]);
                }
                for(int time=481; time<=525; time++)
                    ps.println(time+ " -");
                ps.close();
            }
            catch (Exception e) {System.out.println(e);}
        }	
    }

}
