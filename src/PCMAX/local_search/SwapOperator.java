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

    private static Solution performSwap(Solution currSol, int idxMachineOne, int idxMachineTwo, List<Swap> performedSwaps) {

        List<Solution> tmpSolutions = new ArrayList<>();
        Map<Solution, Swap> swapsBySolution = new HashMap<>();

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

//                // FIRST-FIT
//                if (tmpSol.getMakespan() < currSol.getMakespan()) {
//                    System.out.println("first-fit swap: " + tmpSol.getMakespan());
//                    return tmpSol;
//                }

                Swap swap = new Swap(machineOne, machineTwo);

                Solution sol = new Solution(tmpSol);
                tmpSolutions.add(sol);
                swapsBySolution.put(sol, swap);
            }
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
