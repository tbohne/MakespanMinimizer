import java.util.ArrayList;
import java.util.List;

public class Instance {

    private int numOfMachines;
    private List<Integer> processingTimes;

    public Instance(int numOfMachines, List<Integer> processingTimes) {
        this.numOfMachines = numOfMachines;
        this.processingTimes = processingTimes;
    }

    public Instance(Instance instance) {
        this.numOfMachines = instance.getNumOfMachines();
        this.processingTimes = new ArrayList<>();
        for (int processingTime : instance.getProcessingTimes()) {
            processingTimes.add(processingTime);
        }
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
}
