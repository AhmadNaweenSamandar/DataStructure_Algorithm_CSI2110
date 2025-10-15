public class TryStack2 {

    // Change this file as much or as little as you need to.

    /**
     * Main driver method that will reverse an array.
     *
     * @param args
     */
    protected Integer[] array;
    protected Stack<Integer> stack;

    /** Default constructor. */
    public TryStack2() {
        setArray(50);
    }

    public TryStack2(int n) {
        setArray(n);
    }

    public TryStack2(int n, LinkedStack<Integer> stack) {
        setArray(n);
        this.stack = stack;
    }

    /** Gets the array. */
    public Integer[] getArray() {
        return this.array;
    }

    /** Sets the array. */
    public void setArray(int n) {
        this.array = new Integer[n];
        for (int i = 0; i < n; i++) {
        getArray()[i] = i * 2;
        }
    }

    /** Gets the stack. */
    public Stack<Integer> getStack() {
        return this.stack;
    }

    /** Sets stack using LinkedStack. */
    public void setStack() {
        stack = new LinkedStack<>();
    }

    /** Reverses the array using a LinkedStack. */
    protected void reverseArray() {
        for (int i = 0; i < getArray().length; i++) {
            this.stack.push(getArray()[i]);
        }
        System.out.println("\nInspecting stack (top..bottom): " + this.stack);

        for (int i = 0; i < getArray().length; i++) {
            getArray()[i] = this.stack.pop();
        }
    }

    /** Prints an array of any size. */
    protected void printArray() {
        System.out.println();
        for (int elems : getArray()) {
            System.out.print(elems + "\t");
        }
        System.out.println();
    }

    protected void runSimulation() {
        printArray();
        reverseArray();
        printArray();
    }
    public static void main(String[] args) {

    }
}
