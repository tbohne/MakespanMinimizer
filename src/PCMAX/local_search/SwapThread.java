package PCMAX.local_search;

import PCMAX.Machine;
import PCMAX.Solution;

import java.util.List;
import java.util.Map;

public class SwapThread implements Runnable {

    private final int rangeStartInner;
    private final int rangeEndInner;
    private final int rangeStartOuter;
    private final int rangeEndOuter;

    private final Solution currSol;
    private final int idxMachineOne;
    private final int idxMachineTwo;
    private final Map<Solution, Swap> swapsBySolution;
    private final List<Solution> solutions;

    public SwapThread(
        int rangeStartInner, int rangeEndInner, int rangeStartOuter,
        int rangeEndOuter, List<Solution> solutions, Solution currSol, int idxMachineOne,
        int idxMachineTwo, Map<Solution, Swap> swapsBySolution
    ) {
        this.rangeStartInner = rangeStartInner;
        this.rangeEndInner = rangeEndInner;
        this.rangeStartOuter = rangeStartOuter;
        this.rangeEndOuter = rangeEndOuter;
        this.solutions = solutions;
        this.currSol = currSol;
        this.idxMachineOne = idxMachineOne;
        this.idxMachineTwo = idxMachineTwo;
        this.swapsBySolution = swapsBySolution;
    }

    public void run() {

        for (int jobOne = rangeStartOuter; jobOne < rangeEndOuter; jobOne++) {
            for (int jobTwo = rangeStartInner; jobTwo < rangeEndInner; jobTwo++) {

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

                Swap swap = new Swap(machineOne, machineTwo, jOne, jTwo);
                Solution sol = new Solution(tmpSol);
                solutions.add(sol);
                swapsBySolution.put(sol, swap);
            }
        }
    }
}
