public class Test {

    public static void main(String[] args) {

        Instance instance = InstanceReader.readInstance("res/instances/PCMAX_NU_1_0010_05_0.txt");
        System.out.println("machines: " + instance.getNumOfMachines());
        System.out.println("jobs: " + instance.getNumOfJobs());
        for (int processingTime : instance.getProcessingTimes()) {
            System.out.print(processingTime + " ");
        }
        System.out.println();
    }

}
