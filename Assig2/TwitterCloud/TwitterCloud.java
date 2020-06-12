/* Code for COMP103, Assignment 2
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.awt.Color;
import java.util.*;
import java.io.*;

import comp102.*;

public class TwitterCloud implements UIButtonListener {

    //fields
    /** wordFrequencey is a Map from words to integers,
     * for each word, it contains the frequency of the word (how many times
     * it occurred in the file) */ 
    private Map<String, Integer> wordFrequency;

    /** wordIndex is a Map from words to list of tweets:
    for each word, it contains the list of tweets that the word was found in */
    private Map<String, List<String>> wordIndex;

    //Constructor. Sets up the user interface.
    public TwitterCloud() {
        UI.addButton("Read File", this);    // read file of tweets and construct maps
        UI.addButton("Draw Cloud", this);   // draw a cloud of words based on frequency
        UI.addButton("Search", this);       // list the tweets containing a given word
    }

    /** Responds to the buttons */
    public void buttonPerformed(String button) {
        // YOUR CODE HERE
        if(button.equals("Read File")){
            UI.clearText();
            UI.clearGraphics();
            String file = UIFileChooser.open("Please choose a tweet file: ");
            if(file != null){
                readTweetFile(file);
                UI.println("Read " + wordFrequency.size() + " different words.");
            }
        }
        
        if(button.equals("Draw Cloud")){
            if(wordFrequency == null){
                UI.println("NO FILE EXIST!");
            }
            else{
                UI.clearGraphics();
                drawCloud();
            }
        }
        
        else if(button.equals("Search")){
            if(wordFrequency == null){
                UI.println("NO FILE EXIST!");
            }
            else{
                UI.clearText();
                String word = UI.askString("What do you want to search for?");
                searchTweets(word);
            }
        }
    }
    
    /** Initialises the fields to empty HashMap's, and reads a file of tweets,
    constructing the wordFrequency map (core) and
    the wordIndex map (completion).
    Reads each tweet as a whole line from the file.
    For each tweet, wraps the tweet in a Scanner, and reads each word from the tweet.
    For each word, increments its count in the wordFrequency table,
    and (for the completion) adds the tweet to the words' entry in the wordIndex map.
    */
    private void readTweetFile(String filename){
        try{
            Scanner input = new Scanner(new FileInputStream(filename)); 
            // YOUR CODE HERE
            wordFrequency = new HashMap<String, Integer>();
            wordIndex = new HashMap<String, List<String>>();
            UI.println("Reading" + filename);
            /**
             * The fllowing code get extra help from tutors and Jacky Chang.
             */
            while(input.hasNextLine()){
                String tweet = input.nextLine();
                Scanner sc = new Scanner(tweet);
                
                while(sc.hasNext()){
                    String word = sc.next();
                    if(wordFrequency.containsKey(word)) {
                        int lastWord = wordFrequency.get(word);
                        lastWord += 1;
                        wordFrequency.put(word, lastWord);
                        List<String> findTweets = wordIndex.get(word);
                        
                        if(!tweet.contains(tweet)){
                            findTweets.add(tweet);
                        }
                    }
                    else{
                        wordFrequency.put(word, 1);
                        wordIndex.put(word, new ArrayList<String>(Arrays.asList(tweet)));
                    }
                }
            }
        }
        catch(IOException e){System.out.println("Fail: " + e);}
    }

    /** Draws a "cloud" of all the more frequent words.
    For each word in the wordFrequency map, whose frequency is above
    some threshold, draw the word with a size and color that depends
    on the frequency of the word, at a random position on the graphics pane.
    */
    public void drawCloud() {
        int cloudLeft = 20;  // left and top of the cloud 
        int cloudTop = 40;   
        int cloudWidth = UI.getCanvasWidth() - 60;   // width and height of cloud
        int cloudHeight = UI.getCanvasHeight() - 20;
        // YOUR CODE HERE
        for(String word : wordFrequency.keySet()){
            int fr = wordFrequency.get(word);
            if(fr > 5){
                int x = cloudLeft + (int) (Math.random() * cloudWidth);
                int y = cloudTop + (int) (Math.random() * cloudHeight);
                setSizeAndColor(fr);
                UI.drawString(word, x, y);
            }
        }
    }

    /** Sets the font size and the colour, depending on the argument.
    The higher the frequency, the larger the size and the darker the colour.
     */
    private void setSizeAndColor(int freq){
        int fontSize = 5;
        Color color = Color.LIGHT_GRAY;
        if (freq >= 10000) {
            color = Color.BLUE;
            fontSize = 48;
        }
        else if (freq >= 1000) {
            color = Color.BLACK;
            fontSize = 36;
        }
        else if (freq >= 100) {
            color = Color.DARK_GRAY;
            fontSize = 24;
        }
        else if (freq >= 10) {
            color = Color.GRAY;
            fontSize = 10;
        } 
        UI.setFontSize(fontSize);
        UI.setColor(color);
    }

    /** Prints all the tweets that contain a given word.
    Asks the user for a word, and then looks it up in the wordIndex
    to find the list of tweets.
     */
    private void searchTweets(String word){
        // YOUR CODE HERE
        if(!wordIndex.containsKey(word)){
            UI.printf("No results found for '%s' ! \n", word);
        }
        else{
            for(String tweet: wordIndex.get(word)){
                UI.println(tweet);
            }
            UI.println(word + "is in" + wordIndex.get(word).size() + "tweets.");
        }
    }

    public static void main(String[] args) {
        TwitterCloud tc = new TwitterCloud();
    }
}
