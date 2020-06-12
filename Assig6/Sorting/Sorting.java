/* Code for COMP 103, Assignment 6
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import comp102.*;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

/** Code for Sorting Experiment
 *   - testing code
 *   - sorting algorithms
 *   - utility methods for creating, testing, printing, copying arrays
 */

public class Sorting{

    /* Example method for testing and timing sorting algorithms.
    *  You will need to modify and extend this heavily to do your
    *  performance testing. It should probably run tests on each of the algorithms,
    *  on different sized arrays, and multiple times on each size.
    *  Make sure you create a new array each time you sort - it is not a good test if
    *  you resort the same array after it has been sorted.
    *  Hint: if you want to copy an array, use copyArray (below)
    */
   
    public void testSorts(){
    String[] data;
    int size = 50000;
    long start;
    long time;

    UI.println("\n\n======Selection Sort=======\n");

    data = createArray(size);
    //printData(data);
    start = System.currentTimeMillis();
    selectionSort(data);
    time =  System.currentTimeMillis() - start;

    UI.printf("Number of items:  %,d\n", data.length);
    UI.printf("Sorted correctly: %b\n", testSorted(data));
    UI.printf("Time taken:       %.2f s\n", time/1000.0);

    UI.println("\n=======DONE=========\n");
    }

    public void testAllSorts(){
        String[][] sorts = new String[][]{{},{"Quicksort", "Quicksort2", "QuicksortIns", "Arrays", "Merge", "MergeIter"}};
        int[] sizes = new int[]{500000};
        String[] data;
        String[] platoData = readArrayFromFile(-1,"plato-semisorted.txt");
        UI.printf("%10s\t%6s\t%8s\t%s\n", "Sort name", "length", "time", "Properties");
        for (int i = 0; i < 2; i++){
            for (String name : sorts[i]){
                for (int size : sizes){
                    if (i == 0 && size > 20000) {
                        continue;
                    }
                    data = createArray(size);
                    testSort(data, name, "random");
                    Arrays.sort(data);
                    testSort(data, name, "sorted");
                    reverseArray(data);
                    testSort(data, name, "reverse-sorted");
                    data = copyArray(platoData, size);
                    testSort(data, name, "semi-sorted");
                }
            }
        }
        UI.println("\n=======DONE=========\n"); 
    }
    
    public void testSort(String[]data, String name, String description){
        long start;
        long time = 0;
        int repetitions = 3;
        for (int i = 0; i < repetitions; i++){
            String[] copy = copyArray(data);
            start = System.currentTimeMillis();
            doSort(copy, name);
            time = time + System.currentTimeMillis() - start;
            if (!testSorted(copy)){
                UI.println("**** " + name + " DID NOT SORT CORRECTLY ON " + description);
            }
        }
        UI.printf("%10s\t%6d\t%8.3fs\t%s\n", name, data.length, time / (repetitions*1000.0), description);
    }
    
    public void doSort(String[] data, String name){
        if (name.equals("Selection")){
            selectionSort(data);
        }
        else if (name.equals("Selection2")){ 
            selectionSort2(data);
        }
        else if (name.equals("Insertion")){
            insertionSort(data);
        }
        else if (name.equals("Insertion2")){ 
            insertionSort2(data);
        }
        else if (name.equals("Bubble")){ 
            bubbleSort(data);
        }
        else if (name.equals("Bubble1")){ 
            bubbleSort1(data);
        }
        else if (name.equals("Bubble2")){ 
            bubbleSort2(data);
        }
        else if (name.equals("Merge")){ 
            mergeSort(data);
        }
        else if (name.equals("MergeIter")){ 
            mergeSortIter(data);
        }
        else if (name.equals("Quicksort")){ 
            quickSort(data);
        }
        else if (name.equals("Quicksort2")){ 
            quickSort2(data);
        }
        else if (name.equals("Arrays")){ 
            Arrays.sort(data);
        }
        else if (name.equals("QuicksortIns")){ 
            quickSortIns(data);
        }
    }
    
    public void testASort(){
        String[] data;
        String name = "MergeIter";
        UI.println(name);
        for (int size = 116; size<600000; size=2*size-3){
            data = createArray(size);
            doSort(data, name);
            UI.println("unsorted, size: "+size+ " "+ testSorted(data));
            doSort(data, name);
            UI.println("sorted , size: "+size+ " "+ testSorted(data));
            data = readArrayFromFile(size, "plato-semisorted.txt");
            doSort(data, name);
            UI.println("plato , size: "+size+ " "+ testSorted(data));
        }
        UI.println("\n=======DONE=========\n");
    }
    
