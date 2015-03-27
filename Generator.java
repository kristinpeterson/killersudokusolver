import java.util.ArrayList;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * The Cell class represents each cell in the Killer Sudoku puzzle.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class Generator{

	//?private int step_count;
	private int depth;
	private ArrayList<Constraint> filters;
	private Cell associatedCell;
	private int value;

	public Generator(Cell acell, int d){
		associatedCell = acell;
		depth = d;
		filters = new ArrayList<Constraint>();
        for (Constraint c : Main.board.getConstraints()) {
        	String x = ""+associatedCell.getX();
        	String y = ""+associatedCell.getY();
            if(c.getName().contains(x+y) | c.getName().contains(x+"."+y)){ 
                filters.add(c);
            }
        }
        
	}

	public int getValue(){
		return value;
	}

	public void setValue(int step_count){
		ArrayList<Integer> domain = associatedCell.getDomain();
		/* */
		
	}


}