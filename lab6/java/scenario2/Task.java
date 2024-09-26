import java.util.Random;
import java.util.Date;

public class Task implements Comparable {
    long id;
    Date start;
    Date end;
    TaskProducer producer;
    int priority;

    public Task(long id, TaskProducer producer, int priority) {
        this.id = id;
        this.producer = producer;
        this.start = new Date();
        this.priority = priority;
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

    @Override
    public int compareTo(Object task2) {
        if (!task2.getClass().equals(this.getClass()))
            return Integer.MAX_VALUE;

        return this.priority - (((Task) task2).priority);
    }
}
