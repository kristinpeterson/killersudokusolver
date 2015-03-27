import java.util.ArrayList;
import java.util.Hashtable;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * The FailureReport 
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class FailureReport{

	private ArrayList<Failure> failures;

	public FailureReport(){
		failures = new ArrayList<Failure>();
	}

	public void addFailure(Failure f){
		failures.add(f);
	}


}