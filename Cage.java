/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.util.ArrayList;

public class Cage{

	private int goal;
	private ArrayList cells;

	public Cage(String[] info){
		cells = new ArrayList();
		for (int i; i<info.length; i++) {
			if(i == info.length -1 ){
				goal = Integer.parseInt(info[i]);
			} else {
				cells.add(new Cell(info[i], info[i+1]));
				i++; //increment extra to account for y
			}
		}
	}

}