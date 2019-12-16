package PCMAX;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SolutionWriter {

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

    public static void writeSolutionAsCSV(String filename, Solution sol, String solver) {
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
                bw.write("instance,solver,runtime,obj\n");
            }
            if (sol.isFeasible()) {
                bw.write(sol.getNameOfSolvedInstance() + "," + solver + "," + sol.getTimeToSolve() + "," + sol.getMakespan() + "\n");
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
