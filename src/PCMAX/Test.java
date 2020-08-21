package PCMAX;

import PCMAX.local_search.*;

public class Test {

    /********************** CPLEX CONFIG **********************/
    private static final boolean HIDE_CPLEX_OUTPUT = true;
    // 1 --> feasibility over optimality
    private static final int MIP_EMPHASIS = 1;
    // 0 --> only terminating with optimal solutions before time limit
    private static final double MIP_TOLERANCE = 0.0;
    /**********************************************************/

    /*********************** TS CONFIG ************************/
    private static final LocalSearchAlgorithm.ShortTermStrategies SHORT_TERM_STRATEGY = LocalSearchAlgorithm.ShortTermStrategies.BEST_FIT;
    private static final int UNSUCCESSFUL_NEIGHBOR_GENERATION_ATTEMPTS = 10;
    private static final int NUMBER_OF_NON_IMPROVING_ITERATIONS = 500;
    /**********************************************************/

    /**
     * Solves the instance using CPLEX with an LP formulation.
     *
     * @param instance  - instance to be solved
     * @param timeLimit - time limit to be satisfied
     * @return generated solution
     */
    private static Solution solveWithCPLEX(Instance instance, double timeLimit) {
        System.out.println("solving with CPLEX..");
        MIPFormulation mip = new MIPFormulation(instance, timeLimit, HIDE_CPLEX_OUTPUT, MIP_EMPHASIS, MIP_TOLERANCE);
        return mip.solve();
    }

    /**
     * Solves the instance using LPT.
     *
     * @param instance - instance to be solved
     * @return generated solution
     */
    private static Solution solveWithLPT(Instance instance) {
        double startTime = System.currentTimeMillis();
        System.out.println("solving with LPT..");
        Solution trivialSol = LPTSolver.solve(instance);
        trivialSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return trivialSol;
    }

    /**
     * Solves the instance using SPS.
     *
     * @param instance - instance to be solved
     * @return generated solution
     */
    private static Solution solveWithSPS(Instance instance) {
        double startTime = System.currentTimeMillis();
        PartitionHeuristic sps = new PartitionHeuristic(instance);
        System.out.println("solving with SPS..");
        Solution solSPS = sps.solve();
        solSPS.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return solSPS;
    }

    /**
     * Solves the instance using TS.
     *
     * @param instance   - instance to be solved
     * @param trivialSol - trivial initial solution
     * @param seed       - seed to initialize PRNG
     * @param numOfCores - number of cores to be used
     * @param timeLimit  - time limit to be satisfied
     * @return generated solution
     */
    private static Solution solveWithTabuSearch(Instance instance, Solution trivialSol, long seed, int numOfCores, double timeLimit) {
        int numOfMachineCombinations = instance.getNumOfMachines() * (instance.getNumOfMachines() - 1) / 2;
        SwapOperator swapOperator = new SwapOperator(seed, numOfCores);

        int nbrs = instance.getNumOfJobs() <= 100 ? instance.getNumOfJobs() / 2 : 3;

        TabuSearch ts = new TabuSearch(
            nbrs, SHORT_TERM_STRATEGY, numOfMachineCombinations, UNSUCCESSFUL_NEIGHBOR_GENERATION_ATTEMPTS, swapOperator
        );
        LocalSearch l = new LocalSearch(trivialSol, timeLimit, NUMBER_OF_NON_IMPROVING_ITERATIONS, ts);
        double startTime = System.currentTimeMillis();
        System.out.println("solving with TS..");
        Solution localSearchSol = l.solve();
        localSearchSol.setTimeToSolve((System.currentTimeMillis() - startTime) / 1000.0);
        return localSearchSol;
    }

    /**
     * Parses the prefix of the given path.
     *
     * @param path - path to parse prefix from
     * @return prefix
     */
    private static String parsePrefix(String path) {
        String[] pathArr = path.split("/");
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < pathArr.length - 1; i++) {
            prefix.append(pathArr[i]).append("/");
        }
        return prefix.toString();
    }

    /**
     * Solves the specified instance using all the implemented approaches and writes the solutions as CSV.
     *
     * @param instance     - instance to be solved
     * @param timeLimit    - time limit to be satisfied (relevant for CPLEX and TS)
     * @param seed         - seed to initialize PRNG in TS
     * @param numOfCores   - number of cores to be used in TS
     * @param solutionPath - path to solution file
     */
    private static void solve(Instance instance, double timeLimit, int seed, int numOfCores, String solutionPath) {
        Solution trivialSol = solveWithLPT(instance);
        Solution solSPS = solveWithSPS(instance);
        Solution mipSol = solveWithCPLEX(instance, timeLimit);
        Solution tabuSearchSolution = solveWithTabuSearch(instance, solSPS, seed, numOfCores, timeLimit);

        if (trivialSol.isFeasible() && solSPS.isFeasible() && mipSol.isFeasible() && tabuSearchSolution.isFeasible()) {
            SolutionWriter.writeSolutionAsCSV(solutionPath, trivialSol, "LPT", timeLimit);
            SolutionWriter.writeSolutionAsCSV(solutionPath, solSPS, "SPS", timeLimit);
            SolutionWriter.writeSolutionAsCSV(solutionPath, mipSol, "CPLEX", timeLimit);
            SolutionWriter.writeSolutionAsCSV(solutionPath, tabuSearchSolution, "TS", timeLimit);
        } else {
            System.out.println("generated infeasible solution..");
            System.exit(0);
        }
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

        Instance instance = InstanceReader.readInstance(args[0], parsePrefix(args[0]));
        String solutionPath = args[1];
        int numOfCores = Integer.parseInt(args[2]);
        if (numOfCores > Runtime.getRuntime().availableProcessors()) {
            numOfCores = Runtime.getRuntime().availableProcessors();
            System.out.println("the specified number of cores exceeds the available number of cores: set to "
                    + numOfCores + " cores.");
        }
        double timeLimit = Double.parseDouble(args[3]);
        int seed = Integer.parseInt(args[4]);
        solve(instance, timeLimit, seed, numOfCores, solutionPath);
    }
}