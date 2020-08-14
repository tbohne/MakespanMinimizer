package PCMAX.local_search;

import PCMAX.Solution;

import java.util.*;

/**
 * Tabu search algorithm applied to P||C_max.
 */
public class TabuSearch implements LocalSearchAlgorithm {

    private final int numberOfNeighbors;
    private final LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy;
    private final int unsuccessfulNeighborGenerationAttempts;
    private final int maxTabuListLength;
    private final Queue<Swap> tabuList;
    private final SwapOperator swapOperator;

    /**
     * Constructor
     *
     * @param numberOfNeighbors                      - number of neighbors to be considered in each iteration
     * @param shortTermStrategy                      - short term strategy that is applied
     * @param maxTabuListLength                      - maximum length of the TL
     * @param unsuccessfulNeighborGenerationAttempts - number of failing nbr generation attempts after which the current iteration gets stopped
     * @param swapOperator                           - swap operator to be used to generate neighboring solutions
     */
    public TabuSearch(
        int numberOfNeighbors, LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy,
        int maxTabuListLength, int unsuccessfulNeighborGenerationAttempts, SwapOperator swapOperator
    ) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
        this.maxTabuListLength = maxTabuListLength;
        this.unsuccessfulNeighborGenerationAttempts = unsuccessfulNeighborGenerationAttempts;
        this.tabuList = new LinkedList<>();
        this.swapOperator = swapOperator;
    }

    /**
     * Checks whether the TL contains any of the specified swaps.
     *
     * @param performedSwaps - swaps to be compared to TL
     * @return whether or not the TL contains any of the specified swaps
     */
    private boolean tabuListContainsAnyOfTheSwaps(List<Swap> performedSwaps) {
        for (Swap swap : performedSwaps) {
            if (this.tabuList.contains(swap)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the specified swap operation to the tabu list.
     * Replaces the oldest entry if the maximum length of the tabu list is reached.
     */
    private void forbidSwaps(List<Swap> performedSwaps) {
        if (this.tabuList.size() >= this.maxTabuListLength) {
            while (this.tabuList.size() + performedSwaps.size() >= this.maxTabuListLength) {
                this.tabuList.poll();
            }
        }
        this.tabuList.addAll(performedSwaps);
    }

    /**
     * Returns a best solution from the list of generated solutions based on the makespan.
     *
     * @param solutions - list of generated solutions
     * @return best solution based on makespan
     */
    private Solution getBestSolution(List<Solution> solutions) {
        Solution bestSol = solutions.get(0);
        for (Solution sol : solutions) {
            if (sol.getMakespan() < bestSol.getMakespan()) {
                bestSol = sol;
            }
        }
        return bestSol;
    }

    /**
     * Retrieves a neighbor for the given solution based on certain predefined conditions.
     *
     * @param currSol - current solution to retrieve a neighbor for
     * @param bestSol - so far best solution
     * @return neirhboring solution
     */
    public Solution getNeighbor(Solution currSol, Solution bestSol) {

        List<Solution> nbrs = new ArrayList<>();
        int failCnt = 0;
        Map<Solution, List<Swap>> swapsForSolution = new HashMap<>();

        while (nbrs.size() < this.numberOfNeighbors) {

            List<Swap> performedSwaps = new ArrayList<>();
            Solution neighbor = this.swapOperator.generateSwapNeighbor(currSol, performedSwaps);

            if (!neighbor.isFeasible()) { continue; }
            swapsForSolution.put(neighbor, performedSwaps);

            // FIRST-FIT
            if (this.shortTermStrategy == LocalSearchAlgorithm.ShortTermStrategies.FIRST_FIT
                && !this.tabuListContainsAnyOfTheSwaps(performedSwaps)
                && neighbor.getMakespan() < currSol.getMakespan()
            ) {
                this.forbidSwaps(performedSwaps);
                return neighbor;
            // BEST-FIT
            } else if (!this.tabuListContainsAnyOfTheSwaps(performedSwaps)) {
                nbrs.add(neighbor);
            } else {
                // TABU
                // ASPIRATION CRITERION
                if (neighbor.getMakespan() < bestSol.getMakespan()) {
                    if (this.shortTermStrategy == LocalSearchAlgorithm.ShortTermStrategies.FIRST_FIT) {
                        return neighbor;
                    } else {
                        nbrs.add(neighbor);
                    }
                } else {
                    failCnt++;
                    if (failCnt == this.unsuccessfulNeighborGenerationAttempts) {
                        failCnt = 0;
                        if (nbrs.size() == 0) {
                            this.tabuList.clear();
                        } else {
                            return this.getBestSolution(nbrs);
                        }
                    }
                }
            }
        }
        Solution best = this.getBestSolution(nbrs);
        this.forbidSwaps(swapsForSolution.get(best));
        return best;
    }
}
