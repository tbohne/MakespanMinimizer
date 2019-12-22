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

    private static final String CURRENT_INSTANCE_SET = "S77";

    private static final double TIME_LIMIT = 5;

    public static void main(String[] args) {

        File dir = new File(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/");
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        Arrays.sort(directoryListing);

        for (File file : directoryListing) {

            String instanceName = file.toString().replace(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/", "").replace(".txt", "");
            System.out.println("working on: " + instanceName);
            Instance instance = InstanceReader.readInstance(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/" + instanceName + ".txt", INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/");
            String solutionName = instanceName.replace("instance", "sol");

            MIPFormulation mip = new MIPFormulation(instance, TIME_LIMIT, HIDE_CPLEX_OUTPUT, MIP_EMPHASIS, MIP_TOLERANCE);

            System.out.println("solving with CPLEX..");
            Solution mipSol = mip.solve();

            double startTime = System.currentTimeMillis();
            System.out.println("solving with LPT..");
            Solution trivialSol = LPTSolver.solve(instance);
            trivialSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);

            startTime = System.currentTimeMillis();

            PartitionHeuristic heu = new PartitionHeuristic(instance, TIME_LIMIT);
            System.out.println("solving with SPS");
            Solution sol = heu.solve();
            sol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);

            if (sol.isFeasible() && mipSol.isFeasible() && trivialSol.isFeasible()) {
//                PCMAX.SolutionWriter.writeSolution(SOLUTION_PREFIX + solutionName + ".txt", sol);

                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", trivialSol, "LPT", TIME_LIMIT);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", sol, "SPS", TIME_LIMIT);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", mipSol, "CPLEX", TIME_LIMIT);
            } else {
                System.out.println("generated infeasible solution..");
                System.out.println(sol);
                System.exit(0);
            }
        }
    }
}
