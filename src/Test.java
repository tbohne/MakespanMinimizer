public class Test {

    public static void main(String[] args) {

        System.out.println("##############################################################");
        Instance instance = InstanceReader.readInstance("res/instances/PCMAX_NU_1_0010_05_1.txt");
        System.out.println("machines: " + instance.getNumOfMachines());
        System.out.println("jobs: " + instance.getNumOfJobs());
        for (int processingTime : instance.getProcessingTimes()) {
            System.out.print(processingTime + " ");
        }
        System.out.println();
        System.out.println("##############################################################");

        Solution sol = HeuristicSolver.solve(instance);
        System.out.println(sol);
        SolutionWriter.writeSolution("res/solutions/2.txt", sol);
    }

}
