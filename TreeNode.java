import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TreeNode{

	TreeNode parent;
	private List<TreeNode> children;
	Cell associatedCell = null;
	Integer value = null;

	Board board = new Board();
	static ArrayList<Constraint> appliedConstraints = new ArrayList<Constraint>();
	static Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();

	public TreeNode(){
		// root's board is the Main.board
		board = Main.board;
		children = new ArrayList<TreeNode>();
	}

	public TreeNode(Board board, Integer value, Cell associatedCell){
		this();
		this.board = board.copy();
		setAssociatedCell(associatedCell);
		setValue(value);
	}

	public Board getBoard() {
		return board;
	}

	public void deleteBoard() {
		this.board = null;
	}

	public void setAssociatedCell(Cell associatedCell) {
		this.associatedCell = associatedCell;
	}

	public TreeNode getParent() {
		return this.parent;
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
		for(TreeNode child : children) {
			child.parent = this;
		}

		this.children = children;
	}

	public void addChild(TreeNode child) {
		child.parent = this;
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
		// when setting a value for a node,
		// remove values != this.value from domain
		Cell cell = board.getCell(associatedCell.getX(), associatedCell.getY());
		for(int i = 0; i < cell.getDomain().size(); i++) {
			if(!cell.getDomain().get(i).equals(value)) {
				cell.getDomain().remove(i);
			}
		}

		// set value
		this.value = value;

		// re-run arc consistency with new domain values
		Util.applyArcConsistency(board);
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

	/**
	 * Checks if this node can bear fruitful children
	 *
	 * @return true if this node can bear fruitful children
	 */
	public boolean canBearChildren() {
		for(Constraint constraint : board.getConstraints()) {
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

}