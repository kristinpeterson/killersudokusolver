/*
CECS-551 AI 
Killer Sudoku Solver
*/

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.ArrayList;

public class KillerSudoku{
	
	private ArrayList cages;

	public KillerSudoku(){
		cages = new ArrayList();
	}

	public void addCage(Cage c){
		cages.add(c);
	}

	public static void main(String[] argv) throws Exception {

		KillerSudoku ks = new KillerSudoku();

	    File f = new File("test.txt");
    	FileReader fr = new FileReader(f);
    	BufferedReader br = new BufferedReader(fr);

    	String eachLine = br.readLine();
    	String [] grid;

    	while (eachLine != null) {
    		ks.addCage(new Cage(eachLine.split(",")));
      		eachLine = br.readLine();
    	}
    	System.out.println("Done");
	}

}