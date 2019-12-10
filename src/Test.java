import java.io.File;
import java.util.Arrays;

public class Test {

    private static final String INSTANCE_PREFIX = "res/instances/";
    private static final String SOLUTION_PREFIX = "res/solutions/";

    public static void main(String[] args) {

        File dir = new File(INSTANCE_PREFIX);
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        Arrays.sort(directoryListing);

        for (File file : directoryListing) {

            String instanceName = file.toString().replace("res/instances/", "").replace(".txt", "");
            System.out.println("working on: " + instanceName);
            Instance instance = InstanceReader.readInstance(INSTANCE_PREFIX + instanceName + ".txt");
            String solutionName = instanceName.replace("instance", "sol");


            // TODO: could be toString() for instance
//            System.out.println("machines: " + instance.getNumOfMachines());
//            System.out.println("jobs: " + instance.getNumOfJobs());
//            for (int processingTime : instance.getProcessingTimes()) {
//                System.out.print(processingTime + " ");
//            }

            Solution sol = HeuristicSolver.solve(instance);

            if (sol.isFeasible()) {
                System.out.println("final: " + sol.getMakespan());
//        System.out.println(sol);
                SolutionWriter.writeSolution(SOLUTION_PREFIX + solutionName + ".txt", sol);
            } else {
                System.out.println("generated infeasible solution..");
                System.out.println(sol);
                System.exit(0);
            }

        }
    }
}
