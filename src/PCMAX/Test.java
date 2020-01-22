package PCMAX;

import PCMAX.local_search.*;

import java.io.File;
import java.util.Arrays;

public class Test {

    private static final String INSTANCE_PREFIX = "res/instances/";
    private static final String SOLUTION_PREFIX = "res/solutions/";
    private static final String CURRENT_INSTANCE_SET = "S_TEST";
    private static final double TIME_LIMIT = 180;

    /********************** CPLEX CONFIG **********************/
    private static final boolean HIDE_CPLEX_OUTPUT = true;
    // 1 --> feasibility over optimality
    private static final int MIP_EMPHASIS = 1;
    // 0 --> only terminating with optimal solutions before time limit
    private static final double MIP_TOLERANCE = 0.0;
    /**********************************************************/

    /*********************** TS CONFIG ************************/
    private static final int NUMBER_OF_NEIGHBORS = 5;
    private static final LocalSearchAlgorithm.ShortTermStrategies SHORT_TERM_STRATEGIE = LocalSearchAlgorithm.ShortTermStrategies.BEST_FIT;
    private static final int UNSUCCESSFUL_NEIGHBOR_GENERATION_ATTEMPTS = 10;
    private static final int NUMBER_OF_NON_IMPROVING_ITERATIONS = 900;
    /**********************************************************/

    public static Solution solveWithCPLEX(Instance instance) {
        System.out.println("solving with CPLEX..");
        MIPFormulation mip = new MIPFormulation(instance, TIME_LIMIT, HIDE_CPLEX_OUTPUT, MIP_EMPHASIS, MIP_TOLERANCE);
        return mip.solve();
    }

    public static Solution solveWithLPT(Instance instance) {
        double startTime = System.currentTimeMillis();
        System.out.println("solving with LPT..");
        Solution trivialSol = LPTSolver.solve(instance);
        trivialSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return trivialSol;
    }

    public static Solution solveWithSPS(Instance instance) {
        double startTime = System.currentTimeMillis();
        PartitionHeuristic sps = new PartitionHeuristic(instance);
        System.out.println("solving with SPS..");
        Solution solSPS = sps.solve();
        solSPS.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return solSPS;
    }

    public static Solution solveWithTabuSearch(Instance instance, Solution trivialSol, long seed, int numOfCores) {
        int numOfMachineCombinations = instance.getNumOfMachines() * (instance.getNumOfMachines() - 1) / 2;
        SwapOperator swapOperator = new SwapOperator(seed, numOfCores);
        TabuSearch ts = new TabuSearch(
            NUMBER_OF_NEIGHBORS, SHORT_TERM_STRATEGIE, numOfMachineCombinations, UNSUCCESSFUL_NEIGHBOR_GENERATION_ATTEMPTS, swapOperator
        );
        LocalSearch l = new LocalSearch(trivialSol, TIME_LIMIT, NUMBER_OF_NON_IMPROVING_ITERATIONS, ts);
        double startTime = System.currentTimeMillis();
        System.out.println("solving with TS..");
        Solution localSearchSol = l.solve();
        localSearchSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return localSearchSol;
    }

    public static void main(String[] args) {

        if (args.length != 5) {
            System.out.println("invalid arguments..");
            System.out.println("usage:");
            System.out.println("solver-program <instance> <solution> <number-cores> <time-limit> <seed>");
            System.exit(0);
        } else {
            System.out.println("instance: " + args[0]);
            System.out.println("solution: " + args[1]);
            System.out.println("number-cores: " + args[2]);
            System.out.println("time-limit: " + args[3]);
            System.out.println("seed: " + args[4]);
        }

        int numOfCores = Integer.parseInt(args[2]);
        System.out.println("available number of cores: " + Runtime.getRuntime().availableProcessors());
        if (numOfCores > Runtime.getRuntime().availableProcessors()) {
            numOfCores = Runtime.getRuntime().availableProcessors();
            System.out.println("the specified number of cores exceeds the available number of cores: set to " + numOfCores + " cores.");
        }
        long seed = Long.parseLong(args[4]);

        File dir = new File(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/");
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        Arrays.sort(directoryListing);

        for (File file : directoryListing) {

            String instanceName = file.toString().replace(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/", "").replace(".txt", "");
            System.out.println("working on: " + instanceName);
            Instance instance = InstanceReader.readInstance(INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/" + instanceName + ".txt", INSTANCE_PREFIX + CURRENT_INSTANCE_SET + "/");
            String solutionName = instanceName.replace("instance", "sol");

            Solution trivialSol = solveWithLPT(instance);
            Solution solSPS = solveWithSPS(instance);
            Solution mipSol = solveWithCPLEX(instance);
            Solution tabuSearchSolution = solveWithTabuSearch(instance, solSPS, seed, numOfCores);

            if (trivialSol.isFeasible() && solSPS.isFeasible() && mipSol.isFeasible() && tabuSearchSolution.isFeasible()) {
//                PCMAX.SolutionWriter.writeSolution(SOLUTION_PREFIX + solutionName + ".txt", sol);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", trivialSol, "LPT", TIME_LIMIT);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", solSPS, "SPS", TIME_LIMIT);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", mipSol, "CPLEX", TIME_LIMIT);
                SolutionWriter.writeSolutionAsCSV(SOLUTION_PREFIX + CURRENT_INSTANCE_SET + "_solutions.csv", tabuSearchSolution, "TS", TIME_LIMIT);
            } else {
                System.out.println("generated infeasible solution..");
                System.exit(0);
            }
        }
    }
}