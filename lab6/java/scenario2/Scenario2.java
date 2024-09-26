import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Scenario2 {

    static AtomicLong lastId = new AtomicLong(-1);
    static int numberOfNodes = 3;
    static int numberOfProducers = 3;

    public static void main(String[] args) {
        BlockingQueue<Task> buffer = new PriorityBlockingQueue<Task>();
        ScheduledExecutorService producers = Executors.newScheduledThreadPool(numberOfProducers);
        ExecutorService nodes = Executors.newFixedThreadPool(numberOfNodes);
        ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>> results = new ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>>();
        createProducers(producers, buffer);
        createNodes(numberOfNodes, nodes, buffer, results);
        ScheduledExecutorService printResultTask = Executors.newSingleThreadScheduledExecutor();
        createPrintResult(printResultTask, results);
        System.out.println("Scenario 2 of Lab06!");
        for (;;) {}
    }

    public static Long getNextId() {
        return lastId.incrementAndGet();
    }

    private static void createProducers(ScheduledExecutorService producers, 
                                        BlockingQueue<Task> buffer) {
        
        producers.scheduleAtFixedRate(new TaskProducer(buffer, 0), 0, 13, TimeUnit.SECONDS);
        producers.scheduleAtFixedRate(new TaskProducer(buffer, 1), 0, 7, TimeUnit.SECONDS);
        producers.scheduleAtFixedRate(new TaskProducer(buffer, 2), 0, 3, TimeUnit.SECONDS);
        
    }

    private static void createNodes(int numberOfNodes, ExecutorService nodes, 
                                    BlockingQueue<Task> buffer, 
                                    ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>> results) {
        for (int i = 0; i < numberOfNodes; i++) {
            nodes.submit(new Node(buffer, results));
        }
    }

    private static void createPrintResult(ScheduledExecutorService executorService, 
                                          ConcurrentHashMap<TaskProducer, CopyOnWriteArrayList<Task>> results) {
        executorService.scheduleWithFixedDelay(() -> {
            Enumeration<TaskProducer> keys = results.keys();
            String out = "Resultados: \n";
            while(keys.hasMoreElements()) {
                TaskProducer producer = keys.nextElement();
                CopyOnWriteArrayList<Task> tasks = results.get(producer);
                out += producer.toString() + ": \n";
                long sum = 0;
                for (Task task : tasks) {
                    sum += task.getTimeExecutation();
                    out += "\ttask " + task.id + ", priority: " + task.priority + " : " + task.getTimeExecutation() + "\n";
                }
                out += "\tmedia tempo task: " + (sum / tasks.size()) + "\n";
                System.out.println(out);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
