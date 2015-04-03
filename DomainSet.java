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
public class DomainSet {

    private ArrayList<DomainValue> domainValues;

    /**
     * Default constructor for DomainSet, initializes the domain values list
     *
     */
    public DomainSet() {
        domainValues = new ArrayList<DomainValue>();
    }

    /**
     * Sets the domain values list
     *
     * @param domainValues the domain values list to set
     */
    public void setDomainValues(ArrayList<DomainValue> domainValues) {
        this.domainValues = domainValues;
    }

    /**
     * Adds the given DomainValue to the DomainSet domainValues list
     *
     * @param domainValue the DomainValue to add to the domainValues list
     */
    public void addDomainValue(DomainValue domainValue) {
        domainValues.add(domainValue);
    }

    /**
     * Returns the domain values list
     *
     * @return the domain value list
     */
    public ArrayList<DomainValue> getDomainValues() {
        return domainValues;
    }

    /**
     * Removes the given domain value from the domain values list
     *
     * @param domainValue the domain value to remove
     * @return true if the domain value was removed
     */
    public boolean removeDomainValue(Integer domainValue) {
        return domainValues.remove(domainValue);
    }

    /**
     * Returns true if the DomainSet contains the given DomainValue
     *
     * @param domainValue the DomainValue to search for
     * @return true if the domain set contains the domain value
     */
    public boolean contains(DomainValue domainValue) {
        return domainValues.contains(domainValue);
    }

    /**
     * Returns true if the DomainSet contains the given int domain value
     *
     * @param domainValue the int domain value to search for
     * @return true if the domain set contains the domain value
     */
    public boolean contains(int domainValue) {
        return domainValues.contains(new DomainValue(domainValue));
    }

    /**
     * Returns the size of the domain values list
     *
     * @return the size of the domain values list
     */
    public int domainSize() {
        return domainValues.size();
    }

    /**
     * Returns the string representation of the DomainSet
     *
     * @return the string representation of the DomainSet
     */
    public String toString() {
        return domainValues.toString();
    }

}
