package PCMAX;

import java.util.*;

public class HeuristicSolver {

    public static Solution solveWithLPT(Instance instance) {
        Queue<Machine> machines = new PriorityQueue<>();
        List<Integer> jobs = instance.getProcessingTimes();

        Collections.sort(jobs);
        // longest processing times first
        Collections.reverse(jobs);

        for (int i = 0; i < instance.getNumOfMachines(); i++) { machines.add(new Machine()); }

        for (int job :  jobs) {
            Machine m = machines.poll();
            m.setJob(job);
            machines.add(m);
        }
        Solution sol = new Solution(instance);
        List<Machine> machineAllocations = new ArrayList<>();
        for (Machine m : machines) {
            machineAllocations.add(m);
        }
        sol.setMachineAllocations(machineAllocations);
        return sol;
    }

    public static Solution solve(Instance instance) {
        Solution initialSol =  solveWithLPT(instance);
        Solution partSol = PartitionApproach.solve(instance);
        return initialSol.getMakespan() < partSol.getMakespan() ? initialSol : partSol;
    }
}
