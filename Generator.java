package killersudokusolver;

import java.util.ArrayList;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class Generator{

	private ArrayList<Constraint> filters;
	private Cell variable;
	DomainValue workingHypothesis;
	Integer assignCount;	//counts the number of assignments that the generator has made to its variable

	/**
	 * Constructor for the generator, sets the variable
	 * for the generator to the given cell
	 *
	 * @param variable the cell to set as the generators assigned variable
	 */
	public Generator(Cell variable){
		setVariable(variable);
		filters = new ArrayList<Constraint>();
        for (Constraint c : Main.board.getConstraints()) {
        	String x = ""+variable.getX();
        	String y = ""+variable.getY();
            if(c.getName().contains(x+y) | c.getName().contains(x+"."+y)){ 
                filters.add(c);
            }
        }
        setAssignCount(0);
	}

	/**
	 * Return the current working hypothesis for the generator
	 *
	 * @return the current working hypothesis for the generator
	 */
	public DomainValue getWorkingHypothesis(){
		return workingHypothesis;
	}

	/**
	 * Sets the current working hypothesis for the generator
	 *
	 * @param workingHypothesis the current working hypothesis for the generator
	 */
	public void setWorkingHypothesis(DomainValue workingHypothesis){
		this.workingHypothesis = workingHypothesis;
	}

	/**
	 * Sets the variable for the generator
	 *
	 * @param variable the variable to set for the generator
	 */
	public void setVariable(Cell variable) {
		this.variable = variable;
	}

	/**
	 * Returns the generators variable
	 *
	 * @return the generators variable
	 */
	public Cell getVariable() {
		return variable;
	}

	/**
	 * Sets the assign count for the generator
	 * which indicates how many times this generator has been assigned
	 *
	 * @param assignCount the assign count for the generator
	 */
	public void setAssignCount(Integer assignCount) {
		this.assignCount = assignCount;
	}

	/**
	 * Returns the assign count for the generator
	 *
	 * @return the assign count for the generator
	 */
	public Integer getAssignCount() {
		return assignCount;
	}

	/**
	 * Increments the generators assign count
	 *
	 */
	public void incrementAssignCount() {
		assignCount++;
	}
}