    /* =============== SWAP ================= */
    
    /** Swaps the specified elements of an array.
     *  Used in several of the sorting algorithms
     */
    private void swap(String[ ] data, int here, int there){
        if (here == there) {
            return;
        }
        String temp = data[here];
        data[here] = data[there];
        data[there] = temp;
    }

    /* ===============SELECTION SORT================= */

    /** Sorts the elements of an array of String using selection sort */
    public void selectionSort(String[ ] data){
    // for each position, from 0 up, find the next smallest item 
    // and swap it into place
    for (int place=0; place<data.length-1; place++){
        int minIndex = place;
        for (int sweep=place+1; sweep<data.length; sweep++){
        if (data[sweep].compareTo(data[minIndex]) < 0)
            minIndex=sweep;
        }
        swap(data, place, minIndex);
    }
    }
    
    /** Sorts the elements of an array of String using selection sort, 
       builds up a sorted list from the front and the back*/
    public void selectionSort2(String[ ] data){
        //for each remaining, looking for the min vlue and then swap to the front
        //and swap it into place
        int back = 0;
        int front = data.length-1;
        while(back < front){
            int minIndex = front;
            int maxIndex = front;
            for(int sweep = back; sweep < front; sweep++){
                if(data[sweep].compareTo(data[minIndex]) < 0){
                    minIndex=sweep;
                }
                if(data[sweep].compareTo(data[maxIndex]) > 0){
                    maxIndex=sweep;
                }
            }
            
            swap(data, minIndex, back);
            
            if(maxIndex == back){
                swap(data, minIndex, front);
            }
            else if(maxIndex != minIndex){
                swap(data, maxIndex, front);
            }
            
            back++;
            front--;
        }
    }

    /* ===============INSERTION SORT================= */

    /** Sorts the  elements of an array of String using insertion sort */
    public void insertionSort(String[] data){
    // for each item, from 0, insert into place in the sorted region (0..i-1)
    for (int i=1; i<data.length; i++){
        String item = data[i];
        int place = i;
        while (place > 0  &&  item.compareTo(data[place-1]) < 0){
        data[place] = data[place-1];       // move up
        place--;
        }
        data[place]= item;
    }
    } 
    
    /** Sorts the  elements of an array of String using insertion sort, 
       use binary research to add an item to insert*/
    public void insertionSort2(String[] data){
        // for each item, from 0, insert into place in the sorted region (0..i-1)
        for (int i = 1; i < data.length; i++){
            String item = data[i];
            int low = 0;  
            int high = i;
            while (low < high){
                int mid  =  (low + high) / 2;
                if(item.compareTo(data[mid]) > 0){
                    low = mid + 1;
                }
                else{
                    high = mid;
                }
            }
            //move the low up
            for(int place = i; place > low; place--){
                data[place] = data[place - 1];//move up
            }
            data[low] = item;
        }
    }
    
    /* ===============BUBBLE SORT================= */

    /** Sorts the elements of an array of String using bubble sort */
    public void bubbleSort(String[] data){
    // Repeatedly scan array, swapping adjacent items if out of order
    // Builds a sorted region from the end
    for (int top=data.length-1; top>0; top--){
          for (int sweep=0; sweep<top; sweep++)
          if (data[sweep].compareTo(data[sweep+1]) >0) 
            swap(data, sweep, sweep+1);
        }
    }
    
    /** Sorts the elements of an array of String using bubble sort, 
       the last index on each sweep where it did a swap, and moves top down to that index at the end of the sweep*/
    public  void bubbleSort1(String[] data){
        // Repeatedly scan array, swapping adjacent items if out of order
        // Builds a sorted region from the end
        for (int top = data.length - 1; top > 0; top++){
            int lastSwap = 0;
            for (int sweep = 0; sweep < top; sweep++){
                if (data[sweep].compareTo(data[sweep + 1]) > 0) {
                    swap(data, sweep, sweep + 1);
                    lastSwap = sweep;
                }
            }
            top = lastSwap;
        }
    }
    
    /** Sorts the elements of an array of String using bubble sort, 
       the last index on each sweep where it did a swap, and moves top down to that index at the end of the sweep,
       first sweeping to the right and then sweeping to the left*/
    public  void bubbleSort2(String[] data){
        // Repeatedly scan array, swapping adjacent items if out of order
        // Builds two sorted regions from both end
        int bottom = 0;
        int top = data.length - 1;
        while (bottom < top){
            int lastSwap = 0;
            for (int sweep = bottom; sweep < top; sweep++){
                if (data[sweep].compareTo(data[sweep + 1]) > 0) {
                    swap(data, sweep, sweep + 1);
                    lastSwap = sweep;
                }
            }
            top = lastSwap;
            for (int sweep = top; sweep > bottom; sweep--){
                if (data[sweep - 1].compareTo(data[sweep]) > 0) {
                    swap(data, sweep, sweep - 1);
                    lastSwap = sweep;
                }
            }
            bottom = lastSwap;
        }
    }
    
