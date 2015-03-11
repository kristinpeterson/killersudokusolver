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
        this.satisfyingAssignments = satisfyingAssignments;
    }

    public ArrayList<ArrayList<Integer>> getSatisfyingAssignments() {
        return satisfyingAssignments;
    }

    public void removeAssignment(int cell, int val){
                for(int j=satisfyingAssignments.size()-1; j>=0; j--) {
                    //System.out.println("cell j "+cell + j);
                    if(satisfyingAssignments.get(j).get(cell).equals(new Integer(val))) {
                        System.out.println("cell j "+cell + j);
                        System.out.println("satisfyingAssignment.get(i "+ satisfyingAssignments.get(j) + " val "+ val);
                        //satisfyingAssignments.remove(satisfyingAssignments.get(j));
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
