import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Various utility methods
 *
 */
public class Util {

    /**
     * Prints the necessary output for Milestone 1:
     *
     * Each constraint, dependent variables, cardinality of satisfying assignments
     * before and after arc consistency performed.
     *
     * @throws Exception
     */
    public static void printM1Output() {
        try {
            File of = new File("m1output.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(of));
            String separator = "**************************\n";

            // Temporary output of constraint cardinality before arc consistency has been performed
            output.write(separator);
            for (Constraint c : Main.rowConstraints) {
                String outputString = "Row constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.colConstraints) {
                String outputString = "Column constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.cageConstraints) {
                String outputString = "Sum constraint: " + c.getName() + ":\n\t\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
                output.write(outputString);
            }
            output.write(separator);
            for (Constraint c : Main.nonetConstraints) {
                String outputString = "Grid constraint: " + c.getName() + ":\t" + "Cardinality before AC:\t" + c.preSize + "\tCardinality after AC:\t" + c.getSatisfyingAssignments().size() + "\n";
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

    public static void applyArcConsistency(Board board){
        Hashtable<String, ArrayList<Integer>> nonessential = buildNEfromCageConstraint(new Hashtable<String, ArrayList<Integer>>(), board.getConstraints());
        reduceFromNE(nonessential, board.getConstraints());
    }

    public static void applyArcConsistency( List<Constraint> constraints){
        Hashtable<String, ArrayList<Integer>> nonessential = buildNEfromCageConstraint(new Hashtable<String, ArrayList<Integer>>(), constraints);
        reduceFromNE(nonessential, constraints);
    }

    // Build a list of nonesential values from the cage constraints
    // This is used during the initial arc consistency performed on the Main.board
    private static Hashtable<String, ArrayList<Integer>> buildNEfromCageConstraint(Hashtable<String, ArrayList<Integer>> nonessential, List<Constraint> constraints) {
        for (Constraint c : constraints) {
            //if (!c.getName().startsWith("C")) {
                for (Cell cell : c.getVariables()) {
                    ArrayList<Integer> sa = cell.getDomain();
                    if (sa.size() < 9) {
                        for (int n = 1; n < 10; n++) {
                            if (!sa.contains(new Integer(n))) {
                                addNonEssential(nonessential, "cell_" + cell.getY() + "_" + cell.getX(), n);
                            }
                        }
                    }
                }
            //}
        }
        return nonessential;
    }

    /**
     * Goes through the nonessential list and removes non-essential assignments
     * from constraints
     *
     */
    public static ArrayList<Constraint> reduceFromNE(Hashtable<String, ArrayList<Integer>> nonessential, List<Constraint> constraints) {
        ArrayList<String> keySet = new ArrayList<String>();
        for(String s: nonessential.keySet()) {
            // info[1] is y
            // info[2] is x
            String[] info = s.split("_");
            for (Constraint c : constraints) {
                Cell[] cells = c.getVariables();
                for(int j = 0; j < cells.length; j++) {
                    //if you've found the right cell
                    if (cells[j].getY() == Integer.parseInt(info[1]) && cells[j].getX() == Integer.parseInt(info[2])){
                        //pull the arraylist of nonessential values from the hash table
                        List<Integer> temp = nonessential.get(s);
                        //loop through the array list and remove each nonessential from the constraint and cell
                        for(Integer assignment: temp){
                            c.removeAssignment(j, assignment);
                            cells[j].removeAssignment(assignment);
                        }
                    }
                }
            }
            keySet.add(s);
        }
        for (String key: keySet){
            nonessential.remove(key);
        }
        return new ArrayList<Constraint>(constraints);
    }

    public static void addNonEssential(Hashtable<String, ArrayList<Integer>> nonessential, String ne, int val){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        if(nonessential.containsKey(ne)){
            temp = nonessential.get(ne);
        }
        temp.add(new Integer(val));
        nonessential.put(ne, temp);
    }
}
