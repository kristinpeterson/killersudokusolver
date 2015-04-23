package killersudokusolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

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

    static final Integer MAX_DEPTH = 80;
    static int step_count = 1;
    static ConflictSet unionCS = new ConflictSet();

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

    //Try to assign the variable at curr_depth to a consistent value
    public static ConflictSet extendAssignment(Generator[] generators, Integer curr_depth) {
        Generator g = generators[curr_depth];   // g is the Generator for the current depth
        g.setAssignCount(0); // g has yet to assign its variable to any of the domain values
        ConflictSet cs;
        while(assign_variable(g, curr_depth)){
            if(curr_depth.equals(MAX_DEPTH)){
                recordSolution(generators);
                return null;
            } else {
                //pass control down to the next generator
                cs = extendAssignment(generators, curr_depth + 1);
            }

            if(cs.isEmpty()) {
                //System.out.println("Empty conflict set " + curr_depth);
                return cs;
            }

            if(!cs.contains(g.getVariable())){ //conflict set *does not* contains the Cell - BACKJUMP
                System.out.println("\nBACKJUMP - current depth: " + curr_depth);
                g.setWorkingHypothesis(g.getVariable().getValue());
                return cs;
            }

            System.out.println("\n---REMOVING FROM CONFLICT SET---");
            System.out.println(g.getVariable());
            System.out.println("--------------------------------");
            cs.remove(g.getVariable());
            cs.setStepAssigned(step_count);
            g.getVariable().getValue().setConflictSet(cs);
        }


        System.out.println("\n------------CSUNION-------------");
        System.out.println("curr_depth: " + curr_depth);
        System.out.println(getUnionConflictSet().toString());
        System.out.println("--------------------------------");
        //return the union of the conflict sets associated with each domain value of the cell
        return getUnionConflictSet();
    }

    //return true if a value can be assigned to generator
    private static boolean assign_variable(Generator g, Integer curr_depth){
        DomainValue dv;
        ConflictSet cs;
        ConflictSet union = new ConflictSet();

        while(!(dv = select_next_assignment(g)).equals(new DomainValue(0))){ // a 0 domain value is the marker for no more values
            step_count++;
            g.incrementIndexCount();
            System.out.println("\nDepth: " + g.getVariable().getDepthAssigned() + " g.var " + g.getVariable()
                    + " dv: " + dv.getDomainValue().toString());
            cs = filterCurrentAssignment(g, curr_depth, dv);

             if(cs.isEmpty()){
                System.out.println("\n-------VALID ASSIGNMENT---------");
                System.out.println("Depth: " + curr_depth + " Generator: " + g.getVariable() + " Value: " + dv.getDomainValue().toString());
                System.out.println("--------------------------------");
                g.setWorkingHypothesis(dv);
                setUnionConflictSet(new ConflictSet());
                return true;
            } else {
                cs.setStepAssigned(step_count);
                dv.setConflictSet(cs);
                // Assign the conflict set to the domain value inside the generators assigned variable domainset
                int genDVIndex = g.getVariable().getDomain().getDomainValues().indexOf(dv);
                g.getVariable().getDomain().getDomainValues().get(genDVIndex).setConflictSet(cs);
                union.addVariables(cs.getVariables());
            }
        }
        union.setStepAssigned(step_count);
        setUnionConflictSet(union);
        //System.out.println("\n-----------FAILURE------------");
        //System.out.println("All domain values tried, nothing consistent found. \nConflict set union: " + getUnionConflictSet());
        //System.out.println("--------------------------------");
        return false; //all domain values tried and nothing consistent found
    }

    private static ConflictSet filterCurrentAssignment(Generator g, Integer curr_depth,
                                                       DomainValue currentAssignment){
        ConflictSet cs = new ConflictSet();

        // Iterate over filters to build the ConflictSet for the given assignment (which can be empty)
        for(Constraint c: g.getFilters()) {
            FilterTable ft;
            ft = c.getFilterTableByDepth(curr_depth);
            StringBuilder key = new StringBuilder();

            // if depth of generator (variable) is less than curr_depth, add working hypothesis to key list
            for(Cell variable : c.getVariables()){
                int depthAssigned = variable.getDepthAssigned();
                DomainValue hypothesis = Main.generators[depthAssigned].getWorkingHypothesis();
                if((depthAssigned < curr_depth) && (hypothesis != null)){
                    key.append(hypothesis.getDomainValue()); //append working hypothesis of generators above to key
                }
            }

            key.append(currentAssignment.getDomainValue());

            // if the filter table doesn't contain the key, add the variables associated w/ that filter table to the conflict set
            if (!ft.getTable().containsKey(key.toString())) {
                // Construct conflict set from ft.getVariables()
                // Note: Do not add the current variable to the conflict set (ie self)
                ArrayList<Cell> conflictingVariables = ft.getVariables();
                System.out.println("\nConstraint Violated: " + c.toString() + "\n-\nAdding conflicting variables to cs: " + conflictingVariables.toString());
                conflictingVariables.remove(g.getVariable());
                cs.addVariables(conflictingVariables); //Add all variables that might conflict to the conflict set
            }
        }

        return cs; // cs will be empty if no conflicts identified
    }

    private static boolean hasRecentlyReassignedVariable(ConflictSet cs){
        if(cs == null || cs.isEmpty())
            return true;

        for(Cell c: cs.getVariables()){
            if(c.getLastAssigned() > cs.getStepAssigned())
                return true;
        }

        return false;
    }

    private static DomainValue select_next_assignment(Generator g){
        Cell variable = g.getVariable();
        if (g.getAssignCount() == variable.getDomain().getDomainValues().size())
            return new DomainValue(0);

        g.incrementAssignCount();
        DomainValue dv = g.getWorkingHypothesis();

        if(dv != null && !dv.equals(new DomainValue(0)) && hasRecentlyReassignedVariable(dv.getConflictSet()))
            return dv;

        // Create list of DomainValue candidates that have been recently assigned
        ArrayList<DomainValue> dvCandidates = new ArrayList<DomainValue>();
        for(DomainValue dvCandidate : g.getVariable().getDomain().getDomainValues()) {
            if(hasRecentlyReassignedVariable(dvCandidate.getConflictSet())) {
                dvCandidates.add(dvCandidate);
            }
        }

        // Choose a random DomainValue from the dvCandidates list
        if(dvCandidates.size() > 0) {   // added this condition, because if dvCandidates <= 0, this throws an error
            Random rng = new Random(new Date().getTime());
            int randomIndex = rng.nextInt(dvCandidates.size());
            dv = dvCandidates.get(randomIndex);
            g.getVariable().setValue(dv);
            g.setWorkingHypothesis(dv);
        }
        return dv;
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
                DomainSet domain = cell.getDomain();
                if (domain.domainSize() < 9) {
                    for (int n = 1; n < 10; n++) {
                        if (!domain.contains(n)) {
                            addNonEssential(nonessential, "cell_" + cell.getX() + "_" + cell.getY(), n);
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
    private static boolean reduceFromNE(Hashtable<String, ArrayList<Integer>> nonessential, List<Constraint> constraints) {
        for(String s: nonessential.keySet()) {
            String[] info = s.split("_");
            String x = info[1];
            String y = info[2];

            for (Constraint constraint : constraints) {
                if(constraint.getName().contains(x + y) | constraint.getName().contains(x + "." + y)){ // only go through the constraint if it contains the cell
                    Cell[] cells = constraint.getVariables();
                    for(int j = 0; j < cells.length; j++) {
                        //if you've found the right cell
                        if (cells[j].getY() == Integer.parseInt(y) && cells[j].getX() == Integer.parseInt(x)){
                            //pull the arraylist of nonessential values from the hash table
                            List<Integer> temp = nonessential.get(s);
                            //loop through the array list and remove each nonessential from the constraint and cell
                            for(Integer assignment: temp) {
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
    private static void addNonEssential(Hashtable<String, ArrayList<Integer>> nonessential, String ne, int val){
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

    private static void setUnionConflictSet(ConflictSet cs){
        unionCS = cs;
    }

    private static ConflictSet getUnionConflictSet(){
        return unionCS;
    }

    private static void recordSolution(Generator[] generators) {
        System.out.println("SOLUTION FOUND!");
        printSolutionBoard(generators);
        for(Generator g : generators) {
            System.out.println(g.getVariable() + " : " + g.getVariable().getValue());
        }
    }

    public static void printSolutionBoard(Generator[] generators) {
        int[][] solution = new int[9][9];
        for(Generator g : generators) {
            Cell cell = g.getVariable();
            switch(cell.getY()) {
                case 1: solution[0][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 2: solution[1][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 3: solution[2][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 4: solution[3][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 5: solution[4][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 6: solution[5][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 7: solution[6][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 8: solution[7][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
                case 9: solution[8][cell.getX()-1] = cell.getValue().getDomainValue();
                    break;
            }
        }
        System.out.println();
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("------ WORKING HYPOTHESES  ----------------------------------------------");

        for(Generator g: generators){
            if(g.getWorkingHypothesis() != null)
                System.out.println(g.getVariable() + " - " + g.getWorkingHypothesis());
        }

        System.out.println("Step count "+ step_count);
    }
}