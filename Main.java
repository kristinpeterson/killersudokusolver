package killersudokusolver;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
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
		try {
			getCagesFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add cells to board
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

		Util.applyArcConsistency(board.getConstraints());

		// Print Milestone 1 output (before clearing categorized constraint lists)
		Util.printM1Output();

		// Create tree to search for solution
		board.orderCellsAscending(); // orders cells in increasing satisfying assignment order

		Util.printM2Output();

		Tree tree = new Tree();
		TreeNode root = new TreeNode();
		tree.setRoot(root);
		List<TreeNode> currentLevel = new ArrayList<TreeNode>();
		List<TreeNode> nextLevel = new ArrayList<TreeNode>();
		currentLevel.add(root);
		// Create new level for each cell in the board

		for(int i = 0; i < board.getCells().size(); i++){//board.getCells().size() - 1; i++) {
			Cell currentCell = board.getCells().get(i);
			// Iterate through nodes in current level and add children
			for(int j = currentLevel.size() - 1; j >= 0; j--) {
				TreeNode aNode = currentLevel.get(j);
				for(Integer value : currentCell.getDomain()) {
					TreeNode newNode = new TreeNode(i, value, currentCell);
					newNode.setParent(aNode);
					if(newNode.canBearChildren()) {
						// Only add node if it can bear children
						aNode.addChild(newNode);
						nextLevel.add(newNode);
					}  // add something here to mark dead end? or prune?
				}
				currentLevel.remove(j);
			}

			System.out.println(tree.toStringWithDepth());
			//System.out.println("cell: " + currentCell);
			//System.out.println("level #:\t" + i + "\tNumber of nodes on level\t" + nextLevel.size() + ": " + nextLevel.toString() + "\n");
			// Update current level to next level
			currentLevel.addAll(nextLevel);
			nextLevel.clear();
		}
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
			cageConstraints.add(new Constraint(cage.getCageId(), variables, cage.getPermutatedSolutions()));
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