import javax.xml.ws.soap.MTOM;
import java.util.ArrayList;
import java.util.List;

public class Machine implements Comparable<Machine> {

    private List<Integer> jobs;

    public Machine() {
        this.jobs = new ArrayList<>();
    }

    public void setJob(int processingTime) {
        this.jobs.add(processingTime);
    }

    public List<Integer> getJobs() {
        return this.jobs;
    }

    public int getProcessingTime() {
        int sum = 0;
        for (int job : this.jobs) {
            sum += job;
        }
        return sum;
    }

    @Override
    public int compareTo(Machine other) {
        return this.getProcessingTime() - other.getProcessingTime();
    }

    @Override
    public String toString() {
        String str = "";
        for (int job : this.jobs) {
            str += job + " ";
        }
        return str;
    }
}
