import java.io.*;
import java.util.*;

public class Main {
    private static final String[] DESTINATIONS = { "Los Angeles", "Houston", "New Orleans", "Miami", "New York" };
    private static final Car.Color[] COLORS = Car.Color.values();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) throws Exception {
        int numLists = 3;
        int carsPerList = 100_000;
        int maxThreads = 8;

        QuicksortEngine engine = new QuicksortEngine(maxThreads);
        List<List<Car>> carLists = new ArrayList<>();
        List<Thread> sortThreads = new ArrayList<>();

        for (int i = 0; i < numLists; i++) {
            List<Car> list = generateCarList(carsPerList);
            carLists.add(list);
            saveToFile(list, "cars-" + (i + 1) + ".txt");
        }

        for (int i = 0; i < carLists.size(); i++) {
            final int index = i;
            Thread t = new Thread(() -> {
                List<Car> carList = carLists.get(index);
                long start = System.currentTimeMillis();
                // After some testing, tracking the individual jobs ends up slower, and they all
                // finish at similar times anyway, this way of tracking is adequate and accurate.
                engine.submitJob(new SortJob(carList, 0, carList.size() - 1));
                engine.waitUntilFinished();
                long end = System.currentTimeMillis();
                System.out.println("cars-" + (index + 1) + " sorted in " + (end - start) + " ms");
                try {
                    saveToFile(carList, "cars-" + (index + 1) + "-sorted.txt");
                } catch (IOException e) {
                    // TODO: add better logging to this.
                    System.err.println("Failed to save sorted list to file: cars-" + (index + 1) + "-sorted.txt");
                    e.printStackTrace(); // Halt and catch fire since the program cant save.
                    System.exit(1);
                }
            });
            sortThreads.add(t);
            t.start();
        }

        for (Thread t : sortThreads) t.join();
        engine.shutdown();
    }

    private static List<Car> generateCarList(int count) {
        Set<String> usedSerials = new HashSet<>();
        List<Car> cars = new ArrayList<>();
        Random rand = new Random();

        for (long i = 1; i <= count; i++) {
            String serial;
            do {
                serial = SerialGenerator.generateSerial(rand);
            } while (!usedSerials.add(serial));
            cars.add(new Car(i, serial, COLORS[rand.nextInt(COLORS.length)], DESTINATIONS[rand.nextInt(DESTINATIONS.length)]));
        }
        return cars;
    }


    private static void saveToFile(List<Car> list, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Car car : list) {
                writer.println(car);
            }
        }
    }
}
