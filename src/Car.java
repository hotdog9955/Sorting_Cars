import java.util.*;

public class Car implements Comparable<Car> {
    public enum Color { RED, BLUE, BLACK, WHITE }

    private final long recId;
    private final String serial;
    private final Color color;
    private final String destination;

    public Car(long recId, String serial, Color color, String destination) {
        this.recId = recId;
        this.serial = serial;
        this.color = color;
        this.destination = destination;
    }

    public long getRecId() { return recId; }
    public String getSerial() { return serial; }
    public Color getColor() { return color; }
    public String getDestination() { return destination; }

    private static final List<String> DEST_ORDER = Arrays.asList("Los Angeles", "Houston", "New Orleans", "Miami", "New York");
    private static final List<Color> COLOR_ORDER = Arrays.asList(Color.RED, Color.BLUE, Color.BLACK, Color.WHITE);

    @Override
    public int compareTo(Car other) {
        int cmp = Integer.compare(DEST_ORDER.indexOf(this.destination), DEST_ORDER.indexOf(other.destination));
        if (cmp != 0) return cmp;
        cmp = Integer.compare(COLOR_ORDER.indexOf(this.color), COLOR_ORDER.indexOf(other.color));
        if (cmp != 0) return cmp;
        return this.serial.compareTo(other.serial);
    }

    @Override
    public String toString() {
        return destination + "\t" + color + "\t" + serial + "\t" + recId;
    }
}
