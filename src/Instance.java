import java.util.ArrayList;
import java.util.List;

public class Instance {

    private int numOfMachines;
    private List<Integer> processingTimes;
    private String name;

    public Instance(int numOfMachines, List<Integer> processingTimes, String name) {
        this.numOfMachines = numOfMachines;
        this.processingTimes = processingTimes;
        this.name = name;
    }

    public Instance(Instance instance) {
        this.numOfMachines = instance.getNumOfMachines();
        this.processingTimes = new ArrayList<>();
        this.name = instance.getName();
        for (int processingTime : instance.getProcessingTimes()) {
            processingTimes.add(processingTime);
        }
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
        String str = "machines: " + this.getNumOfMachines() + "\n";
        str += "jobs: " + this.getNumOfJobs() + "\n";
        for (int processingTime : this.getProcessingTimes()) {
            str += processingTime + " ";
        }
        return str + "\n";
    }
}
