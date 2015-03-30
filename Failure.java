package killersudokusolver;

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
public class Failure{

	private int step_count;
	private Cell associatedCell;
	private int value;

	public Failure(Cell acell, int count, int value){
		associatedCell = acell;
		step_count = count;
		this.value = value;
	}

	public int getStepCount(){
		return step_count;
	}

	public Cell getCell(){
		return associatedCell;
	}

	public int getValue(){
		return value;
	}

}