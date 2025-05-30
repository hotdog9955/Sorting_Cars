import java.util.Random;

public class CarBuffer {
    public final int size;
    public final int[] destIndex;
    public final byte[] colorIndex;
    public final char[][] serials;
    public final long[] recIds;

    // Destination lookup
    private static final String[] DEST_NAMES =
            {"Los Angeles","Houston","New Orleans","Miami","New York"};
    public static int destToIndex(String d) {
        for (int i = 0; i < DEST_NAMES.length; i++)
            if (DEST_NAMES[i].equals(d)) return i;
        throw new IllegalArgumentException(d);
    }
    public static String indexToDest(int i) { return DEST_NAMES[i]; }

    // Color lookup by ordinal (fits in byte)
    public enum Color { RED, BLUE, BLACK, WHITE }

    public CarBuffer(int n) {
        this.size = n;
        this.destIndex = new int[n];
        this.colorIndex = new byte[n];
        this.serials = new char[n][12];
        this.recIds = new long[n];
    }

//    public void set(int i, long recId, String serial, Color color, String dest) {
//        recIds[i] = recId;
//        serial.getChars(0, 12, serials[i], 0);
//        colorIndex[i] = (byte) color.ordinal();
//        destIndex[i] = destToIndex(dest);
//    }

    // Compare two positions by dest, color, then serial memcmp
    public int compare(int i, int j) {
        int d = Integer.compare(destIndex[i], destIndex[j]);
        if (d != 0) return d;
        int c = Byte.compare(colorIndex[i], colorIndex[j]);
        if (c != 0) return c;
        // memcmp of 12 chars
        char[] a = serials[i], b = serials[j];
        for (int k = 0; k < 12; k++) {
            int diff = a[k] - b[k];
            if (diff != 0) return diff;
        }
        return 0;
    }

    // Swap all fields between i and j
    public void swap(int i, int j) {
        long tId = recIds[i]; recIds[i] = recIds[j]; recIds[j] = tId;
        int td = destIndex[i]; destIndex[i] = destIndex[j]; destIndex[j] = td;
        byte tc = colorIndex[i]; colorIndex[i] = colorIndex[j]; colorIndex[j] = tc;
        char[] sa = serials[i]; serials[i] = serials[j]; serials[j] = sa;
    }

    // Generate random buffer
    public static CarBuffer randomBuffer(int n) {
        CarBuffer buf = new CarBuffer(n);
        Random rnd = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < n; i++) {
            buf.recIds[i] = i + 1;
            for (int k = 0; k < 12; k++)
                buf.serials[i][k] = chars.charAt(rnd.nextInt(chars.length()));
            buf.colorIndex[i] = (byte) rnd.nextInt(Color.values().length);
            buf.destIndex[i] = rnd.nextInt(DEST_NAMES.length);
        }
        return buf;
    }

    public int compareKey(int i, int kDest, byte kColor, char[] kSerial) {
        int d = Integer.compare(destIndex[i], kDest);
        if (d != 0) return d;
        int c = Byte.compare(colorIndex[i], kColor);
        if (c != 0) return c;
        char[] s = serials[i];
        for (int x = 0; x < 12; x++) {
            int diff = s[x] - kSerial[x]; if (diff != 0) return diff;
        }
        return 0;
    }

    public void writeToFile(String filename) throws java.io.IOException {
        try (java.io.BufferedWriter w =
                     new java.io.BufferedWriter(new java.io.FileWriter(filename))) {
            for (int i = 0; i < size; i++) {
                w.write(indexToDest(destIndex[i]));
                w.write('\t');
                w.write(Color.values()[colorIndex[i]].name());
                w.write('\t');
                w.write(serials[i]);
                w.write('\t');
                w.write(Long.toString(recIds[i]));
                w.newLine();
            }
        }
    }
}

