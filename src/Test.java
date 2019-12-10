public class Test {

    public static void main(String[] args) {

        System.out.println("##############################################################");
        Instance instance = InstanceReader.readInstance("res/instances/PCMAX_U_1_0500_25_4.txt");
        System.out.println("machines: " + instance.getNumOfMachines());
        System.out.println("jobs: " + instance.getNumOfJobs());
        for (int processingTime : instance.getProcessingTimes()) {
            System.out.print(processingTime + " ");
        }
        System.out.println();
        System.out.println("##############################################################");

        Solution sol = HeuristicSolver.solve(instance);

        if (sol.isFeasible()) {
            System.out.println("final: " + sol.getMakespan());
//        System.out.println(sol);
            SolutionWriter.writeSolution("res/solutions/4.txt", sol);
        } else {
            System.out.println("generated infeasible solution..");
        }
    }
}
