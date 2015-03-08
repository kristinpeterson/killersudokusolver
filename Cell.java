/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.util.ArrayList;

public class Cell{

	private int x;
	private int y;
	private int value;
	private ArrayList<Integer> possibleSolutions;

	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		possibleSolutions = new ArrayList<Integer>();
	}

	public Cell(String xs, String ys){
		x = Integer.parseInt(xs);
		y = Integer.parseInt(ys);
		possibleSolutions = new ArrayList<Integer>();
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setValue(int num){
		value = num;
	}

	public int getValue(){
		return value;
	}

	public boolean isSolution(Integer i){
		return possibleSolutions.contains(i);
	}

	public void addSolution(Integer i){
		if(!possibleSolutions.contains(i))
			possibleSolutions.add(i);
	}

	public boolean removeSolution(Integer i){

		return possibleSolutions.remove(i);
	}

	public ArrayList<Integer> getSolutions(){
		return (ArrayList<Integer>)possibleSolutions.clone();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("x: "+x);
		sb.append(" y: "+y);
		sb.append(" solutions: "+ getSolutions());
		
		return sb.toString();
	}

}