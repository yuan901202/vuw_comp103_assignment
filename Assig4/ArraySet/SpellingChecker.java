/* Code for COMP103, Assignment 4
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/** SpellingChecker checks all the words in a file against a dictionary.
 *  It prints out (one per line) any word in the file that is not in the dictionary.
 *  The program can be very simple - it is OK to just have a main method
 *  that contains all the code, but you can use multiple methods if you want.
 *  
 *  The key requirement is that the dictionary should be read from the
 *  dictionary file into a set.
 *  When reading through the document with spelling errors, the program should
 *  check each each word against the dictionary.
 *  Any word that is not in the dictionary is considered to be an error and
 *  should be printed out.
 *
 *  The program should record and print out the total time taken to read
 *  all the words into the dictionary.
 *  It should also record and print out the total time taken to check all the words.
 *
 *  Note that the dictionary and the file to check are assumed to be all
 *  lowercase, with all punctuation removed.
*/

public class SpellingChecker{

    // Suggested Design:
    // 
    // 1. create a Set of String to hold the words in the dictionary.
    //    (For your first version, use a HashSet; Then use your ArraySet.)
    //
    // 2. 
    //   read each word in the file "dictionary.txt" (use a Scanner)
    //   and add each word to the dictionary Set.
    //   Note that the dictionary contains just over 200,000 words
    //
    // 3. 
    //   Ask the user (UIFileChooser) for the file to check
    //   open the file in a Scanner.
    //   record the start time
    //   loop through file reading each word.
    //   check if the word is in dictionary set
    //   if not, print out the word.
    //   print out total time taken
    
    // YOUR CODE HERE
    public static void main(String[] arguments){
        UI.println("Reading dictionary...");
        Set<String> dictionary = new HashSet<String>();
        try{
            Scanner file = new Scanner(new File("dictionary.txt"));
            long start = System.currentTimeMillis();
            while(file.hasNext()){
                dictionary.add(file.next());
                if(dictionary.size()%1000 == 0){
                    UI.println(dictionary.size());
                }
            }
            long loading = (System.currentTimeMillis() - start);
            file.close();
            
            UI.println("Dictionary loaded.");

            Scanner wordsFile = new Scanner(new File(UIFileChooser.open()));
            start = System.currentTimeMillis();
            while (wordsFile.hasNext()){
                String words = wordsFile.next();
                if (!dictionary.contains(words.toLowerCase())){
                    UI.println(words);
                }
            }
            long checking = (System.currentTimeMillis() - start);
            UI.printf("-------------------------------\n Loading took: %.3f seconds\n Checking took: %.3f seconds\n", loading/1000.0, checking/1000.0);
            wordsFile.close();
        
        }catch(IOException e){UI.println("Fail: " + e);}
    }
}
