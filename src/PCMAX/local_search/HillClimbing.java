package PCMAX.local_search;

import PCMAX.Solution;

import java.util.ArrayList;
import java.util.List;

public class HillClimbing implements LocalSearchAlgorithm {

    private final int numberOfNeighbors;
    private final LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy;

    public HillClimbing(int numberOfNeighbors, LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
    }

    public Solution getNeighbor(Solution currSol, Solution bestSol) {

        List<Solution> nbrs = new ArrayList<>();

        while (nbrs.size() < this.numberOfNeighbors) {

            Solution neighbor = this.applyVariableNeighborhood(currSol);

            if (!neighbor.isFeasible()) { continue; }
            // FIRST-FIT
            if (this.shortTermStrategy == LocalSearchAlgorithm.ShortTermStrategies.FIRST_FIT && neighbor.getMakespan() < currSol.getMakespan()) {
                return neighbor;
            // BEST-FIT
            } else {
                nbrs.add(neighbor);
            }
        }
        return this.getBestSolution(nbrs);
    }

    private Solution applyVariableNeighborhood(Solution currSol) {
        return SwapOperator.generateSwapNeighbor(currSol, new ArrayList<>());
    }

    private Solution getBestSolution(List<Solution> solutions) {
        Solution bestSol = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < bestSol.getMakespan()) {
                bestSol = sol;
            }
        }
        return bestSol;
    }
}
