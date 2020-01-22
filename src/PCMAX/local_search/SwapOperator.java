package PCMAX.local_search;

import PCMAX.Solution;

import java.util.*;

public class SwapOperator {

    private Random rand;
    private int numOfCores;

    public SwapOperator(long seed, int numOfCores) {
        this.rand = new Random(seed);
        this.numOfCores = numOfCores;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return this.rand.nextInt((max - min) + 1) + min;
    }

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

    private Solution getRandomSol(List<Solution> solutions, Map<Solution, Swap> swapBySolution, List<Swap> performedSwaps) {
        Solution random = solutions.get(getRandomNumberInRange(0, solutions.size() - 1));
        performedSwaps.add(swapBySolution.get(random));
        return random;
    }

    private Solution performSwap(Solution currSol, int idxMachineOne, int idxMachineTwo, List<Swap> performedSwaps) {

        Map<Solution, Swap> swapsBySolution = new HashMap<>();
        swapsBySolution = Collections.synchronizedMap(swapsBySolution);
        List<Solution> res = new ArrayList<>();
        res = Collections.synchronizedList(res);

        int outerRange = currSol.getMachineAllocations().get(idxMachineOne).getJobs().size();
        int innerRange = currSol.getMachineAllocations().get(idxMachineTwo).getJobs().size();

        List<Thread> threads = new ArrayList<>();
        for (int thread = 0; thread < numOfCores; thread++) {

            int rangeStartOuter = thread / numOfCores * outerRange;
            int rangeEndOuter = (thread + 1) / numOfCores * outerRange;

            threads.add(new Thread(new SwapThread(
                0, innerRange, rangeStartOuter, rangeEndOuter, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
            )));
        }

        try {
            for (Thread t : threads) {
                t.start();
                t.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // with a certain probability, a random solution is selected (not the best) --> variability
        // TODO: test influence of this
        if (Math.random() > 0.85) {
            return getRandomSol(res, swapsBySolution, performedSwaps);
        }
        return getBestSol(res, swapsBySolution, performedSwaps);
    }

    public Solution generateSwapNeighbor(Solution currSol, List<Swap> performedSwaps) {

        // select random machine with at least two jobs assigned to it
        int machineToShiftJobFrom = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().size() < 2) {
            machineToShiftJobFrom =  getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        int targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(targetMachine).getJobs().size() < 2 || targetMachine == machineToShiftJobFrom) {
            targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        return performSwap(currSol, machineToShiftJobFrom, targetMachine, performedSwaps);
    }
}
