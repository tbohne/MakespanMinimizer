package PCMAX;

import java.util.*;

public class PartitionHeuristic {

    private final Instance instance;

    public PartitionHeuristic(Instance instance) {
        this.instance = instance;
    }

    public Solution solve() {
        Solution initialSol = LPTSolver.solve(this.instance);
        Solution partSol = determiningPartialSolutions();
        return initialSol.getMakespan() < partSol.getMakespan() ? initialSol : partSol;
    }

    private Solution determiningPartialSolutions() {

        List<Integer> jobs = this.instance.getProcessingTimes();
        Collections.sort(jobs);
        // order jobs non-increasingly
        Collections.reverse(jobs);

        // partial solution index
        int p = 0;
        PartialSolutionContainer sol = new PartialSolutionContainer(this.instance.getNumOfMachines());
        // get partial solution p (0) and add job 0 to its first machine
        sol.getPartialSolutions().get(p).getMachineAllocations().get(0).getJobs().add(jobs.get(0));

        assert sol.getPartialSolutions().get(p).computeGap() == jobs.get(0);

        // construction
        for (int job = 1; job < jobs.size(); job++) {

            // compute partial solution with biggest gap
            PartialSolution maxGapPartSol = Collections.max(sol.getPartialSolutions());

            if (jobs.get(job) <= maxGapPartSol.computeGap()) {
                // the total processing time needed for the m-th machine to perform all its jobs is updated (the job is added to it)
                maxGapPartSol.getMachineAllocations().get(maxGapPartSol.getMachineAllocations().size() - 1).getJobs().add(jobs.get(job));
                // sort the elements in non-increasing order
                Collections.sort(maxGapPartSol.getMachineAllocations());
                Collections.reverse(maxGapPartSol.getMachineAllocations());

                //////////////////////////// DBG ///////////////////////////////////////
                int expectedDelta = (maxGapPartSol.getMachineAllocations().get(0).getProcessingTime()
                    - maxGapPartSol.getMachineAllocations().get(maxGapPartSol.getMachineAllocations().size() - 1).getProcessingTime());
                int actualDelta = maxGapPartSol.computeGap();
                assert expectedDelta == actualDelta;
                /////////////////////////////////////////////////////////////////////////
            } else {
                p += 1;
                // DBG
                if (p >= sol.getPartialSolutions().size()) {
                    sol.getPartialSolutions().add(new PartialSolution(this.instance.getNumOfMachines()));
                }
                sol.getPartialSolutions().get(p).getMachineAllocations().get(0).getJobs().add(jobs.get(job));
            }
        }
        return sumPartialSolutions(sol);
    }

    private Solution sumPartialSolutions(PartialSolutionContainer sol) {

        // iterate over all partial solutions
        while (sol.getPartialSolutions().size() > 1) {

            // select two ordered partial solutions with biggest gaps
            Collections.sort(sol.getPartialSolutions());
            Collections.reverse(sol.getPartialSolutions());
            // the two partial solutions with highest gaps
            PartialSolution p1 = sol.getPartialSolutions().get(0);
            PartialSolution p2 = sol.getPartialSolutions().get(1);

            PartialSolution newPartial = new PartialSolution(this.instance.getNumOfMachines());

            for (int machineIdx = 0; machineIdx < p1.getMachineAllocations().size(); machineIdx++) {
                List<Integer> jobs = new ArrayList<>();
                jobs.addAll(p1.getMachineAllocations().get(machineIdx).getJobs());
                jobs.addAll(p2.getMachineAllocations().get(this.instance.getNumOfMachines() - 1 - machineIdx).getJobs());
                for (int job : jobs) {
                    newPartial.getMachineAllocations().get(machineIdx).getJobs().add(job);
                }
            }
            sol.getPartialSolutions().remove(p1);
            sol.getPartialSolutions().remove(p2);
            sol.getPartialSolutions().add(newPartial);
        }

        Solution finalSol = new Solution(this.instance);
        finalSol.setMachineAllocations(sol.getPartialSolutions().get(0).getMachineAllocations());

        return finalSol;
    }
}
