/** 
 * Builds a singly linked list of size 5 and prints it to the console.
 * 
 * @author Jochen Lang
 */

class LinkListGeneric<E> {
    private GNode <E> head; // created the first node 

    LinkList( int sz ) {
		if ( sz <= 0 ) {
	    	llist = null;
		} else {
	    	// created the first node 
	    	head = new GNode <E> (sampleElement, null);
	    	GNode <E> current = head; // temp node for loop
	    	

	    // add further nodes 
        for (int i = 1; i < sz; ++i) {
            E elem = (E) ("" + i); // casting to E for Strings by default
            GNode<E> node2Add = new GNode<E>(elem, null);
            current.setNext(node2Add);
            current = node2Add;
        }
	}
    }
    
    /**
     * Print all the elements of the list assuming that they are Strings
     */
    public void print() {
        GNode<E> current = head;
        while (current != null) {
            System.out.print(current.getElement() + " ");
            current = current.getNext();
        }
        System.out.println();
    }

    public void deleteFirst() {
        if (head != null) {
            head = head.getNext();
        }
    }

    public void deleteLast() {
        if (head == null) return; // empty list
        GNode<E> prev = head;
        GNode<E> current = prev.getNext();

        if (current == null) { // only one node
            head = null;
            return;
        }
        while (current.getNext() != null) {
            prev = current;
            current = current.getNext();
        }
        prev.setNext(null); // unlink last node
    }

    // create and display a linked list
    // Test driver
    public static void main(String[] args) {
        // Example 1: Linked list of Strings
        System.out.println("String Linked List:");
        LinkList<String> stringList = new LinkList<>(5, "0");
        stringList.print();
        stringList.deleteFirst();
        stringList.print();
        stringList.deleteLast();
        stringList.print();

        // Example 2: Linked list of Integers
        System.out.println("\nInteger Linked List:");
        LinkList<Integer> intList = new LinkList<>(0, 0); // start empty
        // manually add integers
        intList.head = new GNode<>(10, new GNode<>(20, new GNode<>(30, null)));
        intList.print();
        intList.deleteFirst();
        intList.print();
        intList.deleteLast();
        intList.print();

		System.out.println("String Linked List Test:");

        // Create list of size 5 with Strings
        LinkList<String> stringList = new LinkList<>(5, "0");

        // Print initial list
        stringList.print();

        // Delete first and print
        stringList.deleteFirst();
        stringList.print();

        // Delete last and print
        stringList.deleteLast();
        stringList.print();

        // Delete last repeatedly
        for (int i = 0; i < 3; ++i) {
            stringList.deleteLast();
            stringList.print();
    }
}
