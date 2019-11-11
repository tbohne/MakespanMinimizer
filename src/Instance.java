public class Instance {

    private int numOfMachines;
    private int numOfJobs;
    private int[] processingTimes;

    public Instance(int numOfMachines, int numOfJobs, int[] processingTimes) {
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

    public int[] getProcessingTimes() {
        return this.processingTimes;
    }
}
