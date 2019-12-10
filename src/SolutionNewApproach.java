import java.util.ArrayList;
import java.util.List;

public class SolutionNewApproach implements Comparable<SolutionNewApproach> {

    private int numOfMachines;
    private List<PartialSolution> partialSolutions;

    public SolutionNewApproach(int numOfMachines) {
        this.numOfMachines = numOfMachines;
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

    @Override
    public int compareTo(SolutionNewApproach other) {

        // TODO: implement compareTo
        return 0;

//        if (other.() < this.computeGap()) {
//            return -1;
//        } else if (other.computeGap() > this.computeGap()) {
//            return 1;
//        } else {
//            return 0;
//        }
    }
}
