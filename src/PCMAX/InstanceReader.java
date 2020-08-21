package PCMAX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads instances of the P||C_max problem.
 */
public class InstanceReader {

    /**
     * Reads the seeds from the specified file.
     *
     * @param filename - file to read seeds from
     * @return read seeds
     */
    public static List<Integer> readSeeds(String filename) {

        List<Integer> seeds = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            while (line != null) {
                int seed = Integer.parseInt(line.trim());
                seeds.add(seed);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seeds;
    }

    /**
     * Reads an instance from the specified file.
     *
     * @param filename - file to read instance from
     * @param prefix - instance prefix
     * @return read instance
     */
    public static Instance readInstance(String filename, String prefix) {

        int numOfMachines = 0;
        int numOfJobs;
        List<Integer> processingTimes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            numOfMachines = Integer.parseInt(reader.readLine().trim());
            numOfJobs = Integer.parseInt(reader.readLine().trim());

            for (int idx = 0; idx < numOfJobs; idx++) {
                String line = reader.readLine().trim();
                processingTimes.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.out.println("ERROR: INVALID INSTANCE FILE NAME");
            System.exit(0);
        }

        return new Instance(numOfMachines, processingTimes, filename.replace(prefix, "").replace(".txt", ""));
    }
}
