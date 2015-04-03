package killersudokusolver;

import java.util.ArrayList;

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
public class ConflictSet {

    private ArrayList<Cell> variables;  //the variables of this conflict set
    private Integer stepAssigned; //the step in which this set was assigned to the domain value

    public ConflictSet() {
        setVariables(new ArrayList<Cell>());
        setStepAssigned(0);
    }

    /**
     * Sets the list of variables for the conflict set
     *
     * @param variables the list of variables to set
     */
    public void setVariables(ArrayList<Cell> variables) {
        this.variables = variables;
    }

    /**
     * Returns the list of variables for the conflict set
     *
     * @return the list of variables for the conflict set
     */
    public ArrayList<Cell> getVariables() {
        return variables;
    }

    /**
     * Sets the step in which this set was assigned to the domain value
     *
     * @param stepAssigned the step in which this set was assigned to the domain value
     */
    public void setStepAssigned(Integer stepAssigned) {
        this.stepAssigned = stepAssigned;
    }

    /**
     * Returns the step in which this set was assigned to the domain value
     *
     * @return the step in which this set was assigned to the domain value
     */
    public Integer getStepAssigned() {
        return stepAssigned;
    }

    /**
     * Returns true if the given object is effectively equal to this ConflictSet object
     *
     * @param o the object to compare with this ConflictSet object
     * @return true if the given object is effectively equal to this ConflictSet object
     */
    public boolean equals(Object o) {
        if(o instanceof ConflictSet) {
            if(((ConflictSet) o).getVariables().equals(getVariables()))
                if(((ConflictSet) o).getStepAssigned().equals(getStepAssigned()))
                    return true;
        }
        return false;
    }
}
