package PCMAX.local_search;

import PCMAX.Machine;

public class Swap {

    private final Machine machineOne;
    private final Machine machineTwo;
    private final int jobOne;
    private final int jobTwo;

    public Swap(Machine machineOne, Machine machineTwo, int jobOne, int jobTwo) {
        this.machineOne = machineOne;
        this.machineTwo = machineTwo;
        this.jobOne = jobOne;
        this.jobTwo = jobTwo;
    }

    private Machine getMachineOne() {
        return this.machineOne;
    }

    private Machine getMachineTwo() {
        return this.machineTwo;
    }

    private int getJobOne() {
        return this.jobOne;
    }

    private int getJobTwo() {
        return this.jobTwo;
    }

    @Override
    public String toString() {
        return this.machineOne.getId() + " --- " + this.machineTwo.getId();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Swap)) { return false; }
        if (other == this) { return true; }
        return
                (
                    this.machineOne.equals(((Swap)other).getMachineOne()) && this.machineTwo.equals(((Swap)other).getMachineTwo())
                    && (this.jobOne == ((Swap) other).getJobOne() && this.jobTwo == ((Swap) other).getJobTwo()
                    || this.jobOne == ((Swap) other).getJobTwo()) && this.jobTwo == ((Swap) other).getJobOne()
                )
            ||
                (
                    this.machineOne.equals(((Swap)other).getMachineTwo()) && this.machineTwo.equals(((Swap)other).getMachineOne())
                    && (this.jobOne == ((Swap) other).getJobOne() && this.jobTwo == ((Swap) other).getJobTwo()
                    || this.jobOne == ((Swap) other).getJobTwo()) && this.jobTwo == ((Swap) other).getJobOne()
                );
    }
}
