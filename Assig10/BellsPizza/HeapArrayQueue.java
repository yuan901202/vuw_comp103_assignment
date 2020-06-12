/* Code for COMP 103 Assignment 10
 * Name: Tianfu Yuan
 * Usercode: yuantian
 * ID: 300228072
 */

import java.util.*;

public class HeapArrayQueue <E extends Comparable<? super E> > extends AbstractQueue <E> {
    // YOUR CODE HERE
    private E[] data;
    private int count;
    
    public HeapArrayQueue(){
        this.data = (E[])(new Comparable[7]);
        this.count = 0;
    }
    
    public boolean offer(E value){
        if(value == null){ 
            return false;
        }
        this.ensureCapacity();
        this.data[this.count++] = value;
        this.pushup(this.count - 1);
        return true;
    }

    public E peek(){
        if(this.isEmpty()){ 
            return null;
        }
        return this.data[0];
    }

    public E poll(){
        if(this.isEmpty()){	
            return null;
        }
        E ans = this.data[0];
        this.count--;
        if  (this.count > 0) {
            this.data[0] = this.data[this.count];
            this.pushdown(0);
        }
        return ans;
    }
    
    private void ensureCapacity(){
        if(this.count == this.data.length){
            E[] newData = (E[])new Comparable[this.data.length * 2];
            for (int i = 0; i < this.count; i++){
                newData[i] = this.data[i];
            }
            this.data = newData;
        }
        return;
    }
    
    public int size(){
        return this.count;
    }

    public boolean isEmpty(){ 
        return this.size() == 0; 
    }

    public Iterator<E> iterator(){ 
        return null; 
    }
    
    private void pushup(int index){
        if(index == 0){ 
            return;
        }
        int parent = (index - 1) / 2;
        if(this.data[parent].compareTo(this.data[index]) > 0){
            this.swap(parent, index);
            this.pushup(parent);
        }
        return;
    }
    
    private void pushdown(int index){
        int smallChild = (2 * index) + 1;
        int otherChild = smallChild + 1;

        if(smallChild >= this.count){
            return;
        }
        
        if((this.data[smallChild].compareTo(this.data[otherChild]) > 0) && (otherChild < this.count)){
            smallChild = otherChild;
        }

        if(this.data[index].compareTo(this.data[smallChild]) > 0){
            this.swap(index, smallChild);
            this.pushdown(smallChild);
        }
        return;
    }

    private void swap(int from, int to){
        E temp = this.data[from];
        this.data[from] = this.data[to];
        this.data[to] = temp;
        return;
    }
}