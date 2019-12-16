package PCMAX.local_search;

import PCMAX.Solution;

public class LocalSearch {

    private Solution currSol;
    private Solution bestSol;
    private double timeLimit;
    private double startTime;
    private final LocalSearchAlgorithm localSearchAlgorithm;
    private int iterationOfLastImprovement;
    private final int numberOfNonImprovingIterations;

    /**
     * Constructor
     *
     * @param initialSolution                - initial solution to be improved
     * @param timeLimit                      - time limit for the improvement procedure
     * @param numberOfNonImprovingIterations - number of non improving iterations before termination
     * @param localSearchAlgorithm           - neighborhood structure to be used in the local search
     */
    public LocalSearch(
            Solution initialSolution, double timeLimit, int numberOfNonImprovingIterations, LocalSearchAlgorithm localSearchAlgorithm
    ) {
        this.currSol = new Solution(initialSolution);
        this.bestSol = new Solution(initialSolution);
        this.iterationOfLastImprovement = 0;
        this.numberOfNonImprovingIterations = numberOfNonImprovingIterations;
        this.startTime = System.currentTimeMillis();
        this.timeLimit = 0;
        this.timeLimit = timeLimit;
        this.localSearchAlgorithm = localSearchAlgorithm;
    }

    /**
     * Improves a given solution to a stacking problem using a local search.
     *
     * @return best solution generated in the local search procedure
     */
    public Solution solve() {
        this.startTime = System.currentTimeMillis();
        this.solveIterationsSinceLastImprovement(this.localSearchAlgorithm);
        return this.bestSol;
    }

    /**
     * Retrieves a neighboring solution by applying the operators of the specified neighborhood.
     *
     * @param localSearchAlgorithm - neighborhood structure used to generate neighboring solutions
     * @return neighboring solution
     */
    private Solution getNeighbor(LocalSearchAlgorithm localSearchAlgorithm) {
        Solution sol = localSearchAlgorithm.getNeighbor(this.currSol, this.bestSol);
        System.out.println("curr: " + sol.getMakespan());
        return sol == null ? this.currSol : sol;
    }

    /**
     * Updates the current solution with the best neighbor.
     * Additionally, the best solution gets updated if a new best solution is found.
     *
     * @param iteration    - current iteration
     * @param localSearchAlgorithm - neighborhood structure used to generate neighboring solutions
     */
    private void updateCurrentSolution(int iteration, LocalSearchAlgorithm localSearchAlgorithm) {
        this.currSol = this.getNeighbor(localSearchAlgorithm);
        if (this.currSol.getMakespan() < this.bestSol.getMakespan()) {
            System.out.println("IMPROVEMENT!!!");
            System.exit(0);
            this.bestSol = this.currSol;
            this.iterationOfLastImprovement = iteration;
        }
    }

    /**
     * Performs the local search with a number of non-improving iterations as stop criterion.
     *
     * @param localSearchAlgorithm - neighborhood structure used to generate neighboring solutions
     */
    private void solveIterationsSinceLastImprovement(LocalSearchAlgorithm localSearchAlgorithm) {
        int iteration = 0;
        while (Math.abs(this.iterationOfLastImprovement - iteration) < this.numberOfNonImprovingIterations) {
//            System.out.println(this.bestSol.getMakespan());
            System.out.println("non improving iterations: " + Math.abs(this.iterationOfLastImprovement - iteration));
            if (this.timeLimit != 0 && (System.currentTimeMillis() - this.startTime) / 1000 > this.timeLimit) { break; }
            this.updateCurrentSolution(iteration++, localSearchAlgorithm);
        }
    }
}
