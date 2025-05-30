import java.util.concurrent.atomic.AtomicInteger;

public class SortJob {
    private final CarBuffer buf;
    private final int lo, hi;
    private final AtomicInteger jobs;

    public SortJob(CarBuffer buf, int lo, int hi, AtomicInteger jobs) {
        this.buf = buf; this.lo = lo; this.hi = hi; this.jobs = jobs;
    }

    public void run(QuicksortEngine eng) {
        if (lo < hi) {
            int p = partition();
            jobs.incrementAndGet(); eng.submit(new SortJob(buf, lo, p - 1, jobs));
            jobs.incrementAndGet(); eng.submit(new SortJob(buf, p + 1, hi, jobs));
        }
        if (jobs.decrementAndGet() == 0) {
            synchronized (jobs) { jobs.notify(); }
        }
    }

    private int partition() {
        // median-of-three pivot for predictability
        int mid = (lo + hi) >>> 1;
        if (buf.compare(mid, hi) > 0) buf.swap(mid, hi);
        if (buf.compare(lo, mid) > 0) buf.swap(lo, mid);
        if (buf.compare(mid, hi) > 0) buf.swap(mid, hi);
        buf.swap(mid, hi - 1);
        char[] pivotSerial = buf.serials[hi - 1];
        int pivotDest = buf.destIndex[hi - 1];
        byte pivotColor = buf.colorIndex[hi - 1];
        int i = lo, j = hi - 1;
        while (true) {
            while (i < hi && buf.compareKey(i, pivotDest, pivotColor, pivotSerial) < 0) i++;
            while (j > lo && buf.compareKey(j, pivotDest, pivotColor, pivotSerial) > 0) j--;
            if (i >= j) break;
            buf.swap(i++, j--);
        }
        buf.swap(i, hi - 1);
        return i;
    }
}
