package PCMAX;

import java.util.*;

public class Solution {

    private final Instance instance;
    private List<Machine> machineAllocations;
    private double timeToSolve;

    public Solution(Instance instance) {
        this.instance = instance;
        this.machineAllocations = new ArrayList<>();
    }

    public Solution(Solution sol) {
        this.instance = new Instance(sol.instance);
        this.machineAllocations = new ArrayList<>();
        for (Machine m : sol.getMachineAllocations()) {
            Machine copyMachine = new Machine(m);
            this.machineAllocations.add(copyMachine);
        }
        this.timeToSolve = sol.getTimeToSolveAsDouble();
    }

    public void setTimeToSolve(double timeToSolve) {
        this.timeToSolve = timeToSolve;
    }

    public String getTimeToSolve() {
        return String.format("%.02f", this.timeToSolve).replace(",", ".");
    }

    public double getTimeToSolveAsDouble() {
        return this.timeToSolve;
    }

    public void setMachineAllocations(List<Machine> machineAllocations) {
        this.machineAllocations = machineAllocations;
    }

    public List<Machine> getMachineAllocations() {
        return this.machineAllocations;
    }

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

    public int getMakespan() {
        int makespan = 0;
        for (Machine m : this.machineAllocations) {
            if (m.getProcessingTime() > makespan) {
                makespan = m.getProcessingTime();
            }
        }
        return makespan;
    }

    public String getNameOfSolvedInstance() {
        return this.instance.getName();
    }

    @Override
    public int hashCode() {
        return this.getMakespan() * this.getMachineAllocations().size();
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Solution)) { return false; }
        if (other == this) { return true; }

        // it suffices here to call solution equal when they have the same makespan
        boolean equalMakespan = getMakespan() == ((Solution)other).getMakespan();
        boolean equalSizeOfMachines = this.getMachineAllocations().size() == ((Solution)other).getMachineAllocations().size();
        return equalMakespan && equalSizeOfMachines;
    }

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
