/** 
 * Builds a singly linked list of size 5 and prints it to the console.
 * 
 * @author Jochen Lang
 */

class LinkList {
	//since the doubly linked list has head and tail we need to create it
    private DNode head; //first node created
	private DNode tail; //last node created

	//Constructor: creating a list of size sz
    LinkList( int sz ) {
		//so if no element exists in list (empty), we make the head and tail to be null
		if ( sz <= 0 ) {
	    	head = null; 
			tail = null;
		} else {
			// we create first node
			head = new DNode ("0", null, null);
			DNode current = head; // appointed the pointer of type current to head
		
	    	// add further nodes
	    	for ( int i=1; i<sz; ++i ) {
				// create node and attach it to the list
				DNode node2Add = new DNode( Integer.toString(i), current, null );
				current.setNext(node2Add);   // add first node
				current=node2Add;
	    	}

			// Set the tail reference
			tail = current;
		
		}
    }
    
    /**
     * Print all the elements of the list assuming that they are Strings
     */
    public void print() {
		/* Print the list */
		DNode current = head; // point to the first node
		while (current != null) {
	    	System.out.print((String)current.getElement() + " ");	
	    	current = current.getNext(); // move to the next
		}
		System.out.println();	
    }

    public void deleteFirst() {
		//we check the first edge case if the list in empty
		if ( head != null ) {
	    	return;
		}

		head = head.getNext();
		if (head != null) {
			head.setPrev(null);
		} else {
			tail = null;
		}

	}

    public void deleteLast() {
        /*the deleteLast method is more efficient than the one in Singly Linked list implementation 
        * the time complexty of second if condition is O(1) which in the singly linked list implementation it appears 
        to be O(n), because it went through all the elements
        * so basically it is more efficient then the Singly Linked list implementation
        */
		if ( tail == null ) {
			return; // if again the last element 
		}

		if ( head == tail ) { // only 1 node
	    	head = null;
			tail = null;
		} else {
			tail = tail.getPrev(); //taking the tail back one node
			tail.setNext(null); //make the last element asgin to null (remove it)
		}
	
    // create and display a linked list
    public static void main(String [] args){
		/* Create the list */
		LinkList llist = new LinkList( 5 );
		/* Print the list */
		llist.print();
		/* delete first and print */
		llist.deleteFirst();
		llist.print();
		/* delete last and print 5 times */
		for ( int i=0; i< 5; ++i ) {
	    	llist.deleteLast();
	    	llist.print();
		}
    }
}
