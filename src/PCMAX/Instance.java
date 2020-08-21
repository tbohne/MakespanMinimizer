package PCMAX;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of the P||C_max problem.
 */
public class Instance {

    private final int numOfMachines;
    private final List<Integer> processingTimes;
    private final String name;

    /**
     * Constructor
     *
     * @param numOfMachines   - number of machines
     * @param processingTimes - processing times for the jobs
     * @param name            - instance name
     */
    public Instance(int numOfMachines, List<Integer> processingTimes, String name) {
        this.numOfMachines = numOfMachines;
        this.processingTimes = processingTimes;
        this.name = name;
    }

    /**
     * Copy-Constructor
     *
     * @param instance - instance to be copied
     */
    public Instance(Instance instance) {
        this.numOfMachines = instance.getNumOfMachines();
        this.processingTimes = new ArrayList<>();
        this.name = instance.getName();
        processingTimes.addAll(instance.getProcessingTimes());
    }

    /**
     * Returns the instance's name.
     *
     * @return instance name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the instance's number of machines.
     *
     * @return number of machines
     */
    public int getNumOfMachines() {
        return this.numOfMachines;
    }

    /**
     * Returns the instance's number of jobs.
     *
     * @return number of jobs
     */
    public int getNumOfJobs() {
        return this.processingTimes.size();
    }

    /**
     * Returns the processing times of the instance's jobs.
     *
     * @return processing times
     */
    public List<Integer> getProcessingTimes() {
        return this.processingTimes;
    }

    /**
     * Returns a string representation of the given instance.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("machines: " + this.getNumOfMachines() + "\n");
        str.append("jobs: ").append(this.getNumOfJobs()).append("\n");
        for (int processingTime : this.getProcessingTimes()) {
            str.append(processingTime).append(" ");
        }
        return str + "\n";
    }
}
