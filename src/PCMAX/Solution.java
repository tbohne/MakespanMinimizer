package PCMAX;

import java.util.*;

public class Solution {

    private Instance instance;
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

        List<Integer> processingTimesCopy = new ArrayList<>();
        for (int p : this.instance.getProcessingTimes()) {
            processingTimesCopy.add(p);
        }

        // check whether each job (represented by processing time) is assigned to a machine and not more
        for (Machine m : this.getMachineAllocations()) {
            for (int processingTime : m.getJobs()) {
                if (!processingTimesCopy.contains(processingTime)) { return false; }
                processingTimesCopy.remove(processingTimesCopy.indexOf(processingTime));
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
        if (other == null) return false;

        if (!(other instanceof Solution)) {
            return false;
        }
        if (other == this) {
            return true;
        }

        boolean equalMakespan = getMakespan() == ((Solution)other).getMakespan();
        boolean equalSizeOfMachines = this.getMachineAllocations().size() == ((Solution)other).getMachineAllocations().size();

        if (!equalMakespan || !equalSizeOfMachines) {
            return false;
        }

        // actually it suffices here to call solution equal when they have the same makespan

//        for (int i = 0; i < this.getMachineAllocations().size(); i++) {
//            if (!this.getMachineAllocations().get(i).equals(((Solution)other).getMachineAllocations().get(i))) {
////            Set<Machine> r1 = new HashSet<Machine>(this.getMachineAllocations().get(i));
////            Set<Machine> r2 = new HashSet<>((Solution)other).getMachineAllocations().get(i);
////            return r1.equals(r2);
//
//                Collections.sort(this.getMachineAllocations().get(i).getJobs());
//                Collections.sort(((Solution)other).getMachineAllocations().get(i).getJobs());
////
//                System.out.println(this.getMachineAllocations().get(i));
//                System.out.println(((Solution)other).getMachineAllocations().get(i));
////                System.exit(0);
//
////                System.out.println("false");
//                return false;
//            }
//        }

        return true;
    }

    @Override
    public String toString() {
        String str = "";
        str += this.instance.getNumOfMachines() + "\n";
        str += this.instance.getNumOfJobs() + "\n";
        for (Machine m : this.machineAllocations) {
            for (int j : m.getJobs()) {
                str += j + " ";
            }
            str += "\n";
        }
        str += this.getMakespan() + "\n";
        return str;
    }
}
