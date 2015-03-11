import java.util.ArrayList;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * Represents a Killer Sudoku puzzle,
 *
 * Killer Sudoku is a puzzle that combines elements of Sudoku and Kakuro.
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 */
public class Constraint {

    private String name;
    private Cell[] variables;
    private ArrayList<ArrayList<Integer>> satisfyingAssignments;

    public Constraint(String name, Cell[] variables, ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        setName(name);
        setVariables(variables);
        setSatisfyingAssignments(satisfyingAssignments);
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

    public void removeAssignment(int cell, int val){
        //start for loop from the end as the remove function shifts the rest of the array down
        for(int j = satisfyingAssignments.size() - 1; j >= 0 ; j--) {
            if(satisfyingAssignments.get(j).get(cell).equals(new Integer(val))) {
                satisfyingAssignments.remove(j);
            }
        }
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
