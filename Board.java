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

    public static final int[] POSSIBLE_VALUES = {1,2,3,4,5,6,7,8,9};
    public static final int SIZE = 9;
    public static final int NONET_SIZE = 9;

    private ArrayList<Cage> cages;
    private ArrayList<Cell> cells;

    public Board(){
        cages = new ArrayList<Cage>();
        cells = new ArrayList<Cell>();
    }

    public void addCage(Cage c){
        cages.add(c);
    }

    public ArrayList<Cage> getCages(){
        return cages;
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }

    public void addCell(Cell cell){
        for (Cell c : cells){
            if (c.equals(cell)){
                c.update(cell); //override previous cell solutions
                return;
            }
        }
        cells.add(cell);
    }

    /**
     * Gets a Cell in the Board with the given row/column
     *
     * @param r the row of the cell to get
     * @param c the column of the cell to get
     * @return the Cell at the given row/column, returns null if not found
     */
    public Cell getCell(int r, int c) {
        for(Cell cell : cells) {
            if(cell.getX() == r && cell.getY() == c) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Returns an array of Cells for the given nonet number
     * 	1 2 3
     *  4 5 6
     *  7 8 9
     *
     * @return
     */
    public Cell[] getNonetCells(int nonetNumber) {
        Cell[] cells = new Cell[9];
        int i = 0;
        for(Cell cell : this.cells) {
            if(cell.getNonet() == nonetNumber) {
                if(i == 9) {
                    System.out.println(nonetNumber);
                }
                cells[i] = cell;
                i++;
            }
        }
        return cells;
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
    public List<Stack<Integer>> sumCombinations(Stack<Integer> stack, int sum, int fromIndex,
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
