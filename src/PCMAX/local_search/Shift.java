package PCMAX.local_search;

public class Shift {

    private final int jobTime;
    private final int machine;

    public Shift(int jobTime, int machine) {
        this.jobTime = jobTime;
        this.machine = machine;
    }

    private int getJobTime() {
        return this.jobTime;
    }

    private int getMachine() {
        return this.machine;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Shift && (this.jobTime == ((Shift) object).getJobTime() && this.machine == ((Shift) object).getMachine());
    }

    /**
     * Returns the string representing the shift object.
     *
     * @return string representation of shift
     */
    @Override
    public String toString() {
        return this.jobTime + " --- " + this.machine;
    }
}
