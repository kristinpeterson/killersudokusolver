/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.util.ArrayList;

public class Cage{

	private int goal;
	private ArrayList<Cell> cells;

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
	}

	public int getGoal()  {
		return goal;
	}

	public ArrayList getCells() {
		return cells;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < cells.size(); i++) {
			Cell t = (Cell)cells.get(i);
			sb.append(t.getX());
			sb.append(",");
			sb.append(t.getY());
			sb.append(",");
		}
		sb.append(goal);
		return sb.toString();
	}

}