package PCMAX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartialSolution implements Comparable<PartialSolution> {

    // r-th partial solution
    // contains a list where each entry corresponds to a list representing
    // one machine and containing the jobs performed by that machine

    private List<Machine> machineAllocations;

    public PartialSolution(int numOfMachines) {
        this.machineAllocations = new ArrayList<>();
        for (int i = 0; i < numOfMachines; i++) {
            this.machineAllocations.add(new Machine());
        }
    }

    public PartialSolution(PartialSolution other) {
        this.machineAllocations = new ArrayList<>();
        for (Machine m : other.getMachineAllocations()) {
            this.machineAllocations.add(new Machine(m));
        }
    }

    public List<Machine> getMachineAllocations() {
        return this.machineAllocations;
    }

    public int computeGap() {
        return Math.abs(Collections.max(this.machineAllocations).getProcessingTime() - Collections.min(this.machineAllocations).getProcessingTime());
    }

    public boolean isEmpty() {
        for (Machine machine : this.machineAllocations) {
            if (!machine.toString().contains("-")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(PartialSolution other) {
        if (other.computeGap() < this.computeGap()) {
            return 1;
        } else if (other.computeGap() > this.computeGap()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String str = "";
        for (Machine machine : this.machineAllocations) {
            str += machine.toString();
        }
        return str;
    }
}
