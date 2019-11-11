import java.util.List;

public class Instance {

    private int numOfMachines;
    private int numOfJobs;
    private List<Integer> processingTimes;

    public Instance(int numOfMachines, int numOfJobs, List<Integer> processingTimes) {
        this.numOfMachines = numOfMachines;
        this.numOfJobs = numOfJobs;
        this.processingTimes = processingTimes;
    }

    public int getNumOfMachines() {
        return this.numOfMachines;
    }

    public int getNumOfJobs() {
        return this.numOfJobs;
    }

    public List<Integer> getProcessingTimes() {
        return this.processingTimes;
    }
}
