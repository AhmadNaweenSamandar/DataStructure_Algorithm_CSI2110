

//import net.datastructures.*; //copied LinkedPositionalList from net.datastructures.*

/**
 * 
 * An implementation of Sequence based on LinkedPositionalList
 * Only List methods and bridge methods in Sequence interface need to be provided
 * 
 * Created for csi2110 Lab3, Fall 2021
 * 
 * @author Lucia Moura
 *
 */

public class LinkedSequence<E> extends LinkedPositionalList<E> implements Sequence<E> {
		
	/**
	 * returns position corresponding to index
	 * since need to traverse this LinkedPositionalList to get to index i time is O(n)
	 * @param i the index or rank of the element in the list (first has index 0, second has index 1,etc
	 * @return position of the element or null if index does not exist
	 * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
	 */
         public Position<E> positionAtIndex(int i) throws IndexOutOfBoundsException { 
                 checkIndex(i,size()); // checks whether the given index is in the range [0, size()-1].

                 /******* add your code here **********/
				Position<E> current = first(); //Code written by Ahmad Naween Samandar
        		for (int count = 0; count < i; count++) {
            		current = after(current);
        		}
        		return current;

          } 
	
      /**
        * returns index corresponding to position
        * since need to hop through elements of this LinkedPositionalList to get to position pos time is O(n)
        * @param pos - the position or cell you must located in this LinkedPositionalList
        * @return index (also called rank) in the list or -1 if position not found
        * 
        */
	public int indexAtPosition(Position<E> pos) {
		
                  /******* add your code here **********/
		//Code written by Ahmad Naween Samandar
		if (pos == null) return -1;
        
        int index = 0;
        Position<E> current = first();
        
        while (current != null) {
            if (current == pos) {
                return index;
            }
            current = after(current);
            index++;
        }
        return -1; // position not found
	} 

       /**
	 * Returns (but does not remove) the element at index i.
	 * @param  i   the index of the element to return
	 * @return the element at the specified index
         * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
	 */
	public E get(int i) throws IndexOutOfBoundsException { // gets element at index i
		
                 /******* add your code here **********/
		//Code written by Ahmad Naween Samandar
		Position<E> pos = positionAtIndex(i);
        return pos.getElement();
	}
	
	/**
	  * Replaces the element at the specified index, and returns the element previously stored.
	  * @param  i   the index of the element to replace
	  * @param  e   the new element to be stored
	  * @return the previously stored element
	  * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
	  */
	public E set(int i, E e) throws IndexOutOfBoundsException { // replaces the element at index i with e

               /******* add your code here **********/
		//code written by Ahmad Naween Samandar
        Position<E> pos = positionAtIndex(i);
        return set(pos, e);
	}
	
	/**
	  * Inserts the given element at the specified index of the list, shifting all
	  * subsequent elements in the list one position further to make room
	  * Note: when i=size() it correspond to an addLast when i<size it correspond to addBefore the i-th position
	  * @param  i   the index at which the new element should be stored
	  * @param  e   the new element to be stored
	  * @throws IndexOutOfBoundsException if the index is negative or greater than size()
	  */
	public void add(int i, E e){ // insert a new element which will have index i 
		 /******* add your code here **********/
		//Code written by Ahmad Naween Samandar
		if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
        
        if (i == size()) {
            addLast(e);
        } else {
            Position<E> pos = positionAtIndex(i);
            addBefore(pos, e);
        }
   
	}
	
	/**
	  * Removes and returns the element at the given index, shifting all subsequent
	  * elements in the list one position closer to the front.
	  * @param  i   the index of the element to be removed
	  * @return the element that had be stored at the given index
	  * @throws IndexOutOfBoundsException if the index is negative or greater than size()
	  */
	public E remove(int i) throws IndexOutOfBoundsException { // remove element with index i

		/******* add your code here **********/
		//Code written by Ahmad Naween Samandar
		Position<E> pos = positionAtIndex(i);
        return remove(pos);

	}
	
	  // utility methods
	  /** Checks whether the given index is in the range [0, n-1]. */
	  protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
	    if (i < 0 || i >= n)
	      throw new IndexOutOfBoundsException("Illegal index: " + i);
	  }
}
