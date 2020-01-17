package PCMAX;

import java.util.ArrayList;
import java.util.List;

public class Machine implements Comparable<Machine> {

    private final List<Integer> jobs;
    private final int id;

    public Machine(int id) {
        this.jobs = new ArrayList<>();
        this.id = id;
    }

    public Machine(Machine machine) {
        this.id = machine.getId();
        this.jobs = new ArrayList<>();
        this.jobs.addAll(machine.getJobs());
    }

    public int getId() {
        return this.id;
    }

    public void setJob(int processingTime) {
        this.jobs.add(processingTime);
    }

    public List<Integer> getJobs() {
        return this.jobs;
    }

    public int getProcessingTime() {
        int sum = 0;
        for (int job : this.jobs) {
            sum += job;
        }
        return sum;
    }

    @Override
    public int compareTo(Machine other) {
        return this.getProcessingTime() - other.getProcessingTime();
    }

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

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int job : this.jobs) {
            str.append(job).append(" ");
        }
        return (str.length() == 0) ? "--------------------\n" : str + "\n";
    }
}
