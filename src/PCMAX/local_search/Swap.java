package PCMAX.local_search;

import PCMAX.Machine;

/**
 * Represents a swap operation (4-tuple).
 */
public class Swap {

    private final Machine machineOne;
    private final Machine machineTwo;
    private final int jobOne;
    private final int jobTwo;

    /**
     * Constructor
     *
     * @param machineOne - first machine involved in the swap
     * @param machineTwo - second machine involved in the swap
     * @param jobOne     - first exchanged job
     * @param jobTwo     - second exchanged job
     */
    public Swap(Machine machineOne, Machine machineTwo, int jobOne, int jobTwo) {
        this.machineOne = machineOne;
        this.machineTwo = machineTwo;
        this.jobOne = jobOne;
        this.jobTwo = jobTwo;
    }

    /**
     * Returns the first machine.
     *
     * @return first machine
     */
    private Machine getMachineOne() {
        return this.machineOne;
    }

    /**
     * Returns the second machine.
     *
     * @return second machine
     */
    private Machine getMachineTwo() {
        return this.machineTwo;
    }

    /**
     * Returns the first job.
     *
     * @return first job
     */
    private int getJobOne() {
        return this.jobOne;
    }

    /**
     * Returns the second job.
     *
     * @return second job
     */
    private int getJobTwo() {
        return this.jobTwo;
    }

    /**
     * Creates a string representation of the swap operation.
     *
     * @return string representation of swap
     */
    @Override
    public String toString() {
        return "(" + this.machineOne + ", " + this.machineTwo + ", " + this.jobOne + ", " + this.jobTwo + ")";
    }

    /**
     * Equality check for swap operations.
     *
     * @param other - swap operation to be compared to
     * @return whether the compared swap operations are equal
     */
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
