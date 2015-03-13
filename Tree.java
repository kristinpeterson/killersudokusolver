import java.util.ArrayList;
import java.util.Hashtable;

public class Tree{

	Board treeBoard;
	Cell newAssignment;
	int depth; //depth of tree
	Tree parent;
	static ArrayList<Constraint> appliedConstraints = new ArrayList<Constraint>();
	static Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();

	public Tree(Board b, ArrayList<Constraint> rowConstraints, ArrayList<Constraint> colConstraints, 
				ArrayList<Constraint> cageConstraints, ArrayList<Constraint> nonetConstraints){ // create root of tree
		depth = 0;
		parent = null;
		newAssignment = null;
		treeBoard = b;
		for (Constraint c: rowConstraints ) {
			appliedConstraints.add(c);
		}
		for (Constraint c: colConstraints ) {
			appliedConstraints.add(c);
		}
		for (Constraint c: nonetConstraints ) {
			appliedConstraints.add(c);
		}
		for (Constraint c: cageConstraints ) {
			appliedConstraints.add(c);
		}
	}

	public Tree(Board b, Cell c, int d, Tree p, ArrayList<Constraint> ac){
		depth = d;
		newAssignment = c;
		parent = p;
		treeBoard = b;
		appliedConstraints = ac;
		
		//Apply arc consistency at every level
		buildNEfromConstraints();
		reduceFromNE();
	}

	public boolean canBearChild(){
		for (Constraint c : appliedConstraints) {
			if(c.getSatisfyingAssignments().size() == 0)
				return false; // there is a constraint that has no satisfying assignments
		}
		return true;
	}

	public ArrayList<Tree> createChild(Cell c){
		ArrayList<Tree> newLevel = new ArrayList<Tree>();
		Board child = treeBoard;
		c = child.getCell(c.getX(), c.getY());
		for(Integer i : c.getSolutions()){
			Cell next = new Cell(c.getX(), c.getY());
			next.setValue(i);
			child.addCell(next);
			newLevel.add(new Tree(child, next, depth+1, this, appliedConstraints));
		}
		return newLevel;
	}

	//order by size to make more effective filter (smaller tree)
	//make reduce return new constraint array?

	private void buildNEfromConstraints() {

		boolean one=false,two=false,three=false,four=false,five=false,six=false,seven=false,eight=false,nine =false;
		for (Constraint c : appliedConstraints) {
			ArrayList<ArrayList<Integer>> cSat = c.getSatisfyingAssignments();
			Cell[] cells = c.getVariables();
			for (int i=0; i<cells.length; i++) {
				for(ArrayList<Integer> foo: cSat){
					switch(foo.get(i)){
						case 1: one=true;
						case 2: two=true;
						case 3: three=true;
						case 4: four=true;
						case 5: five=true;
						case 6: six=true;
						case 7: seven=true;
						case 8: eight=true;
						case 9: nine=true;
					}
				}
				String prefix = "cell_" + cells[i].getY() + "_" + cells[i].getX();
				if(!one){
					addNonEssential(prefix, 1); 
					one=false;
				}
				if(!two){
					addNonEssential(prefix, 2);
					two=false;
				}
				if(!three){
					addNonEssential(prefix, 3);
					one=false;
				}
				if(!four){
					addNonEssential(prefix, 4);
					four=false;
				}
				if(!five){
					addNonEssential(prefix, 5);
					five=false;
				}
				if(!six){
					addNonEssential(prefix, 6);
					six=false;
				}
				if(!seven){
					addNonEssential(prefix, 7);
					seven=false;
				}
				if(!eight){
					addNonEssential(prefix, 8);
					eight=false;
				}
				if(!nine){
					addNonEssential(prefix, 9);
					nine=false;
				}
			}
		}
	}

	/**
	 * Goes through the nonessential list and removes non-essential assignments
	 * from rowConstraints
	 *
	 */
	private void reduceFromNE() {

		for(String s: nonessential.keySet()) {
			// info[1] is y
			// info[2] is x
			String[] info = s.split("_");
			for (Constraint c : appliedConstraints) {
				Cell[] cells = c.getVariables();
				for(int j = 0; j < cells.length; j++) {
					//if you've found the right cell
					if (cells[j].getY() == Integer.parseInt(info[1]) && cells[j].getX() == Integer.parseInt(info[2])){
						//pull the arraylist of nonessential values from the hash table
						ArrayList<Integer> temp = nonessential.get(s); 
						//loop through the array list and remove each nonessential from the constraint and cell
						for(Integer assignment: temp){
							c.removeAssignment(j, assignment);
							cells[j].removeSolution(assignment);
						}
					}
				}
				//buildNEfromConstraint(c); //check to see if we can add any non-essentials
			}
		}
	}

    public static void addNonEssential(String ne, int val){
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	if(nonessential.containsKey(ne)){
    		temp = nonessential.get(ne);
    	}
    	temp.add(new Integer(val));
    	nonessential.put(ne, temp);
        
    }

    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if(parent != null)
    		sb.append(parent.toString());
    	sb.append("Tree depth: " + depth + "\n");
    	if(newAssignment != null)
    		sb.append("Assigned = "+ newAssignment.toString());
		return sb.toString();
	}

}