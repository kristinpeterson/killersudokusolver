//package killersudokusolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * A Cage is the grouping of cells in a Killer Sudoku puzzle denoted by a dotted
 * line or by individual colours.
 *
 * Cages contain 2 to 9 cells and a value that the cells in the cage should sum to.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class Cage {

	private int goal;
	private List<Cell> cells;
	private String cageId;
	private List<Stack<Integer>> permutatedSolutions;

	/**
	 * Cage constructor, takes input from raw text file and
	 * constructs Cage object
	 *
	 * Info format:
	 * x,y,x,y,...,g
	 *
	 * where:
	 * x == row
	 * y == col
	 * g == Cage goal
	 *
	 * @param info Cage info from raw text file
	 */
	public Cage(String[] info){
		cells = new ArrayList<Cell>();
		for (int i=0; i < info.length; i++) {
			if(i == info.length - 1 ){
				goal = Integer.parseInt(info[i]);
			} else {
				cells.add(new Cell(info[i], info[i+1]));
				i++; //increment extra to account for y
			}
		}
		cageId = getCageId();
		permutatedSolutions = new ArrayList<Stack<Integer>>();
	}

	public int getGoal()  {
		return goal;
	}

	public int getSize()  {
		return cells.size();
	}

	public List<Cell> getCells() {
		return cells;
	}

	public Cell[] getCellsAsArray() {
		int numCells = this.cells.size();
		Cell[] cells = new Cell[numCells];
		for(int i = 0; i < numCells; i++) {
			cells[i] = this.cells.get(i);
		}
		return cells;
	}

	public boolean rowOnly() {
		int row = cells.get(0).getY();
		for (int i=1; i<cells.size(); i++) {
			if (cells.get(i).getY() != row) {
				return false;
			}
		}
		return true;
	}

	public boolean columnOnly() {
		int col = cells.get(0).getX();
		for (int i=1; i<cells.size(); i++) {
			if (cells.get(i).getX() != col) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the possible solutions for Cells in this Cage
	 * and permutates possible Cage solutions
	 *
	 * @param ps possible solutions for Cells in this Cage
	 */
	public void setSolutions(List<Stack<Integer>> ps){
		// Permutate solutions, populates Cage.permutatedSolutions
		permutePossibleSolutions(ps);
		// Set possible solutions for Cells in this Cage
		for (Stack<Integer> stack : ps) {
			while (!stack.empty()) {
				Integer k = stack.pop();
				for (Cell cell : cells) {
					cell.addDomainValue(k);
				}
			}
		}
	}

	/**
	 * Returns a copy of the list of all permuated possible solutions for this Cage
	 *
	 * @return permutated list of all possible solutions for this Cage
	 */
	public ArrayList<ArrayList<Integer>> getPermutatedSolutions(){
		ArrayList<ArrayList<Integer>> solutionsClone = new ArrayList<ArrayList<Integer>>();
		for (Stack<Integer> s : permutatedSolutions) {
			ArrayList<Integer> solution = new ArrayList<Integer>();
			for (Integer i : s) {
				solution.add(i);
			}
			solutionsClone.add(solution);
		}
		return solutionsClone;
	}


	public String getCageId() {
		StringBuilder sb = new StringBuilder();
		for (Cell t : cells) {
			sb.append(t.getX());
			sb.append(".");
			sb.append(t.getY());
			sb.append(",");
		}
		sb.append("g").append(goal);
		return sb.toString();
	}

	/**
	 * Finds all unique permutations of each solution Stack
	 *
	 * @param ps set of possible solutions that will be permutated
	 */
	private void permutePossibleSolutions(List<Stack<Integer>> ps) {
		for (int i = 0; i < ps.size(); i++) {
			permuteStack(ps.get(i), 0);
		}
	}

	/**
	 * Recursively searches for unique permutations of a given solution
	 * Stack and adds to permuatedSolutions for this Cage
	 *
	 * @param possibleSolution a possible solution for the Cage that will be permutated
	 * @param fromIndex starting index, updated with each recursive call
	 */
	private void permuteStack(Stack<Integer> possibleSolution, int fromIndex){
		for(int i = fromIndex; i < possibleSolution.size(); i++){
			Collections.swap(possibleSolution, i, fromIndex);
			permuteStack(possibleSolution, fromIndex + 1);
			Collections.swap(possibleSolution, fromIndex, i);
		}
		if (fromIndex == possibleSolution.size() - 1){
			// copy stack to retain correct order
			Stack<Integer> ps = new Stack<Integer>();
			for (Integer i : possibleSolution) {
				ps.add(i);
			}
			this.permutatedSolutions.add(ps);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(cageId).append("\n");
		for (Cell cell : cells) {
			sb.append(cell.toString());
		}
		sb.append(" Goal: ").append(goal).append("\n");
		sb.append("Possible Solutions:");
		sb.append(permutatedSolutions);
		return sb.toString();
	}

}