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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVariables(Cell[] variables) {
        this.variables = variables;
    }

    public Cell[] getVaribles() {
        return variables;
    }

    public void setSatisfyingAssignments(ArrayList<ArrayList<Integer>> satisfyingAssignments) {
        this.satisfyingAssignments = satisfyingAssignments;
    }

    public ArrayList<ArrayList<Integer>> getSatisfyingAssignments() {
        return satisfyingAssignments;
    }

}
