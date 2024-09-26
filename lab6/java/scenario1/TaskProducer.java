import java.util.concurrent.BlockingQueue;

public class TaskProducer implements Runnable {

    BlockingQueue<Task> buffer;

    public TaskProducer(BlockingQueue<Task> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            long id = Scenario1.getNextId();
            buffer.put(new Task(id, this));
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}