package killersudokusolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * COURSE: CECS-551 AI
 * PROFESSOR: Todd Ebert
 * PROJECT: Killer Sudoku Solver
 *
 * Various utility methods
 *
 * @author Kristin Peterson
 * @author Ariel Katz
 */
public class Util {

    /**
     * Prints the necessary output for Milestone 1:
     *
     * Each constraint, dependent variables, cardinality of satisfying assignments
     * before and after arc consistency performed.
     *
     * @throws Exception if any issues encountered when writing data to output file
     */
    public static void printM1Output() throws Exception {
        try {
            File of = new File("m1output.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(of));
            String separator = "**************************\n";

            // Temporary output of constraint cardinality before arc consistency has been performed
            output.write(separator);
            for (Constraint c : Main.rowConstraints) {
                String outputString = "Row constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t"
                        + c.initialSatisfyingAssignmentSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.colConstraints) {
                String outputString = "Column constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t"
                        + c.initialSatisfyingAssignmentSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.cageConstraints) {
                String outputString = "Sum constraint: " + c.getName() + ":\n\t\t" + "Cardinality before AC:\t"
                        + c.initialSatisfyingAssignmentSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.nonetConstraints) {
                String outputString = "Grid constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t"
                        + c.initialSatisfyingAssignmentSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }

            output.close();

            // Cleanup:
            // Clear categorized constraint lists from memory
            Main.cageConstraints.clear();
            Main.colConstraints.clear();
            Main.nonetConstraints.clear();
            Main.rowConstraints.clear();
            // Clear Board.cages from memory, no longer needed
            Main.board.getCages().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the necessary output for Milestone 2:
     *
     * Print the "tree" structure level by level, including each levels depth,
     * generator (cell), and filters (constraints).
     *
     * @throws Exception if any issues encountered when writing data to output file
     */
    public static void printM2Output() throws Exception {
        try {
            File of = new File("m2output.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(of));
            String separator = "**************************\n";

            output.write(separator);
            for(int i = 0; i < Main.board.getCells().size(); i++){
                Cell currentCell = Main.board.getCells().get(i);
                String x = Integer.toString(currentCell.getX());
                String y = Integer.toString(currentCell.getY());
                output.write("Depth: " + i + "\tVariable: row " + y + " col " + x + "\n");
                for (Constraint c : Main.board.getConstraints()) {
                    if(c.getName().contains(x + y) | c.getName().contains(x + "." + y )){
                        output.write(c.getName() + "\t");
                    }
                }
                output.write(separator);
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies arc consistency on the given list of constraints
     *
     * @param constraints list of constraints to enforce arc consistency
     * @return true if after arc consistency enforced there are no constraints with empty satisfying assignment lists
     */
    public static boolean applyArcConsistency(ArrayList<Constraint> constraints){
        Hashtable<String, ArrayList<Integer>> nonessential = buildNonessentials(new Hashtable<String, ArrayList<Integer>>(), constraints);
        return reduceFromNE(nonessential, constraints);
    }

    /**
     * Build list of nonessential values based on constraints
     *
     * @param nonessential the nonessential list to build
     * @param constraints constraints to build nonessentials off of
     * @return the nonessential list
     */
    private static Hashtable<String, ArrayList<Integer>> buildNonessentials(Hashtable<String, ArrayList<Integer>> nonessential, List<Constraint> constraints) {
        for (Constraint c : constraints) {
            for (Cell cell : c.getVariables()) {
                ArrayList<Integer> domain = cell.getDomain();
                if (domain.size() < 9) {
                    for (int n = 1; n < 10; n++) {
                        if (!domain.contains(n)) {
                            addNonEssential(nonessential, "cell_" + cell.getY() + "_" + cell.getX(), n);
                        }
                    }
                }
            }
        }
        return nonessential;
    }

    /**
     * Goes through the nonessential list and removes nonessential assignments
     * from constraints
     *
     * @param nonessential a list of nonessential assignments to be removed
     * @param constraints the constraints from which non-essential assignments are being removed
     * @return false if any of the constraints satisfying assignment lists are reduced to zero
     */
    public static boolean reduceFromNE(Hashtable<String, ArrayList<Integer>> nonessential, List<Constraint> constraints) {
        for(String s: nonessential.keySet()) {
            String[] info = s.split("_");
            String y=info[1];
            String x=info[2];
            for (Constraint constraint : constraints) {
                if(constraint.getName().contains(x+y) | constraint.getName().contains(x+"."+y)){ // only go through the constraint if it contains the cell
                    Cell[] cells = constraint.getVariables();
                    for(int j = 0; j < cells.length; j++) {
                        //if you've found the right cell
                        if (cells[j].getY() == Integer.parseInt(y) && cells[j].getX() == Integer.parseInt(x)){
                            //pull the arraylist of nonessential values from the hash table
                            List<Integer> temp = nonessential.get(s);
                            //loop through the array list and remove each nonessential from the constraint and cell
                            for(Integer assignment: temp){
                                constraint.removeAssignment(j, assignment);
                                cells[j].removeDomainValue(assignment);
                            }
                            // Update the satisfying assignments based on the values removed
                            if (temp.size() > 0){
                                constraint.updateVariableAssignments();
                            }
                        }
                    }
                    if(constraint.getSatisfyingAssignments().size() == 0) {
                        return false;
                    }
                }
            }
        }    
        return true;
    }

    /**
     * Add nonessential value to the given nonessential list
     *
     * @param nonessential the nonessential list being added to
     * @param ne a string representation of the nonessential value's associated cell used as a key for the
     *           nonessential list hash
     * @param val the nonessential value to add
     */
    public static void addNonEssential(Hashtable<String, ArrayList<Integer>> nonessential, String ne, int val){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        // If nonessential list already has a value for the given ne key string
        if(nonessential.containsKey(ne)){
            // Save the nonessential list of values for the given key to temp
            temp = nonessential.get(ne);
        }

        // Assign value to temp list
        temp.add(val);

        // Put the temp list into the nonessential values list
        nonessential.put(ne, temp);
    }
}
