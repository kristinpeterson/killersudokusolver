/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Cage{

	private int goal;
	private ArrayList<Cell> cells;
	public String cage_id;

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
		cage_id = getCageId();
	}

	public int getGoal()  {
		return goal;
	}

	public ArrayList<Cell> getCells() {
		return cells;
	}

	public void setSolutions(List<Stack<Integer>> ps){
		for(int i=0; i< ps.size(); i++){
			Stack<Integer> myStack = ps.get(i);
			while(!myStack.empty()){
				Integer k = myStack.pop();
				for (int j=0; j<cells.size(); j++) {
					cells.get(j).addSolution(k);
				}
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(cage_id + "\n");
		for (int i=0; i < cells.size(); i++) {
			Cell t = (Cell)cells.get(i);
			sb.append(t.toString());
		}
		sb.append(" Goal: " + goal + "\n");
		return sb.toString();
	}

	public String getCageId() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < cells.size(); i++) {
			Cell t = (Cell)cells.get(i);
			sb.append(t.getX());
			sb.append("_");
			sb.append(t.getY());
			sb.append(",");
		}
		sb.append("g" + goal);
		return sb.toString();
	}

}