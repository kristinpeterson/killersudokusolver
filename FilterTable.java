package killersudokusolver;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 *
 */
public class FilterTable {

    private Hashtable table;  //table with projected satisfying assignments
    private ArrayList<Cell> variables; //the variables associated with the projection

    /**
     * Default FilterTable constructor, initializes the table and variable list
     *
     */
    public FilterTable() {
        this.table = new Hashtable();
        this.variables = new ArrayList<Cell>();
    }

    /**
     * Set the variable list for the filter table
     *
     * @param variables the variable list to set
     */
    public void setVariables(ArrayList<Cell> variables) {
        this.variables = variables;
    }

    /**
     * Adds the given variable to the variables list
     *
     * @param variable Adds the given variable to the variables list
     */
    public void addVariable(Cell variable) {
        this.variables.add(variable);
    }

    /**
     * Returns the filter table's variable list
     *
     * @return the filter table's variable list
     */
    public ArrayList<Cell> getVariables() {
        return variables;
    }

    /**
     * Sets the filter table
     *
     * @param table the filter table to set
     */
    public void setTable(Hashtable table) {
        this.table = table;
    }

    /**
     * Returns the filter table
     *
     * @return the filter table
     */
    public Hashtable getTable() {
        return table;
    }

}
