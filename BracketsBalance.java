import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/*
 * CSI2110 Lab 3 - lab3.java
 * 
 * Class to check balanced brackets in math expressions
 *
 * Usage: java BracketsBalance <exp> 
 * Example true: java BracketsBalance '{(x+y)-[2*z]}'
 * Example false: java BracketsBalance '{(x+y}-[2*z])'
 * 
 * Lucia Moura, updated 2021
 * Billy Bolton, updated 2021
 *
 */

class BracketsBalance {

    /**
     * A method that uses an ArrayStack to identify if a String has balanced
     * brackets.
     *
     * @param exp the expression to check
     * @return true if brackets are balanced, false otherwise
     */
    private boolean isBalanced(String exp) {
        ArrayStack<Character> stack = new ArrayStack<>();

        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);

            // Push only opening brackets
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            }
            // Handle closing brackets
            else if (ch == ')' || ch == ']' || ch == '}') {
                if (stack.isEmpty()) {
                    return false; // closing bracket without matching opening
                }

                char top = stack.pop();

                if ((ch == ')' && top != '(') ||
                    (ch == ']' && top != '[') ||
                    (ch == '}' && top != '{')) {
                    return false; // mismatched pair
                }
            }
            // ignore all other characters
        }

        // At the end, stack must be empty if balanced
        return stack.isEmpty();
    }

    // ---------- main + helpers (same as given) ----------

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            testInput(args);
        } else {
            testDoc();
        }
    }

    private static void testDoc() throws IOException {
        Path path = Paths.get("tests.txt");

        try (Stream<String> input = Files.lines(path)) {
            input.forEach(expression -> {
                if (!expression.equals(""))
                    checkBrackets(expression);
            });
        } catch (IOException e) {
            System.out.println("Cannot find file.");
        }
    }

    private static void testInput(String[] args) {
        for (String expression : args) {
            checkBrackets(expression);
        }
    }

    private static void checkBrackets(String expression) {
        BracketsBalance brackets = new BracketsBalance();
        System.out.println("Checking expression: " + expression);
        if (brackets.isBalanced(expression))
            System.out.println("The expression is balanced.\n");
        else
            System.out.println("The expression is NOT balanced.\n");
    }
}
