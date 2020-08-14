package PCMAX;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes solutions for the P||C_max problem.
 */
public class SolutionWriter {

    /**
     * Writes the string representation of the solution to the specified file.
     *
     * @param filename - name of the file to write to
     * @param sol      - solution to be written
     */
    public static void writeSolution(String filename, Solution sol) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sol.toString());
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the solution's relevant information to the specified CSV file.
     *
     * @param filename  - name of the CSV file to write to
     * @param sol       - solution to be written
     * @param solver    - solver that was used to generate the solution
     * @param timeLimit - time limit that was satisfied
     */
    public static void writeSolutionAsCSV(String filename, Solution sol, String solver, double timeLimit) {
        try {
            File file = new File(filename);
            boolean newFile = false;
            if (!file.exists()) {
                file.createNewFile();
                newFile = true;
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (newFile) {
                bw.write("instance,solver,time_limit,runtime,obj\n");
            }
            if (sol.isFeasible()) {
                bw.write(
                    sol.getNameOfSolvedInstance() + "," + solver + "," + timeLimit + ","
                        + sol.getTimeToSolve() + "," + sol.getMakespan() + "\n"
                );
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
