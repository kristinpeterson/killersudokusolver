/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KillerSudoku{

	private ArrayList<Cage> cages;

	// These vars are used for finding possible sum combinations for all Cages
	private static int[] possibleValues = {0,1,2,3,4,5,6,7,8,9};
	private Stack<Integer> stack = new Stack<Integer>();
	private int sum = 0;

	public KillerSudoku(){
		cages = new ArrayList<Cage>();
	}

	public static void main(String[] argv) throws Exception {

		KillerSudoku ks = new KillerSudoku();

		/** Get cages from file */
	    File f = new File("test.txt");
    	FileReader fr = new FileReader(f);
    	BufferedReader br = new BufferedReader(fr);

    	String eachLine = br.readLine();
    	String [] grid;

    	while (eachLine != null) {
    		ks.addCage(new Cage(eachLine.split(",")));
      		eachLine = br.readLine();
    	}

		/** Find all combinations that add up to each Cage goal */
		ArrayList<List<Stack<Integer>>> sumCombinationsPerCage = new ArrayList<List<Stack<Integer>>>();
		for(int i = 0; i < ks.cages.size(); i++) {
			int goal = ks.cages.get(i).getGoal();
			int size = ks.cages.get(i).getCells().size();
			List<Stack<Integer>> possibleSolutions = new ArrayList<Stack<Integer>>();
			sumCombinationsPerCage.add(ks.sumCombinations(0, 9, goal, size, possibleSolutions));
		}

		// Printing possible solutions for Cages
		for(int i = 0; i < sumCombinationsPerCage.size(); i++) {
			System.out.println(sumCombinationsPerCage.get(i).toString());
		}

		// TODO: Permutations of possible solutions

	}

	private void addCage(Cage c){
		cages.add(c);
	}

	private List<Stack<Integer>> sumCombinations(int fromIndex, int endIndex, int target, int size, List<Stack<Integer>> possibleSolutions) {

		if (sum == target && stack.size() == size) {
			// possible solution found!
			Stack<Integer> possibleSolution = new Stack<Integer>();
			// Make copy of stack, reason:
			// cannot directly add stack to possibleSolutions list
			// because any changes to stack will propogate to possibleSolutions
			// list, rendering it useless.
			for(int i = 0; i < stack.size(); i++) {
				possibleSolution.add(stack.get(i));
			}
			possibleSolutions.add(possibleSolution);
		}

		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
			if (sum + possibleValues[currentIndex] <= target) {
				stack.push(possibleValues[currentIndex]);
				sum += possibleValues[currentIndex];
				sumCombinations(currentIndex + 1, endIndex, target, size, possibleSolutions);
				sum -= stack.pop();
			}
		}

		return possibleSolutions;
	}

}