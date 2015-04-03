package killersudokusolver;

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
public class DomainValue {

    private Integer value;
    private ConflictSet conflictSet;

    /**
     * Domain value constructor, assigns the domain value
     *
     * @param value the value to assign
     */
    public DomainValue(Integer value) {
        setDomainValue(value);
        setConflictSet(new ConflictSet());
    }

    /**
     * Sets the domain value to the given Integer
     *
     * @param value the value to set
     */
    public void setDomainValue(Integer value) {
        this.value = value;
    }

    /**
     * Returns the domain value
     *
     * @return the domain value
     */
    public Integer getDomainValue() {
        return value;
    }

    /**
     * Set the conflict set for this domain value
     *
     * @param conflictSet the conflict set to set
     */
    public void setConflictSet(ConflictSet conflictSet) {
        this.conflictSet = conflictSet;
    }

    /**
     * Returns the conflict set for this domain value
     *
     * @return the conflict set for this domain value
     */
    public ConflictSet getConflictSet() {
        return this.conflictSet;
    }

    /**
     * Returns string representation of domain value
     *
     * @return the string representation of domain value
     */
    public String toString() {
        return value.toString();
    }

    /**
     * Returns true if the given object is effectively equal to this DomainValue object
     *
     * @param o the object to compare with this DomainValue object
     * @return true if the given object is effectively equal to this DomainValue object
     */
    public boolean equals(Object o) {
        if(o instanceof DomainValue) {
            if(((DomainValue) o).getDomainValue().equals(getDomainValue()))
                if(((DomainValue) o).getConflictSet().equals(getConflictSet()))
                    return true;
        }
        return false;
    }
}
