package PCMAX;

import java.util.*;

public class LPTSolver {

    public static Solution solve(Instance instance) {
        Queue<Machine> machines = new PriorityQueue<>();
        List<Integer> jobs = instance.getProcessingTimes();

        Collections.sort(jobs);
        // longest processing times first
        Collections.reverse(jobs);

        for (int i = 0; i < instance.getNumOfMachines(); i++) { machines.add(new Machine(i)); }

        for (int job :  jobs) {
            Machine m = machines.poll();
            m.setJob(job);
            machines.add(m);
        }
        Solution sol = new Solution(instance);
        List<Machine> machineAllocations = new ArrayList<>(machines);
        sol.setMachineAllocations(machineAllocations);
        return sol;
    }
}
