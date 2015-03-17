import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * Represents the a constraint on the Killer Sudoku puzzle.
 * Each constraint has a set of associated variables (Cells) and
 * satisfying assignments for the constraint.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 */
public class Constraint {

    private String name;
    private Cell[] variables;
    private ArrayList<ArrayList<Integer>> satisfyingAssignments;
    public final int preSize; // initial number of satisfying assignments

    public Constraint(String name, Cell[] variables, ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        setName(name);
        setVariables(variables);
        setSatisfyingAssignments(satisfyingAssignments);
        preSize = satisfyingAssignments.size();
    }

    public Constraint clone(){
        Cell[] copyCells = new Cell[variables.length];
        for(int i=0; i< copyCells.length; i++){
            copyCells[i] = variables[i].clone();
        }
        return new Constraint(this.getName(), copyCells, this.getSatisfyingAssignmentsClone());
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVariables(Cell[] variables) {
        this.variables = variables;
    }

    public Cell[] getVariables() {
        return variables;
    }

    public void setSatisfyingAssignments(ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        ArrayList<ArrayList<Integer>> satisfyingAssignmentsClone = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> assignment : satisfyingAssignments) {
            satisfyingAssignmentsClone.add(assignment);
        }
        this.satisfyingAssignments = satisfyingAssignmentsClone;
    }

    public ArrayList<ArrayList<Integer>> getSatisfyingAssignments() {
        return satisfyingAssignments;
    }

    public ArrayList<ArrayList<Integer>> getSatisfyingAssignmentsClone() {
        ArrayList<ArrayList<Integer>> satisfyingAssignmentsClone = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> assignment : satisfyingAssignments) {
            satisfyingAssignmentsClone.add(assignment);
        }
        return satisfyingAssignmentsClone;
    }

    public void removeAssignment(int cell, int val){
        //start for loop from the end as the remove function shifts the rest of the array down
        for(int j = satisfyingAssignments.size() - 1; j >= 0 ; j--) {
            if(satisfyingAssignments.get(j).get(cell).equals(new Integer(val))) {
                satisfyingAssignments.remove(j);
            }
        }
    }

    /**
     * Checks if the given cell/value combination is a satisfying assignment
     * for this constraint
     *
     * @param cell
     * @param value
     * @return true if the given cell/value
     */
    public boolean isSatisfyingAssignment(Cell cell, Integer value) {
        int index = 0;
        for(int i = 0; i < variables.length; i++) {
            if(variables[i].equals(cell)) {
                index = i;
            }
        }
        for(ArrayList<Integer> satisfyingAssignment : satisfyingAssignments) {
            if(satisfyingAssignment.get(index).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Constraint "+name +"\n");
        for(int i=0; i<variables.length; i++){
            sb.append("Cell " +i + ": " +variables[i].toString()+"\n");
            //sb.append(satisfyingAssignments.get(i));
        }
        sb.append(satisfyingAssignments);
        return sb.toString();
    }

}
