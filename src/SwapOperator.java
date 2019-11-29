import java.util.List;
import java.util.Random;

public class ShiftOperator {

    private static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Solution generateShiftNeighbor(Solution currSol, List<Shift> performedShifts) {

        // select random machine m
        // select random job j assigned to m
        // select random machine z from M \ m
        // assign j to z

        int machineToShiftJobFrom = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
//        System.out.println("machine to shift job from: " + machineToShiftJobFrom);
        // machine has no jobs
        if (currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().size() == 0) { return currSol; }
        int randomJobIdx = getRandomNumberInRange(0, currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().size() - 1);
        int job = currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().get(randomJobIdx);
//        System.out.println("job to be shifted: " + job);
        currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().remove(randomJobIdx);

        int targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        while (targetMachine == machineToShiftJobFrom) {
            targetMachine = getRandomNumberInRange(0, currSol.getMachineAllocations().size() - 1);
        }

        Shift shiftOne = new Shift(job, targetMachine);
//        System.out.println("target machine: " + targetMachine);

        currSol.getMachineAllocations().get(targetMachine).getJobs().add(job);

        if (currSol.getMachineAllocations().get(targetMachine).getJobs().size() == 0) { return currSol; }
        int randomJobIdx2 = getRandomNumberInRange(0, currSol.getMachineAllocations().get(targetMachine).getJobs().size() - 1);
        int job2 = currSol.getMachineAllocations().get(targetMachine).getJobs().get(randomJobIdx2);
//        System.out.println("job to be shifted: " + job);
        currSol.getMachineAllocations().get(targetMachine).getJobs().remove(randomJobIdx2);

        currSol.getMachineAllocations().get(machineToShiftJobFrom).getJobs().add(job2);

        Shift shiftTwo = new Shift(job2, machineToShiftJobFrom);

        performedShifts.add(shiftOne);
        performedShifts.add(shiftTwo);

//        System.out.println("#########################");
//        System.out.println(currSol);
//        System.out.println("#########################");

        return currSol;
    }
}
