package PCMAX.local_search;

import PCMAX.Solution;

import java.util.*;

public class TabuSearch implements LocalSearchAlgorithm {

    private final int numberOfNeighbors;
    private final LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy;
    private final int unsuccessfulNeighborGenerationAttempts;
    private final int maxTabuListLength;
    private int tabuListClears;
    private final Queue<Swap> tabuList;

    public TabuSearch(
        int numberOfNeighbors, LocalSearchAlgorithm.ShortTermStrategies shortTermStrategy,
        int maxTabuListLength, int unsuccessfulNeighborGenerationAttempts
    ) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
        this.maxTabuListLength = maxTabuListLength;
        this.unsuccessfulNeighborGenerationAttempts = unsuccessfulNeighborGenerationAttempts;
        this.tabuListClears = 0;
        this.tabuList = new LinkedList<>();
    }

    /**
     * Clears the entries in the shift tabu list and increments the clear counter.
     */
    private void clearTabuList() {
        this.tabuList.clear();
        this.tabuListClears++;
    }

    public int getTabuListClears() {
        return this.tabuListClears;
    }

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
     * Returns the best solution from the list of generated solutions based on the transport costs.
     *
     * @param solutions - list of generated solutions
     * @return best solution based on the costs
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

    public Solution getNeighbor(Solution currSol, Solution bestSol) {
        List<Solution> nbrs = new ArrayList<>();
        int failCnt = 0;
        Map<Solution, List<Swap>> swapsForSolution = new HashMap<>();

        while (nbrs.size() < this.numberOfNeighbors) {

            List<Swap> performedSwaps = new ArrayList<>();
            Solution neighbor = SwapOperator.generateSwapNeighbor(currSol, performedSwaps);

            System.out.println("TL: " + this.tabuList);

            if (!neighbor.isFeasible()) { continue; }
            swapsForSolution.put(neighbor, performedSwaps);

            // FIRST-FIT
            if (this.shortTermStrategy == LocalSearchAlgorithm.ShortTermStrategies.FIRST_FIT
                && !this.tabuListContainsAnyOfTheSwaps(performedSwaps)
                && neighbor.getMakespan() < currSol.getMakespan()
            ) {
                this.forbidSwaps(performedSwaps);
                System.out.println("FIRST-FIT RETURN");
                return neighbor;
            // BEST-FIT
            } else if (!this.tabuListContainsAnyOfTheSwaps(performedSwaps)) {
                nbrs.add(neighbor);
            } else {
                System.out.println("TABU");
                // TABU
                // ASPIRATION CRITERION
                if (neighbor.getMakespan() < bestSol.getMakespan()) {
                    System.out.println("ASPIRATION!");
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
                            System.out.println("CLEARING TL");
                            this.clearTabuList();
                        } else {
                            System.out.println("FAIL RETURN");
                            return this.getBestSolution(nbrs);
                        }
                    }
                }
            }
        }
        System.out.println("BEST-FIT RETURN");
        Solution best = this.getBestSolution(nbrs);
        this.forbidSwaps(swapsForSolution.get(best));
        return best;
    }
}
