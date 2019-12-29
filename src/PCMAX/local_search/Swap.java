package PCMAX.local_search;

import PCMAX.Machine;

public class Swap {

    private Machine machineOne;
    private Machine machineTwo;

    public Swap(Machine machineOne, Machine machineTwo) {
        this.machineOne = machineOne;
        this.machineTwo = machineTwo;
    }

    public Machine getMachineOne() {
        return this.machineOne;
    }

    public Machine getMachineTwo() {
        return this.machineTwo;
    }

    @Override
    public String toString() {
        return this.machineOne.getId() + " --- " + this.machineTwo.getId();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Swap)) { return false; }
        if (other == this) { return true; }
        return this.machineOne.equals(((Swap)other).getMachineOne()) && this.machineTwo.equals(((Swap)other).getMachineTwo())
            || this.machineOne.equals(((Swap)other).getMachineTwo()) && this.machineTwo.equals(((Swap)other).getMachineOne());
    }
}
