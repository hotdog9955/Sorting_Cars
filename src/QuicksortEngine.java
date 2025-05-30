import java.util.*;

public class QuicksortEngine {
    private final int maxThreads;
    private final Queue<SortJob> jobQueue = new LinkedList<>();
    private int activeThreads = 0;
    private int jobsInProgress = 0;
    private boolean shutdown = false;

    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public synchronized void submitJob(SortJob job) {
        jobQueue.offer(job);
        jobsInProgress++;
        notifyAll();
        if (activeThreads < maxThreads) {
            WorkerThread wt = new WorkerThread(this);
            wt.start();
            activeThreads++;
        }
    }

    public synchronized SortJob fetchJob() {
        while (jobQueue.isEmpty() && !shutdown) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        return jobQueue.poll();
    }

    public synchronized void jobFinished() {
        jobsInProgress--;
        if (jobsInProgress == 0 && jobQueue.isEmpty()) {
            notifyAll();
        }
    }

    public synchronized void waitUntilFinished() {
        while (jobsInProgress > 0 || !jobQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
    }

    public synchronized void shutdown() {
        shutdown = true;
        notifyAll();
    }
}
