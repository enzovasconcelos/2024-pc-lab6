import java.util.Random;
import java.util.Date;

public class Task {
    long id;
    Date start;
    Date end;
    TaskProducer producer;

    public Task(long id, TaskProducer producer) {
        this.id = id;
        this.producer = producer;
        this.start = new Date();
    }

    public void execute() {
        try {
            // generating a number between 1000 and 15000
            long execDuration = 1000 + (long) (new Random().nextFloat() * (15000 - 1000));
            Thread.sleep(execDuration);
            end = new Date();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getTimeExecutation() {
        return end.getTime() - start.getTime();
    }
}