    /* ===============MERGE SORT================= */
    
    /** non-recursive, wrapper method
     *  copy data array into a temporary array 
     *  call recursive mergeSort method     
     */
    public void mergeSort(String[] data) {
    String[] other = new String[data.length];
    for(int i=0; i<data.length; i++)
        other[i]=data[i];
    mergeSort(data, other, 0, data.length); //call to recursive merge sort method
    }
    
    /** Recursive mergeSort method */
    public void mergeSort(String[] data, String[] temp, int low, int high) {
    if(low < high-1) {
        int mid = ( low + high ) / 2;
        mergeSort(temp, data, low, mid);
        mergeSort(temp, data, mid, high);
        merge(temp, data, low, mid, high);
    }
    }
    
    //The fllowing code get extra help from tutor ("mergeSortIter")
    /** Iterative mergeSort method */
    public void mergeSortIter(String[] data) {
        String[] temp = new String[data.length];
        for (int range = 1; range < data.length; range = 2*range) {
            for(int low = 0; low < data.length; low = low + 2*range) {
                int mid = low + range;
                if(mid >= data.length){
                    break;
                }
                int high = mid + range;
                if(high > data.length){
                    high = data.length;
                }
                merge(data, temp, low, mid, high);
            }
            
            for(int i = 0; i < data.length; i++){

                data[i] = temp[i];
            }
            
        }
    }

    /** Merge method
     *  Merge from[low..mid-1] with from[mid..high-1] into to[low..high-1]
     *  Print data array after merge using printData
     */
    public void merge(String[] from, String[] to, int low, int mid, int high) {
    int index = low;      //where we will put the item into "to"
    int indxLeft = low;   //index into the lower half of the "from" range
    int indxRight = mid; // index into the upper half of the "from" range
    while (indxLeft<mid && indxRight < high) {
        if ( from[indxLeft].compareTo(from[indxRight]) <=0 )
        to[index++] = from[indxLeft++];
        else
        to[index++] = from[indxRight++];
    }
    // copy over the remainder. Note only one loop will do anything.
    while (indxLeft<mid)
        to[index++] = from[indxLeft++];
    while (indxRight<high)
        to[index++] = from[indxRight++];
    }


    /*===============QUICK SORT=================*/

    /** Sort data using QuickSort
     *  Print time taken by Quick sort
     *  Print number of times partition gets called
     */

    /** Quick sort recursive call */
    public void quickSort(String[ ] data) {
    quickSort(data, 0, data.length);
    }

    public void quickSort(String[ ] data, int low, int high) {
    if (high-low < 2)      // only one item to sort.
        return;
    else {     // split into two parts, mid = index of boundary
        int mid = partition(data, low, high);
        quickSort(data, low, mid);
        quickSort(data, mid, high);
    }
    }

    /** Partition into small items (low..mid-1) and large items (mid..high-1) 
     *  Print data array after partition
     */
    private int partition(String[] data, int low, int high) {
    String pivot = data[(low+high)/2];
    int left = low-1;
    int right = high;
    while( left < right ) {
        do { 
        left++;       // just skip over items on the left < pivot
        } 
        while (left<high && data[left].compareTo(pivot) < 0);

        do { 
        right--;     // just skip over items on the right > pivot
        } 
        while (right>=low &&data[right].compareTo(pivot) > 0);

        if (left < right) 
        swap(data, left, right);
    }
    return left;
    }

    
    /**quicksort with the median of three values (low, mid, high) of the subrange*/
    public void quickSortIns(String[ ] data) {
        quickSortIns(data, 0, data.length);
    }

    public void quickSortIns(String[ ] data, int low, int high) {
        if(high-low < 2){      
            return;
        }
        else if(high-low <= 5){
            insertionSort(data, low, high);
        }
        else{
            int mid = partitionMed(data, low, high);
            quickSort(data, low, mid);
            quickSort(data, mid, high);
        }
    }
    
    /**use insertion sort onece a subrange gets small */
    public  void insertionSort(String[] data, int low, int high){
        // for each item, from 0, insert into place in the sorted region (low..i-1)
        for (int i = low + 1; i < high; i++){
            String item = data[i];
            int place = i;
            while (place > 0  &&  item.compareTo(data[place-1]) < 0){
                data[place] = data[place - 1];// move up
                place--;
            }
            data[place] = item;
        }
    } 
    
