/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class ArcConsistency{

	public static void enforce(KillerSudoku ks){
		/* Non-essential values */
		LinkedList<String> ne = new LinkedList<String>(); 
		
		ArrayList<Cage> cages = ks.getCages();
		for (Cage cage : cages) {
			ArrayList<Cell> cells = cage.getCells();
			for (Cell t : cells) {
				if (t.getSolutions().size() == 1) {
					t.setValue(t.getSolutions().get(0)); // Set the cell value property
					// Cell value can't repeat in row, column or box
					ne.add("row_" + t.getY() + "_val_" + t.getValue());
					ne.add("col_" + t.getX() + "_val_" + t.getValue());
					ne.add("box_" + t.getNonet() + "_val_" + t.getValue());
					ne.add("cage_" + cage.getCageId() + "_val_" + t.getValue());
				}
			}
		}

		while(ne.size() > 0){
			String[] constraint = ne.pop().split("_");
			for (Cage cage : cages) {
				ArrayList<Cell> cells = cage.getCells();
				for (Cell t : cells) {
					if (t.getSolutions().size() != 1) { //if there isn't aready solution remove non essential from solution set
						if (constraint[0].equals("row") && constraint[1].equals(Integer.toString(t.getY()))) {
							t.removeSolution(new Integer(constraint[3]));
						}
						if (constraint[0].equals("col") && constraint[1].equals(Integer.toString(t.getX()))) {
							t.removeSolution(new Integer(constraint[3]));
						}
						if (constraint[0].equals("box") && constraint[1].equals(Integer.toString(t.getNonet()))) {
							t.removeSolution(new Integer(constraint[3]));
						}
						if (constraint[0].equals("cage") && constraint[1].equals(cage.getCageId())) {
							t.removeSolution(new Integer(constraint[3]));
						}
					}
				}
			}

		}

	}


}