import java.util.ArrayList;
import java.util.List;

public class HillClimbing implements LocalSearchAlgorithm {

    private int numberOfNeighbors;
    private HeuristicSolver.ShortTermStrategies shortTermStrategy;

    // nbh operators
    private ShiftOperator shiftOperator;

    public HillClimbing(int numberOfNeighbors, HeuristicSolver.ShortTermStrategies shortTermStrategy, ShiftOperator shiftOperator) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
        this.shiftOperator = shiftOperator;
    }

    private Solution applyVariableNeighborhood(Solution currSol) {
        return this.shiftOperator.generateShiftNeighbor(currSol);
    }

    /**
     * Returns the best solution from the list of generated solutions based on the transport costs.
     *
     * @param solutions - list of generated solutions
     * @return best solution based on the costs
     */
    public Solution getBestSolution(List<Solution> solutions) {
        Solution bestSol = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < bestSol.getMakespan()) {
                bestSol = sol;
            }
        }
        return bestSol;
    }

    public Solution getNeighbor(Solution currSol, Solution bestSol) {

        List<Solution> nbrs = new ArrayList<>();

        while (nbrs.size() < this.numberOfNeighbors) {

            Solution neighbor = this.applyVariableNeighborhood(currSol);
            if (!neighbor.isFeasible()) { continue; }

            // FIRST-FIT
            if (this.shortTermStrategy == HeuristicSolver.ShortTermStrategies.FIRST_FIT && neighbor.getMakespan() < currSol.getMakespan()) {
                return neighbor;
            // BEST-FIT
            } else {
                nbrs.add(neighbor);
            }
        }
        return this.getBestSolution(nbrs);
    }
}
