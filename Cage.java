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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Cage{

	private int goal;
	private ArrayList<Cell> cells;
	public String cageId;

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
	}

	public int getGoal()  {
		return goal;
	}

	public ArrayList<Cell> getCells() {
		return cells;
	}

	/**
	 * Sets the possible solutions for this Cage
	 *
	 * @param ps possible solutions for this Cage
	 */
	public void setSolutions(List<Stack<Integer>> ps){
		for (Stack<Integer> stack : ps) {
			while (!stack.empty()) {
				Integer k = stack.pop();
				for (Cell cell : cells) {
					cell.addSolution(k);
				}
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(cageId).append("\n");
		for (Cell cell : cells) {
			sb.append(cell.toString());
		}
		sb.append(" Goal: ").append(goal).append("\n");
		return sb.toString();
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

}