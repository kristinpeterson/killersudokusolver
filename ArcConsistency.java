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
import java.util.List;
import java.util.Stack;

public class ArcConsistency{

	public static void enforce(KillerSudoku ks){
		/* Essential values and the spots they've filled */
		LinkedList<String> taken = new LinkedList<String>(); 
		/* non-essential values */
		LinkedList<String> ne = new LinkedList<String>(); 
		
		ArrayList<Cage> cages = ks.getCages();

		for (Cage cage : cages) {

			ArrayList<Cell> cells = cage.getCells();
			List<Stack<Integer>> cageSol = cage.getPermutatedSolutions();
			if (cage.getSize() == 2 && cageSol.size() == 2) {
				System.out.println("Special 2 case");
				/* There are a small number of cases where there are 2 cells in a cage and only 2 possible values 
				making those 2 values non-essential for some other cells in the row or column. */
				Cell cell0 = cells.get(0);
				Cell cell1 = cells.get(1);
				if (cage.rowOnly()) {
					taken.add("row2case_" + cell0.getY() +"_" + cell0.getX() + "_" + cell1.getY() +"_" + cell1.getX() + "_val_" + cell1.getSolutions().get(0) + "_" + cell1.getSolutions().get(1));
				} else if (cage.columnOnly()) {
					taken.add("col2case_" + cell0.getY() +"_" + cell0.getX() + "_" + cell1.getY() +"_" + cell1.getX() + "_val_" + cell1.getSolutions().get(0) + "_" + cell1.getSolutions().get(1));
				} 
				if (cell0.getNonet() == cell1.getNonet()) {
					taken.add("non2case_" + cell0.getY() +"_" + cell0.getX() + "_" + cell1.getY() +"_" + cell1.getX() + "_"+ cell0.getNonet() + "_val_" + cell1.getSolutions().get(0) + "_" + cell1.getSolutions().get(1));
				}
			}

			for (Cell cell : cells) {
				if (cell.getSolutions().size() == 1) {
					cell.setValue(cell.getSolutions().get(0)); // Set the cell value property
					// Cell value can't repeat in row, column or nonet
					taken.add("cell_" + cell.getX() + "_" + cell.getY() + "_val_" + cell.getValue());
					taken.add("nonet_" + cell.getNonet() + "_" + cell.getX() + "_" + cell.getY() + "_val_" + cell.getValue());
					taken.add("cage_" + cage.getCageId() + "_" + cell.getX() + "_" + cell.getY() + "_val_" + cell.getValue());
				} 
			}
		}

		System.out.println("Taken: " + taken);

		while(taken.size() > 0){
			String[] constraint = taken.pop().split("_");
			//System.out.println("0 " + constraint[0]+" 1 " +constraint[1] +" 2 " + constraint[2]+" 3 " +constraint[3] );
			//System.out.println("4 " + constraint[4]+" 5 " +constraint[5] +" 6 " + constraint[6]+" 7 " +constraint[7] );

			for (Cage cage : cages) {
				ArrayList<Cell> cells = cage.getCells();
				for (Cell t : cells) {
					//Special 2 case handling
					if (constraint[0].equals("row2case") || constraint[0].equals("col2case") || constraint[0].equals("non2case")) {
						int y1 = Integer.parseInt(constraint[1]);
						int x1 = Integer.parseInt(constraint[2]);
						int y2 = Integer.parseInt(constraint[3]);
						int x2 = Integer.parseInt(constraint[4]);

						//special row 2 case and cell is in that row
						if (constraint[0].equals("row2case") && y1 == t.getY() && x1 != t.getX() && x2 != t.getX()) { 
							t.removeSolution(new Integer(constraint[6]));
							t.removeSolution(new Integer(constraint[7]));

							//special column 2 case and cell is in that column
						} else if (constraint[0].equals("col2case") && x1 == t.getX() && y1 != t.getY() && y2 != t.getY()){
							t.removeSolution(new Integer(constraint[6]));
							t.removeSolution(new Integer(constraint[7]));

							//special nonet 2 case and cell is in that nonet
						} else if (constraint[0].equals("non2case")){
							int nonet = Integer.parseInt(constraint[5]);
							if( nonet == t.getNonet() && x1 != t.getX() && x2 != t.getX() && y1 != t.getY() && y2 != t.getY()){
								t.removeSolution(new Integer(constraint[7]));
								t.removeSolution(new Integer(constraint[8]));
							}
						}	
					}
					if (constraint[0].equals("nonet") && constraint[1].equals(Integer.toString(t.getNonet())) && !constraint[2].equals(Integer.toString(t.getY())) && !constraint[3].equals(Integer.toString(t.getX())) ) {
						t.removeSolution(new Integer(constraint[5]));
					}
					if (constraint[0].equals("cage") && constraint[1].equals(cage.getCageId()) && !constraint[2].equals(Integer.toString(t.getY())) && !constraint[3].equals(Integer.toString(t.getX()))) {
						t.removeSolution(new Integer(constraint[5])); //remove that value from the cell's domain
						//now check cage permutated solutions for to see if any can be removed given the new info
					}
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
							t.removeSolution(new Integer(constraint[3])); //remove that value from the cell's domain
							//now check cage permutated solutions for to see if any can be removed given the new info
						}
					}
				}
			}
		}
	}


}