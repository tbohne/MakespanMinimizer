import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InstanceReader {

    public static Instance readInstance(String filename) {

        int numOfMachines = 0;
        int numOfJobs = 0;
        int[] processingTimes = new int[numOfJobs];

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line = reader.readLine().trim();
            numOfMachines = Integer.parseInt(line);
            line = reader.readLine().trim();
            numOfJobs = Integer.parseInt(line);
            processingTimes = new int[numOfJobs];

            for (int idx = 0; idx < numOfJobs; idx++) {
                line = reader.readLine().trim();
                processingTimes[idx] = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Instance(numOfMachines, numOfJobs, processingTimes);
    }
}
