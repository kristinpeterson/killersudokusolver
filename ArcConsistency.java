/*
CECS-551 AI 
Kristin Peterson and Ariel Katz
Killer Sudoku Solver
*/

import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class ArcConsistency{

	//initial domain for all variables
	private static int[] possibleValues = {0,1,2,3,4,5,6,7,8,9};

	public static void enforce(KillerSudoku ks){
		/* Non-essential values */
		LinkedList<String> ne = new LinkedList<String>(); 
		
		ArrayList<Cage> cages = ks.getCages();
		for (int i=0; i<cages.size(); i++) {
			ArrayList<Cell> cells = cages.get(i).getCells();
			for (int j=0; j< cells.size() ; j++) {
				Cell t = cells.get(j);
				if(t.getSolutions().size() == 1){
					// Cell value can't repeat in row, column or box
					ne.add("row_"+ t.getY() + "_val_" + t.getValue());
					ne.add("col_"+ t.getX() +  "_val_" + t.getValue());
					ne.add("box_"+ ks.getBox(t) +  "_val_" + t.getValue());
				}

			}
		}

		while(ne.size() > 0){
			String[] constraint = ne.pop().split("_");
			for (int i=0; i<cages.size(); i++) {
				ArrayList<Cell> cells = cages.get(i).getCells();
				for (int j=0; j< cells.size() ; j++) {
					Cell t = cells.get(j);
					if(t.getSolutions().size() != 1){
						if(constraint[0] == "row" && constraint[1].equals(t.getY())){
							t.removeSolution(new Integer(constraint[3]));
						}
						if(constraint[0] == "col" && constraint[1].equals(t.getX())){
							t.removeSolution(new Integer(constraint[3]));
						}
						if(constraint[0] == "box" && constraint[1].equals(ks.getBox(t))){
							t.removeSolution(new Integer(constraint[3]));
						}
					}
				}
			}

		}

	}

}