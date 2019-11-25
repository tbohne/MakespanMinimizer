import java.util.ArrayList;
import java.util.List;

public class Solution {

    private int numOfMachines;
    private int numOfJobs;
    private List<Machine> machineAllocations;

    public Solution(int numOfMachines, int numOfJobs) {
        this.numOfMachines = numOfMachines;
        this.numOfJobs = numOfJobs;
        this.machineAllocations = new ArrayList<>();
    }

    public Solution(Solution sol) {
        this.numOfMachines = sol.numOfMachines;
        this.numOfJobs = sol.numOfJobs;
        this.machineAllocations = new ArrayList<>();
        for (Machine m : sol.getMachineAllocations()) {
            Machine copyMachine = new Machine(m);
            this.machineAllocations.add(copyMachine);
        }
    }

    public void setMachineAllocations(List<Machine> machineAllocations) {
        this.machineAllocations = machineAllocations;
    }

    public List<Machine> getMachineAllocations() {
        return this.machineAllocations;
    }

    public boolean isFeasible() {
        // TODO: implement feasibility check
        return true;
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

    @Override
    public String toString() {
        String str = "";
        str += this.numOfMachines + "\n";
        str += this.numOfJobs + "\n";
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
