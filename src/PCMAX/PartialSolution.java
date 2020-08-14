package PCMAX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a partial solution in the SPS algorithm.
 *
 * r-th partial solution contains a list where each entry corresponds to a list representing
 * one machine and containing the jobs performed by that machine.
 */
public class PartialSolution implements Comparable<PartialSolution> {

    private final List<Machine> machineAllocations;

    /**
     * Constructor
     *
     * @param numOfMachines - number of machines
     */
    public PartialSolution(int numOfMachines) {
        this.machineAllocations = new ArrayList<>();
        for (int i = 0; i < numOfMachines; i++) {
            this.machineAllocations.add(new Machine(i));
        }
    }

    /**
     * Copy-Constructor
     *
     * @param other - partial solution to be copied
     */
    public PartialSolution(PartialSolution other) {
        this.machineAllocations = new ArrayList<>();
        for (Machine m : other.getMachineAllocations()) {
            this.machineAllocations.add(new Machine(m));
        }
    }

    /**
     * Returns the machine allocations.
     *
     * @return machine allocations
     */
    public List<Machine> getMachineAllocations() {
        return this.machineAllocations;
    }

    /**
     * Computes the gap (delta between min and max machine processing time).
     *
     * @return gqp
     */
    public int computeGap() {
        return Math.abs(Collections.max(this.machineAllocations).getProcessingTime() - Collections.min(this.machineAllocations).getProcessingTime());
    }

    /**
     * Compares two partial solutions.
     *
     * @param other - partial solution to be compared to
     * @return whether the solution has a smaller, equal, or larger gap
     */
    @Override
    public int compareTo(PartialSolution other) {
        return Integer.compare(this.computeGap(), other.computeGap());
    }

    /**
     * Returns a string representation for the partial solution.
     *
     * @return string representation for partial solution
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Machine machine : this.machineAllocations) {
            str.append(machine.toString());
        }
        return str.toString();
    }
}
