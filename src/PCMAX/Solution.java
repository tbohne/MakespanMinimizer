package PCMAX;

import java.util.*;

/**
 * Represents a solution (schedule) to P||C_max.
 */
public class Solution {

    private final Instance instance;
    private List<Machine> machineAllocations;
    private double timeToSolve;

    /**
     * Constructor
     *
     * @param instance - instance the schedule is generated for
     */
    public Solution(Instance instance) {
        this.instance = instance;
        this.machineAllocations = new ArrayList<>();
    }

    /**
     * Copy-Constructor
     *
     * @param sol - solution to be copied
     */
    public Solution(Solution sol) {
        this.instance = new Instance(sol.instance);
        this.machineAllocations = new ArrayList<>();
        for (Machine m : sol.getMachineAllocations()) {
            Machine copyMachine = new Machine(m);
            this.machineAllocations.add(copyMachine);
        }
        this.timeToSolve = sol.getTimeToSolveAsDouble();
    }

    /**
     * Sets the runtime to generate the solution.
     *
     * @param timeToSolve - runtime to generate solution
     */
    public void setTimeToSolve(double timeToSolve) {
        this.timeToSolve = timeToSolve;
    }

    /**
     * Retrieves the runtime (as string).
     *
     * @return runtime string
     */
    public String getTimeToSolve() {
        return String.format("%.02f", this.timeToSolve).replace(",", ".");
    }

    /**
     * Retrieves the runtime.
     *
     * @return runtime
     */
    private double getTimeToSolveAsDouble() {
        return this.timeToSolve;
    }

    /**
     * Sets the machine allocations (job assignments to machines).
     *
     * @param machineAllocations - job assignments to the machines
     */
    public void setMachineAllocations(List<Machine> machineAllocations) {
        this.machineAllocations = machineAllocations;
    }

    /**
     * Retrieves the machine allocations.
     *
     * @return machine allocations
     */
    public List<Machine> getMachineAllocations() {
        return this.machineAllocations;
    }

    /**
     * Checks the solution's feasibility.
     *
     * @return whether the schedule is feasible
     */
    public boolean isFeasible() {

        List<Integer> processingTimesCopy = new ArrayList<>(this.instance.getProcessingTimes());

        // checks whether each job (represented by processing time) is assigned to a machine exactly once
        for (Machine m : this.getMachineAllocations()) {
            for (int processingTime : m.getJobs()) {
                if (!processingTimesCopy.contains(processingTime)) { return false; }
                processingTimesCopy.remove((Integer) processingTime);
            }
        }
        return processingTimesCopy.size() == 0;
    }

    /**
     * Retrieves the schedule's makespan.
     *
     * @return makespan of the schedule
     */
    public int getMakespan() {
        int makespan = 0;
        for (Machine m : this.machineAllocations) {
            if (m.getProcessingTime() > makespan) {
                makespan = m.getProcessingTime();
            }
        }
        return makespan;
    }

    /**
     * Retrieves the name of the solved instance.
     *
     * @return name of solved instance
     */
    public String getNameOfSolvedInstance() {
        return this.instance.getName();
    }

    /**
     * Returns the solution's hashcode.
     *
     * @return hashcode of the solution
     */
    @Override
    public int hashCode() {
        return this.getMakespan() * this.getMachineAllocations().size();
    }

    /**
     * Checks equality for two solutions.
     *
     * @param other - solution to be compared to
     * @return whether the two solutions are equal
     */
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Solution)) { return false; }
        if (other == this) { return true; }

        // it suffices here to call solution equal when they have the same makespan
        boolean equalMakespan = getMakespan() == ((Solution)other).getMakespan();
        boolean equalSizeOfMachines = this.getMachineAllocations().size() == ((Solution)other).getMachineAllocations().size();
        return equalMakespan && equalSizeOfMachines;
    }

    /**
     * Returns a string representation of the schedule.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.instance.getNumOfMachines()).append("\n");
        str.append(this.instance.getNumOfJobs()).append("\n");
        for (Machine m : this.machineAllocations) {
            for (int j : m.getJobs()) {
                str.append(j).append(" ");
            }
            str.append("\n");
        }
        str.append(this.getMakespan()).append("\n");
        return str.toString();
    }
}
