package PCMAX.local_search;

import PCMAX.Solution;

import java.util.*;

public class SwapOperator {

    private long seed;

    public SwapOperator(long seed) {
        this.seed = seed;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random(this.seed);
        return r.nextInt((max - min) + 1) + min;
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

        // currently using 4 threads TODO: implement user specified number of threads

        Thread i1 = new Thread(new SwapThread(
            0, innerRange, 0, outerRange / 4, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        i1.start();
        Thread i2 = new Thread(new SwapThread(
            0, innerRange, outerRange / 4, outerRange / 2, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        i2.start();
        Thread i3 = new Thread(new SwapThread(
            0, innerRange, outerRange / 2, outerRange - outerRange / 4, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        i3.start();
        Thread i4 = new Thread(new SwapThread(
            0, innerRange, outerRange - outerRange / 4, outerRange, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        i4.start();

        try {
            i1.join();
            i2.join();
            i3.join();
            i4.join();
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
