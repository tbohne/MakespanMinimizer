package PCMAX.local_search;

import PCMAX.Instance;
import PCMAX.Machine;
import PCMAX.Solution;

import java.util.*;

public class LPTSolver {

    public static Solution solve(Instance instance) {
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
}