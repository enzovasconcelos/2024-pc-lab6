import java.util.concurrent.BlockingQueue;

public class TaskProducer implements Runnable {

    BlockingQueue<Task> buffer;
    int priority;

    public TaskProducer(BlockingQueue<Task> buffer, int priority) {
        this.buffer = buffer;
        this.priority = priority;
    }

    @Override
    public void run() {
        try {
            long id = Scenario2.getNextId();
            buffer.put(new Task(id, this, priority));
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}