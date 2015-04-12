package killersudokusolver;

import java.util.ArrayList;
import java.util.Hashtable;

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
    public final int initialSatisfyingAssignmentSize; // initial number of satisfying assignments
    private ArrayList<FilterTable> filterTables;

    /**
     * Constraint constructor, sets the constraints name,
     * variables, and satisfying assignments list
     *
     * Also sets the constraints initialSatisfyingAssignmentSize
     *
     * @param name the name of this constraint
     * @param variables the variables this constraint pertains to
     * @param satisfyingAssignments the satisfying assignment list for this constraint
     */
    public Constraint(String name, Cell[] variables, ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        setName(name);
        setVariables(variables);
        setSatisfyingAssignments(satisfyingAssignments);
        initialSatisfyingAssignmentSize = satisfyingAssignments.size();
        filterTables = new ArrayList<FilterTable>(81);
        for(int i=0; i<81; i++)
            filterTables.add(new FilterTable());
    }

    /**
     * Returns a deep copy of this constraint
     *
     * @return a deep copy of this constraint
     */
    public Constraint getDeepCopy(){
        Cell[] copyCells = new Cell[variables.length];
        for(int i=0; i< copyCells.length; i++){
            copyCells[i] = variables[i].getDeepCopy();
        }
        return new Constraint(this.getName(), copyCells, this.getSatisfyingAssignmentsDeepCopy());
    }

    public void constructFilterTables(Hashtable<String, Integer> generator_map){
        Cell[] vars_new_order = new Cell[variables.length];
        int[] vars_indices = new int[variables.length];
        int index = 0; // variable to keep track of how full vars_ arrays are
        int[] depths = new int[variables.length]; // keep track of generator depths
        int min_depth = 81; //used to search and order variables
        int done_depth = 0;
        for (int i = 0; i < variables.length; i++) {
            int depth = generator_map.get(""+variables[i].getX()+variables[i].getY());
            depths[i] = depth;
            if(min_depth > depth)
                min_depth = depth;
        }
        //prep structures for filter table
        while(min_depth < 81) {
            for (int i = 0; i < variables.length; i++) {
                if(depths[i] == min_depth){
                    vars_new_order[index] = variables[i]; //keep track of the order of the variables depth
                    vars_indices[index] = i;
                    done_depth = depths[i]; //erase this depth from the record so we can search for next lowest
                    min_depth = 81; //reset min_depth
                    index++;
                }
            }
            //find next lowest depth
            for (int j = 0; j < variables.length; j++) {
                if(min_depth > depths[j] && depths[j] > done_depth)
                    min_depth = depths[j];
            }
        }

        //make filter tables
        ArrayList<Cell> filter_variables = new ArrayList<Cell>();
        for (int k = 0; k < variables.length; k++) {
            FilterTable ft = new FilterTable();
            Hashtable<String, Integer> table = new Hashtable<String, Integer>();

            ft.setVariables(filter_variables);
            for(ArrayList<Integer> sa: satisfyingAssignments){
                StringBuilder s = new StringBuilder();
                for (int j=0; j<=k; j++) {
                    s.append(sa.get(vars_indices[j]));
                }
                table.put(s.toString(), 0); //just need to test for presence of the string
            }
            ft.setTable(table);
            
            for(int j=0; j<=k; j++){
                filter_variables.add(variables[vars_indices[j]]);
            }

            ft.setVariables(filter_variables);
            filterTables.set(depths[k], ft);
        }
    }

    /**
     * Returns the filter table for the given depth
     *
     * @return the filter table for the given depth
     */
    public FilterTable getFilterTableByDepth(int depth) {
        return filterTables.get(depth);
    }

    /**
     * Sets the name of this constraint
     *
     * @param name the name to set for this constraint
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the constraints name as a string
     *
     * @return the constraints name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the variables (an array of cells) for this constraint
     *
     * @param variables the variables (an array of cells) to assign to this constraint
     */
    public void setVariables(Cell[] variables) {
        this.variables = variables;
    }

    /**
     * Returns an array of cells, which are the variables for this constraint
     *
     * @return an array of cells, which are the variables for this constraint
     */
    public Cell[] getVariables() {
        return variables;
    }

    /**
     * Saves a copy of the given satisfying assignment list
     * to the satisfying assignments list for this constraint
     *
     * @param satisfyingAssignments a list of satisfying assignments to assign to this constraint
     */
    public void setSatisfyingAssignments(ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        ArrayList<ArrayList<Integer>> satisfyingAssignmentsClone = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> assignment : satisfyingAssignments) {
            satisfyingAssignmentsClone.add(assignment);
        }
        this.satisfyingAssignments = satisfyingAssignmentsClone;
    }

    /**
     * Returns the satisfying assignment list for this constraint
     *
     * @return the satisfying assignment list for this constraint
     */
    public ArrayList<ArrayList<Integer>> getSatisfyingAssignments() {
        return satisfyingAssignments;
    }

    /**
     * Returns a deep copy of the constriants satisfying assignment list
     *
     * @return a deep copy of the satisfying assignment list
     */
    public ArrayList<ArrayList<Integer>> getSatisfyingAssignmentsDeepCopy() {
        ArrayList<ArrayList<Integer>> satisfyingAssignmentsClone = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> assignment : satisfyingAssignments) {
            satisfyingAssignmentsClone.add(assignment);
        }
        return satisfyingAssignmentsClone;
    }

    /**
     * Removes a satisfying assignment from the constraints
     * satisfying assignment list
     *
     * @param cellIndex the index of the cell (variable) from which an assignment is being eliminated
     * @param val the value to remove from satisfying assignments list
     */
    public void removeAssignment(int cellIndex, int val){
        //start for loop from the end as the remove function shifts the rest of the array down
        for(int j = satisfyingAssignments.size() - 1; j >= 0 ; j--) {
            if(satisfyingAssignments.get(j).get(cellIndex).equals(new Integer(val))) {
                satisfyingAssignments.remove(j);
            }
        }
    }

    /**
     * Updates the constraint variables domain based on
     * the constraint's satisfying assignments list
     *
     */
    public void updateVariableAssignments(){
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
                    variables[i].removeDomainValue(domainValue);
                else
                    markers[domainValue] = false; //reset marker to false for next loop
            }
        }
    }

    /**
     * Returns a string representation of the constraint
     * Constraint + [constraint-name] + Cell + [constriants-variable-list] +
     * [constraints-satisfying-assignments-list]
     *
     * @return a string representation of the constraint
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Constraint " + name +"\n");
        for(int i=0; i<variables.length; i++){
            sb.append("Cell " + i + ": " + variables[i].toString() + "\n");
        }
        sb.append(satisfyingAssignments);
        return sb.toString();
    }

}