    //The fllowing code get extra help from tutors ("partitionMed" and "median")
    /**partition into samll items */
    private int partitionMed(String[] data, int low, int high) {
        String pivot = median(data[low], data[(low + high) / 2], data[high - 1]);
        int left = low - 1;
        int right = high;
        while( left < right ) {
            do{ 
                left++;
            } 
            while(left < high && data[left].compareTo(pivot) < 0);

            do{ 
                right--;
            } 
            while(right >= low && data[right].compareTo(pivot) > 0);

            if (left < right){ 
                swap(data, left, right);
            }
        }
        return left;
    }
    
    /**find the median of three values */
    private String median(String a, String b, String c){
        String med = b;
        if (a.compareTo(b) < 0){
            if (c.compareTo(a) < 0){
                med = a;
            }
            else if (c.compareTo(b) < 0){
                med = c;
            }
            else{
                med = b;
            }
        }
        else{
            if (c.compareTo(b) < 0){
                med = b;
            }
            else if (c.compareTo(a) < 0){
                med = c;
            }
            else{
                med = a;
            }
        }
        return med;
    }
    
    /** Quick sort, second version:  simpler partition method
     *   faster or slower?  */
    public  void quickSort2(String[ ] data) {
    quickSort2(data, 0, data.length);
    }

    public void quickSort2(String[ ] data, int low, int high) {
    if (low+1 >= high) // no items to sort.
        return;
    else {     // split into two parts, mid = index of boundary
        int mid = partition2(data, low, high);
        quickSort2(data, low, mid);
        quickSort2(data, mid+1, high);
    }
    }

    public int partition2(String[] data, int low, int high){
    swap(data, low, (low+high)/2);    // choose pivot and put at position low
    String pivot = data[low];
    int mid = low;
    for(int i = low+1; i < high; i++){  // for each item after the pivot
        if ( data[i].compareTo(pivot) <0 ){
        mid++;                      // move mid point up
        swap(data, i, mid);
        }
    }
    swap(data, low, mid);   // move pivot to the mid point
    return mid;
    }


    /* =====   Utility methods ============================================ */


    /** Tests whether an array is in sorted order
     */
    public boolean testSorted(String[] data) {
    for (int i=1; i<data.length; i++){
        if (data[i].compareTo(data[i-1]) < 0)
        return false;
    }
    return true;
    }

    public void printData(String[] data){
    for (String str : data){
        UI.println(str);
    }
    }

        /** Constructs an array of Strings by making random String values */
    public String[] createArray(int size) {
    Random randGenerator = new Random();
    String[] data = new String[size];
    for (int i=0; i<size; i++){
        char[] chars = new char[5];
        for (int c=0; c<chars.length; c++){
        chars[c] = (char) ('a' + randGenerator.nextInt(26));
        }
        String str = new String(chars);
        data[i] = str;
    }
    return data;
    }

    /** Constructs an array of Strings by reading a file */
    public String[] readArrayFromFile(int size, String filename) {
    File file = new File(filename);
    if (!file.exists()){
        UI.println("file "+filename+" does not exist");
        return null;
    }
    List<String> temp = new ArrayList<String> ();
        try {
            Scanner scan = new Scanner(new File(filename));
            while (scan.hasNext() && temp.size() < size ) 
                temp.add(scan.next());
            scan.close();
        }
        catch(IOException ex) {   // what to do if there is an io error.
            UI.println("File reading failed: " + ex);
        }
    if (temp.size() < size)
        size = temp.size();
    
    String[] data = new String[size];
    for (int i =0; i<size; i++){
        data[i] = temp.get(i);
    }
    return data;
    }

    /** Create a new copy of an array of data */
    public String[] copyArray(String[] data){
    String[] newData = new String[data.length];
    for (int i=0; i<data.length; i++){
        newData[i] = data[i];
    }
    return newData;
    }

    public String[] copyArray(String[] data, int size){
        if (size > data.length){
            size = data.length;
        }
        String[] newData = new String[size];
        for (int i = 0; i < size; i++){
            newData[i] = data[i];
        }
        return newData;
    }
    
    public void reverseArray(String[] data){
        int bot = 0;
        int top = data.length - 1;
        while (bot < top){
            swap(data, bot++, top--);
        }
    }
    
    public static void main(String[] args){
    Sorting sorter = new Sorting();
    sorter.testSorts();
    }
}