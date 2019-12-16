import java.util.ArrayList;
import java.util.List;

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
