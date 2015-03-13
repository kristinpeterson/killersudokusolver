import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	static Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();

	static Board board = new Board();

	public static void main(String[] argv) throws Exception {
		/** Get cages from file */
	    File f = new File("testcase2.txt");
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
		String separator = "**************************\n";

		/** Find all combinations that add up to each Cage goal */
		for(Cage aCage : board.getCages()) {
			int goal = aCage.getGoal();
			int size = aCage.getCells().size();
			int sum = 0;
			Stack<Integer> stack = new Stack<Integer>();
			List<Stack<Integer>> possibleSolutions = new ArrayList<Stack<Integer>>();
			possibleSolutions = board.sumCombinations(stack, sum, 0, goal, size, possibleSolutions);
			aCage.setSolutions(possibleSolutions);
		}

		buildConstraints();
		buildNEfromCageConstraint();
		reduceFromNE(rowConstraints);
		reduceFromNE(colConstraints);
		reduceFromNE(nonetConstraints);

		// Temporary output of constraint cardinality before arc consistency has been performed
		output.write(separator);
		for(Constraint c : rowConstraints) {
			String outputString="Row constraint: "+c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
			output.write(outputString);
		}
		output.write(separator);
		for(Constraint c : colConstraints) {
			String outputString="Column constraint: "+c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size()+ "\n";
			output.write(outputString);
		}
		output.write(separator);
		for(Constraint c : cageConstraints) {
			String outputString="Sum constraint: "+c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size()+ "\n";
			output.write(outputString);
		}
		output.write(separator);
		for(Constraint c : nonetConstraints) {
			String outputString="Grid constraint: "+c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size()+ "\n";
			output.write(outputString);
		}

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
						Cell[] variables = {board.getCell(r,c), board.getCell(r,i)};
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
						String constraintName = nonetName + cells[r].getY() + cells[r].getX() + "x" + cells[i].getY() + cells[i].getX();
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
			cageConstraints.add(new Constraint(cage.getCageId(), variables, cage.getPermutatedSolutions()));
		}
	}

	/**
	 * Builds the nonessential list
	 *
	 */
	private static void buildNEfromCageConstraint() {
		for (Constraint c : cageConstraints) {
			for (Cell cell : c.getVariables()) {
				ArrayList<Integer> ps = cell.getSolutions();
				if (ps.size() < 9) {
					for (int n = 1; n < 10; n++) {
						if (!ps.contains(new Integer(n))) {
							addNonEssential("cell_" + cell.getY() + "_" + cell.getX(), n);
						}
					}
				}
			}
		}
	}

	private static void buildNEfromConstraints(ArrayList<Constraint> allConstraints) {

		boolean one=false,two=false,three=false,four=false,five=false,six=false,seven=false,eight=false,nine =false;
		for (Constraint c : allConstraints) {
			ArrayList<ArrayList<Integer>> cSat = c.getSatisfyingAssignments();
			Cell[] cells = c.getVariables();
			for (int i=0; i<cells.length; i++) {
				for(ArrayList<Integer> foo: cSat){
					switch(foo.get(i)){
						case 1: one=true;
						case 2: two=true;
						case 3: three=true;
						case 4: four=true;
						case 5: five=true;
						case 6: six=true;
						case 7: seven=true;
						case 8: eight=true;
						case 9: nine=true;
					}
				}
				String prefix = "cell_" + cells[i].getY() + "_" + cells[i].getX();
				if(!one){
					addNonEssential(prefix, 1); 
					one=false;
				}
				if(!two){
					addNonEssential(prefix, 2);
					two=false;
				}
				if(!three){
					addNonEssential(prefix, 3);
					one=false;
				}
				if(!four){
					addNonEssential(prefix, 4);
					four=false;
				}
				if(!five){
					addNonEssential(prefix, 5);
					five=false;
				}
				if(!six){
					addNonEssential(prefix, 6);
					six=false;
				}
				if(!seven){
					addNonEssential(prefix, 7);
					seven=false;
				}
				if(!eight){
					addNonEssential(prefix, 8);
					eight=false;
				}
				if(!nine){
					addNonEssential(prefix, 9);
					nine=false;
				}
			}
		}
	}

	/**
	 * Goes through the nonessential list and removes non-essential assignments
	 * from rowConstraints
	 *
	 */
	private static void reduceFromNE(ArrayList<Constraint> allConstraints) {

		for(String s: nonessential.keySet()) {
			// info[1] is y
			// info[2] is x
			String[] info = s.split("_");
			for (Constraint c : allConstraints) {
				Cell[] cells = c.getVariables();
				for(int j = 0; j < cells.length; j++) {
					//if you've found the right cell
					if (cells[j].getY() == Integer.parseInt(info[1]) && cells[j].getX() == Integer.parseInt(info[2])){
						//pull the arraylist of nonessential values from the hash table
						ArrayList<Integer> temp = nonessential.get(s); 
						//loop through the array list and remove each nonessential from the constraint and cell
						for(Integer assignment: temp){
							c.removeAssignment(j, assignment);
							cells[j].removeSolution(assignment);
						}
					}
				}
				//buildNEfromConstraint(c); //check to see if we can add any non-essentials
			}
		}
	}

    public static void addNonEssential(String ne, int val){
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	if(nonessential.containsKey(ne)){
    		temp = nonessential.get(ne);
    	}
    	temp.add(new Integer(val));
    	nonessential.put(ne, temp);
        
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