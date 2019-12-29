package PCMAX;

import java.util.ArrayList;
import java.util.List;

public class Machine implements Comparable<Machine> {

    private List<Integer> jobs;
    private int id;

    public Machine(int id) {
        this.jobs = new ArrayList<>();
        this.id = id;
    }

    public Machine(Machine machine) {
        this.id = machine.getId();
        this.jobs = new ArrayList<>();
        for (int job : machine.getJobs()) {
            this.jobs.add(job);
        }
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
        if (other == null) return false;

        if (!(other instanceof Machine)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return this.getId() == ((Machine) other).getId();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        String str = "";
        for (int job : this.jobs) {
            str += job + " ";
        }
        return str.isEmpty() ? "--------------------\n" : str + "\n";
    }
}
