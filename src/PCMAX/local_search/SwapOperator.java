package PCMAX.local_search;

import PCMAX.Solution;

import java.util.*;

/**
 * Swap operator to be applied in the TS to generate neighboring solutions.
 */
public class SwapOperator {

    private final Random rand;
    private final int numOfCores;

    /**
     * Constructor
     *
     * @param seed       - seed to initialize the pseudo random number generator
     * @param numOfCores - number of cores to be used during the operator application
     */
    public SwapOperator(long seed, int numOfCores) {
        this.rand = new Random(seed);
        this.numOfCores = numOfCores;
    }

    /**
     * Retrieves a pseudo random number from the specified range.
     *
     * @param min - min element of range
     * @param max - max element of range
     * @return pseudo random number
     */
    private int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return this.rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Retrieves the best solution from the specified list and stores the
     * corresponding swap operation in the list of performed swaps.
     *
     * @param solutions      - list of solutions to retrieve the best from
     * @param swapBySolution - map that contains the swap that lead to specific solutions
     * @param performedSwaps - list of performed swaps to generate neighboring solutions
     * @return best solution
     */
    private Solution getBestSol(List<Solution> solutions, Map<Solution, Swap> swapBySolution, List<Swap> performedSwaps) {
        Solution best = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < best.getMakespan()) {
                best = sol;
            }
        }
        performedSwaps.add(swapBySolution.get(best));
        return best;
    }

    /**
     * Performs the swap operations and returns a best solution from the generated ones.
     *
     * @param currSol        - solution to apply swap operator to
     * @param idxMachineOne  - first machine involved in swaps
     * @param idxMachineTwo  - second machine involved in swaps
     * @param performedSwaps - list of performed swaps
     * @return best generated solution
     */
    private Solution performSwap(Solution currSol, int idxMachineOne, int idxMachineTwo, List<Swap> performedSwaps) {

        int outerRange = currSol.getMachineAllocations().get(idxMachineOne).getJobs().size();
        int innerRange = currSol.getMachineAllocations().get(idxMachineTwo).getJobs().size();
        List<List<Solution>> threadSolutions = new ArrayList<>();
        List<Map<Solution, Swap>> threadSwaps = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        for (int thread = 0; thread < numOfCores; thread++) {

            int rangeStartOuter = (int)((float)thread / (float)numOfCores * (float)outerRange);
            int rangeEndOuter = (int)((float)(thread + 1) / (float)numOfCores * (float)outerRange);

            List<Solution> currThreadSolutions = new ArrayList<>();
            threadSolutions.add(currThreadSolutions);
            Map<Solution, Swap> currThreadSwaps = new HashMap<>();
            threadSwaps.add(currThreadSwaps);

            Thread t = new Thread(new SwapThread(
                0, innerRange, rangeStartOuter, rangeEndOuter, currThreadSolutions,
                currSol, idxMachineOne, idxMachineTwo, currThreadSwaps
            ));
            t.start();
            threads.add(t);
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Solution> solutions = new ArrayList<>();
        for (List<Solution> sol : threadSolutions) {
            solutions.addAll(sol);
        }

        Map<Solution, Swap> swaps = new HashMap<>();
        for (Map<Solution, Swap> swap : threadSwaps) {
            swaps.putAll(swap);
        }

        return getBestSol(solutions, swaps, performedSwaps);
    }

    /**
     * Generates a swap neighbor.
     *
     * @param currSol        - solution to generate neighbor for
     * @param performedSwaps - list of performed swap operations.
     * @return best generated solution
     */
    public Solution generateSwapNeighbor(Solution currSol, List<Swap> performedSwaps) {

        // select random machine with at least two jobs assigned to it
        int machineToShiftJobFrom = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().size() < 1) {
            machineToShiftJobFrom =  getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        int targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(targetMachine).getJobs().size() < 1 || targetMachine == machineToShiftJobFrom) {
            targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        return performSwap(currSol, machineToShiftJobFrom, targetMachine, performedSwaps);
    }
}
