import java.util.*;

public class HeuristicSolver {

    /**
     * Enumeration containing the different short term strategies for the tabu search.
     */
    public enum ShortTermStrategies {
        FIRST_FIT,
        BEST_FIT
    }

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
        Solution sol = new Solution(instance.getNumOfMachines(), instance.getNumOfJobs());
        List<Machine> machineAllocations = new ArrayList<>();
        for (Machine m : machines) {
            machineAllocations.add(m);
        }
        sol.setMachineAllocations(machineAllocations);
        return sol;
    }

    public static Solution solve(Instance instance) {
        Solution initialSol =  solveWithLPT(instance);
        ShiftOperator shift = new ShiftOperator();
        HillClimbing hillClimbing = new HillClimbing(2000, ShortTermStrategies.BEST_FIT, shift);
        System.out.println("initial sol: " + initialSol.getMakespan());
        LocalSearch search = new LocalSearch(initialSol, 0, 5, hillClimbing);
        return search.solve();
    }
}
