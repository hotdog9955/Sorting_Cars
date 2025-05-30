import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuicksortEngine {
    private final AtomicInteger active = new AtomicInteger(0);
    private final LinkedList<SortJob> queue = new LinkedList<>();
    private volatile boolean shutdown = false;
    private final List<Thread> workers = new java.util.ArrayList<>();

    public QuicksortEngine(int maxThreads) {
        for (int i = 0; i < maxThreads; i++) {
            Thread t = new Thread(this::workerLoop);
            t.start(); workers.add(t);
        }
    }

    public void sort(CarBuffer buf) throws InterruptedException {
        AtomicInteger jobs = new AtomicInteger(1);
        submit(new SortJob(buf, 0, buf.size - 1, jobs));
        // wait until all jobs done
        synchronized (jobs) {
            while (jobs.get() > 0) jobs.wait();
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown = true;
        synchronized (queue) { queue.notifyAll(); }
        for (Thread t : workers) t.join();
    }

    public void submit(SortJob job) {
        synchronized (queue) {
            queue.addLast(job);
            queue.notify();
        }
    }

    private void workerLoop() {
        while (true) {
            SortJob job;
            synchronized (queue) {
                while (queue.isEmpty() && !shutdown) try { queue.wait(); } catch (InterruptedException ignored) {}
                if (shutdown && queue.isEmpty()) break;
                job = queue.removeFirst();
            }
            job.run(this);
        }
    }
}