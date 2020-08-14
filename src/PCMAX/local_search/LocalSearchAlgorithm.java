package PCMAX.local_search;

import PCMAX.Solution;

/**
 * Interface to be implemented by different local search algorithms.
 */
public interface LocalSearchAlgorithm {

    /**
     * Enumeration containing the different short term strategies for the local search.
     */
    enum ShortTermStrategies {
        FIRST_FIT,
        BEST_FIT
    }

    /**
     * Returns a neighboring solution based on the neighborhood structure used in the local search algorithm.
     *
     * @param currSol - current solution to retrieve a neighbor for
     * @param bestSol - so far best solution
     * @return neighboring solution
     */
    Solution getNeighbor(Solution currSol, Solution bestSol);
}