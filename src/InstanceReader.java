import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstanceReader {

    public static Instance readInstance(String filename) {

        int numOfMachines = 0;
        int numOfJobs = 0;
        List<Integer> processingTimes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line = reader.readLine().trim();
            numOfMachines = Integer.parseInt(line);
            line = reader.readLine().trim();
            numOfJobs = Integer.parseInt(line);

            for (int idx = 0; idx < numOfJobs; idx++) {
                line = reader.readLine().trim();
                processingTimes.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Instance(numOfMachines, numOfJobs, processingTimes);
    }
}
