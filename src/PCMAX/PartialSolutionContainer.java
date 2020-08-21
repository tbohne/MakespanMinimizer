package PCMAX;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for partial solutions used in the SPS algorithm.
 */
public class PartialSolutionContainer {

    private final List<PartialSolution> partialSolutions;

    /**
     * Constructor
     *
     * @param numOfMachines - number of machines
     */
    public PartialSolutionContainer(int numOfMachines) {
        this.partialSolutions = new ArrayList<>();
        for (int i = 0; i < numOfMachines; i++) {
            this.partialSolutions.add(new PartialSolution(numOfMachines));
        }
    }

    /**
     * Returns the partial solutions.
     *
     * @return partial solutions
     */
    public List<PartialSolution> getPartialSolutions() {
        return this.partialSolutions;
    }
}
