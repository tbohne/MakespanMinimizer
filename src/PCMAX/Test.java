package PCMAX;

import PCMAX.local_search.LPTSolver;

import java.io.File;
import java.util.Arrays;

public class Test {

    private static final String INSTANCE_PREFIX = "res/instances/";
    private static final String SOLUTION_PREFIX = "res/solutions/";

    /********************** CPLEX CONFIG **********************/
    private static final boolean HIDE_CPLEX_OUTPUT = true;
    // 1 --> feasibility over optimality
    private static final int MIP_EMPHASIS = 1;
    // 0 --> only terminating with optimal solutions before time limit
    private static final double MIP_TOLERANCE = 0.0;
    /**********************************************************/

    private static final double TIME_LIMIT = 500.0;

    public static void main(String[] args) {

        File dir = new File(INSTANCE_PREFIX);
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        Arrays.sort(directoryListing);

        for (File file : directoryListing) {

            String instanceName = file.toString().replace(INSTANCE_PREFIX, "").replace(".txt", "");
            System.out.println("working on: " + instanceName);
            Instance instance = InstanceReader.readInstance(INSTANCE_PREFIX + instanceName + ".txt");
            String solutionName = instanceName.replace("instance", "sol");

            MIPFormulation mip = new MIPFormulation(instance, TIME_LIMIT, HIDE_CPLEX_OUTPUT, MIP_EMPHASIS, MIP_TOLERANCE);

            Solution mipSol = mip.solve();

            double startTime = System.currentTimeMillis();
            Solution trivialSol = LPTSolver.solve(instance);
            trivialSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);

            startTime = System.currentTimeMillis();

            PartitionHeuristic heu = new PartitionHeuristic(instance, TIME_LIMIT);
            Solution sol = heu.solve();
            sol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);

            if (sol.isFeasible() && mipSol.isFeasible() && trivialSol.isFeasible()) {
//                System.out.println("lpt: " + trivialSol.getMakespan());
//                System.out.println("sps: " + sol.getMakespan());
//                System.out.println("opt: " + mipSol.getMakespan());
//                PCMAX.SolutionWriter.writeSolution(SOLUTION_PREFIX + solutionName + ".txt", sol);

                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + "solutions.csv", trivialSol, "LPT");
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + "solutions.csv", sol, "SPS");
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + "solutions.csv", mipSol, "OPT");
            } else {
                System.out.println("generated infeasible solution..");
                System.out.println(sol);
                System.exit(0);
            }
        }
    }
}
