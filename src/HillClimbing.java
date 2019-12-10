import java.util.ArrayList;
import java.util.List;

public class HillClimbing implements LocalSearchAlgorithm {

    private int numberOfNeighbors;
    private HeuristicSolver.ShortTermStrategies shortTermStrategy;

    // nbh operators
    private SwapOperator swapOperator;

    public HillClimbing(int numberOfNeighbors, HeuristicSolver.ShortTermStrategies shortTermStrategy, SwapOperator swapOperator) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
        this.swapOperator = swapOperator;
    }

    private Solution applyVariableNeighborhood(Solution currSol) {
        return this.swapOperator.generateSwapNeighbor(currSol, new ArrayList<>());
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
            System.out.println("###################################");
            System.out.println(neighbor);
            System.out.println("###################################");
            if (!neighbor.isFeasible()) { continue; }

            // FIRST-FIT
            if (this.shortTermStrategy == HeuristicSolver.ShortTermStrategies.FIRST_FIT && neighbor.getMakespan() < currSol.getMakespan()) {
                System.out.println("IMPROVEMENT!!!!!!!!!!!!!!!!!!!!!!!");
                System.exit(0);
                return neighbor;
            // BEST-FIT
            } else {
                nbrs.add(neighbor);
            }
        }
        return this.getBestSolution(nbrs);
    }
}
