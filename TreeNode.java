import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeNode{

	TreeNode parent = null;
	private List<TreeNode> children;
	Cell associatedCell = null;
	Integer value = null;
	int depth = 0;

	//static ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	static Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();

	public TreeNode(){
		children = new ArrayList<TreeNode>();
	}

	public TreeNode(int d, Integer value, Cell associatedCell){
		children = new ArrayList<TreeNode>();
		setAssociatedCell(associatedCell.clone());
		setValue(value);
		depth = d;
	}

	/*public Board getBoard() {
		return board;
	}

	public void deleteBoard() {
		this.board = null;
	}*/

	public void setAssociatedCell(Cell associatedCell) {
		this.associatedCell = associatedCell.clone();
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void setParent(TreeNode p) {
		this.parent =p;
	}

	public List<TreeNode> getChildren() {
		return this.children;
	}

	public int getNumberOfChildren() {
		return getChildren().size();
	}

	public boolean hasChildren() {
		return (getNumberOfChildren() > 0);
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void addChild(TreeNode child) {
		children.add(child);
	}

	public void addChildAt(int index, TreeNode child) throws IndexOutOfBoundsException {
		child.parent = this;
		children.add(index, child);
	}

	public void removeChildren() {
		this.children = new ArrayList<TreeNode>();
	}

	public void removeChildAt(int index) throws IndexOutOfBoundsException {
		children.remove(index);
	}

	public TreeNode getChildAt(int index) throws IndexOutOfBoundsException {
		return children.get(index);
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		associatedCell.setValue(value);
		this.value = value;

		// re-run arc consistency with new domain values
		//Util.applyArcConsistency(board);
	}

	public String toString() {
		if(getValue() != null) {
			return getValue().toString();
		} else {
			return "";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TreeNode other = (TreeNode) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public String toStringVerbose() {
		String stringRepresentation = getValue().toString() + ":[";

		for (TreeNode node : getChildren()) {
			stringRepresentation += node.getValue().toString() + ", ";
		}

		//Pattern.DOTALL causes ^ and $ to match. Otherwise it won't. It's retarded.
		Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(stringRepresentation);

		stringRepresentation = matcher.replaceFirst("");
		stringRepresentation += "]";

		return stringRepresentation;
	}

	public String toStringA() {
		String stringRepresentation = new String();
		stringRepresentation += "depth "+depth+"\n";
		/*for (TreeNode node : getChildren()) {
			stringRepresentation += node.getValue().toString() + ", ";
		}

		//Pattern.DOTALL causes ^ and $ to match. Otherwise it won't. It's retarded.
		Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(stringRepresentation);

		stringRepresentation = matcher.replaceFirst("");
		stringRepresentation += "]";*/
		if(parent!=null){
			stringRepresentation += "parent value "+parent.toString() +"\n";
		}
		return stringRepresentation+"associatedCell " + associatedCell.toString();
	}

	/**
	 * Checks if this node can bear fruitful children
	 *
	 * @return true if this node can bear fruitful children
	 */
	public boolean canBearChildren(ArrayList<Constraint> constraints) {
		ArrayList<Constraint> treeConstraints = new ArrayList<Constraint>();
		for(Constraint constraint : constraints) {
			treeConstraints.add(constraint);
		}
		treeConstraints = Util.applyArcConsistency(this, treeConstraints);
           /* for (Constraint c : treeConstraints) {
                System.out.println("Constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n");
            }*/
		for(Constraint constraint : treeConstraints) {
			// for each constraint on board
			for(Cell variable : constraint.getVariables()) {
				// search variables of that constraint
				if(variable.equals(associatedCell)) {
					// Constraint on this nodes's associatedCell found!
					if(constraint.isSatisfyingAssignment(associatedCell, value)) {
						// if nextCell (depth) contains a satisfying assignment that matches
						// this node's associatedCell and value, it can bear children
						return true;
					}
				}
			}
		}
		return false;
	}

	public Cell[] traverseUp(){
		if (associatedCell == null){return new Cell[0];}
			
		//System.out.println("traverseUp "+associatedCell);
		Cell[] cells = {associatedCell};
		//return cells;
		if (parent ==null ){//depth <= 0) {
			return cells;
		} else {
			return Util.concatArray(cells, parent.traverseUp());
		}
	}

}