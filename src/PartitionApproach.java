import java.util.*;

public class PartitionApproach {

    public static Solution solve(Instance instance) {
        return determiningPartialSolutions(instance);
    }

    private static Solution determiningPartialSolutions(Instance instance) {

        List<Integer> jobs = instance.getProcessingTimes();
        Collections.sort(jobs);
        // order jobs non-increasingly
        Collections.reverse(jobs);

        // partial solution index
        int p = 0;
        SolutionNewApproach sol = new SolutionNewApproach(instance.getNumOfMachines());

        // get partial solution p (0) and add job 0 to its first machine
        sol.getPartialSolutions().get(p).getMachineAllocations().get(0).getJobs().add(jobs.get(0));

        // DBG
        assert sol.getPartialSolutions().get(p).computeGap() == jobs.get(0);

//        System.out.println("gap: " + sol.getPartialSolutions().get(p).computeGap());

        // construction
        for (int job = 1; job < jobs.size(); job++) {

            // compute partial solution with biggest gap
            PartialSolution maxGapPartSol = Collections.max(sol.getPartialSolutions());

//            System.out.println("gap: " + maxGapPartSol.computeGap());

            if (jobs.get(job) <= maxGapPartSol.computeGap()) {

//                System.out.println("IF");

                // the total processing time needed for the m-th machine to perform all its jobs
                // is updated (the job is added to it)
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

            // PROBLEM: HERE WE CAN GET DUPLICATES !!!!!

//            if (jobs.get(job) > maxGapPartSol.computeGap()) {
//                System.out.println("ELSE");

                p += 1;
                // new partial solution

                // DBG
                if (p >= sol.getPartialSolutions().size()) {
                    sol.getPartialSolutions().add(new PartialSolution(instance.getNumOfMachines()));
                }

                sol.getPartialSolutions().get(p).getMachineAllocations().get(0).getJobs().add(jobs.get(job));
            }
        }

//        for (PartialSolution ps : sol.getPartialSolutions()) {
//            System.out.println("###################################");
//            System.out.println("---- PARTIAL SOLUTION: " + sol.getPartialSolutions().indexOf(ps));
//            System.out.println(ps);
//            System.out.println("###################################");
//        }
        return sumPartialSolutions(sol, p, instance);
    }

    public static Solution sumPartialSolutions(SolutionNewApproach sol, int p, Instance instance) {

        // construction

        // iterate over all partial solutions
        while (sol.getPartialSolutions().size() > 1) {

            // select two ordered partial solutions with biggest gaps
            Collections.sort(sol.getPartialSolutions());
            // TODO: check whether we need to reverse here
            Collections.reverse(sol.getPartialSolutions());

            // the two partial solutions with highest gaps
            PartialSolution p1 = sol.getPartialSolutions().get(0);
            PartialSolution p2 = sol.getPartialSolutions().get(1);

            PartialSolution newPartial = new PartialSolution(instance.getNumOfMachines());

            for (int machineIdx = 0; machineIdx < p1.getMachineAllocations().size(); machineIdx++) {
                List<Integer> jobs = new ArrayList<>();
                for (int job : p1.getMachineAllocations().get(machineIdx).getJobs()) {
                    jobs.add(job);
                }
                for (int job : p2.getMachineAllocations().get(instance.getNumOfMachines() - 1 - machineIdx).getJobs()) {
                    jobs.add(job);
                }
                for (int job : jobs) {
                    newPartial.getMachineAllocations().get(machineIdx).getJobs().add(job);
                }
            }
            sol.getPartialSolutions().remove(sol.getPartialSolutions().indexOf(p1));
            sol.getPartialSolutions().remove(sol.getPartialSolutions().indexOf(p2));
            sol.getPartialSolutions().add(newPartial);
        }

        List<Integer> pTimes = new ArrayList<>();
        for (Machine m : sol.getPartialSolutions().get(0).getMachineAllocations()) {
            pTimes.add(m.getProcessingTime());
        }
//        System.out.println("time: " + Collections.max(pTimes));

        Solution finalSol = new Solution(instance);
        finalSol.setMachineAllocations(sol.getPartialSolutions().get(0).getMachineAllocations());

        return finalSol;
    }
}
