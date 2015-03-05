/*
CECS-551 AI 
Killer Sudoku Solver
*/

public class Cell{

	private int x;
	private int y;
	private int value;

	public Cell(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Cell(String xs, String ys){
		x = Integer.parseInt(xs);
		y = Integer.parseInt(ys);
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

	public int getValue(){
		return value;
	}

}