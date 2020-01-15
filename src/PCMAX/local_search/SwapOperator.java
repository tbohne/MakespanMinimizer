package PCMAX.local_search;

import PCMAX.Machine;
import PCMAX.Solution;

import java.util.*;

public class SwapOperator {

    private static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static Solution returnBestSol(List<Solution> solutions, Map<Solution, Swap> swapBySolution,  List<Swap> performedSwaps) {
        Solution best = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < best.getMakespan()) {
                best = sol;
            }
        }
        performedSwaps.add(swapBySolution.get(best));
        return best;
    }

    private static Solution returnRandomSol(List<Solution> solutions, Map<Solution, Swap> swapBySolution, List<Swap> performedSwaps) {
        Solution random = solutions.get(getRandomNumberInRange(0, solutions.size() - 1));
        performedSwaps.add(swapBySolution.get(random));
        return random;
    }

    private static List<Solution> singleThreaded(Solution currSol, int idxMachineOne, int idxMachineTwo, Map<Solution, Swap> swapsBySolution) {

        List<Solution> tmpSolutions = new ArrayList<>();

        // exchange each pair of jobs
        for (int jobOne = 0; jobOne < currSol.getMachineAllocations().get(idxMachineOne).getJobs().size(); jobOne++) {
            for (int jobTwo = 0; jobTwo < currSol.getMachineAllocations().get(idxMachineTwo).getJobs().size(); jobTwo++) {

                Solution tmpSol = new Solution(currSol);
                Machine machineOne = tmpSol.getMachineAllocations().get(idxMachineOne);
                Machine machineTwo = tmpSol.getMachineAllocations().get(idxMachineTwo);

                int jOne = machineOne.getJobs().get(jobOne);
                int jTwo = machineTwo.getJobs().get(jobTwo);
                machineOne.getJobs().remove(jobOne);
                machineTwo.getJobs().remove(jobTwo);
                machineOne.getJobs().add(jTwo);
                machineTwo.getJobs().add(jOne);

                tmpSol.getMachineAllocations().set(idxMachineOne, machineOne);
                tmpSol.getMachineAllocations().set(idxMachineTwo, machineTwo);

////                // FIRST-FIT
//                if (tmpSol.getMakespan() < currSol.getMakespan()) {
//                    System.out.println("first-fit swap: " + tmpSol.getMakespan());
//                    return tmpSol;
//                }

                Swap swap = new Swap(machineOne, machineTwo, jOne, jTwo);

                Solution sol = new Solution(tmpSol);
                tmpSolutions.add(sol);
                swapsBySolution.put(sol, swap);
            }
        }

        return tmpSolutions;
    }

    private static boolean equalRes(List<Solution> res1, List<Solution> res2) {
        if (res1.size() != res2.size()) { return false; }
        Set<Solution> r1 = new HashSet<>(res1);
        Set<Solution> r2 = new HashSet<>(res2);
        return r1.equals(r2);
    }

    private static Solution performSwap(Solution currSol, int idxMachineOne, int idxMachineTwo, List<Swap> performedSwaps) {

        Map<Solution, Swap> swapsBySolution = new HashMap<>();
        swapsBySolution = Collections.synchronizedMap(swapsBySolution);

        double startTime = System.currentTimeMillis();
        List<Solution> tmpSolutions = singleThreaded(currSol, idxMachineOne, idxMachineTwo, swapsBySolution);
        System.out.println("runtime single threaded: " + (System.currentTimeMillis() - startTime)/ 1000);

        List<Solution> res = new ArrayList<>();
        res = Collections.synchronizedList(res);

        int outerRange = currSol.getMachineAllocations().get(idxMachineOne).getJobs().size();
        int innerRange = currSol.getMachineAllocations().get(idxMachineTwo).getJobs().size();

        Thread i1 = new Thread(new SwapThread(
            "i1", 0, innerRange, 0, outerRange / 4, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        Thread i2 = new Thread(new SwapThread(
            "i2", 0, innerRange, outerRange / 4, outerRange / 2, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        Thread i3 = new Thread(new SwapThread(
                "i3", 0, innerRange, outerRange / 2, outerRange - outerRange / 4, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));
        Thread i4 = new Thread(new SwapThread(
                "i4", 0, innerRange, outerRange - outerRange / 4, outerRange, res, currSol, idxMachineOne, idxMachineTwo, swapsBySolution
        ));

        startTime = System.currentTimeMillis();
        i1.start();
        i2.start();
        i3.start();
        i4.start();

        try {
            i1.join();
            i2.join();
            i3.join();
            i4.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("runtime multi threaded: " + (System.currentTimeMillis() - startTime)/ 1000);
        System.out.println("equal res: " + equalRes(tmpSolutions, res));

        // with a certain probability, a random solution is selected (not the best) --> variability
        if (Math.random() > 0.85) {
            return returnRandomSol(tmpSolutions, swapsBySolution, performedSwaps);
        }
        return returnBestSol(tmpSolutions, swapsBySolution, performedSwaps);
    }

    public static Solution generateSwapNeighbor(Solution currSol, List<Swap> performedSwaps) {

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
