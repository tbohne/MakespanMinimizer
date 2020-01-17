package PCMAX;

import java.util.ArrayList;
import java.util.List;

public class Instance {

    private final int numOfMachines;
    private final List<Integer> processingTimes;
    private final String name;

    public Instance(int numOfMachines, List<Integer> processingTimes, String name) {
        this.numOfMachines = numOfMachines;
        this.processingTimes = processingTimes;
        this.name = name;
    }

    public Instance(Instance instance) {
        this.numOfMachines = instance.getNumOfMachines();
        this.processingTimes = new ArrayList<>();
        this.name = instance.getName();
        processingTimes.addAll(instance.getProcessingTimes());
    }

    public String getName() {
        return this.name;
    }

    public int getNumOfMachines() {
        return this.numOfMachines;
    }

    public int getNumOfJobs() {
        return this.processingTimes.size();
    }

    public List<Integer> getProcessingTimes() {
        return this.processingTimes;
    }

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
