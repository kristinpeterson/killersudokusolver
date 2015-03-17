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
public class Cell{

	private int x;
	private int y;
	private int value;
	private ArrayList<Integer> domain;
	private ArrayList<Constraint> constraints; // constraints associated w/ this cell

	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		domain = new ArrayList<Integer>();
		constraints = new ArrayList<Constraint>();
	}

	public Cell(String xs, String ys){
		x = Integer.parseInt(xs);
		y = Integer.parseInt(ys);
		domain = new ArrayList<Integer>();
		constraints = new ArrayList<Constraint>();
	}

	public Cell clone(){
		Cell copy = new Cell(x,y);
		copy.domain = getDomain();
		copy.value = value;
		copy.constraints = getConstraints();
		return copy;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	public ArrayList<Constraint> getConstraints() {
		ArrayList<Constraint> clone = new ArrayList<Constraint>();
		for(Constraint c : constraints) {
			clone.add(c);
		}
		return clone;
	}

	public void setValue(Integer num){
		value = num.intValue();
		domain.clear();
		addDomainValue(num);
	}

	public int getValue() {
		return value;
	}

	public boolean equals(Cell c){
		return this.x == c.getX() && this.y == c.getY();
	}

	public boolean update(Cell c){
		if(this.equals(c)){
			this.value = c.value;
			domain = c.getDomain();
			return false;
		}
		return false;
	}

	public void addDomainValue(Integer i){
		if(!domain.contains(i))
			domain.add(i);
	}

	public boolean removeAssignment(Integer i){
		return domain.remove(i);
	}

	public ArrayList<Integer> getDomain() {
		ArrayList<Integer> solutionsClone = new ArrayList<Integer>();
		for (Integer i : domain) {
			solutionsClone.add(i);
		}
		return solutionsClone;
	}

	/** Return which nonet this cell would be in
	 *  1 2 3
	 *  4 5 6
	 *  7 8 9
	 *
	 *  Nonet: A 3×3 grid of cells, as outlined by the bolder lines in the puzzle
	*/
	public int getNonet(){
		if (this.getX() < 4){
			if (this.getY() < 4) {
				return 1;
			} else if (this.getY() < 7){
				return 2;
			}
			return 3;

		} else if (this.getX() < 7){
			if (this.getY() < 4) {
				return 4;
			} else if (this.getY() < 7){
				return 5;
			}
			return 6;

		} else {
			if (this.getY() < 4) {
				return 7;
			} else if (this.getY() < 7){
				return 8;
			}
			return 9;
		}

	}

	public String toString() {
		return "x: " + x + " y: " + y + " domain: " + getDomain() + "\n";
	}

}