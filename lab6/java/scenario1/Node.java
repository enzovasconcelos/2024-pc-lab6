import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node implements Runnable {

    BlockingQueue<Task> buffer;
    ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>> results;

    public Node(BlockingQueue<Task> buffer, ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>> results) {
      this.buffer = buffer;
      this.results = results;
    }

    @Override
    public void run() {
      try {
        for(;;) {
          Task task = buffer.take();
          task.execute();
          CopyOnWriteArrayList<Task> tasks = results.get(task.producer);
          if (tasks == null) {
            tasks = new CopyOnWriteArrayList<Task>();
          }
          tasks.add(task);
          results.put(task.producer, tasks);
        }
      } catch(InterruptedException e) {
        System.err.println(e);
      }
    }
}
