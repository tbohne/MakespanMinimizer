package PCMAX.local_search;

import PCMAX.Machine;
import PCMAX.Solution;

import java.util.List;
import java.util.Map;

/**
 * Thread that is used to generate neighboring solutions based on the swap-neighborhood.
 */
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

    /**
     * Constructor
     *
     * @param rangeStartInner - starting idx for the inner loop
     * @param rangeEndInner   - ending idx for the inner loop
     * @param rangeStartOuter - starting idx for the outer loop
     * @param rangeEndOuter   - ending idx for the outer loop
     * @param solutions       - list to store neighboring solutions in
     * @param currSol         - solution to generate neighbors for
     * @param idxMachineOne   - first machine involved in the swap
     * @param idxMachineTwo   - second machine involved in the swap
     * @param swapsBySolution - map to store swaps that lead to the solutions
     */
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

    /**
     * Swaps each pair of jobs in the specified range for the specified machines and stores
     * the generated solutions and swap operations.
     */
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
                solutions.add(tmpSol);
                swapsBySolution.put(tmpSol, swap);
            }
        }
    }
}
