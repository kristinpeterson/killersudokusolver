package killersudokusolver;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Hashtable;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * This is the main class for this Killer Sudoku solver.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class Main {

	static ArrayList<Constraint> rowConstraints = new ArrayList<Constraint>();
	static ArrayList<Constraint> colConstraints = new ArrayList<Constraint>();
	static ArrayList<Constraint> nonetConstraints = new ArrayList<Constraint>();
	static ArrayList<Constraint> cageConstraints = new ArrayList<Constraint>();

	static Board board = new Board();

	static final Integer MAX_DEPTH = 80;

	public static void main(String[] argv) throws Exception {
		/** Get cages from file */
		try {
			getCagesFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/** Add cells to board */
		for(Cage cage : board.getCages()) {
			for(Cell cell : cage.getCells()) {
				board.addCell(cell);
			}
		}

		/**
		 * Find all combinations of possible solutions
		 * that add up to each Cage goal
		 */
		for(Cage aCage : board.getCages()) {
			int goal = aCage.getGoal();
			int size = aCage.getCells().size();
			int sum = 0;
			Stack<Integer> stack = new Stack<Integer>();
			List<Stack<Integer>> possibleSolutions = new ArrayList<Stack<Integer>>();
			possibleSolutions = board.sumCombinations(stack, sum, 0, goal, size, possibleSolutions);
			aCage.setSolutions(possibleSolutions);
		}

		// Build list of all problem constraints
		buildConstraints();

		// Milestone 1: apply arc consistency and print results to m1output.txt
		Util.applyArcConsistency(board.getConstraints());
		Util.printM1Output();

		// Milestone 2: order cells in ascending-domain-size order and print results to m2output.txt
		board.orderCellsAscending();
		Util.printM2Output();

		// Milestone 3: solve...
		Generator[] generators = new Generator[81];
		Hashtable<String, Integer> generator_map = new Hashtable<String, Integer>();
		List<Cell> cells = board.getCells();
		for( int i=0; i<cells.size(); i++){
			generators[i] = new Generator(cells.get(i));
			generator_map.put( ""+ cells.get(i).getX()+ cells.get(i).getY(), new Integer(i));
		}

		for(Constraint c: board.getConstraints()){
			c.constructFilterTables(generator_map);
		}

		Util.extendAssignment(generators, 1, 0);

	}

	/**
	 * Get cages from text file
	 *
	 */
	private static void getCagesFromFile() throws Exception {
		try {
			File f = new File("testcase2.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String eachLine = br.readLine();

			while (eachLine != null) {
				board.addCage(new Cage(eachLine.split(",")));
				eachLine = br.readLine();
			}
			fr.close();
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builds all constraint sets: row, column, nonet, cage
	 *
	 */
	private static void buildConstraints() {
		// Starting satisfying assignments for constraints
		ArrayList<ArrayList<Integer>> rcInitSatisfyingAssignments = buildInitialRCSA();

		// Build rowConstraints
		for(int r = 1; r <= Board.SIZE; r++) {
			for(int c = 1; c <= Board.SIZE; c++) {
				for(int i = c; i <= Board.SIZE; i++) {
					if (c != i) {
						String constraintName = "Cx" + r + c + "x" + r + i;
						Cell[] variables = {board.getCell(r,c), board.getCell(r,i)};
						Constraint constraint = new Constraint(constraintName, variables, rcInitSatisfyingAssignments);
						rowConstraints.add(constraint);
					}
				}
			}
		}

		// Build colConstraints
		for(int c = 1; c <= Board.SIZE; c++) {
			for(int r = 1; r <= Board.SIZE; r++) {
				for(int i = r; i <= Board.SIZE; i++) {
					if (r != i) {
						String constraintName = "Cx" + r + c + "x" + i + c;
						Cell[] variables = {board.getCell(r,c), board.getCell(i,c)};
						Constraint constraint = new Constraint(constraintName, variables, rcInitSatisfyingAssignments);
						colConstraints.add(constraint);
					}
				}
			}
		}

		// Build nonetConstraints
		for(int n = 1; n <= Board.NONET_SIZE; n++) {
			String nonetName = "Cn" + n +"x";
			Cell[] cells = board.getNonetCells(n);

			for(int r = 1; r < 9; r++) {
				for(int i = r; i < 9; i++) {
					if (r != i) {
						String constraintName = nonetName + cells[r].getX() + cells[r].getY() + "x" + cells[i].getX() + cells[i].getY();
						Cell[] variables = {cells[r], cells[i]};
						Constraint constraint = new Constraint(constraintName, variables, rcInitSatisfyingAssignments);
						nonetConstraints.add(constraint);
					}
				}
			}
		}

		// Build cageConstraints
		for(Cage cage : board.getCages()) {
			Cell[] variables = cage.getCellsAsArray();
			cageConstraints.add(new Constraint(cage.toString(), variables, cage.getPermutatedSolutions()));
		}

		// Assign all constraints to the puzzle Board.constraints member
		ArrayList<Constraint> allConstraints = new ArrayList<Constraint>(cageConstraints);
		allConstraints.addAll(colConstraints);
		allConstraints.addAll(nonetConstraints);
		allConstraints.addAll(rowConstraints);
		board.setConstraints(allConstraints);
	}

	/**
	 * Builds the initial satisfying assignment list for Row and Column constraints
	 *
	 * @return a list of all satisfying assignments (before pruning)
	 */
	private static ArrayList<ArrayList<Integer>> buildInitialRCSA() {
		ArrayList<ArrayList<Integer>> assignmentList = new ArrayList<ArrayList<Integer>>();
		for(int i = 1; i <= Board.SIZE; i++) {
			for(int j = 1; j <= Board.SIZE; j++) {
				if(i != j) {
					ArrayList<Integer> assignment = new ArrayList<Integer>();
					assignment.add(i);
					assignment.add(j);
					assignmentList.add(assignment);
				}
			}
		}
		return assignmentList;
	}
}