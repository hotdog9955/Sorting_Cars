import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SerialGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // Thread-safe set of issued serials
    private static final Set<String> usedSerials =
            Collections.synchronizedSet(new HashSet<>());

    public static String generateSerial(Random rand) {
        String serial;
        // keep trying until we get a brand-new one
        do {
            serial = randomString(rand);
        } while (!usedSerials.add(serial)); // add() returns false if already present
        return serial;
    }

    private static String randomString(Random rand) {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    // Example usage
    public static void main(String[] args) {
        Random rand = new Random();
        System.out.println(generateSerial(rand));
        System.out.println(generateSerial(rand));
        // …won’t ever print the same one twice in this JVM instance
    }
}