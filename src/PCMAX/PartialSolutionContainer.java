package PCMAX;

import java.util.ArrayList;
import java.util.List;

public class PartialSolutionContainer {

    private List<PartialSolution> partialSolutions;

    public PartialSolutionContainer(int numOfMachines) {
        this.partialSolutions = new ArrayList<>();
        for (int i = 0; i < numOfMachines; i++) {
            this.partialSolutions.add(new PartialSolution(numOfMachines));
        }
    }

    public List<PartialSolution> getPartialSolutions() {
        return this.partialSolutions;
    }

    public void setPartialSolutions(List<PartialSolution> partialSolutions) {
        this.partialSolutions = partialSolutions;
    }
}
