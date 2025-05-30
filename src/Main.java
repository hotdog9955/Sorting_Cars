public class Main {
    public static void main(String[] args) throws Exception {
        int lists = 3, size = 100_000, maxT = 4;
        QuicksortEngine engine = new QuicksortEngine(maxT);
        CarBuffer[] buffers = new CarBuffer[lists];
        for (int i = 0; i < lists; i++) {
            buffers[i] = CarBuffer.randomBuffer(size);
            buffers[i].writeToFile("cars-"+(i+1)+".txt");
        }
        for (int i = 0; i < lists; i++) {
            long t0 = System.nanoTime();
            engine.sort(buffers[i]);
            long dt = (System.nanoTime()-t0)/1_000_000;
            buffers[i].writeToFile("cars-"+(i+1)+"-sorted.txt");
            System.out.println("List "+(i+1)+" sorted in "+dt+"ms");
        }
        engine.shutdown();
    }
}