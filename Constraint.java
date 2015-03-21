package killersudokusolver;

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

    public void removeAssignment(int cellIndex, int val){
        //start for loop from the end as the remove function shifts the rest of the array down
        for(int j = satisfyingAssignments.size() - 1; j >= 0 ; j--) {
            if(satisfyingAssignments.get(j).get(cellIndex).equals(new Integer(val))) {
                satisfyingAssignments.remove(j);
            }
        }
        checkAssignments();
    }

    public void checkAssignments(){
        // For every variable in this constraint
        for(int i = 0; i < variables.length; i++){
            /*
                Set markers to false so that any domain values that aren't found in a satisfying assignment
                will be deleted from the cells domain
             */
            boolean [] markers = {true, false, false, false, false, false, false, false, false, false};

            // For all of this Constraint's satisfying assignments
            for(ArrayList<Integer> sa: satisfyingAssignments){
                // Get the domain value for the current variable/cell
                Integer cellDomainValue = sa.get(i);
                // And set marker for that value to true
                markers[cellDomainValue.intValue()] = true; //for every value found in a satisfying assignment mark it as true
            }

            // For each possible domain value
            for (int domainValue = 1; domainValue <= 9; domainValue++) {
                if(!markers[domainValue])
                    // if there were no satisfying assignments found for this variable/cell
                    // remove it from the cell domain
                    variables[i].removeAssignment(domainValue);
                else
                    markers[domainValue] = false; //reset marker to false for next loop
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
