import java.util.*;

public class TabuSearch implements LocalSearchAlgorithm {

    private int numberOfNeighbors;
    private HeuristicSolver.ShortTermStrategies shortTermStrategy;
    private int unsuccessfulNeighborGenerationAttempts;
    private int maxTabuListLength;
    private int tabuListClears;
    private Queue<Shift> tabuList;

    // nbh operators
    private SwapOperator swapOperator;

    public TabuSearch(
        int numberOfNeighbors, HeuristicSolver.ShortTermStrategies shortTermStrategy,
        int maxTabuListLength, int unsuccessfulNeighborGenerationAttempts, SwapOperator swapOperator
    ) {
        this.numberOfNeighbors = numberOfNeighbors;
        this.shortTermStrategy = shortTermStrategy;
        this.maxTabuListLength = maxTabuListLength;
        this.unsuccessfulNeighborGenerationAttempts = unsuccessfulNeighborGenerationAttempts;
        this.tabuListClears = 0;
        this.unsuccessfulNeighborGenerationAttempts = unsuccessfulNeighborGenerationAttempts;
        this.tabuList = new LinkedList<>();
        this.swapOperator = swapOperator;
    }

    /**
     * Clears the entries in the shift tabu list and increments the clear counter.
     */
    public void clearTabuList() {
        this.tabuList.clear();
        this.tabuListClears++;
    }

    public int getTabuListClears() {
        return this.tabuListClears;
    }

    public boolean tabuListContainsAnyOfTheShifts(List<Shift> performedShifts) {
        for (Shift shift : performedShifts) {
            if (this.tabuList.contains(shift)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the specified shift operation to the tabu list.
     * Replaces the oldest entry if the maximum length of the tabu list is reached.
     *
     */
    public void forbidShifts(List<Shift> performedShifts) {
        if (this.tabuList.size() >= this.maxTabuListLength) {
            while (this.tabuList.size() + performedShifts.size() >= this.maxTabuListLength) {
                this.tabuList.poll();
            }
        }
        for (Shift shift : performedShifts) {
            this.tabuList.add(shift);
        }
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
        int failCnt = 0;
        Map<Solution, List<Shift>> shiftsForSolution = new HashMap<>();

        System.out.println("TL: " + this.tabuList.size());

        while (nbrs.size() < this.numberOfNeighbors) {

            List<Shift> performedShifts = new ArrayList<>();
            performedShifts.clear();
            Solution neighbor = this.swapOperator.generateSwapNeighbor(currSol, performedShifts);

//            System.out.println("TL: " + this.tabuList);
//            System.out.println("shift: " + performedShifts);

            if (!neighbor.isFeasible()) { continue; }
            shiftsForSolution.put(neighbor, performedShifts);

            // FIRST-FIT
            if (this.shortTermStrategy == HeuristicSolver.ShortTermStrategies.FIRST_FIT
                && !this.tabuListContainsAnyOfTheShifts(performedShifts)
                && neighbor.getMakespan() < currSol.getMakespan()
            ) {
                this.forbidShifts(performedShifts);
//                System.out.println("FIRST-FIT RETURN");
                return neighbor;
            // BEST-FIT
            } else if (!this.tabuListContainsAnyOfTheShifts(performedShifts)) {
                nbrs.add(neighbor);
            } else {
//                System.out.println("TABU");
                // TABU
                // ASPIRATION CRITERION
                if (neighbor.getMakespan() < bestSol.getMakespan()) {
//                    System.out.println("ASPIRATION!");
                    if (this.shortTermStrategy == HeuristicSolver.ShortTermStrategies.FIRST_FIT) {
                        return neighbor;
                    } else {
                        nbrs.add(neighbor);
                    }
                } else {
                    failCnt++;
                    if (failCnt == this.unsuccessfulNeighborGenerationAttempts) {
                        failCnt = 0;

                        if (nbrs.size() == 0) {
//                            System.out.println("CLEARING TL");
                            this.clearTabuList();
                        } else {
                            System.out.println("FAIL RETURN");
                            return this.getBestSolution(nbrs);
                        }
                    }
                }
            }
        }
//        System.out.println("BEST-FIT RETURN");
        Solution best = this.getBestSolution(nbrs);
        this.forbidShifts(shiftsForSolution.get(best));
        return best;
    }
}
