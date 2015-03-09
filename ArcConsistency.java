/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * The ArcConsistency class enforces arc consistency on the puzzle.
 *
 * Arc Consistency: A variable of a constraint satisfaction problem is arc-consistent with
 * another one if each of its admissible values is consistent with some admissible value of the second variable.
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

			for (Cell cell : cells) {
				if (cell.getSolutions().size() == 1) {
					cell.setValue(cell.getSolutions().get(0)); // Set the cell value property
					// Cell value can't repeat in row, column or nonet
					ne.add("row_" + cell.getY() + "_val_" + cell.getValue());
					ne.add("col_" + cell.getX() + "_val_" + cell.getValue());
					ne.add("nonet_" + cell.getNonet() + "_val_" + cell.getValue());
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
						if (constraint[0].equals("nonet") && constraint[1].equals(Integer.toString(t.getNonet()))) {
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