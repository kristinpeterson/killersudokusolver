package killersudokusolver;

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
public class Cell {

	private int x;
	private int y;
	private DomainSet domain;
	private int nonet;

	// used during forward checking / backtracking
	private DomainValue value;	//currently assigned value
	private Integer last_assigned;  //step that this variable was last assigned


	/**
	 * Cell constructor, sets x and y values based on given
	 * int parameters.
	 * Initializes empty domain list.
	 * x and y values are from 1..9
	 *
	 * @param x the cells x value (row)
	 * @param y the cells y value (column)
	 */
	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		this.domain = new DomainSet();
		this.nonet = setNonet();
		setLastAssigned(0);
	}

	/**
	 * Cell constructor, sets x and y values based on given
	 * string parameters.
	 * Initializes empty domain list.
	 * x and y values are from 1..9
	 *
	 * @param xs string representation of the cells x value
	 * @param ys string representation of the cells y value
	 */
	public Cell(String xs, String ys){
		this.x = Integer.parseInt(xs);
		this.y = Integer.parseInt(ys);
		this.domain = new DomainSet();
		this.nonet = setNonet();
		setLastAssigned(0);
	}

	/**
	 * Returns a deep copy of the cell
	 *
	 * @return a deep copy of the cell
	 */
	public Cell getDeepCopy(){
		Cell copy = new Cell(x, y);
		copy.domain = getDomainDeepCopy();
		return copy;
	}

	/**
	 * Returns the x value of this cell.
	 *
	 * @return the x value of this cell
	 */
	public int getX(){
		return x;
	}

	/**
	 * Returns the y value of this cell.
	 *
	 * @return the y value of this cell
	 */
	public int getY(){
		return y;
	}

	/**
	 * Checks if given cell is equal to this cell
	 * (based on x and y values)
	 *
	 * @param cell the cell being checked for equality to this cell
	 * @return true if the given cell is equal to this cell (based on x and y values)
	 */
	public boolean equals(Cell cell){
		return this.x == cell.getX() && this.y == cell.getY();
	}

	/**
	 * Adds a value to the cells domain list
	 *
	 * @param domainValue the value to add to this cells domain list
	 */
	public void addDomainValue(DomainValue domainValue){
		if(!domain.contains(domainValue))
			domain.addDomainValue(domainValue);
	}

	/**
	 * Removes the given value from the domain.
	 *
	 * @param domainValue the domain value to remove
	 * @return true if the domain value was successfully removed
	 */
	public boolean removeDomainValue(Integer domainValue){
		return domain.removeDomainValue(domainValue);
	}

	/**
	 * Return a deep copy of this cells domain values list
	 *
	 * @return a deep copy of this cells domain values list
	 */
	public DomainSet getDomainDeepCopy() {
		DomainSet domainClone = new DomainSet();
		for (DomainValue i : domain.getDomainValues()) {
			domainClone.getDomainValues().add(i);
		}
		return domainClone;
	}

	/**
	 * Returns a DomainSet for this cell
	 *
	 * @return a DomainSet for this cell
	 */
	public DomainSet getDomain() {
		return this.domain;
	}

	public DomainValue getValue(){
		return value;
	}

	public void setValue(DomainValue dv){
		value = dv;
	}

	public Integer getLastAssigned(){
		return last_assigned;
	}

	public void setLastAssigned(Integer count){
		last_assigned = count;
	}

	/**
	 * Sets which nonet this cell would be in
	 *  1 2 3
	 *  4 5 6
	 *  7 8 9
	 *
	 *  Nonet: A 3×3 grid of cells, as outlined by the bolder lines in the puzzle
	*/
	public int setNonet(){
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

	/**
	 * Returns which nonet number the cell is in
	 *  1 2 3
	 *  4 5 6
	 *  7 8 9
	 *
	 * @return which nonet number the cell is in
	 */
	public int getNonet() {
		return nonet;
	}

	/**
	 * Returns a string representation of this cell
	 * x: [x-value] y: [y-value] domain: [cells-domain-list]
	 *
	 * @return a string representation of this cell
	 */
	public String toString() {
		return "x: " + x + " y: " + y + " domain: " + getDomain();
	}

}