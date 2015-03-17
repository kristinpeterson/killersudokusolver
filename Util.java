import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;

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
    /*public static void printM1Output() {
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

            // Add list of constraints to each cell
            for(Constraint constraint : Main.board.getConstraints()) {
                for(Cell cell : constraint.getVariables()) {
                    Main.board.getCell(cell.getX(), cell.getY()).addConstraint(constraint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void applyArcConsistency(Board board){
        Hashtable<String, ArrayList<Integer>> nonessential = buildNEfromCageConstraint(new Hashtable<String, ArrayList<Integer>>(), board.getConstraints());
        reduceFromNE(nonessential, board.getConstraints());
        nonessential = buildNEfromConstraints(nonessential, board.getConstraints());
        reduceFromNE(nonessential, board.getConstraints());
    }

    public static ArrayList<Constraint> applyArcConsistency(TreeNode tree, ArrayList<Constraint> constraints){
        Hashtable<String, ArrayList<Integer>> nonessential = new Hashtable<String, ArrayList<Integer>>();
        for (Cell c: tree.traverseUp()) {
            for(int i=0; i<9; i++){
                if(c.getValue() != Board.POSSIBLE_VALUES[i]){
                    String constraintName = "cell_" + c.getY() + "_"+c.getX();
                    addNonEssential(nonessential, constraintName, Board.POSSIBLE_VALUES[i]);
                } 
                
           }
        }
        constraints = reduceFromNE(nonessential, constraints);
        //nonessential = buildNEfromConstraints(nonessential, constraints);
        //constraints = reduceFromNE(nonessential, constraints);
        return constraints;
    }

    public static Cell applyArcConsistency(Cell nextCell, TreeNode tree, ArrayList<Constraint> constraints){
        constraints = applyArcConsistency(tree, constraints);
        for (Constraint c: constraints) {
            for(Cell cell : c.getVariables()){
                if(cell.equals(nextCell)){
                    return cell.clone();
                }
           }
        }
        return nextCell;
    }

    private static Hashtable<String, ArrayList<Integer>> buildNEfromCageConstraint(Hashtable<String, ArrayList<Integer>> nonessential, ArrayList<Constraint> constraints) {
        for (Constraint c : constraints) {
            if (!c.getName().startsWith("C")) {
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
            }
        }
        return nonessential;
    }

    private static Hashtable<String, ArrayList<Integer>> buildNEfromConstraints(Hashtable<String, ArrayList<Integer>> nonessential, ArrayList<Constraint> constraints) {
        boolean one=false,two=false,three=false,four=false,five=false,six=false,seven=false,eight=false,nine =false;
        for (Constraint c : constraints) {
            ArrayList<ArrayList<Integer>> cSat = c.getSatisfyingAssignments();
            Cell[] cells = c.getVariables();
            for (int i=0; i < cells.length; i++) {
                for(ArrayList<Integer> foo: cSat){
                    switch(foo.get(i)){
                        case 1: one=true;
                        case 2: two=true;
                        case 3: three=true;
                        case 4: four=true;
                        case 5: five=true;
                        case 6: six=true;
                        case 7: seven=true;
                        case 8: eight=true;
                        case 9: nine=true;
                    }
                }
                String prefix = "cell_" + cells[i].getY() + "_" + cells[i].getX();
                if(!one){
                    addNonEssential(nonessential, prefix, 1);
                    one=false;
                }
                if(!two){
                    addNonEssential(nonessential, prefix, 2);
                    two=false;
                }
                if(!three){
                    addNonEssential(nonessential, prefix, 3);
                    one=false;
                }
                if(!four){
                    addNonEssential(nonessential, prefix, 4);
                    four=false;
                }
                if(!five){
                    addNonEssential(nonessential, prefix, 5);
                    five=false;
                }
                if(!six){
                    addNonEssential(nonessential, prefix, 6);
                    six=false;
                }
                if(!seven){
                    addNonEssential(nonessential, prefix, 7);
                    seven=false;
                }
                if(!eight){
                    addNonEssential(nonessential, prefix, 8);
                    eight=false;
                }
                if(!nine){
                    addNonEssential(nonessential, prefix, 9);
                    nine=false;
                }
            }
        }
        return nonessential;
    }

    
    /**
     * Goes through the nonessential list and removes non-essential assignments
     * from rowConstraints
     *
     */
    private static ArrayList<Constraint> reduceFromNE(Hashtable<String, ArrayList<Integer>> nonessential, ArrayList<Constraint> constraints) {
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
                        ArrayList<Integer> temp = nonessential.get(s);
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
        return constraints;
    }

    private static void addNonEssential(Hashtable<String, ArrayList<Integer>> nonessential, String ne, int val){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        if(nonessential.containsKey(ne)){
            temp = nonessential.get(ne);
        }
        temp.add(new Integer(val));
        nonessential.put(ne, temp);
    }

    public static ArrayList<Constraint> sortConstraintBySize(ArrayList<Constraint> ac) {
        ArrayList<Constraint> sorted = new ArrayList<Constraint>();
        //Base cases as this method is recursive
        if(ac.size()<=1){
            return ac;
        } else if (ac.size() == 2){
            if(ac.get(0).getSatisfyingAssignments().size() < ac.get(1).getSatisfyingAssignments().size())
                return ac;
            else{
                sorted.add(ac.get(1));
                sorted.add(ac.get(0));
                return sorted;
            }
        }
        ArrayList<Constraint> smaller = new ArrayList<Constraint>();
        ArrayList<Constraint> bigger = new ArrayList<Constraint>();
        ArrayList<Constraint> equal = new ArrayList<Constraint>();
        int pindex = ac.size()/2;
        int pivot = ac.get(pindex).getSatisfyingAssignments().size(); //size of middle element as pivot
        for (int h = 0; h < ac.size(); h++) {
            if (h!=pindex){
                if(ac.get(h).getSatisfyingAssignments().size() > pivot)
                    bigger.add(ac.get(h));
                if(ac.get(h).getSatisfyingAssignments().size() < pivot)
                    smaller.add(ac.get(h));
                if(ac.get(h).getSatisfyingAssignments().size() == pivot)
                    equal.add(ac.get(h));
            }
        }
        smaller = sortConstraintBySize(smaller);
        smaller.addAll(equal);
        bigger = sortConstraintBySize(bigger);
        smaller.addAll(bigger);
        return smaller;
    }

    public static Cell[] concatArray(Cell[] first, Cell[] second){
        Cell[] combo = new Cell[first.length + second.length];
        int combo_index=0;
        int i=0;
        while(i<first.length){
            combo[combo_index] = first[i];
            i++; combo_index++;
        }
        i=0;
        while(i<second.length){
            combo[combo_index] = second[i];
            i++; combo_index++;
        }
        return combo;
    }

}
