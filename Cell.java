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
	private ArrayList<Integer> possibleSolutions;

	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		possibleSolutions = new ArrayList<Integer>();
	}

	public Cell(String xs, String ys){
		x = Integer.parseInt(xs);
		y = Integer.parseInt(ys);
		possibleSolutions = new ArrayList<Integer>();
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setValue(int num){
		value = num;
	}

	public void setValue(Integer num){
		value = num.intValue();
		possibleSolutions.clear();
		addSolution(num);
	}

	public boolean equals(Cell c){
		return this.x == c.getX() && this.y == c.getY();
	}

	public boolean update(Cell c){
		if(this.equals(c)){
			this.value = c.value;
			possibleSolutions = c.getSolutions();
			return false;
		}
		return false;
	}

	public int getValue(){
		return value;
	}

	public boolean isSolution(Integer i){
		return possibleSolutions.contains(i);
	}

	public void addSolution(Integer i){
		if(!possibleSolutions.contains(i))
			possibleSolutions.add(i);
	}

	public boolean removeSolution(Integer i){
		return possibleSolutions.remove(i);
	}

	public ArrayList<Integer> getSolutions() {
		ArrayList<Integer> solutionsClone = new ArrayList<Integer>();
		for (Integer i : possibleSolutions) {
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
		return "x: " + x + " y: " + y + " domain: " + getSolutions() + "\n";
	}

}