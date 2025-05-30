import java.util.Collections;
import java.util.List;

public class WorkerThread extends Thread {
    private final QuicksortEngine engine;

    public WorkerThread(QuicksortEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        while (true) {
            SortJob job = engine.fetchJob();
            if (job == null) break;
            quicksort(job.getList(), job.getLeft(), job.getRight());
            engine.jobFinished();
        }
    }

    private void quicksort(List<Car> list, int low, int high) {
        if (low < high) {
            int pivot = partition(list, low, high);
            engine.submitJob(new SortJob(list, low, pivot - 1));
            engine.submitJob(new SortJob(list, pivot + 1, high));
        }
    }

    private int partition(List<Car> list, int low, int high) {
        Car pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}
