import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

	public static void main(String[] argv) throws Exception {
		/** Get cages from file */
	    File f = new File("test.txt");
    	FileReader fr = new FileReader(f);
    	BufferedReader br = new BufferedReader(fr);

    	String eachLine = br.readLine();

    	while (eachLine != null) {
    		board.addCage(new Cage(eachLine.split(",")));
      		eachLine = br.readLine();
    	}

		// Add cells to board
		for(Cage cage : board.getCages()) {
			for(Cell cell : cage.getCells()) {
				board.addCell(cell);
			}
		}

		/* Iteration 1 output */

		File of = new File("output.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(of));

		/** Find all combinations that add up to each Cage goal */
		for(Cage aCage : board.getCages()) {
			int goal = aCage.getGoal();
			int size = aCage.getCells().size();
			int sum = 0;
			Stack<Integer> stack = new Stack<Integer>();
			List<Stack<Integer>> possibleSolutions = new ArrayList<Stack<Integer>>();
			possibleSolutions = board.sumCombinations(stack, sum, 0, goal, size, possibleSolutions);
			aCage.setSolutions(possibleSolutions);
			System.out.println("\nPermutated Solutions:\n" + aCage.getPermutatedSolutions());
			System.out.println(aCage.toString());
			output.write(aCage.toString());
		}

		for(Cage aCage : board.getCages()) {
			//System.out.println(aCage.toString());
			output.write(aCage.toString());
		}

		buildConstraints();

        output.close();

	}

	/**
	 * Builds all constraint sets: row, column, nonet
	 *
	 * TODO: add representation of cage constraints as set of Constraint objects
	 */
	private static void buildConstraints() {
		// Starting satisfying assignments for constraints
		ArrayList<ArrayList<Integer>> rcInitSatisfyingAssignments = buildInitialRCSA();
		ArrayList<ArrayList<Integer>> nonetInitSatisfyingAssignments = buildInitialNonetSA();

		// Build rowConstraints
		for(int r = 1; r <= Board.SIZE; r++) {
			for(int c = 1; c <= Board.SIZE; c++) {
				for(int i = c; i <= Board.SIZE; i++) {
					if (c != i) {
						String constraintName = "Cx" + r + c + "x" + r + i;
						Constraint constraint = new Constraint(constraintName, rcInitSatisfyingAssignments);
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
						Constraint constraint = new Constraint(constraintName, rcInitSatisfyingAssignments);
						colConstraints.add(constraint);
					}
				}
			}
		}

		// Build nonetConstraints
		for(int n = 1; n <= Board.NONET_SIZE; n++) {
			String nonetName = "Cn" + n;
			nonetConstraints.add(new Constraint(nonetName, nonetInitSatisfyingAssignments));
		}

		// Build cageConstraints
		for(Cage cage : board.getCages()) {
			cageConstraints.add(new Constraint(cage.getCageId(), cage.getPermutatedSolutions()));
		}

		// Temporary output of constraint cardinality before arc consistency has been performed
		System.out.println("**************************");
		System.out.println("ROW CONSTRAINTS");
		System.out.println("**************************");
		for(Constraint c : rowConstraints) {
			System.out.println(c.getName() + "\n\t\t" + "Cardinality of constraint before AC:\t" + c.getSatisfyingAssignments().size());
		}
		System.out.println("**************************");
		System.out.println("COLUMN CONSTRAINTS");
		System.out.println("**************************");
		for(Constraint c : colConstraints) {
			System.out.println(c.getName() + "\n\t\t" + "Cardinality of constraint before AC:\t" + c.getSatisfyingAssignments().size());
		}
		System.out.println("**************************");
		System.out.println("NONET CONSTRAINTS");
		System.out.println("**************************");
		for(Constraint c : nonetConstraints) {
			System.out.println(c.getName() + "\n\t\t" + "Cardinality of constraint before AC:\t" + c.getSatisfyingAssignments().size());
		}
		System.out.println("**************************");
		System.out.println("CAGE CONSTRAINTS");
		System.out.println("**************************");
		for(Constraint c : cageConstraints) {
			System.out.println(c.getName() + "\n\t\t" + "Cardinality of constraint before AC:\t" + c.getSatisfyingAssignments().size());
		}
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

	/**
	 * Builds the initial satisfying assignment list for Nonet constraints
	 *
	 * @return a list of all satisfying assignments (before pruning)
	 */
	private static ArrayList<ArrayList<Integer>> buildInitialNonetSA() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		permuteNonetSA(Board.POSSIBLE_VALUES, 0, result);
		return result;
	}

	/**
	 * Recursive method to find all unique permutations of nonet satisfying assignments
	 *
	 * @return a list of all satisfying assignments (before pruning)
	 */
	private static void permuteNonetSA(int[] values, int fromIndex, ArrayList<ArrayList<Integer>> result) {
		if (fromIndex >= values.length) {
			ArrayList<Integer> item = convertArrayToList(values);
			result.add(item);
		}

		for (int j = fromIndex; j <= values.length - 1; j++) {
			swap(values, fromIndex, j);
			permuteNonetSA(values, fromIndex + 1, result);
			swap(values, fromIndex, j);
		}
	}
	private static ArrayList<Integer> convertArrayToList(int[] num) {
		ArrayList<Integer> item = new ArrayList<Integer>();
		for (int h = 0; h < num.length; h++) {
			item.add(num[h]);
		}
		return item;
	}

	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}


}