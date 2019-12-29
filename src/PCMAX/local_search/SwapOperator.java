package PCMAX.local_search;

import PCMAX.Machine;
import PCMAX.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwapOperator {

    private static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static Solution returnBestSol(List<Solution> solutions) {
        Solution best = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < best.getMakespan()) {
                best = sol;
            }
        }
        return best;
    }

    private static Solution performSwap(Solution currSol, int idxMachineOne, int idxMachineTwo, List<Shift> performedShifts) {

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
                if (tmpSol.getMakespan() < currSol.getMakespan()) {
                    System.out.println(tmpSol.getMakespan());
                    return tmpSol;
                }
                tmpSolutions.add(new Solution(tmpSol));
            }
        }

        // a swap consists of two shifts
        // TODO: add shifts to performed shifts

        return returnBestSol(tmpSolutions);
    }

    public static Solution generateSwapNeighbor(Solution currSol, List<Shift> performedShifts) {

        // select random machine with at least two jobs assigned to it
        int machineToShiftJobFrom = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().size() < 2) {
            machineToShiftJobFrom =  getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        int targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (currSol.getMachineAllocations().get(targetMachine).getJobs().size() < 2 || targetMachine == machineToShiftJobFrom) {
            targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        return performSwap(currSol, machineToShiftJobFrom, targetMachine, performedShifts);
    }
}
