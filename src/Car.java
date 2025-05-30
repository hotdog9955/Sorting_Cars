import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Car implements Comparable<Car>, Serializable {
    private long recId;
    private String serial;
    private Color color;
    private String destination;

    public enum Color { RED, BLUE, BLACK, WHITE }

    private static final List<String> DEST_ORDER = Arrays.asList(
            "Los Angeles", "Houston", "New Orleans", "Miami", "New York"
    );

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

    private int getDestinationOrder() {
        return DEST_ORDER.indexOf(destination);
    }

    @Override
    public int compareTo(Car other) {
        int destComp = Integer.compare(this.getDestinationOrder(), other.getDestinationOrder());
        if (destComp != 0) return destComp;
        int colorComp = Integer.compare(this.color.ordinal(), other.color.ordinal());
        if (colorComp != 0) return colorComp;
        return this.serial.compareTo(other.serial);
    }
}