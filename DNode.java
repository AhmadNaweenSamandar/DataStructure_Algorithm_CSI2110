/**
 * A simple node class for a doubly-linked list.
 * Each node has:
 *   - a reference to the stored element
 *   - a reference to the next node
 *   - a reference to the previous node
 */
public class DNode {
    private Object element; //The element it self which will be storing the data 
    private DNode next; // the next pointer of type DNode that points to next element
    private DNode prev; // the prev pointer of type DNode that points to the previous element

    // Default constructor: at the initial state where there is no list created the node and points are null (pointing to nothing)
    public DNode() {
        this(null, null, null);
    }

    // Constructor: element + links (creating links)
    public DNode(Object e, DNode p, DNode n) {
        element = e;
        prev = p;
        next = n;
    }

    // Getters
    public Object getElement() { return element; }
    public DNode getNext() { return next; }
    public DNode getPrev() { return prev; }

    // Setters
    public void setElement(Object newElem) { element = newElem; }
    public void setNext(DNode newNext) { next = newNext; }
    public void setPrev(DNode newPrev) { prev = newPrev; }
}
