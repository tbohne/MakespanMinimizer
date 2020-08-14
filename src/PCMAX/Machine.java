package PCMAX;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a machine in a P||C_max scenario.
 */
public class Machine implements Comparable<Machine> {

    private final List<Integer> jobs;
    private final int id;

    /**
     * Constructor
     *
     * @param id - ID of the machine
     */
    public Machine(int id) {
        this.jobs = new ArrayList<>();
        this.id = id;
    }

    /**
     * Copy-Constructor
     *
     * @param machine - machine to be copied
     */
    public Machine(Machine machine) {
        this.id = machine.getId();
        this.jobs = new ArrayList<>();
        this.jobs.addAll(machine.getJobs());
    }

    /**
     * Retrieves the machines ID.
     *
     * @return machine ID
     */
    private int getId() {
        return this.id;
    }

    /**
     * Adds the specified processing time to the machine.
     *
     * @param processingTime - processing time to be added to the job
     */
    public void setJob(int processingTime) {
        this.jobs.add(processingTime);
    }

    /**
     * Retrieves the jobs assigned to the machine (the processing times).
     *
     * @return processing times assigned to the machine
     */
    public List<Integer> getJobs() {
        return this.jobs;
    }

    /**
     * Retrieves the machine's total processing time.
     *
     * @return total processing time
     */
    public int getProcessingTime() {
        int sum = 0;
        for (int job : this.jobs) {
            sum += job;
        }
        return sum;
    }

    /**
     * Compares one machine to another based on their total processing times.
     *
     * @param other  - machine to be compared to
     * @return whether this machine has smaller equal or greater processing time
     */
    @Override
    public int compareTo(Machine other) {
        return this.getProcessingTime() - other.getProcessingTime();
    }

    /**
     * Determines whether two machines are equal.
     *
     * @param other - machine to be compared to
     * @return whether the two machines are equal
     */
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Machine)) { return false; }
        if (other == this) { return true; }

        boolean equalID = this.getId() == ((Machine) other).getId();
        boolean equalNumOfJobs = this.getJobs().size() == ((Machine) other).getJobs().size();
        if (!equalID || !equalNumOfJobs) { return false; }

        for (int i = 0; i < this.getJobs().size(); i++) {
            if (!this.getJobs().get(i).equals(((Machine) other).getJobs().get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the machine's ID as its hashcode.
     *
     * @return machine ID
     */
    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * String representation of the machine.
     *
     * @return string representation for machine
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int job : this.jobs) {
            str.append(job).append(" ");
        }
        return (str.length() == 0) ? "--------------------\n" : str + "\n";
    }
}
