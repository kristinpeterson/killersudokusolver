import java.util.ArrayList;
import java.util.Hashtable;

public class Tree{

	Board treeBoard;
	Cell newAssignment;
	int depth; //depth of tree
	Tree parent;
	static ArrayList<Constraint> appliedConstraints = new ArrayList<Constraint>();
	static Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();

	public Tree(Board b, ArrayList<Constraint> allConstraints){ // create root of tree
		depth = 0;
		parent = null;
		newAssignment = null;
		treeBoard = b;
		for (Constraint c: allConstraints ) {
			appliedConstraints.add(c);
		}

		applyArcConsistency();
		appliedConstraints = sortConstraintBySize(appliedConstraints);
		for (Constraint c: appliedConstraints ) {
			System.out.println("Constraint " +c.getName() + " size " +c.getSatisfyingAssignments().size());
		}

	}

	public Tree(Board b, Cell c, int d, Tree p, ArrayList<Constraint> ac){
		depth = d;
		newAssignment = c;
		parent = p;
		treeBoard = b;
		applyArcConsistency();
	}

	public boolean canBearChild(){
		for (Constraint c : appliedConstraints) {
			if(c.getSatisfyingAssignments().size() == 0)
				return false; // there is a constraint that has no satisfying assignments
		}
		return true;
	}

	public ArrayList<Tree> createChild(){
		ArrayList<Tree> newLevel = new ArrayList<Tree>();
		Board child = this.treeBoard;
		Cell c = getNextLevel();
		c = child.getCell(c.getX(), c.getY());
		ArrayList<Constraint> conClone = new ArrayList<Constraint>();
		for (Constraint con: appliedConstraints ) {
			conClone.add(con);
		}
		for(Integer i : c.getSolutions()){
			Cell next = new Cell(c.getX(), c.getY());
			next.setValue(i);
			child.addCell(next);
			newLevel.add(new Tree(child, next, depth+1, this, conClone));
		}
		return newLevel;
	}

	private Cell getNextLevel(){
		for (Constraint c: appliedConstraints) {
			if(c.getSatisfyingAssignments().size() > 1){
				for (Cell cell : c.getVariables()){
					if (cell.getSolutions().size() > 1)
						return cell;
				}
			}
		}
		return new Cell(0,0);
	}

	private void applyArcConsistency(){
		buildNEfromCageConstraint();
		reduceFromNE();
		buildNEfromConstraints();
		reduceFromNE();
	}

	private static void buildNEfromCageConstraint() {
		for (Constraint c : appliedConstraints) {
			if (!c.getName().startsWith("C")) {
				for (Cell cell : c.getVariables()) {
					ArrayList<Integer> ps = cell.getSolutions();
					if (ps.size() < 9) {
						for (int n = 1; n < 10; n++) {
							if (!ps.contains(new Integer(n))) {
								addNonEssential("cell_" + cell.getY() + "_" + cell.getX(), n);
							}
						}
					}
				}
			}
		}
	}

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

    private static ArrayList<Constraint> sortConstraintBySize(ArrayList<Constraint> ac) {
		ArrayList<Constraint> sorted = new ArrayList<Constraint>();
		//Base cases as this method is recursive
		if(ac.size()<=1){
			return ac;
		} else if (ac.size() == 2){
			if(ac.get(0).getSatisfyingAssignments().size() < ac.get(1).getSatisfyingAssignments().size())
				return ac;
			else{
				sorted.add(ac.get(1));
				sorted.add(ac.get(0));
				return sorted;
			}
		}
		ArrayList<Constraint> smaller = new ArrayList<Constraint>();
		ArrayList<Constraint> bigger = new ArrayList<Constraint>();
		ArrayList<Constraint> equal = new ArrayList<Constraint>();
		int pindex = ac.size()/2;
		int pivot = ac.get(pindex).getSatisfyingAssignments().size(); //size of middle element as pivot
		for (int h = 0; h < ac.size(); h++) {
			if (h!=pindex){
				if(ac.get(h).getSatisfyingAssignments().size() > pivot)
					bigger.add(ac.get(h));
				if(ac.get(h).getSatisfyingAssignments().size() < pivot)
					smaller.add(ac.get(h));
				if(ac.get(h).getSatisfyingAssignments().size() == pivot)
					equal.add(ac.get(h));
			}
		}
		smaller = sortConstraintBySize(smaller);
		smaller.addAll(equal);
		bigger = sortConstraintBySize(bigger);
		smaller.addAll(bigger);
		return smaller;
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

	public int getDepth(){
		return depth;
	}

}