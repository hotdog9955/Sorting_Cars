import java.util.List;

public class SortJob {
    private final List<Car> list;
    private final int left;
    private final int right;

    public SortJob(List<Car> list, int left, int right) {
        this.list = list;
        this.left = left;
        this.right = right;
    }

    public List<Car> getList() { return list; }
    public int getLeft() { return left; }
    public int getRight() { return right; }
}
