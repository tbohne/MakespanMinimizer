package PCMAX.local_search;

import PCMAX.Machine;
import PCMAX.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwapThread implements Runnable {

    private Thread t;
    private String threadName;

    private int rangeStartInner;
    private int rangeEndInner;
    private int rangeStartOuter;
    private int rangeEndOuter;

    private Solution currSol;
    private int idxMachineOne;
    private int idxMachineTwo;
    private Map<Solution, Swap> swapsBySolution;

    private List<Solution> solutions;

    public SwapThread(
        String name, int rangeStartInner, int rangeEndInner, int rangeStartOuter,
        int rangeEndOuter, List<Solution> solutions, Solution currSol, int idxMachineOne,
        int idxMachineTwo, Map<Solution, Swap> swapsBySolution
    ) {
        this.threadName = name;
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

        double startTime = System.currentTimeMillis();
//        System.out.println("running: " + threadName);

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

////                // FIRST-FIT
//                if (tmpSol.getMakespan() < currSol.getMakespan()) {
//                    System.out.println("first-fit swap: " + tmpSol.getMakespan());
//                    return tmpSol;
//                }

                Swap swap = new Swap(machineOne, machineTwo, jOne, jTwo);

                Solution sol = new Solution(tmpSol);
                solutions.add(sol);
                swapsBySolution.put(sol, swap);

            }
        }
//        System.out.println("thread " + threadName + " exiting with runtime: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
    }

    public void start() {
//        System.out.println("starting " +  threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
