import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * Represents a Killer Sudoku puzzle,
 *
 * Killer Sudoku is a puzzle that combines elements of Sudoku and Kakuro.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 */
public class Board {

    private ArrayList<Cage> cages;

    private static final int[] POSSIBLE_VALUES = {1,2,3,4,5,6,7,8,9};

    public Board(){
        cages = new ArrayList<Cage>();
    }

    public void addCage(Cage c){
        cages.add(c);
    }

    public ArrayList<Cage> getCages(){
        return cages;
    }

    /**
     * Find all possible combinations of integers that add to the
     * Cage goal, number of integers limited to Cage size.
     *
     * @param stack list of possible solutions for a cage
     * @param sum the sum of integers in the stack
     * @param fromIndex index to start with when searching possible values (search from left to right)
     * @param goal the Cage sum goal
     * @param size the size of the cage, number of elements each solution stack should have
     * @param possibleSolutions list of possible solution stacks
     * @return the possible solutions for the Cage
     */
    private List<Stack<Integer>> sumCombinations(Stack<Integer> stack, int sum, int fromIndex,
                                                 int goal, int size, List<Stack<Integer>> possibleSolutions) {

        if (sum == goal && stack.size() == size) {
            // possible solution found!
            Stack<Integer> possibleSolution = new Stack<Integer>();

			/* Make copy of solution stack, reason:
			 * cannot directly add stack to possibleSolutions list
			 * because any changes to stack will propagate to possibleSolutions
			 * list, rendering it useless.
			 */
            for (Integer i : stack) {
                possibleSolution.add(i);
            }

            possibleSolutions.add(possibleSolution);
            return possibleSolutions;
        }

        for (int currentIndex = fromIndex; currentIndex < POSSIBLE_VALUES.length; currentIndex++) {
            if (sum + POSSIBLE_VALUES[currentIndex] <= goal) {
                stack.push(POSSIBLE_VALUES[currentIndex]);
                sum += POSSIBLE_VALUES[currentIndex];
                sumCombinations(stack, sum, currentIndex + 1, goal, size, possibleSolutions);
                sum -= stack.pop();
            }
        }

        return possibleSolutions;
    }

}
