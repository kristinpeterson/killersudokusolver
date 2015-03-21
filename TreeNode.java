package killersudokusolver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TreeNode{

	private TreeNode parent = null;
	private List<TreeNode> children;
	private Cell cell = null;
	private Integer value = null;
	private int depth = 0;

	public TreeNode(){
		children = new ArrayList<TreeNode>();
	}

	public TreeNode(int d, Integer value, Cell associatedCell){
		children = new ArrayList<TreeNode>();
		setCell(associatedCell.clone());
		setValue(value);
		depth = d;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void setParent(TreeNode p) {
		this.parent =p;
	}

	public int getDepth() {
		return depth;
	}

	public List<TreeNode> getChildren() {
		return this.children;
	}

	public void addChild(TreeNode child) {
		children.add(child);
	}

	public TreeNode getChildAt(int index) throws IndexOutOfBoundsException {
		return children.get(index);
	}

	public int getNumberOfChildren() {
		return getChildren().size();
	}

	public boolean hasChildren() {
		return (getNumberOfChildren() > 0);
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell.clone();
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		cell.setValue(value);
		this.value = value;
	}

	public String toString() {
		if(getValue() != null) {
			return getValue().toString();
		} else {
			return "";
		}
	}

	/**
	 * Checks if this node can bear fruitful children
	 *
	 * @return true if this node can bear fruitful children
	 */
	public boolean canBearChildren() {
		// Copy the board constraints, so that they aren't effected
		// when enforcing forward checking child bearing rule
		ArrayList<Constraint> constraints = Main.board.getConstraintsDeepCopy();
		Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();
		boolean canBearChildren = true;
		TreeNode currentNode = this;

		// Build list of nonessential values for this cell and all it's ancestors
		// Traverses up from current node up to root,
		// when currentNode.getCell() == null you're at the root
		while(currentNode != null && currentNode.getCell() != null) {
			//System.out.println("currentNode.getCell().getNon: " + currentNode.getCell().getNonessential().toString());
			for(Integer neValue : currentNode.getCell().getNonessential()) {
				Util.addNonEssential(nonessential, "cell_" + currentNode.getCell().getY() + "_" + currentNode.getCell().getX(), neValue);
				//System.out.println("adding to ne list: " + "cell_" + currentNode.getCell().getY() + "_" + currentNode.getCell().getX() + " val: " + neValue);
			}
			currentNode = currentNode.getParent();
		}

		// Apply arc consistency like a baus
		Util.reduceFromNE(nonessential, constraints);
		canBearChildren = Util.applyArcConsistency(constraints);
		
		return canBearChildren;
	}
}