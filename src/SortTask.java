import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SortTask {
    private AtomicInteger jobCount = new AtomicInteger(1);
    private CountDownLatch latch = new CountDownLatch(1);
    private long startTime, endTime;
    private java.util.List<Car> list;

    public SortTask(java.util.List<Car> list) {
        this.list = list;
    }

    void start() {
        startTime = System.currentTimeMillis();
    }

    public void complete() {
        endTime = System.currentTimeMillis();
        latch.countDown();
    }

    public void incrementJobs(int count) {
        jobCount.addAndGet(count);
    }

    public int decrementJobs() {
        return jobCount.decrementAndGet();
    }

    public long awaitCompletion() throws InterruptedException {
        latch.await();
        return endTime - startTime;
    }

    public java.util.List<Car> getList() {
        return list;
    }
}