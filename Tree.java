import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * A tree structure used to search for a solution that
 * satisfies all the problem constraints.
 *
 * The tree enforces static variable ordering, forward-checking childbearing rule,
 * and uses the associatedCell approach for extending an assignment (i.e. generating
 * the children of a given node).
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class Tree {

    enum TreeTraversalOrderEnum {
        PRE_ORDER,
        POST_ORDER
    }

    private TreeNode root;

    public Tree() {
        super();
    }

    public TreeNode getRoot() {
        return this.root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        int numberOfNodes = 0;

        if(root != null) {
            numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; //1 for the root!
        }

        return numberOfNodes;
    }

    private int auxiliaryGetNumberOfNodes(TreeNode node) {
        int numberOfNodes = node.getNumberOfChildren();

        for(TreeNode child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }

        return numberOfNodes;
    }

    public boolean exists(Integer valueToFind) {
        return (find(valueToFind) != null);
    }

    public TreeNode find(Integer valueToFind) {
        TreeNode returnNode = null;

        if(root != null) {
            returnNode = auxiliaryFind(root, valueToFind);
        }

        return returnNode;
    }

    private TreeNode auxiliaryFind(TreeNode currentNode, Integer valueToFind) {
        TreeNode returnNode = null;
        int i = 0;

        if (currentNode.getValue().equals(valueToFind)) {
            returnNode = currentNode;
        }

        else if(currentNode.hasChildren()) {
            i = 0;
            while(returnNode == null && i < currentNode.getNumberOfChildren()) {
                returnNode = auxiliaryFind(currentNode.getChildAt(i), valueToFind);
                i++;
            }
        }

        return returnNode;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public List<TreeNode> build(TreeTraversalOrderEnum traversalOrder) {
        List<TreeNode> returnList = null;

        if(root != null) {
            returnList = build(root, traversalOrder);
        }

        return returnList;
    }

    public List<TreeNode> build(TreeNode node, TreeTraversalOrderEnum traversalOrder) {
        List<TreeNode> traversalResult = new ArrayList<TreeNode>();

        if(traversalOrder == TreeTraversalOrderEnum.PRE_ORDER) {
            buildPreOrder(node, traversalResult);
        }

        else if(traversalOrder == TreeTraversalOrderEnum.POST_ORDER) {
            buildPostOrder(node, traversalResult);
        }

        return traversalResult;
    }

    private void buildPreOrder(TreeNode node, List<TreeNode> traversalResult) {
        traversalResult.add(node);

        for(TreeNode child : node.getChildren()) {
            buildPreOrder(child, traversalResult);
        }
    }

    private void buildPostOrder(TreeNode node, List<TreeNode> traversalResult) {
        for(TreeNode child : node.getChildren()) {
            buildPostOrder(child, traversalResult);
        }

        traversalResult.add(node);
    }

    public Map<TreeNode, Integer> buildWithDepth(TreeTraversalOrderEnum traversalOrder) {
        Map<TreeNode, Integer> returnMap = null;

        if(root != null) {
            returnMap = buildWithDepth(root, traversalOrder);
        }

        return returnMap;
    }

    public Map<TreeNode, Integer> buildWithDepth(TreeNode node, TreeTraversalOrderEnum traversalOrder) {
        Map<TreeNode, Integer> traversalResult = new LinkedHashMap<TreeNode, Integer>();

        if(traversalOrder == TreeTraversalOrderEnum.PRE_ORDER) {
            buildPreOrderWithDepth(node, traversalResult, 0);
        }

        else if(traversalOrder == TreeTraversalOrderEnum.POST_ORDER) {
            buildPostOrderWithDepth(node, traversalResult, 0);
        }

        return traversalResult;
    }

    private void buildPreOrderWithDepth(TreeNode node, Map<TreeNode, Integer> traversalResult, int depth) {
        traversalResult.put(node, depth);

        for(TreeNode child : node.getChildren()) {
            buildPreOrderWithDepth(child, traversalResult, depth + 1);
        }
    }

    private void buildPostOrderWithDepth(TreeNode node, Map<TreeNode, Integer> traversalResult, int depth) {
        for(TreeNode child : node.getChildren()) {
            buildPostOrderWithDepth(child, traversalResult, depth + 1);
        }
        traversalResult.put(node, depth);
    }

    public String toString() {
        String stringRepresentation = "";
        if(root != null) {
            stringRepresentation = build(TreeTraversalOrderEnum.PRE_ORDER).toString();
        }
        return stringRepresentation;
    }

    public String toStringWithDepth() {
       String stringRepresentation = "";

        if(root != null) {
            stringRepresentation = buildWithDepth(TreeTraversalOrderEnum.PRE_ORDER).toString();
        }

        return stringRepresentation;
    }
}
