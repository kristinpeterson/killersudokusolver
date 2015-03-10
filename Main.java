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

	public static void main(String[] argv) throws Exception {

		Board board = new Board();

		/** Get cages from file */
	    File f = new File("test.txt");
    	FileReader fr = new FileReader(f);
    	BufferedReader br = new BufferedReader(fr);

    	String eachLine = br.readLine();

    	while (eachLine != null) {
    		board.addCage(new Cage(eachLine.split(",")));
      		eachLine = br.readLine();
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

		for(Cage aCage : ks.cages) {
			//System.out.println(aCage.toString());
			output.write(aCage.toString());
		}

        output.close();

	}

}